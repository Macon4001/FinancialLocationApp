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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {



    LinearLayout register;
    Button login;
    EditText email,password;
    String emailstr,passwordstr;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init Views
        register =  findViewById(R.id.register);
        login =  findViewById(R.id.login);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditTextValueCheck check = new EditTextValueCheck();
                if (check.hasValue(email) && check.hasValue(password)) {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    emailstr = email.getText().toString().trim();
                    passwordstr = password.getText().toString().trim();


                    mAuth.signInWithEmailAndPassword(emailstr,passwordstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {



                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference walletentries = database.getReference("wallet-entries");

                                walletentries.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                            if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid())){
                                                progressDialog.dismiss();

                                                // move to next screen
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }else{
                                                progressDialog.dismiss();

                                                Toast.makeText(getApplicationContext(),"User Don't Exist",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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