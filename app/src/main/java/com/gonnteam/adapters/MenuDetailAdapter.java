package com.gonnteam.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gonnteam.R;
import com.gonnteam.activities.FoodMenuDetailActivity;
import com.gonnteam.fragments.FoodDetailFragment;
import com.gonnteam.models.Food;
import com.gonnteam.models.FoodMenu;
import com.gonnteam.models.Ingredient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentListenOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.MenuDetailViewHolder> {

    private Context context;
    private String menuID;
    private CollectionReference mMenuRef;
    private CollectionReference mFoodRef;
    private ArrayList<String> foodID;
    private ArrayList<Food> foods;

    private AlertDialog dialog;
    private TextView txtCustomTitle;
    private int deletePosition;
    private int[] price;

    public MenuDetailAdapter(Context context, ArrayList<String> foodID, String menuID) {
        this.context = context;
        this.foodID = foodID;
        foods = new ArrayList<>();
        this.menuID = menuID;
        fetchData();
        // init dialog
        dialog = new AlertDialog.Builder(context).create();
        txtCustomTitle = new EditText(context);
        // edit style dialog o day ne m
        txtCustomTitle.setText("Bạn có đồng ý muốn xóa?");
        txtCustomTitle.setBackgroundColor(Color.DKGRAY);
        txtCustomTitle.setPadding(10, 10, 10, 10);
        txtCustomTitle.setGravity(Gravity.CENTER);
        txtCustomTitle.setTextColor(Color.WHITE);
        txtCustomTitle.setTextSize(20);
        txtCustomTitle.setEnabled(false);
        // end style
        dialog.setCustomTitle(txtCustomTitle);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteFood();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

    }

    public void fetchData() {
        foods.clear();
        for (int i = 0; i < foodID.size(); i++) {
            FirebaseFirestore.getInstance()
                    .collection("foods").document(foodID.get(i))
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            foods.add(documentSnapshot.toObject(Food.class));
                            price = new int[foods.size()];
                            notifyDataSetChanged();
                        }
                    });
        }
    }

    private void deleteFood(){
        foods.remove(deletePosition);
        foodID.remove(deletePosition);
        FirebaseFirestore.getInstance()
                .collection("menus").document(menuID)
                .update("foods", foodID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyDataSetChanged();
                    }
                });
    }

    @NonNull
    @Override
    public MenuDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.menu_detail_item, parent, false);
        return new MenuDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuDetailViewHolder holder, final int position) {
        holder.setBackdrop(foods.get(position).getBackdrop(), context);
        holder.setTitle(foods.get(position).getTitle());
        holder.setCal(0);
        holder.setPrice(0);
        holder.imgBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent foodDetail = new Intent(context, FoodMenuDetailActivity.class);
                foodDetail.putExtra("body",foods.get(position).getBody());
                foodDetail.putExtra("title",foods.get(position).getTitle());
                foodDetail.putExtra("backdrop",foods.get(position).getBackdrop());
                context.startActivity(foodDetail);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePosition = position;
                dialog.show();
            }
        });
        Food food = foods.get(position);
        price[position] = 0;
        for(int i=0; i < food.getIngredients().size(); i++){
            final Ingredient fIngre = food.getIngredients().get(i);
            FirebaseFirestore.getInstance()
                    .collection("ingredients")
                    .whereEqualTo("name", fIngre.getName())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<Ingredient> temp;
                            temp = queryDocumentSnapshots.toObjects(Ingredient.class);
                            if (!temp.isEmpty()){
                                Ingredient ingre = temp.get(0);
                                if (!fIngre.getUnit().equals(ingre.getUnit())){
                                    // nguyên liệu khác đơn vị
                                    price[position] += fIngre.getAmount() * ingre.getPrice() / 1000;
                                    holder.setPrice(price[position]);

                                } else {
                                    // nguyên liệu cùng đơn vị
                                    price[position] += fIngre.getAmount() * ingre.getPrice() / ingre.getAmount();
                                    holder.setPrice(price[position]);
                                }
                            }
                        }});
        }
        // holder.setPrice(getPrice(foods.get(position)));
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
            btnDelete = itemView.findViewById(R.id.btnDelete);
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
            txtCal.setText(cal + " Cal");
        }

        public void setPrice(int price) {
            txtPrice.setText(price + " VND");
        }

    }
}
