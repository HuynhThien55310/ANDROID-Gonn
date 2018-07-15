package com.gonnteam.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by MrThien on 2017-11-05.
 */
@IgnoreExtraProperties
public class User extends BaseObservable {
    public String email;
    public String firstName;
    public String lastName;
    public int gender;
    public String avatar;
    private String uid;
    public double height;
    public double weight;
    public int age;
    public int activity_level;


    public double getHeight() {
        return height;
    }

    public double getBMR(){
        if (gender == 0){
            // nam
            return (13.397 * weight + 4.799 * height) - (5.677 * age) + 88.362;
        }else {
            return (9.247 * weight + 3.098 * height) - (4.330 * age) + 447.593;
        }
    }

    public double getBMI(){
        return Math.floor(weight / (height*height) * 10)/10;
    }

    public double getTDEE(double bmr){
        double TDEE = 0;
        switch (activity_level){
            case 0:
                TDEE = bmr * 1.2;
                break;
            case 1:
                TDEE = bmr * 1.375;
                break;
            case 2:
                TDEE = bmr * 1.55;
                break;
            case 3:
                TDEE = bmr * 1.725;
                break;
            case 4:
                TDEE = bmr * 1.9;
                break;
        }
        return  TDEE;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(int activity_level) {
        this.activity_level = activity_level;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User() {
        // Default constructor
    }

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Bindable
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDisplayName(){
        return this.lastName + " " + this.firstName;
    }
}
