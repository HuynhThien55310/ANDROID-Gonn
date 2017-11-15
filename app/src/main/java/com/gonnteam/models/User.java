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
    public String gender;
    public Date dateOfBirth;
    public String avatar;
    public String id;
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
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Bindable
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }
}
