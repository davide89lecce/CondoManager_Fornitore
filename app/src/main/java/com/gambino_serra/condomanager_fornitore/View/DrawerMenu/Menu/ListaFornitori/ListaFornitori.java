package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_fornitore.Model.Entity.Fornitore;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
//import com.gambino_serra.condomanager_fornitore.Old_Model.Entity.Segnalazione;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class ListaFornitori extends Fragment {
    // the Menu initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";
    String username;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<Fornitore> data;
    public static View.OnClickListener myOnClickListener;
    Context context;
    //String condominoNome;
    private ArrayList<Fornitore> datas;

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String uidCondomino;
    //private String stabile;
    Map<String, Object> FornitoreMap;
    ArrayList<Fornitore> fornitori;

    private com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori.OnFragmentInteractionListener mListener;

    public ListaFornitori() {}

    /**
     * Use this factory method to create a new instance of this Menu using the provided parameters.
     */
    public static com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori newInstance(String param1, String param2) {
        com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori fragment = new com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this Menu
        return inflater.inflate(R.layout.bacheca_lista_fornitori, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onStart() {
        super.onStart();

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        data = new ArrayList<Fornitore>();
        FornitoreMap = new HashMap<String,Object>();
        fornitori = new ArrayList<Fornitore>();

        myOnClickListener = new com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.ListaFornitori.ListaFornitori.MyOnClickListener(context);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view2);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //lettura di TUTTI i fornitori TODO: sistemare con solo fornitori del condominio in questione

        Query query = FirebaseDB.getFornitori().orderByChild("categoria");
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                FornitoreMap = new HashMap<String,Object>();
                FornitoreMap.put("uid", dataSnapshot.getKey());
                for ( DataSnapshot child : dataSnapshot.getChildren() ) {
                    FornitoreMap.put(child.getKey(), child.getValue());
                }

                Fornitore fornitore = new Fornitore(
                        FornitoreMap.get("uid").toString(),
                        FornitoreMap.get("nome").toString(),
                        FornitoreMap.get("nome_azienda").toString(),
                        FornitoreMap.get("categoria").toString(),
                        FornitoreMap.get("partita_iva").toString(),
                        FornitoreMap.get("telefono").toString(),
                        FornitoreMap.get("indirizzo").toString(),
                        FornitoreMap.get("email").toString());

                fornitori.add(fornitore);

                adapter = new AdapterListaFornitori(fornitori);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {      }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {      }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {      }

            @Override
            public void onCancelled(FirebaseError firebaseError) {      }
        });

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * Menu to allow an interaction in this Menu to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private static class MyOnClickListener extends AppCompatActivity implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            detailsFornitore(v);
        }

        private void detailsFornitore(View v) {

            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.textViewUidFornitore);
            String selectedName = (String) textViewName.getText();

            Bundle bundle = new Bundle();
            bundle.putString("uidFornitore", selectedName);

            Intent intent = new Intent(context, DettaglioFornitore.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }

}
