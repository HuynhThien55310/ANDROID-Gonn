package com.gonnteam.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrThien on 2017-12-13.
 */

public class FoodFirebaseAdapter {
    private FirestoreRecyclerAdapter adapter;
    private List<Food> data;
    private Context context;
    private Query query;
    private Like like;
    private boolean mProcessLike = false;
    private CollectionReference mLikeRef;
    private CollectionReference mFoodRef;
    private CollectionReference mCmtRef;
    private String foodID;
    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private boolean likeInit = false;
    public FoodFirebaseAdapter(final Query query, final Context context) {
        this.context = context;
        this.query = query;
        this.query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                data = new ArrayList<>();

                data = documentSnapshots.toObjects(Food.class);

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
                Bundle bundle = new Bundle();
                bundle.putSerializable("food", food);
                detail.putExtra("bundle", bundle);
                detail.putExtra("foodID", foodID);

                // set like number
                Query countLike = mLikeRef.whereEqualTo("foodID", detail.getStringExtra("foodID"));
                countLike.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        holder.setLike(documentSnapshots.getDocuments().size());
                    }
                });

                if (fuser != null){
                    countLike.whereEqualTo("userID",fuser.getUid())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                        if (documentSnapshots.size() != 0){
                                            holder.setBtnLike(true);
                                        }else {
                                            holder.setBtnLike(false);

                                        }
                                }
                            });
                }

                //set comment number
                Query countCmt = mCmtRef.whereEqualTo("foodID", detail.getStringExtra("foodID"));
                countCmt.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        holder.setCmt(documentSnapshots.size());
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
                                        if (documentSnapshots.isEmpty()) {
                                            // user did not like current food
                                            Like like = new Like(detail.getStringExtra("foodID"), userID);
                                            //holder.setBtnLike(true);
                                            mLikeRef.add(like);
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

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.d("Lỗi ở main", e.getMessage());
            }
        };
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBackdrop;
        private ImageButton btnLike;

        public FoodViewHolder(View itemView) {
            super(itemView);
            btnLike = itemView.findViewById(R.id.btnLike);
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
            Picasso.with(context).load(backdrop).into(imgBackdrop);
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
