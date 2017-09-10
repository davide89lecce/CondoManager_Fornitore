package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso.InterventoInCorso.RapportiIntervento;

import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gambino_serra.condomanager_fornitore.View.Dialog.DialogConfermaArchiviaIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;

public class InserimentoRapportoIntervento extends AppCompatActivity {

    EditText notaAmministratore;
    EditText notaPersonale;
    ConstraintLayout conferma;
    ConstraintLayout annulla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserimento_rapporto_intervento);

//        firebaseAuth = FirebaseAuth.getInstance();
//
//        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
//        username = sharedPrefs.getString(LOGGED_USER, "").toString();
//
//        if (getIntent().getExtras() != null)
//        {
//            bundle = getIntent().getExtras();
//            idIntervento = bundle.get("idIntervento").toString();
//
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            editor.putString("idIntervento", idIntervento);
//            editor.apply();
//        }
//        else
//        {
//            idIntervento = sharedPrefs.getString("idIntervento", "").toString();
//            bundle = new Bundle();
//            bundle.putString("idIntervento", idIntervento);
//        }

        notaAmministratore = (EditText) findViewById(R.id.textViewNotaAmministratore);
        notaPersonale = (EditText) findViewById(R.id.textViewNotaPersonale);
        conferma = (ConstraintLayout) findViewById(R.id.btnConferma);
        annulla = (ConstraintLayout) findViewById(R.id.btnAnnulla);

    }



    @Override
    public void onStart() {
        super.onStart();

        conferma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: SALVA NOTE E CAMBIA IN COMPLETATO
            }
        });

        annulla.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: TORNA INDIETRO
            }
        });
    }

//    Bundle bundle;
//    String idIntervento = "";
//    private FirebaseAuth firebaseAuth;
//    final String idTicket = bundle.getString("idTicket");
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.inserimento_rapporto_intervento);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        bundle = getIntent().getExtras();
//        idIntervento = bundle.get("idIntervento").toString();
//
//
//
//        firebase.child(idTicket).child("stato").setValue("completato");
//
//        Intent intent = new Intent(getActivity(), MainDrawer.class);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//          }


}
