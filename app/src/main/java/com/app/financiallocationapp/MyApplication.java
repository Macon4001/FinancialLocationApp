package com.app.financiallocationapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance("https://financial-planning-app-42381-default-rtdb.firebaseio.com").setPersistenceEnabled(true);
    }
}
