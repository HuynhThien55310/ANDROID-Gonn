package com.gonnteam.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gonnteam.R;
import com.gonnteam.models.Food;
import com.google.firebase.database.DataSnapshot;
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
            protected void onBindViewHolder(FoodViewHolder holder, final int position, Food model) {


                holder.setTitle(data.get(position).getTitle());
                holder.setSocialInfo(data.get(position).getLike(),
                        data.get(position).getComment(), data.get(position).getShare());
                holder.setBackdrop(data.get(position).getBackdrop(), context);

                holder.imgBackdrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,data.get(position).getId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.d("Lỗi ở main",e.getMessage());
            }
        };
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBackdrop;

        public FoodViewHolder(View itemView) {
            super(itemView);
        }

        public void setTitle(String title){
            TextView txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTitle.setText(title);
        }

        public void setSocialInfo(int like, int comment, int share){
            TextView txtSocialInfo = itemView.findViewById(R.id.txtLikeCmtShare);
            txtSocialInfo.setText(like + " Thích - " + comment + " Bình luận - " + share + " Chia sẻ");
        }

        public void setBackdrop(String backdrop, Context context){
            imgBackdrop = itemView.findViewById(R.id.imgBackdrop);
            Picasso.with(context).load(backdrop).into(imgBackdrop);
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
