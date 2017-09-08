package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class DettaglioInterventoInCorso extends Fragment {

    private static final String MY_PREFERENCES = "preferences";
    private static final String LOGGED_USER = "username";

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    Context context;

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

    private Firebase firebaseDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    Map<String, Object> ticketInterventoMap;

    Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dettaglio_intervento_in_corso, container, false);
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


        // Avvaloro i nuovi rierimenti al layout
        TdataTicket = (TextView) getActivity().findViewById(R.id.D_Data);
        Tstabile = (TextView) getActivity().findViewById(R.id.D_Condominio);
        Tindirizzo = (TextView) getActivity().findViewById(R.id.D_Indirizzo);
        Toggetto = (TextView) getActivity().findViewById(R.id.D_Oggetto);
        TAmministratore = (TextView) getActivity().findViewById(R.id.D_Amministratore);
        Trichiesta = (TextView) getActivity().findViewById(R.id.D_Descrizione);
        Tfoto = (ImageView) getActivity().findViewById(R.id.D_Foto);
        TidTicketIntervento = (TextView) getActivity().findViewById(R.id.D_IDIntervento);

        ticketInterventoMap = new HashMap<String, Object>();
        // Avvalora il primo oggetto del map con l'ID dell'intervento recuperato
        ticketInterventoMap.put("idIntervento", idIntervento);

        //Floating button
        materialDesignFAM = (FloatingActionMenu) getActivity().findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) getActivity().findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) getActivity().findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) getActivity().findViewById(R.id.material_design_floating_action_menu_item3);

        materialDesignFAM.hideMenu(true);
        materialDesignFAM.setClosedOnTouchOutside(true);
        materialDesignFAM.showMenu(true);


        // materialDesignFAM.setOnTouchListener(new View.OnTouchListener() {
        //     @Override
        //     public boolean onTouch(View v, MotionEvent event) {
        //         //if(event.getAction() == MotionEvent.ACTION_UP){
        //
        //             if(materialDesignFAM.isOpened())
        //             { fl.setBackgroundColor(Color.GRAY);
        //
        //             }
        //             else
        //             { fl.setBackgroundColor(Color.TRANSPARENT); }
        //             // Do what you want
        //             return true;
        //         }
        //         return true; // consume the event
        //     }
        // });


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               // DialogFragment newFragment = new DialogNuovaSegnalazione();
               // newFragment.show(getActivity().getFragmentManager(), "NuovaSegnalazione");
               // getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               // DialogFragment newFragment = new DialogNuovoMessaggio();
               // newFragment.show(getActivity().getFragmentManager(), "NuovoMessaggio");
               // getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  }
        });

        //Firebase retrieve data
        Query intervento;
        intervento = FirebaseDB.getInterventi().orderByKey().equalTo(idIntervento);

        intervento.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ticketInterventoMap = new HashMap<String, Object>();
                ticketInterventoMap.put("idIntervento", idIntervento);

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ticketInterventoMap.put(child.getKey(), child.getValue());
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
                        ticketInterventoMap.get("stato").toString(),
                        ticketInterventoMap.get("priorità").toString(),
                        ticketInterventoMap.get("foto").toString(),
                        ticketInterventoMap.get("url").toString(),
                        "ciao", "ciao", "ciao", "ciao", "ciao"
                );

                try {
                    TdataTicket.setText(ticketIntervento.getDataTicket().toString());
                    Tstabile.setText(ticketIntervento.getStabile().toString());
                    Toggetto.setText(ticketIntervento.getOggetto().toString());
                    TAmministratore.setText(ticketIntervento.getUidAmministratore().toString());
                    Trichiesta.setText(ticketIntervento.getRichiesta().toString());
                    Tindirizzo.setText("indirizzo ancora non presente");
                    //Tfoto.setQUALCOSA TODO: AGGIUNGERE FOTO e INDIRIZZO

                    if (ticketInterventoMap.get("foto").toString() != "-") {
                        Picasso.with(context).load(ticketIntervento.getUrl()).fit().centerCrop().into(Tfoto);
                    }


                    TidTicketIntervento.setText(ticketIntervento.getIdTicketIntervento());
                } catch (NullPointerException e) {
                }
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

