package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso.InterventoInCorso.DettaglioIntervento;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.firebase.client.ValueEventListener;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.View.Dialog.DialogConfermaChiusuraIntervento;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.MainDrawer;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso.InterventoInCorso.RapportiIntervento.InserimentoRapportoIntervento;
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

public class DettaglioInterventoInCorso extends android.support.v4.app.Fragment {

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
    ConstraintLayout Chiudi;
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

        showProgressDialog();

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
        TidTicketIntervento = (TextView) getActivity().findViewById(R.id.Hidden_ID);
        Chiudi = (ConstraintLayout) getActivity().findViewById(R.id.btnChiudiIntervento);
        ChiamaAmministratore = (ImageView) getActivity().findViewById(R.id.imageViewChiamaAmministratore);
        Mappa = (ImageView) getActivity().findViewById(R.id.btnMappa);

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

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#FF0000"));
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#FFFF00"));
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#00FF00"));
                materialDesignFAM.close(true);
            }
        });


                Chiudi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), InserimentoRapportoIntervento.class);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
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
                        //try {
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

                        try {
                            TdataTicket.setText(ticketIntervento.getDataTicket().toString());
                            Tstabile.setText(ticketIntervento.getNomeStabile().toString());
                            Toggetto.setText(ticketIntervento.getOggetto().toString());
                            TAmministratore.setText(ticketIntervento.getNomeAmministratore().toString());
                            Trichiesta.setText(ticketIntervento.getRichiesta().toString());
                            Tindirizzo.setText(ticketIntervento.getIndirizzoStabile().toString());


                            if ( ! "-".equals( ticketIntervento.getFoto() ) ) {
                                Picasso.with(context).load(ticketIntervento.getFoto()).fit().centerCrop().into(Tfoto);
                            }

                            TidTicketIntervento.setText(ticketIntervento.getIdTicketIntervento());



                        //Setta priorità floating action button
                            String priorità = ticketIntervento.getPriorità();
                            switch(priorità) {
                                case "3" :
                                {
                                    materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#FF0000"));
                                    break;
                                }

                                case "2":
                                {
                                    materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#FFFF00"));
                                    break;
                                }

                                case "1":
                                {
                                    materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#00FF00"));
                                    break;
                                }

                                default:
                            }
                        } catch (NullPointerException e) {}

                        hideProgressDialog();

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

    //Inizio Gestione dialog caricamento dati
    //
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            setMessage();
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void setMessage() {

        mProgressDialog.setMessage("Caricamento in corso...");
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
    //
    //Fine gestione dialog caricamento dati
}


// todo: interessa
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

