package com.gonnteam.models;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by MrThien on 2017-12-17.
 */

public class Like extends BaseObservable implements Serializable{
    private String foodID;
    private String userID;

    public Like() {
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
