package com.rubus.event_hub;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class pending_events extends AppCompatActivity
        {

    protected List<events> eventsList = new ArrayList<>();
    protected List<events> events = new ArrayList<>();
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressBar progressBar;
    protected pending_event_adapter eAdapter;
    public String keyid, start_key,college;


    private int mTotalItemCount = 0;
    private int mLastVisibleItemPosition;
    private boolean mIsLoading = false;
    protected int mposts = 1;
    boolean last_item = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Toolbar  mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mActionBarToolbar);
            getSupportActionBar().setTitle("Pending events");

        }

        catch (Exception e){

            Log.d("pending events",e.getMessage());
        }

        finally {
            Log.d("pending event","ignored");
        }

      Toolbar  mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Pending events");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.pBar);
        eAdapter = new pending_event_adapter(getApplicationContext(), eventsList);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(eAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference colRef = database.getReference("eventhub/college_profiles");

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            Query query1 = colRef.child(userUid);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user_profile profile = dataSnapshot.getValue(user_profile.class);
                    college= profile.getCollege();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        prepare_eventData(mposts);



        eAdapter.setOnItemClickListener(new pending_event_adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("mainActivity", "onItemClick position: " + position);
                Log.d("mainActivity", "onItemClick position: " + v);
                String post_id = eventsList.get(position).getPostId();

                   Intent intent = new Intent(pending_events.this, pending_event_detail.class);
                   intent.putExtra("postId", post_id);
                   startActivity(intent);
                   finish();




            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d("mainActivity", "onItemLongClick pos = " + position);
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                mTotalItemCount = mLayoutManager.getItemCount();
                mLastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

                if (!mIsLoading && mTotalItemCount <= (mLastVisibleItemPosition + mposts)) {

                    prepare_neweventData(keyid);

                    // System.out.println(keyid);
                    mIsLoading = true;


                }


            }
        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000); // Delay in millis

                last_item = false;
                swipeRefreshLayout.setEnabled(true);

                prepare_eventData(mposts);




            }
        });




    }


    protected void prepare_eventData(int mposts) {
        //events event = new events("Mad Max: Fury Road", "Action & Adventure");
        //eventsList.add(event);


        progressBar.setVisibility(ProgressBar.VISIBLE);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("eventhub/events");


        final Query query;

        query = myRef.orderByKey().limitToLast(mposts);

        events.clear();

        // String userId=myRef.push().getKey();
        // myRef.child(userId).setValue()




        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {


                events event = dataSnapshot.getValue(events.class);

                if (!last_item) {


                    if (prevChildKey == null) {
                        keyid = dataSnapshot.getKey();
                        System.out.println(keyid);


                    }


                    System.out.println("first method");
                    last_item = true;
                    eventsList.clear();

                    if(!event.isStatus() && event.getCollege().equals(college))
                          eventsList.add(event);


                    eAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(ProgressBar.GONE);

                    mIsLoading = false;



                    if (dataSnapshot.getChildrenCount() > 1) {
                        prepare_neweventData(keyid);
                        mIsLoading = true;
                    }


                }
            }


            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mIsLoading = false;

                Log.w("event", "Failed to read value.", error.toException());
            }


        });


    }


    protected void prepare_neweventData(final String mposts) {
        //events event = new events("Mad Max: Fury Road", "Action & Adventure");
        //eventsList.add(event);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("eventhub/events");
        Query query;


        query = myRef.orderByKey().endAt(mposts).limitToLast(4);

        //System.out.println(mposts);


        // String userId=myRef.push().getKey();
        // myRef.child(userId).setValue();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();
                int post = 0;

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    post++;

                    if (post == 1)
                        start_key = dataSnapshot1.getKey();

                    if (!keyid.equals(dataSnapshot1.getKey())) {


                        System.out.println("Second method");
                        events event = dataSnapshot1.getValue(events.class);

                        if(!event.isStatus() && event.getCollege()==college)
                             events.add(event);


                        mIsLoading = false;

                    } else
                        keyid = start_key;
                }

                Collections.reverse(events);
                eventsList.addAll(events);
                eAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mIsLoading = false;
            }
        });


    }






}











