package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by condomanager_fornitore on 06/03/17.
 */

public class DettaglioIntervento extends AppCompatActivity {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String username = "";
    String idIntervento = "";
    String data = "";
    String segnalazione = "";
    String usernameCondomino = "";
    String condominio = "";
    String telefonoAmministratore = "";
    String fornitore = "";
    String stato = "";
    String idCondominio = "";
    String impiantoNome = "";
    String azienda = "";
    String condomino = "";

    // Oggetti di Layout NUOVI
    TextView mOggetto;
    TextView mDescrizione;
    TextView mStato;
    TextView mUltimoAggiornamento;
    TextView mFornitore;


    TextView dataT;
    TextView segnalazioneT;
    TextView condominoT;
    TextView impiantoT;
    TextView fornitoreT;
    TextView statoT;
    TextView condominioT;
    LinearLayout btnChiama;
    TextView descrizioneStatoT;
    ImageView imageStatoI;

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
        setContentView(R.layout.dettaglio_intervento);

        firebaseAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            idIntervento = bundle.get("idIntervento").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("idIntervento", idIntervento);
            editor.apply();

        } else {
            //TODO: perchè
            idIntervento = sharedPrefs.getString("idSegnalazione", "").toString();

            bundle = new Bundle();
            bundle.putString("idSegnalazione", idIntervento);

        }

        // Avvaloro i nuovi rierimenti al layout
        mOggetto = (TextView) findViewById(R.id.D_Oggetto);
        mDescrizione = (TextView) findViewById(R.id.D_Descrizione);
        mStato = (TextView) findViewById(R.id.D_Stato);
        mUltimoAggiornamento = (TextView) findViewById(R.id.D_UltimoAggiornamento);
        mFornitore = (TextView) findViewById(R.id.D_Fornitore);

        /*
        dataT = (TextView) findViewById(R.id.dataD);
        segnalazioneT = (TextView) findViewById(R.id.segnalazioneD);
        condominoT = (TextView) findViewById(R.id.condominoD);
        //impiantoT = (TextView) findViewById(R.id.impiantoD);
        fornitoreT = (TextView) findViewById(R.id.fornitoreD);
        //statoT = (TextView) findViewById(R.id.statoD);
        condominioT = (TextView) findViewById(R.id.condominioD);
        btnChiama = (LinearLayout) findViewById(R.id.btnChiama);
        descrizioneStatoT = (TextView) findViewById(R.id.descrizioneStatoD);
        imageStatoI = (ImageView) findViewById(R.id.imageStatoD);
*/

        ticketInterventoMap = new HashMap<String, Object>();
        // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
        ticketInterventoMap.put("id", idIntervento);

        Query intervento;
        intervento = FirebaseDB.getInterventi().orderByKey().equalTo("idSegnalazione");

        intervento.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot figlio : dataSnapshot.getChildren() )
                    try {
                        ticketInterventoMap.put(figlio.getKey(), figlio.getValue(String.class));
                    }catch (NullPointerException e) {
                        ticketInterventoMap.put(figlio.getKey(), "-");
                    }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

try {
    mOggetto.setText(ticketInterventoMap.get("oggetto").toString());

    mDescrizione.setText(ticketInterventoMap.get("descrizione_condomini").toString());

    mStato.setText(ticketInterventoMap.get("stato").toString());

    mUltimoAggiornamento.setText(ticketInterventoMap.get("data_ultimo_aggiornamento").toString());

    mFornitore.setText(ticketInterventoMap.get("fornitore").toString());

}catch (NullPointerException e){
    Toast.makeText( getApplicationContext(), "Non riesco ad aprire l'oggetto"+ e.toString(), Toast.LENGTH_LONG).show();
}
        /*
        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ticketInterventoMap = new HashMap<String, Object>();
                // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
                ticketInterventoMap.put("id", dataSnapshot.getKey());

                // per ognuna delle coppie chiave valore, inseriamo il corrispondente elemento nel map creato
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ticketInterventoMap.put(child.getKey(), child.getValue());
                }

                TicketIntervento ticketIntervento = new TicketIntervento(
                        ticketInterventoMap.get("id").toString(),
                        ticketInterventoMap.get("amministratore").toString(),
                        ticketInterventoMap.get("data_ticket").toString(),
                        ticketInterventoMap.get("data_ultimo_aggiornamento").toString(),
                        ticketInterventoMap.get("fornitore").toString(),
                        ticketInterventoMap.get("messaggio_condomino").toString(),
                        ticketInterventoMap.get("descrizione_condomini").toString(),
                        ticketInterventoMap.get("oggetto").toString(),
                        ticketInterventoMap.get("priorità").toString(),
                        ticketInterventoMap.get("rapporti_intervento").toString(),
                        ticketInterventoMap.get("richiesta").toString(),
                        ticketInterventoMap.get("stabile").toString(),
                        ticketInterventoMap.get("stato").toString());

                if(ticketIntervento.getStato().equals("A")){
                    descrizioneStatoT.setText("Questa richiesta è in attesa di essere presa in carico");
                    imageStatoI.setImageResource(R.drawable.sand_clock2);
                }else if(ticketIntervento.getStato().equals("B") || stato.equals("C") || stato.equals("D")){
                    descrizioneStatoT.setText("Questa richiesta è in corso d'opera");
                    imageStatoI.setImageResource(R.drawable.wrench);
                }else if(ticketIntervento.getStato().equals("E") || stato.equals("F")){
                    descrizioneStatoT.setText("I lavori per questo intervento sono stati conclusi");
                    imageStatoI.setImageResource(R.drawable.checked);
                }else if(ticketIntervento.getStato().equals("G")){
                    descrizioneStatoT.setText("Questa richiesta è stata rifiutata.\nContattare l'amministratore per maggiori dettagli");
                    imageStatoI.setImageResource(R.drawable.error);
                }

                dataT.setText(ticketIntervento.getDataTicket());
                segnalazioneT.setText(ticketIntervento.getOggetto());
                condominioT.setText(ticketIntervento.getDataUltimoAggiornamento());
                descrizioneStatoT.setText(ticketIntervento.getMessaggioCondomino());

                //final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPrefs.edit();
                //editor.putString("data", ticketIntervento.getDataTicket());
                //editor.putString("segnalazione", ticketIntervento.getOggetto());
                //editor.putString("condomino", usernameCondomino);
                //editor.putString("idCondominio", idCondominio);
                //editor.putString("fornitore", fornitore);
                //editor.putString("stato", stato);
                //editor.apply();


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
*/

    }


}