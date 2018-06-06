package com.gonnteam.models;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MrThien on 2017-11-15.
 */

public class Food implements Serializable {
    private String id;
    private String backdrop;
    private String body;
    private String title;
    private String alias;
    private int share;
    private int view;
    private int like;
    private int comment;
    private ArrayList<Ingredient> ingredients;

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getCmt() {
        return comment;
    }

    public void setCmt(int cmt) {
        this.comment = cmt;
    }

    public Food() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }


    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }



    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
