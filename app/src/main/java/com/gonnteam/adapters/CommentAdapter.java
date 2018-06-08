package com.gonnteam.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gonnteam.R;
import com.gonnteam.activities.FoodDetailActivity;
import com.gonnteam.activities.MainActivity;
import com.gonnteam.models.Comment;
import com.gonnteam.models.Food;
import com.gonnteam.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by MrThien on 2017-12-17.
 */

public class CommentAdapter {
    private FirestoreRecyclerAdapter adapter;
    private List<Comment> data;
    private Context context;
    private Query query;
    private DocumentReference mDocRef;

    public CommentAdapter(final Query query, final Context context) {
        this.context = context;
        this.query = query;
        this.query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                data = new ArrayList<>();
                data = documentSnapshots.toObjects(Comment.class);
            }
        });

        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(this.query, Comment.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.comment_item_recyclerview, parent, false);
                return new CommentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CommentViewHolder holder, final int position, Comment model) {
                final Comment comment = data.get(position);
                final String avatar = "";
                String name = "";
                mDocRef = FirebaseFirestore.getInstance().document("users/" + comment.getUserID());
                mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user.getAvatar() == null || user.getAvatar() == "") {
                                holder.imgAvatar.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                            } else if (user.getAvatar().contains("data:image/jpeg;base64")) {
                                String ava = user.getAvatar().substring(user.getAvatar().indexOf(",") + 1);
                                byte[] decodedString = Base64.decode(ava, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                holder.imgAvatar.setImageBitmap(decodedByte);
                            } else {
                                Picasso.with(context).load(user.getAvatar()).into(holder.imgAvatar);
                            }
                            holder.setCmt(comment.getText());
                            holder.setName(user.getLastName() + " " + user.getFirstName());
                        }
                    }
                });


            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.d("Lỗi ở main",e.getMessage());
            }
        };
    }
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgAvatar;
        public CommentViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }


        public void setName(String name){
            TextView txtName = itemView.findViewById(R.id.txtName);
            txtName.setText(name);
        }

        public void setCmt(String cmt){
            TextView txtCmt = itemView.findViewById(R.id.txtText);
            txtCmt.setText(cmt);
        }

    }



    public CommentAdapter() {
    }

    public FirestoreRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FirestoreRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
