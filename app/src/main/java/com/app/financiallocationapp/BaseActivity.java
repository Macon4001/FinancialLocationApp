package com.app.financiallocationapp;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    public String getUid() {

        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
