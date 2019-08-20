package com.rubus.event_hub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class register_selection extends AppCompatActivity {

    protected Button student_signup,college_signup,btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_selection);

        student_signup=findViewById(R.id.btn_register_student);
        college_signup=findViewById(R.id.btn_register_college);
        btn_login=findViewById(R.id.btn_login);

    }

    @Override

    public void onStart(){
        super.onStart();

        student_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register_selection.this, register_student.class));
                finish();

            }
        });

        college_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register_selection.this, register_college.class));
                finish();
            }
        });

        btn_login .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register_selection.this, login_selection.class));
                finish();
            }
        });

    }
}

