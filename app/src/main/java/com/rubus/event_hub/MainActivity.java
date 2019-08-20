package com.rubus.event_hub;

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
import android.widget.ProgressBar;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected List<events> eventsList = new ArrayList<>();
    protected   List<events> events=new ArrayList<>();
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected  ProgressBar progressBar;
    protected event_adapter eAdapter;
    private FirebaseAuth auth;
    public String keyid,start_key;






    private int mTotalItemCount = 0;
    private int mLastVisibleItemPosition;
    private boolean mIsLoading = false;
    protected int mposts=1;
    boolean last_item=false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        auth = FirebaseAuth.getInstance();

          final TextView nav_username=headerview.findViewById(R.id.nav_username);
          final TextView nav_email=headerview.findViewById(R.id.nav_email);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        progressBar =findViewById(R.id.pBar);
        eAdapter = new event_adapter(getApplicationContext(),eventsList);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(eAdapter);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
       final DatabaseReference myRef = database.getReference("eventhub/posts/comments");

        prepare_eventData(mposts);





        eAdapter.setOnItemClickListener(new event_adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("mainActivity", "onItemClick position: " + position);
                Log.d("mainActivity", "onItemClick position: " + v);
               String post_id= eventsList.get(position).getPostId();


               Intent intent=new Intent(MainActivity.this,event_detail.class);
               intent.putExtra("postId",post_id);
               startActivity(intent);
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


        FirebaseDatabase.getInstance().getReference("eventhub/posts/likes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                System.out.println("child added");
                String postId=dataSnapshot.getKey();

                update_likes(postId);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                System.out.println("child added");
                String postId=dataSnapshot.getKey();

                update_likes(postId);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("child removed");
                String postId=dataSnapshot.getKey();
                update_likes(postId);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("child added");
                String postId=dataSnapshot.getKey();

                update_comments(postId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("child added");
                String postId=dataSnapshot.getKey();

                update_comments(postId);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("child added");
                String postId=dataSnapshot.getKey();

                update_comments(postId);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Refresh items


                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                // Stop animation (This will be after 3 seconds)
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 2000); // Delay in millis

                        last_item = false;
                        swipeRefreshLayout.setEnabled(true);

                        prepare_eventData(mposts);


                    }
                });


        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {



                last_item=false;
                prepare_eventData(mposts);

                String user;
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {

                    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();


                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    int i=  pref.getInt("college",0);
                    editor.apply();
                    System.out.println(i);

                    if(i==1)
                    {
                        user="eventhub/college_profiles";
                    }

                    else{

                        user="eventhub/student_profiles";
                    }







            FirebaseDatabase.getInstance().getReference(user).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user_profile profile= dataSnapshot.getValue(user_profile.class);

                           try {



                               nav_username.setText(profile.getName());
                               nav_email.setText(profile.getEmail());
                           }

                           catch (Exception e){

                               Log.d("navigation drawer", e.getMessage());
                           }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                else {

                    nav_username.setText("eventhub");
                    nav_email.setText("rubus.com");
                }


            }
        });



    }


void update_likes(final String postId){

    for(int i=0;i<eventsList.size();i++) {

        if (eventsList.get(i).getPostId().equals(postId)) {
           final int j=i;
            FirebaseDatabase.getInstance().getReference("eventhub/posts/likes").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getChildrenCount());
                    eAdapter.notifyItemChanged(j, dataSnapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return;
        }
    }





}


    void update_comments(final String postId){
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference myRef = database.getReference("eventhub/posts/comments");

        for(int i=0;i<eventsList.size();i++) {


            System.out.println(eventsList.get(i).getPostId());
            System.out.println(postId);
            if (eventsList.get(i).getPostId().equals(postId)) {
                System.out.println("go");
                final int j=i;
                myRef.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.getChildrenCount());
                        long child=dataSnapshot.getChildrenCount();
                        int inchild=(int)child;
                        eAdapter.notifyItemChanged(j, inchild);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return;
            }
        }





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
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot,String prevChildKey) {



                events event = dataSnapshot.getValue(events.class);



                    if (!last_item) {


                        if (prevChildKey == null) {
                            keyid = dataSnapshot.getKey();
                            System.out.println(keyid);


                        }


                        System.out.println("first method");
                        last_item = true;
                        eventsList.clear();
                          if(event.isStatus()) {
                              eventsList.add(event);
                              eAdapter.notifyDataSetChanged();
                          }
                        progressBar.setVisibility(ProgressBar.GONE);

                        mIsLoading = false;

                        if (dataSnapshot.getChildrenCount() > 1) {
                            prepare_neweventData(keyid);
                            mIsLoading = true;
                        }





                }
            }


            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {}


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
                int post=0;

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                    post++;

                    if(post==1)
                        start_key = dataSnapshot1.getKey();

                    if (!keyid.equals(dataSnapshot1.getKey())) {



                        System.out.println("Second method");
                        events event = dataSnapshot1.getValue(events.class);

                         if(event.isStatus())
                              events.add(event);




                        mIsLoading = false;

                    }

                    else
                        keyid=start_key;
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




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        if (id == R.id.action_logout) {


            auth.signOut();
            Toast.makeText(MainActivity.this, "Signout ! ", Toast.LENGTH_LONG).show();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            return true;


        }




        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(auth.getCurrentUser()==null) {
            invalidateOptionsMenu();
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signin) {

            if(auth.getCurrentUser()==null)
                startActivity(new Intent(MainActivity.this, login_selection.class));

            else
                Toast.makeText(MainActivity.this, "Already login ! ", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_event) {
            if(auth.getCurrentUser()!=null) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

           int i=  pref.getInt("college",0);


                  if(i==1)
                     startActivity(new Intent(MainActivity.this,create_event_college.class));

                  else
                      startActivity(new Intent(MainActivity.this,create_event.class));
            }

            else
                Toast.makeText(MainActivity.this, "not logged in ! ", Toast.LENGTH_LONG).show();
        }

        else if (id == R.id.nav_myprofile){

                if(auth.getCurrentUser()!=null)
                {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    int i=  pref.getInt("college",0);


                    if(i==1)
                        startActivity(new Intent(MainActivity.this,profile_ui.class));
                }

                else
                    Toast.makeText(MainActivity.this, "not logged in ! ", Toast.LENGTH_LONG).show();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;




    }




}
