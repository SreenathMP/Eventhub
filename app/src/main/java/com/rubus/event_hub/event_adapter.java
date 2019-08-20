package com.rubus.event_hub;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.like.OnLikeListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Date;
import java.util.List;



public class event_adapter extends RecyclerView.Adapter<event_adapter.MyViewHolder> {

    private List<events> eventsList;
    private Context mContext;
    private FirebaseAuth auth=FirebaseAuth.getInstance();


    private static ClickListener clickListener;





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView post_title,college_name,event_details,event_date,tv_comments,like_text,tv_like;
        private ImageView banner;
        private LikeButton likeButton;
        private RelativeLayout relativeLayout1,relativeLayout2;



        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            post_title = (TextView) view.findViewById(R.id.tv_post_title);
            college_name = (TextView) view.findViewById(R.id.tv_college_name);
            event_details=(TextView)view.findViewById(R.id.tv_event_details);
            event_date=(TextView)view.findViewById(R.id.tv_event_date);
            banner = (ImageView) view.findViewById(R.id.imgView_postPic);
            likeButton=(LikeButton)view.findViewById(R.id.like_button);
            tv_comments = (TextView) view.findViewById(R.id.tv_comment);
            tv_like=(TextView)view.findViewById(R.id.tv_like);
            relativeLayout1=(RelativeLayout) view.findViewById(R.id.like_box);
            relativeLayout2=(RelativeLayout) view.findViewById(R.id.cmnt_box);
            like_text=(TextView)view.findViewById(R.id.like_text);
            relativeLayout1.setOnClickListener(this);
            relativeLayout2.setOnClickListener(this);
            tv_comments.setOnClickListener(this);


        }

        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        event_adapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


    public event_adapter(Context context, List<events> eventsList) {
        this.eventsList = eventsList;
        this.mContext = context;
    }


    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);

        return new MyViewHolder(itemView);
    }


    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, List<Object> payloads) {
        if(!payloads.isEmpty()) {
            System.out.println("payloads");
            if (payloads.get(0) instanceof Long) {
                System.out.println(String.valueOf((Long) payloads.get(0)));

                holder.tv_like.setText(String.valueOf((Long) payloads.get(0)));

            }
            else {
                System.out.println(String.valueOf((Integer) payloads.get(0)));

                holder.tv_comments.setText(String.valueOf((Integer) payloads.get(0))+ " comments");
            }



        }else {
            super.onBindViewHolder(holder,position, payloads);
        }
    }

    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
       // events event = eventsList.get(getItemCount() - 1 - position);


        final events event = eventsList.get(position);

        if (mContext == null)
            return;






        holder.post_title.setText(event.getEvent_title());
        holder.college_name.setText(event.getCollege());
        holder.event_details.setText(event.getDescription());
        holder.event_date.setText("Date: "+event.getDate1()+"  "+event.getTime1());




        Picasso.with(mContext).load(event.getBannerUrl()).into(holder.banner);

        if (event.getBannerUrl().equals("0")) {
            holder.banner.setVisibility(View.GONE);
        } else {
            holder.banner.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(event.getBannerUrl()).into(holder.banner);

        }

        holder.likeButton.setLiked(false);

        if(auth.getCurrentUser()!=null) {

            String likeref="eventhub/userActivity/"+auth.getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference(likeref).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if(dataSnapshot.hasChild(event.getPostId())) {


                            System.out.println("postId = " + event.getPostId());
                            System.out.println("position = " + position);
                            System.out.println("datasnapshot = " + dataSnapshot.getKey());
                            System.out.println(dataSnapshot.getValue());
                            holder.likeButton.setLiked(true);
                            holder.like_text.setText("liked");


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

FirebaseDatabase.getInstance().getReference("eventhub/posts/likes").child(event.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        holder.tv_like.setText(String.valueOf(dataSnapshot.getChildrenCount()));

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

        FirebaseDatabase.getInstance().getReference("eventhub/posts/comments").child(event.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.tv_comments.setText(String.valueOf(dataSnapshot.getChildrenCount())+ " comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


holder.like_text.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(auth.getCurrentUser()!=null) {

            if (!holder.likeButton.isLiked()) {
                holder.likeButton.setLiked(true);
                holder.like_text.setText("Liked");
                String postId = eventsList.get(position).getPostId();
                String ref = "eventhub/posts/likes/" + postId;
                String mref="eventhub/userActivity/"+auth.getCurrentUser().getUid()+"/likes";

                FirebaseDatabase.getInstance().getReference(ref).child(auth.getCurrentUser().getUid()).setValue(true);
                FirebaseDatabase.getInstance().getReference(mref).child(postId).setValue(true);
            }

            else {

                holder.likeButton.setLiked(false);
                holder.like_text.setText("Like");
                String postId = eventsList.get(position).getPostId();
                String ref = "eventhub/posts/likes/" + postId;
                String mref="eventhub/userActivity/"+auth.getCurrentUser().getUid()+"/likes";
                FirebaseDatabase.getInstance().getReference(ref).child(auth.getCurrentUser().getUid()).setValue(null);
                FirebaseDatabase.getInstance().getReference(mref).child(postId).setValue(null);
            }
        }
        else{

            Toast.makeText(mContext, "login to Like", Toast.LENGTH_LONG).show();
        }


    }
});


        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser()!=null) {

                    if (!holder.likeButton.isLiked()) {
                        holder.likeButton.setLiked(true);
                        holder.like_text.setText("Liked");
                        String postId = eventsList.get(position).getPostId();
                        String ref = "eventhub/posts/likes/" + postId;
                        String mref="eventhub/userActivity/"+auth.getCurrentUser().getUid()+"/likes";

                        FirebaseDatabase.getInstance().getReference(ref).child(auth.getCurrentUser().getUid()).setValue(true);
                        FirebaseDatabase.getInstance().getReference(mref).child(postId).setValue(true);
                    }

                    else {

                        holder.likeButton.setLiked(false);
                        holder.like_text.setText("Like");
                        String postId = eventsList.get(position).getPostId();
                        String ref = "eventhub/posts/likes/" + postId;
                        String mref="eventhub/userActivity/"+auth.getCurrentUser().getUid()+"/likes";
                        FirebaseDatabase.getInstance().getReference(ref).child(auth.getCurrentUser().getUid()).setValue(null);
                        FirebaseDatabase.getInstance().getReference(mref).child(postId).setValue(null);
                    }
                }
                else{

                    Toast.makeText(mContext, "login to Like", Toast.LENGTH_LONG).show();
                }


            }
        });






        holder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("like box cliked");
            }
        });

        holder.relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("comment box clicked");

                String post_id= eventsList.get(position).getPostId();


                Intent intent=new Intent(mContext,commentsActivity.class);
                intent.putExtra("event_post_id",post_id);
               v.getContext().startActivity(intent);

            }
        });

        holder.tv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("comments clicked");
                String post_id= eventsList.get(position).getPostId();


                Intent intent=new Intent(mContext,commentsActivity.class);
                intent.putExtra("event_post_id",post_id);
                v.getContext().startActivity(intent);
            }
        });


    }


    public int getItemCount() {
        return eventsList.size();
    }




}