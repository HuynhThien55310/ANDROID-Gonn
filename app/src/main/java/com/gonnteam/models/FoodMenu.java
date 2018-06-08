package com.gonnteam.models;

import java.util.ArrayList;
import java.util.Date;

public class FoodMenu {
    private String id;
    private String uid;
    private String title;
    private ArrayList<String> foods;
    public Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public FoodMenu() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<String> foods) {
        this.foods = foods;
    }

    public void setId(String id) {
        this.id = id;
    }
}
