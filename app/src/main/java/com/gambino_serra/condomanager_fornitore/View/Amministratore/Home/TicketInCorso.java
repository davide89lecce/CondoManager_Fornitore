package com.gambino_serra.condomanager_fornitore.View.Amministratore.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gambino_serra.condomanager_fornitore.Model.Entity.Segnalazione;
import com.gambino_serra.condomanager_fornitore.Model.HTTPRequest.Amministratore.HTTPRequestSegnalazioni;
import com.gambino_serra.condomanager_fornitore.Model.JsonSerializable.JsonSegnalazione;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.gambino_serra.condomanager_fornitore.View.Amministratore.TicketInCorso.DetailsTicketInCorso;
import com.gambino_serra.condomanager_fornitore.View.Amministratore.TicketRifiutato.DetailsTicketRifiutato;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TicketInCorso extends Fragment
    implements Response.Listener<String>, Response.ErrorListener {

        private static final String MY_PREFERENCES = "preferences";
        private static final String LOGGED_USER = "username";
        String username;
        private static RecyclerView.Adapter adapter;
        private RecyclerView.LayoutManager layoutManager;
        private static RecyclerView recyclerView;
        private ArrayList<Segnalazione> data;
        public static View.OnClickListener myOnClickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_ticket_in_corso, container, false);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

        data = new ArrayList<Segnalazione>();

        myOnClickListener = new TicketInCorso.MyOnClickListener(getActivity().getApplicationContext());

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view2);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        username = sharedPrefs.getString(LOGGED_USER, "").toString();

        HTTPRequestSegnalazioni.Segnalazioni(username, getActivity().getApplicationContext(), this, this);

    }


private static class MyOnClickListener extends AppCompatActivity implements View.OnClickListener {

    private final Context context;

    private MyOnClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        detailsSegnalazione(v);
    }

    private void detailsSegnalazione(View v) {

        int selectedItemPosition = recyclerView.getChildPosition(v);
        RecyclerView.ViewHolder viewHolder
                = recyclerView.findViewHolderForPosition(selectedItemPosition);
        TextView textViewName
                = (TextView) viewHolder.itemView.findViewById(R.id.textViewIdSegnalazione);
        String selectedName = (String) textViewName.getText();

        Bundle bundle = new Bundle();
        bundle.putString("idSegnalazione", selectedName);

        TextView textViewStato
                = (TextView) viewHolder.itemView.findViewById(R.id.textStato);
        String stato = (String) textViewStato.getText();

        if(stato.equals("B")){

            Intent intent = new Intent(context, DetailsTicketInCorso.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }else if(stato.equals("C")){

            Intent intent = new Intent(context, DetailsTicketInCorso.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if(stato.equals("D")) {

            Intent intent = new Intent(context, DetailsTicketRifiutato.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
}

    /**
     * Il metodo e' invocato alla risposta (dati ricevuti da database altervista) della richiesta delle segnalazioni.
     * @param response
     */
    @Override
    public void onResponse(String response) {

        if (!response.equals("null")) {

            final ArrayList<JsonSegnalazione> productList = new JsonConverter<JsonSegnalazione>().toArrayList(response, JsonSegnalazione.class);

            for (int i = 0; i < productList.size(); i++) {

                if(productList.get(i).stato.contains("B") || productList.get(i).stato.contains("C") || productList.get(i).stato.contains("D")){

                    Segnalazione s = new Segnalazione(productList.get(i).idSegnalazione, productList.get(i).data, productList.get(i).segnalazione,
                            productList.get(i).condomino, productList.get(i).idCondominio, productList.get(i).impianto, productList.get(i).fornitore, productList.get(i).stato, productList.get(i).condominio);

                    data.add(s);
                }
            }

            adapter = new AdapterCardsTicketInCorso(data);
            recyclerView.setAdapter(adapter);


        }
    }

    /**
     * Il metodo viene invocato in caso di problemi nella ricezione della risposta.
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {}


}