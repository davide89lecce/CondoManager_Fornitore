package com.gambino_serra.condomanager_fornitore.Model.FirebaseDB;

import android.content.Context;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;


public class FirebaseDB {

    private static Firebase firebase;
    private static DatabaseReference databaseReference;
    private static Context mCtx;
    private static String DB = new String ("https://condomanager-a5aa6.firebaseio.com/");

    private FirebaseDB(Context context) {
        mCtx = context;
    }

    public static synchronized Firebase getFirebase(){
        if(firebase == null) {
            firebase = new Firebase(DB);
        }
        return firebase;
    }



    public static synchronized Firebase getCondomini(){
        firebase = new Firebase(DB + "Condomini");
        return firebase;
    }

    public static synchronized Firebase getStabili(){
        firebase = new Firebase(DB + "Stabili");
        return firebase;
    }

    public static synchronized Firebase getInterventi(){
        firebase = new Firebase(DB + "Ticket_intervento");
        return firebase;
    }

    public static synchronized Firebase getMessaggiCondomino(){
        firebase = new Firebase(DB + "Messaggi_condomino");
        return firebase;
    }

    public static synchronized Firebase getFornitori(){
        firebase = new Firebase(DB + "Fornitori");
        return firebase;
    }
}
