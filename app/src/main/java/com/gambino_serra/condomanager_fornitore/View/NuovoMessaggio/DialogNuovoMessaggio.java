package com.gambino_serra.condomanager_fornitore.View.NuovoMessaggio;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.gambino_serra.condomanager_fornitore.Model.Entity.MessaggioCondomino;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class DialogNuovoMessaggio extends DialogFragment {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String uidCondomino;
    private String stabile;
    private String uidAmministratore;

    EditText descrizioneSegnalazioneE;
    String descrizioneSegnalazione;
    String username;

    private Button mSelectImage;
    private Button mSelectCamera;
    private ImageView mImmagine;
    private StorageReference mStorage;
    private StorageReference filepath;
    private static final int GALLERY_INTENT = 2; // Codice per definire l'intent specifico per la Galleria
    private static final int TAKE_PICTURE = 1;
    //private static final int CAMERA_REQUEST_CODE = 1; // Codice per definire l'intent specifico per la Camera
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;

    File sdImageMainDirectory;
    private Uri UriImmagine = null; //per sovrascrivere il percorso nel quale sarà presente l'immagine selezionata

    public DialogNuovoMessaggio() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        TextView title = new TextView(getActivity());
        title.setText(R.string.title_nuovo_messaggio);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(30);
        title.setBackgroundResource(R.color.primarySegnalazione);
        title.setTextColor(Color.WHITE);
        builder.setCustomTitle(title);

        builder.setView(inflater.inflate(R.layout.dialog_nuova_segnalazione_messaggio, null))

                .setPositiveButton(R.string.nuova_segnalazione_conferma, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Messaggi_condomino");


                        descrizioneSegnalazione = descrizioneSegnalazioneE.getText().toString();



                        //SALVA IMMAGINE IN STORAGE FIREBASE
                        //TODO: inserire controllo nel caso in cui non ci siano foto allegat
                        if ( UriImmagine != null) {
                            filepath = mStorage.child("Photo").child(UriImmagine.getLastPathSegment());

                            filepath.putFile(UriImmagine).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {//TODO : Nono funzionano i Toast
                                    Toast.makeText( getActivity().getApplicationContext(), "Segnalazione Inviata con Successo", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) { //TODO: problemi su contesto Toast
                                    Toast.makeText( getContext(), "Non è stato possibile inviare la segnalazione", Toast.LENGTH_LONG).show();
                                }
                            });

                            // DA CHIEDERE SE SERVE ANCORA
                            final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("descrizioneSegnalazione", descrizioneSegnalazione);
                            editor.apply();
                        }


                        // Aggiunge la segnalazione al Database
                        try
                        {
                            if (filepath != null)
                                addSegnalazioneCondomino(databaseReference,descrizioneSegnalazione,filepath.toString());
                            else
                                addSegnalazioneCondomino(databaseReference,descrizioneSegnalazione,"-");
                        }catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(), "Problemi", Toast.LENGTH_LONG).show();
                        }


                        dismiss();
                    }
                })

                .setNeutralButton(R.string.nuova_segnalazione_annulla, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {

                        dismiss();
                    }
                });

        return builder.create();

    }

    @Override
    public void onStart() {
        super.onStart();
        TextView testo = (TextView) this.getDialog().findViewById(R.id.textNuovaSegnalazione);
        testo.setText(R.string.nuovo_messaggio);

        descrizioneSegnalazioneE = (EditText) this.getDialog().findViewById(R.id.textDescrizioneSegnalazione);

        //lettura uid condomino -->  codice fiscale stabile, uid amministratore
        uidCondomino = firebaseAuth.getCurrentUser().getUid().toString();
        firebaseDB = FirebaseDB.getCondomini().child(uidCondomino);
        firebaseDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.getKey().toString().equals("stabile")){
                    stabile = dataSnapshot.getValue().toString();

                    firebaseDB = FirebaseDB.getStabili().child(stabile);
                    firebaseDB.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                            if(dataSnapshot.getKey().toString().equals("amministratore")) {
                                uidAmministratore = dataSnapshot.getValue().toString();
                            }
                        }
                        @Override
                        public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        mImmagine = (ImageView) this.getDialog().findViewById(R.id.ImmagineAnteprima);

        mSelectImage = (Button) this.getDialog().findViewById(R.id.insertImage);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentGallery = new Intent(Intent.ACTION_PICK);
                intentGallery.setType("image/*");
                startActivityForResult(intentGallery,GALLERY_INTENT);

            }
        });


        mSelectCamera = (Button) this.getDialog().findViewById(R.id.takeImage);
        mSelectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                /**PER SALVARE UNA FOTO*/
                // PASSO 1 : percorso
                // recuperiamo tramite Environment il percorso di default per il salvataggio della foto
                File photoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                // PASSO 2 : nome
                // utilizziamo una funzione per assegnare un nome unico ala foto che abbia come codice la data in cui
                // è stata stampata
                String photoName = createPhotoName();

                // PASSO 3 : creo il file in cui salvare la foto con percorso e nome creati
                File photoFile = new File( photoDirectory, photoName );


                // Salvo l'uri dell'immagine per poi salvarla su firebase
                UriImmagine = Uri.fromFile(photoFile);

                intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                // Comando che salverà la foto scattata nella galleria a seconda del URI assegnato
                // contenente sia il nome che il percorso dell'immagine
                intentCamera.putExtra( MediaStore.EXTRA_OUTPUT, UriImmagine );

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, UriImmagine);
                }else {
                    File file = new File(UriImmagine.getPath());
                    Uri photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }
                intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (intentCamera.resolveActivity(getActivity().getApplicationContext().getPackageManager()) != null) {
                    startActivityForResult(intentCamera, TAKE_PICTURE);
                }


                //startActivityForResult(intentCamera,TAKE_PICTURE);

            }
        });



    }

    /**
     * Metodo utile alla creazione di nomi unici per la storicizzazione di elementi come foto
     * è possibile specificare il formato di data desiderato e personalizzare il nome
     *
     * @return
     */
    private String createPhotoName() {
        SimpleDateFormat sdf = new SimpleDateFormat( "ddMMyyyy_HHmmss");
        String timestamp = sdf.format(new Date());
        return "CondomanagerPhoto" + timestamp + ".jpg";
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }


    private void addSegnalazioneCondomino(DatabaseReference postRef, final String descrizioneSegnalazione, final String percorsoFoto) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                //Legge il valore del nodo counter
                Integer counter = mutableData.child("counter").getValue(Integer.class);

                if (counter == null) {
                    return Transaction.success(mutableData);
                }

                //Incrementa counter
                counter = counter + 1;

                //Ricava la data e la formatta nel formato appropriato
                Date newDate = new Date(new Date().getTime());
                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm ");
                String stringdate = dt.format(newDate);

                //Instanziamo un nuovo oggetto MessaggioCondomino contenente tutte le informazioni
                //per la creazione di un nuovo nodo Messaggi_condomino su Firebase
                MessaggioCondomino m = new MessaggioCondomino(counter.toString(),stringdate,"Messaggio", descrizioneSegnalazione,uidCondomino,uidAmministratore, stabile, percorsoFoto);

                //Setta il nome del nodo del messaggio (key)
                mutableData.child(counter.toString()).setValue(m);
                //Setta il counter del nodo Messaggi_condomino
                mutableData.child("counter").setValue(counter);


                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }


    /**
     * Funzione che si attiverà una volta che l'activity chiamata per catturare un'immagine restituirà un risultato
     * ovvero appunto l'immagine che si desidera inserire nel db
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if ( requestCode == TAKE_PICTURE || requestCode == GALLERY_INTENT) {

                if ( requestCode == GALLERY_INTENT ){
                    // Sovrascrive l'uri dell'immagine da stampare ogni volta che viene restituita una photo con ActivityResult
                    UriImmagine = data.getData();
                }

                // ci servirà per visualizzare l'immagine selezionata prima di inviarla e salvarla su Firebase
                InputStream inputStream;

                try {
                    inputStream = getContext().getContentResolver().openInputStream(UriImmagine);

                    // Mappiamo la view per visualizzare l'input stream a schermo
                    Bitmap bt = BitmapFactory.decodeStream(inputStream);
                    mImmagine.setImageBitmap(bt);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Non riesco ad aprire l'immagine", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}