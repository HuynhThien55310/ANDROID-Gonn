package com.gonnteam.models;

import android.databinding.BaseObservable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by MrThien on 2017-12-17.
 */

public class Comment extends BaseObservable implements Serializable {
    private String foodID;
    private String userID;
    private String text;
//    private Object postedAt;

    public Comment(String foodID, String userID, String text) {
        this.foodID = foodID;
        this.userID = userID;
        this.text = text;
//        postedAt = new Date();
    }

    public Comment() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    public Date getPostedAt() {
//        return postedAt;
//    }

//    public void setPostedAt(Date postedAt) {
//        this.postedAt = postedAt;
//    }
}
