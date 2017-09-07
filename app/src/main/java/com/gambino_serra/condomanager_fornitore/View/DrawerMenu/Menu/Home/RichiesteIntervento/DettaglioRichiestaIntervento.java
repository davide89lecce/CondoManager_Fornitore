package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.RichiesteIntervento;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DettaglioRichiestaIntervento extends AppCompatActivity {
    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String username = "";
    String idIntervento = "";

    // Oggetti di Layout NUOVI
    TextView TidTicketIntervento;
    TextView TAmministratore;
    TextView TdataTicket;
    TextView Toggetto;
    TextView Trichiesta;
    TextView Tstabile;
    TextView Tindirizzo;
    ImageView Tfoto;

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mStorage;

    Map<String, Object> ticketInterventoMap;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_richiesta_intervento);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null)
        {
            bundle = getIntent().getExtras();
            idIntervento = bundle.get("idIntervento").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("idIntervento", idIntervento);
            editor.apply();
        }
        else
        {
            //TODO: perchè
            idIntervento = sharedPrefs.getString("idIntervento", "").toString();
            bundle = new Bundle();
            bundle.putString("idIntervento", idIntervento);
        }

        // Avvaloro i nuovi rierimenti al layout
        TdataTicket = (TextView) findViewById(R.id.D_Data);
        Tstabile = (TextView) findViewById(R.id.D_Condominio);
        Tindirizzo = (TextView) findViewById(R.id.D_Indirizzo);
        Toggetto = (TextView) findViewById(R.id.D_Oggetto);
        TAmministratore = (TextView) findViewById(R.id.D_Amministratore);
        Trichiesta = (TextView) findViewById(R.id.D_Descrizione);
        Tfoto = (ImageView) findViewById(R.id.D_Foto);
        TidTicketIntervento = (TextView) findViewById(R.id.D_IDIntervento);

        ticketInterventoMap = new HashMap<String, Object>();
        // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
        ticketInterventoMap.put("idIntervento", idIntervento);

        Query intervento;
        intervento = FirebaseDB.getInterventi().orderByKey().equalTo(idIntervento);

        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // ticketInterventoMap = new HashMap<String,Object>();
                //ticketInterventoMap.put("idIntervento",idIntervento);

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    ticketInterventoMap.put(child.getKey(),child.getValue());
                }

                TicketIntervento ticketIntervento = new TicketIntervento(
                        ticketInterventoMap.get("idIntervento").toString(),
                        ticketInterventoMap.get("amministratore").toString(),
                        ticketInterventoMap.get("data_ticket").toString(),
                        ticketInterventoMap.get("data_ultimo_aggiornamento").toString(),
                        ticketInterventoMap.get("fornitore").toString(),
                        ticketInterventoMap.get("messaggio_condomino").toString(),
                        ticketInterventoMap.get("aggiornamento_condomini").toString(),
                        ticketInterventoMap.get("descrizione_condomini").toString(),
                        ticketInterventoMap.get("oggetto").toString(),
                        ticketInterventoMap.get("rapporti_intervento").toString(),
                        ticketInterventoMap.get("richiesta").toString(),
                        ticketInterventoMap.get("stabile").toString(),
                        ticketInterventoMap.get("stato").toString() ,
                        ticketInterventoMap.get("priorità").toString() ,
                        ticketInterventoMap.get("foto").toString(),
                        ticketInterventoMap.get("url").toString()
                );

                try {
                    TdataTicket.setText(ticketIntervento.getDataTicket().toString());
                    Tstabile.setText(ticketIntervento.getStabile().toString());
                    Toggetto.setText(ticketIntervento.getOggetto().toString());
                    TAmministratore.setText(ticketIntervento.getUidAmministratore().toString());
                    Trichiesta.setText(ticketIntervento.getRichiesta().toString());
                    Tindirizzo.setText("indirizzo ancora non presente");


                    if ( ticketInterventoMap.get("foto").toString() != "-" ) {

                        Picasso.with(getApplicationContext()).load( ticketIntervento.getUrl() ).fit().centerCrop().into(Tfoto) ;
                    }

                    //Toast.makeText(getApplicationContext(), "Non riesco ad aprire l'oggetto " + e.toString(), Toast.LENGTH_LONG).show();

                    TidTicketIntervento.setText(ticketIntervento.getIdTicketIntervento());

                }catch (NullPointerException e){}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
    }
}
