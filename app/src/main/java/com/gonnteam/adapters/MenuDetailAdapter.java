package com.gonnteam.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gonnteam.R;
import com.gonnteam.models.Food;
import com.gonnteam.models.FoodMenu;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentListenOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.MenuDetailViewHolder> {

    private Context context;
    private Query query;
    private CollectionReference mMenuRef;
    private CollectionReference mFoodRef;
    private ArrayList<String> foodID;
    private ArrayList<Food> foods;

    public MenuDetailAdapter(Context context, ArrayList<String> foodID) {
        this.context = context;
        this.foodID = foodID;
        initAdapter();
    }

    private void initAdapter() {
        foods = new ArrayList<>();
        for (int i = 0; i < foodID.size(); i++) {
            FirebaseFirestore.getInstance()
                    .collection("foods").document(foodID.get(i))
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            foods.add(documentSnapshot.toObject(Food.class));
                            notifyDataSetChanged();
                        }
                    });
        }
    }

    @NonNull
    @Override
    public MenuDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.menu_detail_item, parent, false);
        return new MenuDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDetailViewHolder holder, int position) {
        holder.setBackdrop(foods.get(position).getBackdrop(), context);
        holder.setTitle(foods.get(position).getTitle());
        holder.setCal(0);
        holder.setPrice(0);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public static class MenuDetailViewHolder extends RecyclerView.ViewHolder {

        public ImageButton btnDelete;
        private TextView txtCal;
        private TextView txtPrice;
        private TextView txtTitle;
        private ImageView imgBackdrop;


        public MenuDetailViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCal = itemView.findViewById(R.id.txtCal);
            imgBackdrop = itemView.findViewById(R.id.imgBackdrop);
        }

        public void setTitle(String title) {
            txtTitle.setText(title);
        }

        public void setBackdrop(String backdrop, Context context) {
            imgBackdrop = itemView.findViewById(R.id.imgBackdrop);
            if (backdrop.contains("data:image/jpeg;base64")) {
                backdrop = backdrop.substring(backdrop.indexOf(",") + 1);
                byte[] decodedString = Base64.decode(backdrop, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgBackdrop.setImageBitmap(decodedByte);
            } else {
                Picasso.with(context).load(backdrop).into(imgBackdrop);
            }
        }

        public void setCal(int cal) {
            txtCal.setText(cal + "");
        }

        public void setPrice(int price) {
            txtPrice.setText(price + "");
        }

    }
}
