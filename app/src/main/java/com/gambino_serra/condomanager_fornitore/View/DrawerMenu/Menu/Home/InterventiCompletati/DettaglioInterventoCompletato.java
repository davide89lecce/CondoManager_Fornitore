package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiCompletati;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.View.Dialog.DialogConfermaArchiviaIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;

public class DettaglioInterventoCompletato extends AppCompatActivity {
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
    ConstraintLayout Archivia;
    ImageView ChiamaAmministratore;
    ImageView Mappa;

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
        setContentView(R.layout.dettaglio_intervento_completato);

        firebaseAuth = FirebaseAuth.getInstance();

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
            idIntervento = sharedPrefs.getString("idIntervento", "").toString();
            bundle = new Bundle();
            bundle.putString("idIntervento", idIntervento);
            }

        // Avvaloro i nuovi riferimenti al layout
        TdataTicket = (TextView) findViewById(R.id.D_Data);
        Tstabile = (TextView) findViewById(R.id.D_Condominio);
        Tindirizzo = (TextView) findViewById(R.id.D_Indirizzo);
        Toggetto = (TextView) findViewById(R.id.D_Oggetto);
        TAmministratore = (TextView) findViewById(R.id.D_Amministratore);
        Trichiesta = (TextView) findViewById(R.id.D_Descrizione);
        Tfoto = (ImageView) findViewById(R.id.D_Foto);
        TidTicketIntervento = (TextView) findViewById(R.id.Hidden_ID);
        Archivia = (ConstraintLayout) findViewById(R.id.btnArchivia);
        ChiamaAmministratore = (ImageView) findViewById(R.id.imageViewChiamaAmministratore);
        Mappa = (ImageView) findViewById(R.id.btnMappa);

        ticketInterventoMap = new HashMap<String, Object>();
        // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
        ticketInterventoMap.put("idIntervento", idIntervento);
    }

    @Override
    public void onStart() {
        super.onStart();

        Archivia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("idTicket", TidTicketIntervento.getText().toString());

                DialogConfermaArchiviaIntervento newFragment = new DialogConfermaArchiviaIntervento();
                newFragment.show(getFragmentManager(), "DialogArchiviazione");
                newFragment.setArguments(bundle);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
        });

//        ChiamaAmministratore.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {//TODO: CHIAMA AMMNINISTRATORE
//                DialogChiamaAmministratore newFragment = new DialogChiamaAmministratore();
//                newFragment.show(getFragmentManager(), "DialogChiama");
//                newFragment.setArguments(bundle);
//                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//                }
//        });

//        Mappa.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { //TODO: PULSANTE RAGGIUNGI INTERVENTO
//                DialogChiamaAmministratore newFragment = new DialogChiamaAmministratore();
//                newFragment.show(getFragmentManager(), "DialogChiama");
//                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//                }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Query intervento;
        intervento = FirebaseDB.getInterventi().orderByKey().equalTo(idIntervento);

        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    ticketInterventoMap.put(child.getKey(),child.getValue());
                    }

                recuperaDettagliTicket(ticketInterventoMap);
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

    public void recuperaDettagliTicket(final Map<String, Object> ticketInterventoMap) {

        final Map<String, Object> ticketInterventoMap2 = new HashMap<String, Object>();

        Query query2;
        query2 = FirebaseDB.getStabili().orderByKey().equalTo(ticketInterventoMap.get("stabile").toString());

        query2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //HashMap temporaneo per immagazzinare i dati dello stabile
                // per ognuno dei figli presenti nello snapshot, ovvero per tutti i figli di un singolo nodo Stabile
                // recuperiamo i dati per inserirli nel MAP
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ticketInterventoMap2.put(child.getKey(), child.getValue());
                    }

                Firebase nomeAmm = FirebaseDB.getAmministratori()
                        .child( ticketInterventoMap.get("amministratore").toString() )
                        .child("nome");

                nomeAmm.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ticketInterventoMap2.put("nomeAmministratore", dataSnapshot.getValue().toString() );


                        // Avvaloro tutti i dati della card che mi interessano inserendone i relativi dati
                        // anche quelli provenienti dallo stabile sovrascrivendo i codici passati in ticketIntervento
                        // Avvaloriamo una variabile TicketIntervento appositamente creata in modo da inserire poi questo
                        // oggetto all'interno di un Array di interventi che utilizzeremo per popolare la lista Recycle
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
                                ticketInterventoMap.get("stato").toString(),
                                ticketInterventoMap.get("priorità").toString(),
                                ticketInterventoMap.get("foto").toString(),
                                ticketInterventoMap2.get("nomeAmministratore").toString(),
                                ticketInterventoMap2.get("nome").toString(),
                                ticketInterventoMap2.get("indirizzo").toString(),
                                ticketInterventoMap2.get("latitudine").toString(),
                                ticketInterventoMap2.get("longitudine").toString()
                            );

                        try
                            {
                            TdataTicket.setText(ticketIntervento.getDataTicket().toString());
                            Tstabile.setText(ticketIntervento.getNomeStabile().toString());
                            Toggetto.setText(ticketIntervento.getOggetto().toString());
                            TAmministratore.setText(ticketIntervento.getNomeAmministratore().toString());
                            Trichiesta.setText(ticketIntervento.getRichiesta().toString());
                            Tindirizzo.setText(ticketIntervento.getIndirizzoStabile().toString());
                            TidTicketIntervento.setText(ticketIntervento.getIdTicketIntervento().toString());

                            if ( ! "-".equals( ticketIntervento.getFoto() ) ) {
                                Picasso.with(getApplicationContext()).load(ticketIntervento.getFoto()).fit().centerCrop().into(Tfoto);
                                }

                            }
                        catch (NullPointerException e) {}
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) { }
                });
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