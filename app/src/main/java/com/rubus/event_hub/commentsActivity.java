package com.rubus.event_hub;

import android.content.SharedPreferences;
import android.icu.util.TimeZone;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class commentsActivity extends AppCompatActivity {

    protected Toolbar commentToolbar;

    private EditText comment_field;
    protected ImageView comment_post_btn;

    protected RecyclerView comment_list;
    protected CommentsRecyclerAdapter commentsRecyclerAdapter;
    protected List<Comments> commentsList;



    protected String event_post_id;
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);






         event_post_id = getIntent().getStringExtra("event_post_id");
         System.out.println(event_post_id);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

       final int i=  pref.getInt("college",0);
editor.apply();
       // event_post_id="-Ld-9bu2WeKhr2adr5Da";

        comment_field = findViewById(R.id.comment_field);
        comment_post_btn = findViewById(R.id.comment_post_btn);
        comment_list = findViewById(R.id.comment_list);

        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentsRecyclerAdapter);





        FirebaseDatabase.getInstance().getReference("eventhub/posts/comments").child(event_post_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Comments comments = dataSnapshot.getValue(Comments.class);
                System.out.println(dataSnapshot.getValue());
                commentsList.add(comments);
                commentsRecyclerAdapter.notifyDataSetChanged();
                System.out.println(dataSnapshot.getValue());



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        comment_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  String user;

                  if(i==1)
                      user="college_profiles";

                     else
                         user="student_profiles";

                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("eventhub/"+user);
                    Query query;
                    final String userUid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                    query = myRef.child(userUid);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user_profile profile = dataSnapshot.getValue(user_profile.class);
                            current_user_id=profile.getName();
                            comment(current_user_id);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }

                else
                    Toast.makeText(commentsActivity.this, "login to comment", Toast.LENGTH_SHORT).show();



            }
        });



    }

    protected void comment(String current_user_id){
        String comment_message = comment_field.getText().toString();
        if (!comment_message.equals("") && !current_user_id.equals("")) {

            Comments comment = new Comments(comment_message, current_user_id);

            FirebaseDatabase.getInstance().getReference("eventhub/posts/comments").child(event_post_id).push().setValue(comment);
            Toast.makeText(commentsActivity.this, "comment", Toast.LENGTH_SHORT).show();
        }

           comment_field.getText().clear();
    }
}
