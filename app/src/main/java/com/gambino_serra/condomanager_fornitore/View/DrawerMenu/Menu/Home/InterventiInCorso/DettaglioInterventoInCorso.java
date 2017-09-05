package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;

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
import java.util.HashMap;
import java.util.Map;


public class DettaglioInterventoInCorso extends AppCompatActivity {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String username = "";
    String idIntervento = "";

    // Oggetti di Layout NUOVI
    TextView TdataTicket;
    TextView Tindirizzo;
    TextView Toggetto;
    TextView Tstabile;
    TextView TidTicket;
    ImageView Tfoto;

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Map<String, Object> ticketInterventoMap;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_intervento_in_corso);

        firebaseAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            idIntervento = bundle.get("idIntervento").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("idIntervento", idIntervento);
            editor.apply();
            }
        else
            {
            idIntervento = sharedPrefs.getString("idIntervento", "").toString();
            bundle = new Bundle();
            bundle.putString("idIntervento", idIntervento);
            }

        // Avvaloro i nuovi rierimenti al layout
        TidTicket = (TextView) findViewById(R.id.IDTicket);
        TdataTicket = (TextView) findViewById(R.id.DataAgg_Interv);
        Tstabile = (TextView) findViewById(R.id.D_Condominio);
        Toggetto = (TextView) findViewById(R.id.D_Oggetto);
        Tindirizzo = (TextView) findViewById(R.id.D_Indirizzo);
        //Tfoto = (ImageView) findViewById(R.id.D_foto);


        ticketInterventoMap = new HashMap<String, Object>();
        // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
        ticketInterventoMap.put("idIntervento", idIntervento);

        Query intervento;
        intervento = FirebaseDB.getInterventi().orderByKey().equalTo("idIntervento");

        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ticketInterventoMap = new HashMap<String,Object>();
                ticketInterventoMap.put("idIntervento",idIntervento);

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    ticketInterventoMap.put(child.getKey(),child.getValue());
                    }

                TicketIntervento ticketIntervento = new TicketIntervento(
                        ticketInterventoMap.get("id").toString(),
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
                        ticketInterventoMap.get("priorità").toString() //,
                        //ticketInterventoMap.get("foto").toString()
                );

                TdataTicket.setText(ticketIntervento.getDataTicket());
                Tstabile.setText(ticketIntervento.getStabile());
                Toggetto.setText(ticketIntervento.getOggetto());
                //Tindirizzo.setText(ticketIntervento.getIndirizzo());
                //Tfoto TODO: AGGIUNGERE FOTO e INDIRIZZO

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




// todo: interessa a totò
//        intervento.addValueEventListener (new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for ( DataSnapshot figlio : dataSnapshot.getChildren() )
//                    try {
//                        ticketInterventoMap.put(figlio.getKey(), figlio.getValue(String.class));
//                        }
//                    catch (NullPointerException e)
//                        {
//                        ticketInterventoMap.put(figlio.getKey(), "-");
//                        }
//                }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) { }
//        });