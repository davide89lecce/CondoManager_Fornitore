package com.gambino_serra.condomanager_fornitore.Old_Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.android.volley.Response;
import com.gambino_serra.condomanager_fornitore.Old_Model.HTTPRequest.Utente.HTTPRequestLogin;
import com.gambino_serra.condomanager_fornitore.tesi.R;
import com.gambino_serra.condomanager_fornitore.Old_View.Amministratore.Home.AmministratoreHomeActivity;
import com.gambino_serra.condomanager_fornitore.Old_View.Condomino.Home_old.CondominoHomeActivity;
import com.gambino_serra.condomanager_fornitore.Old_View.Fornitore.Home.FornitoreHomeActivity;
import com.gambino_serra.condomanager_fornitore.Old_View.Utente.RegisterActivity;

import static android.content.Context.MODE_PRIVATE;


public class Login {

    private static final String MY_PREFERENCES = "preferences";
    private static final String TIPO_UTENTE = "tipoUtente";
    private static final String LOGGED_USER = "username";

    /**
     * Il metodo verifica che i parametri username e password non siano vuoti
     */
    public static void checkFields(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener, String username, String password){

        if (!username.equals("") && !password.equals("")) {
            requestLogin(username, password, context, listener, errorListener);
            }
        else {
            Toast.makeText(context.getApplicationContext(), R.string.insert_name_password, Toast.LENGTH_LONG).show();
            }
    }

    /**
     * Il metodo effettua la richiesta al database remoto per l'autenticazione dell'utente
     */
    public static void requestLogin(String username, String password, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        HTTPRequestLogin.RequestLogin(context, username, password, listener, errorListener);
    }

    /**
     * Il metodo verifica la risposta ricevuta dalla richiesta di autenticazione e indirizza l'utente nella sua Activity.
     */
    public static void checkLogin(String response, Context context, String username) {

        if (!response.equals("null")) {

            if(response.equals("correct_login_amministratore")){

                setPrefs(context.getApplicationContext(), username, "A");
                Intent in = new Intent(context.getApplicationContext(), AmministratoreHomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            } else if(response.equals("correct_login_fornitore")) {

                setPrefs(context.getApplicationContext(), username, "F");
                Intent in = new Intent(context.getApplicationContext(), FornitoreHomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            } else if(response.equals("correct_login_condomino")) {

                setPrefs(context.getApplicationContext(), username, "C");
                Intent in = new Intent(context.getApplicationContext(), CondominoHomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            } else if(response.equals("no_login")) {

                Toast.makeText(context.getApplicationContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Il metodo inizializza le SharedPreferences dell'applicazione.
     */
    public static void setPrefs(Context context, String username, String tipoUtente) {

        final SharedPreferences sharedPrefs = context.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LOGGED_USER, username);  // usato come identificativo utente.
        editor.putString(TIPO_UTENTE, tipoUtente); // usato come identificativo del tipo di utente.
        editor.apply();
    }

    /**
     *  Il metodo verifica che le SharedPreferences contengano dati, nel caso contrario l'utente risulterà non connesso.
     */
    public static void userState(Context context){

        final SharedPreferences sharedPrefs = context.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        if (!sharedPrefs.getAll().isEmpty()) {
            getStatusAndGoHome(context);
        }
    }

    /**
     * Il metodo verifica il tipo di utente e lo indirizza nella sua Home Activity.
     */
    private static void getStatusAndGoHome(Context context) {

        final SharedPreferences sharedPrefs = context.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        if (sharedPrefs.getString(TIPO_UTENTE, "").equals("A")) {
            Intent in = new Intent(context.getApplicationContext(), AmministratoreHomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
        else if (sharedPrefs.getString(TIPO_UTENTE, "").equals("F")) {
            Intent in = new Intent(context.getApplicationContext(), FornitoreHomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
        else if (sharedPrefs.getString(TIPO_UTENTE, "").equals("C")) {
            Intent in = new Intent(context.getApplicationContext(), CondominoHomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }

    /**
     * Il metodo avvia l'activity per la registrazione utente.
     */
    public static void startRegisterActivity(Context context) {
        Intent in = new Intent(context.getApplicationContext(), RegisterActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }
}