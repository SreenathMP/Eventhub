package com.rubus.event_hub;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class event_detail extends AppCompatActivity {

    private ImageView banner_image;
    private TextView event_title,event_location,event_date,phone_contact,gmail_contact,website_contact,event_description,event_venue;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eventRef = database.getReference("eventhub/events");

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    protected String url,postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        FrameLayout bannerLayout = findViewById(R.id.bannerLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("event details");

        banner_image=findViewById(R.id.banner_image);
        event_title=findViewById(R.id.event_title);
        event_location=findViewById(R.id.event_location);
        event_date=findViewById(R.id.event_date);
        phone_contact=findViewById(R.id.phone_number);
        gmail_contact=findViewById(R.id.gmail);
        website_contact=findViewById(R.id.website);
        event_description=findViewById(R.id.event_description);
        event_venue=findViewById(R.id.event_venue);

        postId= getIntent().getStringExtra("postId");

        bannerLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent intent=new Intent(event_detail.this,LongImageActivity.class);
                intent.putExtra("url",url);
               startActivity(intent);
            }
        });

display_event_detail();






    }



    void display_event_detail(){


        Query query;

        query = eventRef.child(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events event = dataSnapshot.getValue(events.class);
                url=event.getBannerUrl();
                Picasso.with(getApplicationContext()).load(R.drawable.placeholder_banner).into(banner_image);

                if (!event.getBannerUrl().equals("0")) {
                    Picasso.with(getApplicationContext()).load(event.getBannerUrl()).into(banner_image);
                }



                event_title.setText(event.getEvent_title());
                event_location.setText(event.getVenue());
                event_date.setText(event.getDate1()+" "+event.getTime1()+" To "+event.getDate2());
                phone_contact.setText(event.getPhone_number());
                event_description.setText(event.getDescription());
                event_venue.setText(event.getVenue());
                gmail_contact.setText(event.getEmail());
                website_contact.setText(event.getWebsite());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_event_detail, menu);

        return super.onCreateOptionsMenu(menu);

    }
}
