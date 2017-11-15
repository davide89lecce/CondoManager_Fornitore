package com.gambino_serra.condomanager_fornitore.View.Dialog;


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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.utilities.Base64;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class DialogVisualizzaFotoRapporto extends DialogFragment {
    private static final String MY_PREFERENCES = "preferences";

    private String foto;
    private Bundle bundle;
    private FirebaseAuth firebaseAuth;
    private InputStream inputStream;

    public DialogVisualizzaFotoRapporto() { }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        firebaseAuth = FirebaseAuth.getInstance();




//        TextView title = new TextView(getActivity());
//        title.setText("Foto");
//        title.setGravity(Gravity.CENTER);
//        title.setBackgroundResource(R.color.colorPrimary);
//        builder.setCustomTitle(title);

        builder.setView(inflater.inflate(R.layout.dialog_visualizza_foto_rapporto, null))

                .setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
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

        ImageView fotoRapporto = (ImageView) this.getDialog().findViewById(R.id.foto_rapporto);

        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

        if (getActivity().getIntent().getExtras() != null)
            {
            bundle = getArguments();
            foto = bundle.get("foto").toString();
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("foto", foto);
            editor.apply();
            }
        else
            {
            foto = sharedPrefs.getString("idIntervento", "").toString();
            bundle = new Bundle();
            bundle.putString("foto", foto);
            }

            if(!foto.equals("-")) {
                Picasso.with((getActivity().getApplicationContext())).load(foto).fit().centerCrop().into(fotoRapporto);
                }

        fotoRapporto.getLayoutParams().width = 720;
        fotoRapporto.getLayoutParams().height = 480;
        fotoRapporto.requestLayout();
    }
}

