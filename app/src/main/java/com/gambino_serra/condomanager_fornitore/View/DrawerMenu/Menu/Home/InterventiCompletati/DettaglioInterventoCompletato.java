package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiCompletati;

import android.app.DialogFragment;
import android.content.Intent;
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
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.View.Dialog.DialogChiamaAmministratore;
import com.gambino_serra.condomanager_fornitore.View.Dialog.DialogConfermaArchiviaIntervento;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;
import static com.gambino_serra.condomanager_fornitore.tesi.R.layout.dialog_conferma_archiviazione;

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
            //TODO: perchè
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
        TidTicketIntervento = (TextView) findViewById(R.id.D_IDIntervento);
        Archivia = (ConstraintLayout) findViewById(R.id.btnArchivia);
        ChiamaAmministratore = (ImageView) findViewById(R.id.imageViewChiamaAmministratore);
        Mappa = (ImageView) findViewById(R.id.btnMappa);


        ticketInterventoMap = new HashMap<String, Object>();
        // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
        ticketInterventoMap.put("idIntervento", idIntervento);

        Query intervento;
        intervento = FirebaseDB.getInterventi().orderByKey().equalTo(idIntervento);

        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //ticketInterventoMap = new HashMap<String,Object>();
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
                        ticketInterventoMap.get("url").toString(),
                        "ciao","ciao","ciao","ciao","ciao"
                );

                try {
                    TdataTicket.setText(ticketIntervento.getDataTicket().toString());
                    Tstabile.setText(ticketIntervento.getStabile().toString());
                    Toggetto.setText(ticketIntervento.getOggetto().toString());
                    TAmministratore.setText(ticketIntervento.getUidAmministratore().toString());
                    Trichiesta.setText(ticketIntervento.getRichiesta().toString());
                    Tindirizzo.setText("indirizzo ancora non presente");
                    //Tfoto.setQUALCOSA TODO: AGGIUNGERE FOTO e INDIRIZZO

                    if ( ticketInterventoMap.get("foto").toString() != "-" ) {
                        Picasso.with(getApplicationContext()).load( ticketIntervento.getUrl() ).fit().centerCrop().into(Tfoto) ;
                        }

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

    @Override
    public void onStart() {
        super.onStart();

        Archivia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogConfermaArchiviaIntervento newFragment = new DialogConfermaArchiviaIntervento();
                newFragment.show(getFragmentManager(), "DialogArchiviazione");
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
        });

        ChiamaAmministratore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogChiamaAmministratore newFragment = new DialogChiamaAmministratore();
                newFragment.show(getFragmentManager(), "DialogChiama");
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
        });

        Mappa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                DialogChiamaAmministratore newFragment = new DialogChiamaAmministratore();
//                newFragment.show(getFragmentManager(), "DialogChiama");
//                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

    }

}