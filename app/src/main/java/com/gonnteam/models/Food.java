package com.gonnteam.models;

import android.databinding.BaseObservable;

import java.util.Date;

/**
 * Created by MrThien on 2017-11-15.
 */

public class Food extends BaseObservable {
    private String id;
    private String backdrop;
    private String body;
    private String[] type;
    private String title;
    private Date posted;
    private String video;
    private int like;
    private int share;
    private int comment;

    public Food() {
    }

    public Food(String backdrop, String body, String[] type, String title, Date posted, String video, int like, int share, int comment) {
        this.backdrop = backdrop;
        this.body = body;
        this.type = type;
        this.title = title;
        this.posted = posted;
        this.video = video;
        this.like = like;
        this.share = share;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
