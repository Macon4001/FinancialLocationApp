package com.app.financiallocationapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BudgetEntry {

    public String categoryID;
    public String name;
    public String lat;
    public String lng;

    public long timestamp;
    public long balanceDifference;
    public BudgetEntry() {

    }

    public BudgetEntry(String categoryID, String name, long timestamp, long balanceDifference, String lat, String lng) {
        this.categoryID = categoryID;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        this.balanceDifference = balanceDifference;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getBalanceDifference() {
        return balanceDifference;
    }

    public void setBalanceDifference(long balanceDifference) {
        this.balanceDifference = balanceDifference;
    }


    //    public BudgetEntry(String categoryID, String name, long timestamp, long balanceDifference) {
//        this.categoryID = categoryID;
//        this.name = name;
//        this.timestamp = -timestamp;
//        this.balanceDifference = balanceDifference;
//    }

}