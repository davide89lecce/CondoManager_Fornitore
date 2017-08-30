package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_fornitore.Model.Entity.Fornitore;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
//import com.gambino_serra.condomanager_fornitore.Old_View.Condomino.Dialog.DialogChiamaAmministratore;
import com.gambino_serra.condomanager_fornitore.Old_View.Amministratore.Dialog.DialogChiamaFornitore;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DettaglioFornitore extends AppCompatActivity {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String nome_azienda = "";
    String nome = "";
    String categoria = "";
    String partita_iva = "";
    String telefono = "";
    String email = "";
    String indirizzo = "";

    TextView Tnome_azienda;
    TextView Tnome;
    TextView Tcategoria;
    TextView Ttelefono;
    TextView Tpartita_iva;
    TextView Temail;
    TextView Tindirizzo;
    ImageView BTNchiama_fornitore;
    String uidFornitore;

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Map<String, Object> dettaglioFornitoreMap;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_fornitore);

        firebaseAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        //username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            uidFornitore = bundle.get("uidFornitore").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("uidfornitore", uidFornitore);
            editor.apply();

        } else {

            uidFornitore = sharedPrefs.getString("uidFornitore", "").toString();

            bundle = new Bundle();
            bundle.putString("uidFornitore", uidFornitore);

        }

        Tnome_azienda = (TextView) findViewById(R.id.FornnomeAzienda);
        Tcategoria = (TextView) findViewById(R.id.FornSettore);
        Tpartita_iva = (TextView) findViewById(R.id.FornPartIVA);
        Ttelefono = (TextView) findViewById(R.id.FornTelefono);
        Temail = (TextView) findViewById(R.id.FornEmail);
        Tindirizzo = (TextView) findViewById(R.id.FornIndirizzo);
        BTNchiama_fornitore = (ImageView) findViewById(R.id.imageViewFornChiama);


        Query prova;
        prova = FirebaseDB.getFornitori().orderByKey().equalTo(uidFornitore);

        prova.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                dettaglioFornitoreMap = new HashMap<String, Object>();
                dettaglioFornitoreMap.put("uidFornitore", dataSnapshot.getKey());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    dettaglioFornitoreMap.put(child.getKey(), child.getValue());
                }

                Fornitore fornitore = new Fornitore(
                        dettaglioFornitoreMap.get("uidFornitore").toString(),
                        dettaglioFornitoreMap.get("nome").toString(),
                        dettaglioFornitoreMap.get("nome_azienda").toString(),
                        dettaglioFornitoreMap.get("categoria").toString(),
                        dettaglioFornitoreMap.get("partita_iva").toString(),
                        dettaglioFornitoreMap.get("telefono").toString(),
                        dettaglioFornitoreMap.get("indirizzo").toString(),
                        dettaglioFornitoreMap.get("email").toString());


                Tnome_azienda.setText(fornitore.getNome_azienda());
                Tcategoria.setText(fornitore.getCategoria());
                Tpartita_iva.setText(fornitore.getPartita_iva());
                Ttelefono.setText(fornitore.getTelefono());
                Temail.setText(fornitore.getEmail());
                Tindirizzo.setText(fornitore.getIndirizzo());
                /*
                final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("data", ticketIntervento.getDataTicket());
                editor.putString("segnalazione", ticketIntervento.getOggetto());
                editor.putString("condomino", usernameCondomino);
                editor.putString("idCondominio", idCondominio);
                editor.putString("fornitore", fornitore);
                editor.putString("stato", stato);
                editor.apply();
                */

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        BTNchiama_fornitore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DialogChiamaFornitore();
                bundle.putString("telefono",Ttelefono.getText().toString());
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "ChiamaFornitore");
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });


    }


}
