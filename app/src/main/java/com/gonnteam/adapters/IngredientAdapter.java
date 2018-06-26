package com.gonnteam.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gonnteam.R;
import com.gonnteam.models.FoodMenu;
import com.gonnteam.models.Ingredient;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter {
    private FirestoreRecyclerAdapter adapter;
    private List<Ingredient> data;
    private List<Ingredient> dataCopy;
    private Context context;
    private Query query;

    public IngredientAdapter(Context context) {
        this.context = context;
        initAdapter();
    }

    public void initAdapter(){
        query = FirebaseFirestore.getInstance()
                .collection("ingredients");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                data = new ArrayList<>();
                dataCopy = new ArrayList<>();
                try {
                    data = queryDocumentSnapshots.toObjects(Ingredient.class);
                    dataCopy.addAll(data);
                } catch (NullPointerException n) {
                    return;
                }
            }
        });

        FirestoreRecyclerOptions<Ingredient> options = new FirestoreRecyclerOptions.Builder<Ingredient>()
                .setQuery(this.query, Ingredient.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<Ingredient, IngredientViewHolder>(options){

            @NonNull
            @Override
            public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.market_price_item, parent, false);
                return new IngredientViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull IngredientViewHolder holder, int position, @NonNull Ingredient model) {
                if(position < data.size()){
                    holder.setTitle(data.get(position).getName());
                    holder.setPrice(data.get(position));
                }
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

    }

    public void filter(String text){
        data.clear();
        if(text.isEmpty()){
            data.addAll(dataCopy);
        } else{
            text = text.toLowerCase();
            for(Ingredient item: dataCopy){
                if(item.getAlias().toLowerCase().contains(text) || item.getName().toLowerCase().contains(text)){
                    data.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtPrice;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }

        public void setTitle(String title) {
            txtTitle.setText(title);
        }

        public void setPrice(Ingredient ingredient) {
            if(ingredient.getUnit().equals("kg") || ingredient.getUnit().equals("chục") || ingredient.getUnit().equals("l") ){
                txtPrice.setText("Giá: " + ingredient.getPrice() + " VND/" + ingredient.getUnit() );
            }else {
                txtPrice.setText("Giá: " + ingredient.getPrice() + " VND/" + ingredient.getAmount() +ingredient.getUnit());
            }
        }
    }

    public FirestoreRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FirestoreRecyclerAdapter adapter) {
        this.adapter = adapter;
    }
}
