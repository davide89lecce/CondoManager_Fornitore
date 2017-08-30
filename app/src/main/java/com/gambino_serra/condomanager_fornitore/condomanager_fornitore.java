package com.gambino_serra.condomanager_fornitore;


import android.app.Application;

import com.firebase.client.Firebase;

public class condomanager_fornitore extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
