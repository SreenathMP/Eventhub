package com.rubus.event_hub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_college extends AppCompatActivity {


    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    protected Button btnSignup, btnLogin, btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_college);
        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);


        if (auth.getCurrentUser() != null) {
            Intent intent=new Intent(this,create_event.class);
            startActivity(intent);
            finish();
        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_college.this,register_college.class));
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(loginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                btnLogin.setEnabled(false);
                btnLogin.setClickable(false);

                if (TextUtils.isEmpty(email)) {
                    btnLogin.setEnabled(true);
                    btnLogin.setClickable(true);
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    btnLogin.setEnabled(true);
                    btnLogin.setClickable(true);
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login_college.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    btnLogin.setEnabled(true);
                                    btnLogin.setClickable(true);
                                    if (password.length() < 6) {
                                        inputPassword.setError("minimum password length is 6");

                                    }

                                    else {
                                        Toast.makeText(login_college.this, "Username or Password Is Incorrect", Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    final String uid= auth.getCurrentUser().getUid();
                                    final String account="college";

                                    FirebaseDatabase.getInstance().getReference("eventhub/users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            login_auth verify_user=dataSnapshot.getValue(login_auth.class);

                                            try {
                                                if(verify_user.getAccount().equals(account))
                                                {
                                                    Toast.makeText(login_college.this, "login as college", Toast.LENGTH_LONG).show();
                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putInt("college",1);
                                                    editor.commit();
                                                    Intent intent=new Intent(login_college.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                else {
                                                    auth.signOut();
                                                    btnLogin.setEnabled(true);
                                                    btnLogin.setClickable(true);
                                                    Toast.makeText(login_college.this, "No Such Account ", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            catch (Exception e){

                                                Log.d("login error",e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                }
                            }
                        });
            }
        });
    }


}

