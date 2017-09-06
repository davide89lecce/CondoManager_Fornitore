package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BachecaInterventiInCorso extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<TicketIntervento> data;
    public static View.OnClickListener myOnClickListener;
    Context context;
    FloatingActionButton buttonMaps;

    private FirebaseAuth firebaseAuth;
    private String uidFornitore;
    Map<String, Object> ticketInterventoMap;
    ArrayList<TicketIntervento> interventi;

    public static BachecaInterventiInCorso newInstance() {
        BachecaInterventiInCorso fragment = new BachecaInterventiInCorso();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_interventi_in_corso, container, false);
        }

    @Override
    public void onStart() {
        super.onStart();

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        data = new ArrayList<TicketIntervento>();
        ticketInterventoMap = new HashMap<String,Object>();
        interventi = new ArrayList<TicketIntervento>();

        buttonMaps = (FloatingActionButton) getActivity().findViewById(R.id.button);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mappa = new Intent(getActivity(), MappaInterventiInCorso.class);
                startActivity(mappa);
            }
        });

        myOnClickListener = new MyOnClickListener(context);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //lettura uid condomino -->  codice fiscale stabile, uid amministratore
        uidFornitore = firebaseAuth.getCurrentUser().getUid().toString();

        Query query;
        query = FirebaseDB.getInterventi().orderByChild("fornitore").equalTo(uidFornitore);


        // la query seleziona solo gli interventi con un determinato fornitore
        //il listener lavora sui figli della query, ovvero su titti gli interventi recuperati
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                //HashMap temporaneo per immagazzinare i dati di un ticket
                ticketInterventoMap = new HashMap<String,Object>();
                ticketInterventoMap.put("id", dataSnapshot.getKey()); //primo campo del MAP

                // per ognuno dei figli presenti nello snapshot, ovvero per tutti i figli di un singolo nodo Interv
                // recuperiamo i dati per inserirli nel MAP
                for ( DataSnapshot child : dataSnapshot.getChildren() ) {
                    ticketInterventoMap.put(child.getKey(), child.getValue());
                    }

                // Avvaloriamo una variabile TicketIntervento appositamente creata in modo da inserire poi questo
                // oggetto all'interno di un Array di interventi che utilizzeremo per popolare la lista Recycle
                try{
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
                            ticketInterventoMap.get("priorità").toString(),
                            ticketInterventoMap.get("foto").toString()
                    );

                    if(ticketIntervento.getStato().equals("in corso"))
                        {
                        // inserisce l'oggetto ticket nell'array interventi
                        interventi.add(ticketIntervento);
                        }
                }
                catch (NullPointerException e)
                    {
                    Toast.makeText(getActivity().getApplicationContext(), "Non riesco ad aprire l'oggetto "+ e.toString(), Toast.LENGTH_LONG).show();
                    }

                // Utilizziamo l'adapter per popolare la recycler view
                adapter = new AdapterInterventiInCorso(interventi);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
    }

    private static class MyOnClickListener extends AppCompatActivity implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            detailsIntervento(v);
        }

        private void detailsIntervento(View v) {

            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.D_IDIntervento);
            String selectedName = (String) textViewName.getText();

            Bundle bundle = new Bundle();
            bundle.putString("idIntervento", selectedName);

            Intent intent = new Intent(context, DettaglioInterventoInCorso.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }
}
