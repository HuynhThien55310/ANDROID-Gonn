package com.gonnteam.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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
import com.gonnteam.models.Ingredient;
import com.gonnteam.models.Menu;
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
    private List<Menu> data;
    private Context context;
    private Query query;
    private CollectionReference mMenuRef;
    private CollectionReference mFoodRef;
    private CollectionReference mIngreRef;
    private String menuID;
    private String[] foodID;
    private String uid;
    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    private float totalPrice;
    private float totalCal;

    public MenuAdapter(Context context, Query query) {
        this.context = context;
        this.query = query;
        initAdapter();
    }


    public void initAdapter(){
        this.query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                data = new ArrayList<>();
                data = documentSnapshots.toObjects(Menu.class);
            }
        });

        FirestoreRecyclerOptions<Menu> options = new FirestoreRecyclerOptions.Builder<Menu>()
                .setQuery(this.query, Menu.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<Menu, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MenuViewHolder holder, int position, Menu model) {
                mFoodRef = FirebaseFirestore.getInstance().collection("foods");
                mIngreRef = FirebaseFirestore.getInstance().collection("ingredients");
                mAuth = FirebaseAuth.getInstance();
                fuser = mAuth.getCurrentUser();
                // get menu & put to Menu detail
                menuID = getSnapshots().getSnapshot(position).getId();
                foodID = data.get(position).getFoodID();
                final Menu menu = data.get(position);


                // binding menu value;
                // totalCal = calCal();
                // totalPrice = calPrice();
                holder.setMenuName(menu.getTitle());
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


    private int calCal(){
        return 1;
    }



    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMenuName;
        private TextView txtTotalPrice;
        private TextView txtTotalCal;

        public MenuViewHolder(View itemView) {
            super(itemView);
            txtMenuName = itemView.findViewById(R.id.txtMenuName);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtTotalCal = itemView.findViewById(R.id.txtTotalCal);
        }

        public void setMenuName(String title) {
            TextView txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTitle.setText(title);
        }

        public void setTotalPrice(int price) {
            TextView txtLike = itemView.findViewById(R.id.txtLike);
            txtLike.setText(price + " VND");

        }

        public void setTotalCal(int cal) {
            TextView txtCmt = itemView.findViewById(R.id.txtCmt);
            txtCmt.setText(cal + " Cal");
        }


    }

    public FirestoreRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FirestoreRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public List<Menu> getData() {
        return data;
    }

    public void setData(List<Menu> data) {
        this.data = data;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
