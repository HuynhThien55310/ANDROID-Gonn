package com.gonnteam.models;

public class Menu {
    String id;
    String uid;
    String title;
    String[] foodID;

    public Menu() {
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

    public String[] getFoodID() {
        return foodID;
    }

    public void setFoodID(String[] foodID) {
        this.foodID = foodID;
    }
}
