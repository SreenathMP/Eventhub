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



public class pending_event_adapter extends RecyclerView.Adapter<pending_event_adapter.MyViewHolder> {

    private List<events> eventsList;
    private Context mContext;
    private FirebaseAuth auth=FirebaseAuth.getInstance();


    private static ClickListener clickListener;





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView post_title,college_name,event_details,event_date,tv_comments,like_text,tv_like;
        private ImageView banner;
        private LikeButton likeButton,heart;
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
            heart=(LikeButton)view.findViewById(R.id.heart);

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
        pending_event_adapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


    public pending_event_adapter(Context context, List<events> eventsList) {
        this.eventsList = eventsList;
        this.mContext = context;
    }


    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);

        return new MyViewHolder(itemView);
    }




    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        // events event = eventsList.get(getItemCount() - 1 - position);


        final events event = eventsList.get(position);

        if (mContext == null)
            return;




holder.likeButton.setVisibility(View.INVISIBLE);
holder.relativeLayout2.setVisibility(View.INVISIBLE);
holder.tv_like.setVisibility(View.INVISIBLE);
holder.tv_comments.setVisibility(View.INVISIBLE);
holder.like_text.setVisibility(View.INVISIBLE);
holder.heart.setVisibility(View.INVISIBLE);

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








    }


    public int getItemCount() {
        return eventsList.size();
    }




}