package com.gonnteam.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.gonnteam.activities.MenuDetailActivity;
import com.gonnteam.models.Food;
import com.gonnteam.models.FoodMenu;
import com.gonnteam.models.Ingredient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter {
    private FirestoreRecyclerAdapter adapter;
    private List<FoodMenu> data;
    private Context context;
    private Query query;
    private CollectionReference mMenuRef;
    private CollectionReference mFoodRef;
    private CollectionReference mIngreRef;
    private String menuID;
    private ArrayList<String> foods;
    private String uid;
    private String foodID;
    private int deletePosition;
    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private float totalPrice;
    private float totalCal;
    private Activity menuActivity;

    private AlertDialog dialog;
    private TextView txtCustomTitle;

    public MenuAdapter(Context context, Query query, String foodID, Activity menuActivity) {
        this.context = context;
        this.query = query;
        this.foodID = foodID;
        this.menuActivity = menuActivity;
        initAdapter();
    }


    public void initAdapter() {
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
                deleteMenu();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        this.query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                data = new ArrayList<>();
                try {
                    data = documentSnapshots.toObjects(FoodMenu.class);
                } catch (NullPointerException n) {
                    return;
                }

            }
        });

        FirestoreRecyclerOptions<FoodMenu> options = new FirestoreRecyclerOptions.Builder<FoodMenu>()
                .setQuery(this.query, FoodMenu.class)
                .build();


        this.adapter = new FirestoreRecyclerAdapter<FoodMenu, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MenuViewHolder holder, final int position, FoodMenu model) {
                mFoodRef = FirebaseFirestore.getInstance().collection("foods");
                mIngreRef = FirebaseFirestore.getInstance().collection("ingredients");
                mMenuRef = FirebaseFirestore.getInstance().collection("menus");
                mAuth = FirebaseAuth.getInstance();
                fuser = mAuth.getCurrentUser();
                // get menu & put to Menu detail
                menuID = getSnapshots().getSnapshot(position).getId();
                // foodID = data.get(position).getFoodID();
                final FoodMenu menu = data.get(position);


                // binding menu value;
                // totalCal = calCal();
                // totalPrice = calPrice();
                holder.setMenuName(menu.getTitle());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (foodID == "" || foodID == null) {
                            if (menu.getFoods() == null || menu.getFoods().size() == 0) {
                                Toast.makeText(context, "Thực đơn trống", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // click trong menu
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("foods", menu.getFoods());
                            Intent menuDetail = new Intent(context, MenuDetailActivity.class);
                            menuDetail.putExtra("bundle", bundle);
                            menuDetail.putExtra("menuID", menu.getId());
                            context.startActivity(menuDetail);
                        } else {
                            // click tu food detail
                            if (menu.getFoods() == null) {
                                menu.setFoods(new ArrayList<String>());
                                menu.getFoods().add(foodID);
                                mMenuRef.document(menu.getId()).set(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        menuActivity.finish();
                                    }
                                });
                            } else if (!menu.getFoods().contains(foodID)) {
                                menu.getFoods().add(foodID);
                                mMenuRef.document(menu.getId()).set(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        menuActivity.finish();
                                    }
                                });
                            } else {
                                Toast.makeText(context, "Bạn đã thêm món này rồi", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        deletePosition = position;
                        dialog.show();
                    }
                });
                // holder.setTotalCal(totalCal);
                // .setTotalPrice(Math.round(totalPrice));
            }


            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }
        };
    }

    private void deleteMenu(){
        FirebaseFirestore.getInstance()
                .collection("menus").document(data.get(deletePosition).getId())
                .delete();
    }

    private float calPrice() {
//        totalPrice = 0;
//        for(int i=0; i< foodID.length; i++){
//            mFoodRef.document(foodID[i]).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(final DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                    final Food food = documentSnapshot.toObject(Food.class);
//                    if (food.getIngredients() == null || food.getIngredients().size() == 0){
//                        return;
//                    }
//                    for(int j=0; j < food.getIngredients().size(); j++){
//                        final Ingredient ingreJ = food.getIngredients().get(j);
//                        mIngreRef.whereEqualTo("name",ingreJ
//                                .getName()).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                                List<Ingredient> temp;
//                                temp = documentSnapshots.toObjects(Ingredient.class);
//                                if (!temp.isEmpty()){
//                                    Ingredient ingre = temp.get(0);
//                                    if (ingre.getUnit() == ingreJ.getUnit()){
//                                        // nguyên liệu cùng đơn vị
//                                        totalPrice += ingreJ.getAmount() * ingre.getPrice() / 1000;
//
//                                    } else {
//                                        // nguyên liệu khác đơn vị
//                                        totalPrice += ingreJ.getAmount() * ingre.getPrice() / ingre.getAmount();
//                                    }
//                                }
//
//                            }
//                        });
//                    }
//                }
//            });
//        }

        return totalPrice;
    }


    private int calCal() {
        return 1;
    }


    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMenuName;
        private TextView txtTotalPrice;
        private TextView txtTotalCal;
        public ImageButton btnDelete;

        public MenuViewHolder(View itemView) {
            super(itemView);
            txtMenuName = itemView.findViewById(R.id.txtTitle);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtTotalCal = itemView.findViewById(R.id.txtTotalCal);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void setMenuName(String title) {
            txtMenuName.setText(title);
        }

        public void setTotalPrice(int price) {
            txtTotalPrice.setText(price + " VND");

        }

        public void setTotalCal(int cal) {
            txtTotalCal.setText(cal + "");
        }


    }

    public FirestoreRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FirestoreRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public List<FoodMenu> getData() {
        return data;
    }

    public void setData(List<FoodMenu> data) {
        this.data = data;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
