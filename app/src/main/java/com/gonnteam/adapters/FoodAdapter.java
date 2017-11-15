package com.gonnteam.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gonnteam.R;
import com.gonnteam.models.Food;

import java.util.List;

/**
 * Created by MrThien on 2017-11-15.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foods;

    public FoodAdapter(List<Food> foods) {
        this.foods = foods;
    }

    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_item_recylerview, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodAdapter.FoodViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        public FoodViewHolder(View itemView) {
            super(itemView);
        }
    }
}
