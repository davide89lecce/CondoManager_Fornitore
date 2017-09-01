
package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.Old_Model.Entity.Segnalazione;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BachecaInterventi extends Fragment {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";
    String username;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<Segnalazione> data;
    public static View.OnClickListener myOnClickListener;
    Context context;
    String condominoNome;
    private ArrayList<Segnalazione> datas;

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String uidCondomino;
    private String stabile;
    Map<String, Object> ticketInterventoMap;
    ArrayList<TicketIntervento> interventi;

    public static BachecaInterventi newInstance() {
        BachecaInterventi fragment = new BachecaInterventi();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_bacheca_interventi, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        data = new ArrayList<Segnalazione>();
        ticketInterventoMap = new HashMap<String,Object>();
        interventi = new ArrayList<TicketIntervento>();

        myOnClickListener = new MyOnClickListener(context);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        //lettura uid condomino -->  codice fiscale stabile, uid amministratore
        uidCondomino = firebaseAuth.getCurrentUser().getUid().toString();
        firebaseDB = FirebaseDB.getCondomini().child(uidCondomino);
        firebaseDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.getKey().toString().equals("stabile")){
                    //ricavo codicefiscale stabile
                    stabile = dataSnapshot.getValue().toString();

                    Query prova;
                    prova = FirebaseDB.getInterventi().orderByChild("stabile").equalTo(stabile);

                    prova.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String temp = dataSnapshot.getKey().toString();

                            ticketInterventoMap = new HashMap<String,Object>();
                            ticketInterventoMap.put("id", dataSnapshot.getKey());
                            for ( DataSnapshot child : dataSnapshot.getChildren() ) {
                                ticketInterventoMap.put(child.getKey(), child.getValue());
                            }

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
                                        ticketInterventoMap.get("stato").toString() );

                                interventi.add(ticketIntervento);
                                }catch (NullPointerException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Non riesco ad aprire l'oggetto"+ e.toString(), Toast.LENGTH_LONG).show();
                                }


                            adapter = new AdapterBachecaInterventi(interventi);
                            recyclerView.setAdapter(adapter);

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
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



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

            Intent intent = new Intent(context, DettaglioIntervento.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }
}
