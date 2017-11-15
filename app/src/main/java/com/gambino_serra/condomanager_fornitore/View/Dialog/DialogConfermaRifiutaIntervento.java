package com.gambino_serra.condomanager_fornitore.View.Dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.gambino_serra.condomanager_fornitore.Model.Entity.CardRapportoIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.MainDrawer;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import java.util.Date;
import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class DialogConfermaRifiutaIntervento extends DialogFragment {

    private static final String MY_PREFERENCES = "preferences";

    String idIntervento;
    FirebaseAuth firebaseAuth;
    Firebase firebase;
    Firebase firebaseIntervento;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DialogConfermaRifiutaIntervento() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Bundle bundle = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final String idTicket= bundle.getString("idTicket");
        firebase = FirebaseDB.getInterventi();
        final SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        idIntervento = sharedPrefs.getString("idIntervento", "").toString();

        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Rapporti_intervento");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseIntervento = FirebaseDB.getInterventi();

        builder.setView(inflater.inflate(R.layout.dialog_conferma_rifiuta_intervento, null))

                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {

                        // aggiorna il ticket per inserire un rapporto fittizio di rifiuto
                        firebase.child(idTicket).child("stato").setValue("rifiutato");
                        firebase.child(idTicket).child("numero_aggiornamenti").setValue(1);

                        addCardRapporto( databaseReference, "intervento rifiutato", "-", idIntervento);

                        Intent intent = new Intent(getActivity(), MainDrawer.class);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                        Toast.makeText(getActivity(), "INTERVENTO RIFIUTATO", Toast.LENGTH_SHORT);
                        }
                    })

                .setNeutralButton("ANNULLA", new DialogInterface.OnClickListener() {
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


    private void addCardRapporto(DatabaseReference postRef, final String notaAmministratore, final String notaPersonale, final String idTicket) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                //final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

                //Legge il valore del nodo counter
                Integer counter = mutableData.child("counter").getValue(Integer.class);
                if (counter == null) {
                    return Transaction.success(mutableData);
                }

                //Ricava la data e la formatta nel formato appropriato
                Date newDate = new Date(new Date().getTime());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String stringdate = dt.format(newDate);

                //Instanziamo un nuovo oggetto card rapporto intervento contenente tutte le informazioni
                //per la creazione di un nuovo nodo rapporto_intervento su Firebase
                CardRapportoIntervento m = new CardRapportoIntervento(counter.toString(),stringdate, notaAmministratore, notaPersonale, idTicket, "-");

                counter = counter + 1;

                //Setta il nome del nodo del messaggio (key)
                mutableData.child(counter.toString()).child("data").setValue( m.getData() );
                mutableData.child(counter.toString()).child("nota_amministratore").setValue( m.getNotaAmministratore() );
                mutableData.child(counter.toString()).child("nota_fornitore").setValue( m.getNotaFornitore() );
                mutableData.child(counter.toString()).child("ticket_intervento").setValue( m.getTicketIntervento() );
                mutableData.child(counter.toString()).child("foto").setValue( m.getFoto() );
                mutableData.child(counter.toString()).child("letto").setValue("no");

                //Setta il counter del nodo rapproto_intervento
                mutableData.child("counter").setValue(counter);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

}