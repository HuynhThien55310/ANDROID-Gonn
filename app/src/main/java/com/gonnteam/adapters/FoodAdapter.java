package com.gonnteam.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gonnteam.R;
import com.gonnteam.activities.FoodDetailActivity;
import com.gonnteam.activities.LoginActivity;
import com.gonnteam.models.Food;
import com.gonnteam.models.Like;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrThien on 2017-12-13.
 */

public class FoodAdapter {
    private FirestoreRecyclerAdapter adapter;
    private List<Food> data;
    private Context context;
    private Query query;
    private boolean mProcessLike = false;
    private CollectionReference mLikeRef;
    private CollectionReference mFoodRef;
    private CollectionReference mCmtRef;
    private String foodID;
    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private boolean isLoaded = false;

    // share image to facebook
    private ShareDialog shareDialog;
    private Activity parent;
    public FoodAdapter(Query query, Context context, Activity parent) {
        this.context = context;
        this.query = query;
        this.parent = parent;
        initAdapter();
    }

    public void initAdapter(){
        this.query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (!isLoaded){
                    if (documentSnapshots != null){
//                        data = new ArrayList<>();
//                        data = documentSnapshots.toObjects(Food.class);
                        try {
                            data = documentSnapshots.toObjects(Food.class);
                        }catch (RuntimeException r){
                            Log.d("CRASH ID: ",r.getMessage());
                        }
                    }
                    isLoaded = true;
                }
            }

        });

        FirestoreRecyclerOptions<Food> options = new FirestoreRecyclerOptions.Builder<Food>()
                .setQuery(this.query, Food.class)
                .build();


        this.adapter = new FirestoreRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.food_item_recylerview, parent, false);
                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FoodViewHolder holder, final int position, Food model) {
                //get firebase instance
                mLikeRef = FirebaseFirestore.getInstance().collection("likes");
                mFoodRef = FirebaseFirestore.getInstance().collection("foods");
                mCmtRef = FirebaseFirestore.getInstance().collection("comments");
                mAuth = FirebaseAuth.getInstance();
                fuser = mAuth.getCurrentUser();
                // get food
                foodID = getSnapshots().getSnapshot(position).getId();
                final Food food = data.get(position);
                final Intent detail = new Intent(context, FoodDetailActivity.class);
                detail.putExtra("foodID", foodID);
                detail.putExtra("title",food.getTitle());
                detail.putExtra("backdrop",food.getBackdrop());
                detail.putExtra("body",food.getBody());



                // set like number
                Query countLike = mLikeRef.whereEqualTo("foodID", detail.getStringExtra("foodID"));
                countLike.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(documentSnapshots == null || documentSnapshots.isEmpty()){
                            holder.setLike(0);
                        } else {
                            holder.setLike(documentSnapshots.size());
                        }
                    }
                });

                if (fuser != null){
                    FirebaseFirestore.getInstance()
                            .collection("likes")
                            .whereEqualTo("foodID", detail.getStringExtra("foodID"))
                            .whereEqualTo("userID", fuser.getUid())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if(queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty()){
                                        holder.setBtnLike(false);
                                    }else {
                                        holder.setBtnLike(true);
                                    }
                                }
                            });
                }

                //set comment number
                Query countCmt = mCmtRef.whereEqualTo("foodID", detail.getStringExtra("foodID"));
                countCmt.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(documentSnapshots == null || documentSnapshots.isEmpty()){
                            holder.setCmt(0);
                        } else {
                            holder.setCmt(documentSnapshots.size());
                        }
                    }
                });

                // set btn share
                holder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareDialog = new ShareDialog(parent);
                        Bitmap bitmap = ((BitmapDrawable)holder.imgBackdrop.getDrawable()).getBitmap();
                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(bitmap)
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .build();
                        shareDialog.show(content);
                    }
                });

                // binding food value
                holder.setTitle(food.getTitle());
                holder.setCmt(0);
                holder.setShare(food.getShare());
                holder.setBackdrop(food.getBackdrop(), context);
                holder.imgBackdrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // increase view value
                        mFoodRef.document(detail.getStringExtra("foodID"))
                                .update("view", food.getView() +1);
//                        try{
//                            context.startActivity(detail);
//                        }catch (Exception i){
//
//                        }
                        context.startActivity(detail);
                    }
                });

                holder.btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //check user already login

                        if (fuser == null) {
                            // if user did not login -> login activity
                            Intent login = new Intent(context, LoginActivity.class);
                            context.startActivity(login);
                        } else {
                            // already login
                            final String userID = fuser.getUid();
                            mProcessLike = true;
                            Query likeQuery = mLikeRef.whereEqualTo("foodID", detail.getStringExtra("foodID")).whereEqualTo("userID", userID);
                            //
                            likeQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                    if (mProcessLike) {
                                        if (documentSnapshots == null || documentSnapshots.isEmpty() ) {
                                            // user did not like current food
                                            Like like = new Like(detail.getStringExtra("foodID"), userID);
                                            //holder.setBtnLike(true);
                                            FirebaseFirestore.getInstance().collection("likes")
                                                    .add(like);
                                            mFoodRef.document(detail.getStringExtra("foodID"))
                                                    .update("like", food.getLike() + 1);
                                            mProcessLike = false;
                                        } else {
                                            // user liked -> unlike
                                            String likedRef = documentSnapshots.getDocuments().get(0).getId();
                                            //holder.setBtnLike(false);
                                            mLikeRef.document(likedRef).delete();
                                            mFoodRef.document(detail.getStringExtra("foodID"))
                                                    .update("like", food.getLike() - 1);
                                            mProcessLike = false;
                                        }
                                    }

                                }
                            });
                        }

                    }
                });

            }

        };
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBackdrop;
        private ImageButton btnLike;
        private ImageButton btnShare;
        private Bitmap bitBackdrop;
        public FoodViewHolder(View itemView) {
            super(itemView);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnShare = itemView.findViewById(R.id.btnShare);
        }

        public void setTitle(String title) {
            TextView txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTitle.setText(title);
        }

        public void setLike(int like) {
            TextView txtLike = itemView.findViewById(R.id.txtLike);
            txtLike.setText(like + "");
        }

        public void setCmt(int cmt) {
            TextView txtCmt = itemView.findViewById(R.id.txtCmt);
            txtCmt.setText(cmt + "");
        }

        public void setShare(int share) {
            TextView txtShare = itemView.findViewById(R.id.txtShare);
            txtShare.setText(share + "");
        }

        public void setBackdrop(String backdrop, Context context) {
            imgBackdrop = itemView.findViewById(R.id.imgBackdrop);
            if (backdrop.contains("data:image/jpeg;base64")) {
                backdrop = backdrop.substring(backdrop.indexOf(",") + 1);
                byte[] decodedString = Base64.decode(backdrop, Base64.DEFAULT);
                bitBackdrop = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgBackdrop.setImageBitmap(bitBackdrop);
            } else {
                Picasso.with(context).load(backdrop).into(imgBackdrop);
            }
        }

        public void setBtnLike(boolean isLiked) {
            if (isLiked) {
                btnLike.setImageResource(R.drawable.ic_already_like);
            } else {
                btnLike.setImageResource(R.drawable.ic_like);

            }
        }

    }


    public FirestoreRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FirestoreRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public List<Food> getData() {
        return data;
    }

    public void setData(List<Food> data) {
        this.data = data;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
