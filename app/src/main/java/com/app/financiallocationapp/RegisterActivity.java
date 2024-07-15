package com.app.financiallocationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {


    Button register;
    LinearLayout login;
    EditText email,password;
    String emailstr,passwordstr;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init Views
        register =  findViewById(R.id.register);
        login =  findViewById(R.id.login);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditTextValueCheck check = new EditTextValueCheck();
                if (check.hasValue(email) && check.hasValue(password)) {
                    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    emailstr = email.getText().toString().trim();
                    passwordstr = password.getText().toString().trim();


                    mAuth.createUserWithEmailAndPassword(emailstr,passwordstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference aa = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("currency");
                                aa.child("left").setValue(true);
                                aa.child("space").setValue(true);
                                aa.child("symbol").setValue("$");


                                DatabaseReference aa1 = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("userSettings");
                                aa1.child("dayOfMonthStart").setValue(0);
                                aa1.child("dayOfWeekStart").setValue(0);
                                aa1.child("homeCounterPeriod").setValue(0);
                                aa1.child("homeCounterType").setValue(0);
                                aa1.child("limit").setValue(0);

                                DatabaseReference walletentries = database.getReference("wallet-entries").child(FirebaseAuth.getInstance().getUid());
                                walletentries.child("timestamp").setValue(currentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();

                                        // move to next screen
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }




                        }
                    });

                }



            }
        });
    }


    @Override
    public void onBackPressed() {

    }
}