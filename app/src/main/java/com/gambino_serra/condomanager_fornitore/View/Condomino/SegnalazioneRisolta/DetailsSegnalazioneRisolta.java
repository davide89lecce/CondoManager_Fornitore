package com.gambino_serra.condomanager_fornitore.View.Condomino.SegnalazioneRisolta;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gambino_serra.condomanager_fornitore.Model.HTTPRequest.Amministratore.HTTPRequestCondominio;
import com.gambino_serra.condomanager_fornitore.Model.HTTPRequest.Amministratore.HTTPRequestCondomino;
import com.gambino_serra.condomanager_fornitore.Model.HTTPRequest.Amministratore.HTTPRequestIntervento;
import com.gambino_serra.condomanager_fornitore.Model.HTTPRequest.Amministratore.HTTPRequestSegnalazioni;
import com.gambino_serra.condomanager_fornitore.Model.HTTPRequest.Condomino.HTTPRequestAmministratore;
import com.gambino_serra.condomanager_fornitore.Model.JsonSerializable.JsonAmministratore;
import com.gambino_serra.condomanager_fornitore.Model.JsonSerializable.JsonCondominio;
import com.gambino_serra.condomanager_fornitore.Model.JsonSerializable.JsonCondomino;
import com.gambino_serra.condomanager_fornitore.Model.JsonSerializable.JsonIntervento;
import com.gambino_serra.condomanager_fornitore.Model.JsonSerializable.JsonSegnalazione;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.gambino_serra.condomanager_fornitore.View.Condomino.Dialog.DialogChiamaAmministratore;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;

/**
 * Created by condomanager_fornitore on 06/03/17.
 */

public class DetailsSegnalazioneRisolta extends AppCompatActivity
        implements Response.Listener<String>, Response.ErrorListener{

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    String amministratore = "";
    String idSegnalazione = "";
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
    String dataIntervento = "";
    String intervento = "";

    TextView dataT;
    TextView segnalazioneT;
    TextView condominoT;
    TextView impiantoT;
    TextView fornitoreT;
    TextView statoT;
    TextView condominioT;
    TextView dataInterventoT;
    TextView interventoT;
    LinearLayout btnChiama;
    TextView descrizioneStatoT;
    ImageView imageStatoI;

    Response.Listener<String> listener = this;
    Response.ErrorListener errorListener = this;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione_risolta);

        final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        amministratore = sharedPrefs.getString(LOGGED_USER, "").toString();

        if (getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            idSegnalazione = bundle.get("idSegnalazione").toString();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("idSegnalazione", idSegnalazione);
            editor.apply();

        } else {

            idSegnalazione = sharedPrefs.getString("idSegnalazione", "").toString();

            bundle = new Bundle();
            bundle.putString("idSegnalazione", idSegnalazione);

        }

        dataT = (TextView) findViewById(R.id.dataD);
        segnalazioneT = (TextView) findViewById(R.id.segnalazioneD);
        condominoT = (TextView) findViewById(R.id.condominoD);
        //impiantoT = (TextView) findViewById(R.id.impiantoD);
        fornitoreT = (TextView) findViewById(R.id.fornitoreD);
        //statoT = (TextView) findViewById(R.id.statoD);
        condominioT = (TextView) findViewById(R.id.condominioD);
        dataInterventoT = (TextView) findViewById(R.id.dataInterventoD);
        interventoT = (TextView) findViewById(R.id.interventoD);
        btnChiama = (LinearLayout) findViewById(R.id.btnChiama);
        descrizioneStatoT = (TextView) findViewById(R.id.descrizioneStatoD);
        imageStatoI = (ImageView) findViewById(R.id.imageStatoD);

        HTTPRequestSegnalazioni.getSegnalazione(idSegnalazione, getApplicationContext(), this, this);
        HTTPRequestIntervento.getIntervento(idSegnalazione,getApplicationContext(),listener,errorListener);

        btnChiama.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DialogChiamaAmministratore();
                bundle.putString("telefono",telefonoAmministratore);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "ChiamaAmministratore");
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

            }
        });


    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

        if(!response.equals("null")) {

            if (response.contains("[{\"idSegnalazione\"")) {

                final ArrayList<JsonSegnalazione> productList = new JsonConverter<JsonSegnalazione>().toArrayList(response, JsonSegnalazione.class);

                data = productList.get(0).data;
                segnalazione = productList.get(0).segnalazione;
                usernameCondomino = productList.get(0).condomino;
                idCondominio = productList.get(0).idCondominio.toString();
                fornitore = productList.get(0).fornitore;
                stato = productList.get(0).stato;

                dataT.setText(data);
                segnalazioneT.setText(segnalazione);
                condominioT.setText(condominio);

                HTTPRequestCondomino.getCondomino(usernameCondomino, getApplicationContext(), this, this);
                HTTPRequestCondominio.getCondominio(idCondominio, getApplicationContext(), this, this);

                if(stato.equals("A")){
                    descrizioneStatoT.setText("Questa richiesta è in attesa di essere presa in carico");
                    imageStatoI.setImageResource(R.drawable.sand_clock2);
                }else if(stato.equals("B") || stato.equals("C") || stato.equals("D")){
                    descrizioneStatoT.setText("Questa richiesta è in corso d'opera");
                    imageStatoI.setImageResource(R.drawable.wrench);
                }else if(stato.equals("E") || stato.equals("F")){
                    descrizioneStatoT.setText("I lavori per questo intervento sono stati conclusi");
                    imageStatoI.setImageResource(R.drawable.checked);
                }else if(stato.equals("G")){
                    descrizioneStatoT.setText("Questa richiesta è stata rifiutata.\nContattare l'amministratore per maggiori dettagli");
                    imageStatoI.setImageResource(R.drawable.error);
                }

                final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("data", data);
                editor.putString("segnalazione", segnalazione);
                editor.putString("condomino", usernameCondomino);
                editor.putString("idCondominio", idCondominio);
                editor.putString("fornitore", fornitore);
                editor.putString("stato", stato);
                editor.apply();

            } else if (response.contains("[{\"idCondominio\"")) {

                final ArrayList<JsonCondominio> productList = new JsonConverter<JsonCondominio>().toArrayList(response, JsonCondominio.class);

                condominio = productList.get(0).condominio;
                condominioT.setText(productList.get(0).condominio);

                HTTPRequestAmministratore.getAmministratore(productList.get(0).ammministratore,getApplicationContext(),this,this);

                final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("condominio", condominio);
                editor.apply();

            } else if (response.contains("[{\"usernameC\"")) {

                final ArrayList<JsonCondomino> productList = new JsonConverter<JsonCondomino>().toArrayList(response, JsonCondomino.class);

                condominoT.setText(productList.get(0).cognome + " " + productList.get(0).nome);
                condomino = productList.get(0).cognome + " " + productList.get(0).nome;

                final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("condominoNome",condomino);
                editor.apply();

            } else if (response.contains("[{\"usernameA\"")) {

                final ArrayList<JsonAmministratore> productList = new JsonConverter<JsonAmministratore>().toArrayList(response, JsonAmministratore.class);

                telefonoAmministratore = productList.get(0).telefono;

                final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("telefono",telefonoAmministratore);
                editor.apply();

            } else if (response.contains("[{\"idIntervento\"")) {

                final ArrayList<JsonIntervento> productList = new JsonConverter<JsonIntervento>().toArrayList(response, JsonIntervento.class);

                dataIntervento = productList.get(0).data.substring(0,16);
                intervento = productList.get(0).intervento;
                dataInterventoT.setText(dataIntervento);
                interventoT.setText(intervento);

                final SharedPreferences sharedPrefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("dataIntervento", dataIntervento);
                editor.putString("intervento", intervento);
                editor.apply();

            }
        }
    }


}