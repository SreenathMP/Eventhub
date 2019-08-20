package com.rubus.event_hub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class login_selection extends AppCompatActivity {


    protected Button student_login,college_login,btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);

        student_login=findViewById(R.id.btn_login_student);
        college_login=findViewById(R.id.btn_login_college);
        btn_signup=findViewById(R.id.btn_signup);

    }

    @Override

    public void onStart(){
        super.onStart();

student_login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(login_selection.this, login_student.class));
        finish();

    }
});

college_login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(login_selection.this, login_college.class));
        finish();
    }
});

       btn_signup .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_selection.this, register_selection.class));
                finish();
            }
        });

    }
}
