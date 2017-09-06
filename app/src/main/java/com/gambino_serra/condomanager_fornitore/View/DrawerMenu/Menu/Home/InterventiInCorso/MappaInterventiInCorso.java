package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.gambino_serra.condomanager_fornitore.Model.Entity.MarkerIntervento;
import com.gambino_serra.condomanager_fornitore.Model.Entity.TicketIntervento;
import com.gambino_serra.condomanager_fornitore.Model.FirebaseDB.FirebaseDB;
import com.gambino_serra.condomanager_fornitore.View.DrawerMenu.MainDrawer;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * La classe gestisce l'invio della richiesta, della relativa posizione e la scelta dell'Helper al quale inviare la richiesta di coda,
 * mediante l'uso delle mappe e l'utilizzo delle GoogleApiClient.
 */
@RuntimePermissions
public class MappaInterventiInCorso extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, OnMapReadyCallback, GoogleMap.OnMapClickListener{

    static final int TIME_DIALOG_ID1 = 1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    final private static String MY_PREFERENCES = "kiuPreferences";
    final private static String EMAIL = "email";
    final Bundle bundle = new Bundle();
    ImageView closeButton;
    View view;
    StringBuilder ora_richiesta;
    LatLng ltln;
    String address;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private int ora;
    private int minuti;

    private FirebaseAuth firebaseAuth;
    private String uidFornitore;
    Map<String, Object> ticketInterventoMap;
    ArrayList<MarkerIntervento> interventi;
    private ArrayList<TicketIntervento> data;

    // mTimeSetListener viene avvalorato nel momento in cui l'utente imposta un orario nella Dialog.
    private android.app.TimePickerDialog.OnTimeSetListener mTimeSetListener = new android.app.TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ora = hourOfDay;
            minuti = minute;
            updateDisplay();
        }
    };

    /**
     * Il metodo formatta l'orario.
     */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappa_interventi_in_corso);

        firebaseAuth = FirebaseAuth.getInstance();
        data = new ArrayList<TicketIntervento>();
        ticketInterventoMap = new HashMap<String,Object>();
        interventi = new ArrayList<MarkerIntervento>();

        closeButton = (ImageView) findViewById(R.id.closeButton2);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MappaInterventi);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Errore caricamento mappe", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Il metodo permette di caricare le mappe di Google.
     */
    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            MappaInterventiInCorsoPermissionsDispatcher.getMyLocationWithCheck(this);
            map.setOnMapClickListener(this);
            }
        else
            {
            Toast.makeText(this, "Errore caricamento mappe", Toast.LENGTH_SHORT).show();
            }
    }

    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getMyLocation() {
        if (map != null) {
            //Adesso che la mappa e' caricata puo' ricevere la psosizione
            map.setMyLocationEnabled(true);
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }

    /**
     * Il metodo permette la connessione al Client dei servizi di Google delle mappe.
     */
    protected void connectClient() {
        // Connette il Client
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Il metodo e' chiamato quando l'Activity torna visibile (foreground).
     */
    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    /*
     * Il metodo e' chiamato quando l'Activity perde la visibilita'.
     */
    @Override
    protected void onStop() {
        // Disconnette il Client
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Il metodo gestisce i risultati ritornati dal FragmentActivity dei Google Play services.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide cosa fare in base al codice di richiesta originale.
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                //Se il risultato del codice e' Activity.RESULT_OK, prova a riconnettersi.
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    /**
     * Il metodo verifica che i servizi di Google siano disponibili, in caso contrario una Dialog viene visualizzata al'utente.
     */
    private boolean isGooglePlayServicesAvailable() {

        // Verifica disponibilita' dei servizi di Google
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // Se Google Play services sono disponibili
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
            }
        else {
            // Ricevo la Error Dialog dai servizi Google Play.
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // Se Google Play services puo' fornire una Error Dialog
            if (errorDialog != null) {
                // Creazione di un DialogFragment
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getFragmentManager(), "Location Updates");
                }
            return false;
        }
    }

    /**
     * Il metodo viene invocato dal Location Services quando la richiesta di connessione al client
     * e' avvenuta con successo. In questo momento si puo' richiedere la posizione corrente.
     */
    @Override
    public void onConnected(Bundle dataBundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
            }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null)
            {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.animateCamera(cameraUpdate);
            }
        else
            {
            Toast.makeText(this, "Abilita GPS", Toast.LENGTH_SHORT).show();
            }
        startLocationUpdates();
    }

    /**
     * Il metodo permette di aggiornare la posizione.
     */
    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
            }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void onLocationChanged(Location location) { }

    /**
     * Il metodo e' invocato dal Location Services se la connessione con il client si interrrompe a causa di un errore.
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconesso", Toast.LENGTH_SHORT).show();
            }
        else if (i == CAUSE_NETWORK_LOST)
            {
            Toast.makeText(this, "Rete dati assente", Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * Il metodo viene invocato dal Location Services se lo stesso servizio fallisce
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // Se il problema di connessione e' risolvibile (da Google) allora viene
        // inviato un Intent all'Activity predisposta a risolvere il problema.
        if (connectionResult.hasResolution()) {
            try
                {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                }
            catch (IntentSender.SendIntentException e)//L'eccezione e' sollevata nel caso in cui l'Intent viene eliminato.
                {
                e.printStackTrace();  // Log the error
                }
        }
        else
            {
            Toast.makeText(getApplicationContext(), "Servizio GPS non disponibile", Toast.LENGTH_LONG).show();
            }
    }

    /**
     * Il metodo permette di caricare e visualizzare la mappa nella UI dell'applicazione e le sue componenti invocando il metodo loadMap(map).
     */
    @Override
    public void onMapReady(GoogleMap map) {

        loadMap(map);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent setting = new Intent(MappaInterventiInCorso.this, MainDrawer.class);
                startActivity(setting);
                }
        });

        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Seleziona il marker sulla mappa", Snackbar.LENGTH_LONG);
        View view1 = snack.getView();
        TextView tv = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    /**
     * Il metodo permete di posizionare il marker dell'utente e di visualizzare sulla mappa gli Helper disponibili.
     */
    @Override
    public void onMapClick(LatLng latLng) {

        map.clear();
        ltln = latLng;
        map.addMarker(new MarkerOptions()
                .position(ltln)
                .title("Io"));

        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Seleziona un intervento sulla mappa", Snackbar.LENGTH_LONG);
        View view1 = snack.getView();
        TextView tv = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try
            {
            addresses = geocoder.getFromLocation(ltln.latitude, ltln.longitude, 1);
            }
        catch (IOException ioException)
            {
            Log.d("errore", "lettura coordinate");
            }
        catch (IllegalArgumentException illegalArgumentException)
            {
            Log.d("errore", "coordinate non valide");
            }

        //Gestione del caso che non sia trovato un indirizzo valido
        if ((addresses != null) && (addresses.size() > 3))
            {
            Address addr = addresses.get(0);
            address = addr.getAddressLine(0).toString() + " " + addr.getAddressLine(1).toString() + " " + addr.getAddressLine(2).toString();
            address = address.replace("'", "").toString();
            }
        else if ((addresses != null) && (addresses.size() == 3))
            {
            Address addr = addresses.get(0);
            address = addr.getAddressLine(0).toString() + " " + addr.getAddressLine(1).toString() + " " + addr.getAddressLine(2).toString();
            address = address.replace("'", "").toString();
            }
        else if ((addresses != null) && (addresses.size() == 2))
            {
            Address addr = addresses.get(0);
            address = addr.getAddressLine(0).toString() + " " + addr.getAddressLine(1).toString();
            address = address.replace("'", "").toString();
            }
        else if ((addresses != null) && (addresses.size() == 1))
            {
            Address addr = addresses.get(0);
            address = addr.getAddressLine(0).toString();
            address = address.replace("'", "").toString();
            }
        else
            {
            //address = getResources().getString(R.string.address_not_available);
            }

        //Caricamento interventi su mappa

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
          ticketInterventoMap = new HashMap<String, Object>();
         ticketInterventoMap.put("id", dataSnapshot.getKey()); //primo campo del MAP
         // per ognuno dei figli presenti nello snapshot, ovvero per tutti i figli di un singolo nodo Interv
           // recuperiamo i dati per inserirli nel MAP
             for (DataSnapshot child : dataSnapshot.getChildren())
                {
                ticketInterventoMap.put(child.getKey(), child.getValue());
                }

        visualizzaMarkers(ticketInterventoMap);

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

        /*
        LatLng ltlnHelpers;
        final ArrayList<JsonProfiloHelper> productList = new JsonConverter<JsonProfiloHelper>().toArrayList(response, JsonProfiloHelper.class);
        for (final JsonProfiloHelper object : productList) {
            ltlnHelpers = new LatLng(object.coordinate_latitudine, object.coordinate_longitudine);
            map.addMarker(new MarkerOptions()
                    .position(ltlnHelpers)
                    .title(object.cognome.toString() + " " + object.nome.toString())
                    .snippet(String.valueOf(getResources().getString(R.string.click_here_for_details)))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                             @Override
                                             public void onInfoWindowClick(Marker arg0) {
                                                 for (final JsonProfiloHelper object : productList) {

                                                    // if ((object.cognome.toString() + " " + object.nome.toString()).equals(arg0.getTitle().toString())) {
                                                         //Fragment newFragment = new DettaglioInterventoInCorso();
                                                         /*
                                                         bundle.putString("email_kiuer", prefs.getString(EMAIL, ""));
                                                         bundle.putString("email_helper", object.email.toString());
                                                         bundle.putString("inizio_disp", object.inizio_disp.toString());
                                                         bundle.putString("fine_disp", object.fine_disp.toString());
                                                         bundle.putString("tariffa_oraria", object.tariffa_oraria.toString());
                                                         bundle.putString("coordinate_latitudine", String.valueOf(ltln.latitude));
                                                         bundle.putString("coordinate_longitudine", String.valueOf(ltln.longitude));
                                                         bundle.putString("luogo", address.toString());
                                                         bundle.putString("code_effettuate", object.code_effettuate.toString());
                                                         bundle.putString("ora_richiesta", getResources().getString(R.string.set_request_time));
                                                         bundle.putString("nome", object.nome.toString());
                                                         bundle.putString("cognome", object.cognome.toString());
                                                         bundle.putFloat("rating", object.rating);
                                                         bundle.putInt("num_feedback", object.num_feedback);
                                                         */
                                                         //newFragment.setArguments(bundle);
                                                         //newFragment.show(getFragmentManager(), "KiuerMaps");
                                                     //}
                                               //  }
                                            // }
                                         //});


        /*
        //Lettura degli Helper disponibili
        final SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String url = "http://www.kiu.altervista.org/read_helpers.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("null")) {
                    LatLng ltlnHelpers;
                    final ArrayList<JsonProfiloHelper> productList = new JsonConverter<JsonProfiloHelper>().toArrayList(response, JsonProfiloHelper.class);
                    for (final JsonProfiloHelper object : productList) {
                        ltlnHelpers = new LatLng(object.coordinate_latitudine, object.coordinate_longitudine);
                        map.addMarker(new MarkerOptions()
                                .position(ltlnHelpers)
                                .title(object.cognome.toString() + " " + object.nome.toString())
                                .snippet(String.valueOf(getResources().getString(R.string.click_here_for_details)))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker arg0) {
                            for (final JsonProfiloHelper object : productList) {
                                if ((object.cognome.toString() + " " + object.nome.toString()).equals(arg0.getTitle().toString())) {
                                    DialogFragment newFragment = new KiuerMapsDetails();
                                    bundle.putString("email_kiuer", prefs.getString(EMAIL, ""));
                                    bundle.putString("email_helper", object.email.toString());
                                    bundle.putString("inizio_disp", object.inizio_disp.toString());
                                    bundle.putString("fine_disp", object.fine_disp.toString());
                                    bundle.putString("tariffa_oraria", object.tariffa_oraria.toString());
                                    bundle.putString("coordinate_latitudine", String.valueOf(ltln.latitude));
                                    bundle.putString("coordinate_longitudine", String.valueOf(ltln.longitude));
                                    bundle.putString("luogo", address.toString());
                                    bundle.putString("code_effettuate", object.code_effettuate.toString());
                                    bundle.putString("ora_richiesta", getResources().getString(R.string.set_request_time));
                                    bundle.putString("nome", object.nome.toString());
                                    bundle.putString("cognome", object.cognome.toString());
                                    bundle.putFloat("rating", object.rating);
                                    bundle.putInt("num_feedback", object.num_feedback);
                                    newFragment.setArguments(bundle);
                                    newFragment.show(getFragmentManager(), "KiuerMaps");
                                }
                            }
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_update_volley, Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        */
    }

    /**
     * Il metodo gestisce la creazione della Dialog TimePicker.
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID1:
                return new android.app.TimePickerDialog(this, mTimeSetListener, ora, minuti, false);
        }
        return null;
    }

    // updates the time we display in the TextView

    /**
     * Il metodo permette di aggiornare il l'orario e visualizzarlo nella Dialog di richiesta.
     */
    private void updateDisplay() {
        //ora_richiesta = new StringBuilder().append(pad(ora)).append(":").append(pad(minuti));
        //DialogFragment newFragment = new KiuerMapsDetails();
        //bundle.putString("ora_richiesta", ora_richiesta.toString());
        //newFragment.setArguments(bundle);
        //newFragment.show(getFragmentManager(), "KiuerMaps");
        }

    /**
     * Il metodo visualizza il TimePicker.
     */
    public void showTimePickerRichiesta() {
        showDialog(TIME_DIALOG_ID1);
        }


    /**
     * Il metodo gestisce la visualizzazione della richiesta di coda inviata e lo reindirizza alla home del Kiuer.
     */
    public void richiestaInviata() {
        Intent kiuerHomeActivity = new Intent(MappaInterventiInCorso.this, DettaglioInterventoInCorso.class);
        startActivity(kiuerHomeActivity);
        Toast.makeText(getApplicationContext(), "Richiesta inviata", Toast.LENGTH_SHORT).show();
        }

    /**
     * Il metodo gestisce la visualizzazione della richiesta di coda non inviata.
     */
    public void richiestaNonInviata() {
        Toast.makeText(getApplicationContext(), "Richiesta non inviata", Toast.LENGTH_SHORT).show();
        }

    /**
     * Il metodo gestisce la comunicazione, tramite Dialog, degli errori che possono verificarsi.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
            }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
            }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
            }
    }

    public void visualizzaMarkers(final Map<String, Object> ticketInterventoMap){

        final Map<String, Object> ticketInterventoMap2 = new HashMap<String, Object>();
        Query query2;
        query2 = FirebaseDB.getStabili().orderByKey().equalTo(  ticketInterventoMap.get("stabile").toString() );
        query2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                //HashMap temporaneo per immagazzinare i dati dello stabile
                // per ognuno dei figli presenti nello snapshot, ovvero per tutti i figli di un singolo nodo Stabile
                // recuperiamo i dati per inserirli nel MAP
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ticketInterventoMap2.put(child.getKey(), child.getValue());

                }

                // Avvaloriamo una variabile TicketIntervento appositamente creata in modo da inserire poi questo
                // oggetto all'interno di un Array di interventi che utilizzeremo per popolare la lista Recycle
                try {
                    MarkerIntervento markerIntervento = new MarkerIntervento(
                            ticketInterventoMap.get("id").toString(),
                            ticketInterventoMap.get("data_ticket").toString(),
                            ticketInterventoMap.get("oggetto").toString(),
                            ticketInterventoMap.get("richiesta").toString(),
                            ticketInterventoMap.get("stabile").toString(),
                            ticketInterventoMap.get("stato").toString(),
                            ticketInterventoMap.get("priorità").toString(),
                            ticketInterventoMap2.get("latitudine").toString(),
                            ticketInterventoMap2.get("longitudine").toString(),
                            ticketInterventoMap2.get("nome").toString(),
                            ticketInterventoMap2.get("indirizzo").toString()
                    );

                    if (markerIntervento.getStato().equals("in corso")) {
                        // inserisce l'oggetto ticket nell'array interventi
                        interventi.add(markerIntervento);

                        //codice latitudine longitudine
                        LatLng ltlnHelpers;
                        ltlnHelpers = new LatLng(Double.parseDouble(markerIntervento.getLatitudine()), Double.parseDouble(markerIntervento.getLongitudine()));
                        map.addMarker(new MarkerOptions()
                                .position(ltlnHelpers)
                                .title(markerIntervento.getOggetto().toString())
                                .snippet(markerIntervento.getRichiesta().toString())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }

                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Non riesco ad aprire l'oggetto " + e.toString(), Toast.LENGTH_LONG).show();
                }
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
}