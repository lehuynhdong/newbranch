package com.example.dongle.location.Database.Model;

import android.text.TextUtils;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class Place {
    private String placeID;
    private String categoryID;
    private byte[] placeimg;
    private String placeName;
    private String placeAddress;
    private String placePrice;
    private String placeTime;
    private String placeDescription;
    private double placeLat;
    private double placeLng;

    public Place(Builder builder){
        this.placeID= builder.placeID;
        this.categoryID= builder.categoryID;
        this.placeimg= builder.placeimg;
        this.placeName= builder.placeName;
        this.placeAddress= builder.placeAddress;
        this.placePrice= builder.placePrice;
        this.placeTime= builder.placeTime;
        this.placeDescription= builder.placeDescription;
        this.placeLat= builder.placeLat;
        this.placeLng=builder.placeLng;

    }


    public String getCategoryID() {
        return categoryID;
    }

    public double getPlaceLng() {
        return placeLng;
    }

    public String getPlaceID() {
        return placeID;
    }

    public byte[] getPlaceimg() {
        return placeimg;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public String getPlacePrice() {
        return placePrice;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public String getplaceTime() {
        return placeTime;
    }

    public static class Builder{
        private String placeID;
        private String categoryID;
        private byte[] placeimg;
        private String placeName;
        private String placeAddress;
        private String placePrice;
        private String placeTime;
        private String placeDescription;
        private double placeLat;
        private double placeLng;

        public Builder setPlaceID(String placeID) {
            this.placeID = placeID;
            return this;
        }

        public Builder setPlaceimg(byte[] placeimg) {
            this.placeimg = placeimg;
            return this;
        }

        public Builder setPlaceName(String placeName) {
            this.placeName = placeName;
            return this;
        }

        public Builder setPlaceAddress(String placeAddress) {
            this.placeAddress = placeAddress;
            return this;
        }

        public Builder setPlaceDescription(String placeDescription) {
            this.placeDescription = placeDescription;
            return this;
        }

        public Builder setPlaceLat(double placeLat) {
            this.placeLat = placeLat;
            return this;
        }

        public Builder setPlaceLng(double placeLng) {
            this.placeLng = placeLng;
            return this;
        }

        public Builder setCategoryID(String categoryID) {
            this.categoryID = categoryID;
            return this;
        }

        public Builder setPlacePrice(String placePrice) {
            this.placePrice = placePrice;
            return this;
        }

        public Builder setPlaceTime(String placeTime) {
            this.placeTime = placeTime;
            return this;
        }

        public Place build(){
            return new Place(this);
        }
    }


    public static boolean validateInput(String placeName, String placeTime, String placeAddress, String placePrice, String placeDescription, String CategoryID){
        return (TextUtils.isEmpty(placeName)
                || TextUtils.isEmpty(placeTime)
                || TextUtils.isEmpty(placeAddress)
                || TextUtils.isEmpty(placePrice)
                || TextUtils.isEmpty(placeDescription)
                || TextUtils.isEmpty(CategoryID)) ? false : true ;
    }
}
