package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso.InterventoInCorso.RapportiIntervento;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.gambino_serra.condomanager_fornitore.Model.Entity.CardTicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RapportiIntervento extends Fragment {

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


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<TicketIntervento> data;
    Context context;

    private FirebaseAuth firebaseAuth;
    private String uidFornitore;
    Map<String, Object> ticketInterventoMap;
    ArrayList<CardTicketIntervento> interventi;
    Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rapporti_intervento, container, false);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

        context = getContext();

        firebaseAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

        idIntervento = sharedPrefs.getString("idIntervento", "").toString();

        bundle = new Bundle();
        bundle.putString("idIntervento", idIntervento);


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        uidFornitore = firebaseAuth.getCurrentUser().getUid().toString();

        Query query;
        query = FirebaseDB.getRapportiIntervento().orderByChild("idIntervento").equalTo(idIntervento);
    }

}