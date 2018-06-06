package com.gonnteam.models;

import java.util.ArrayList;

public class FoodMenu {
    private String id;
    private String uid;
    private String title;
    private ArrayList<String> foods;

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
