package com.rubus.event_hub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class profile_ui extends AppCompatActivity {


    TextView btn_view_profile,btn_pending_events,btn_create_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ui);

        btn_view_profile=(TextView) findViewById(R.id.btn_view_profile);
        btn_pending_events=findViewById(R.id.btn_pending_events);
        btn_create_event=findViewById(R.id.btn_create_event);

       String user= getIntent().getStringExtra("user");

btn_pending_events.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

startActivity(new Intent(profile_ui.this,pending_events.class));

    }
});


        btn_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(profile_ui.this,create_event_college.class));


            }
        });
    }


}