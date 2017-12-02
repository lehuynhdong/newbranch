package com.example.dongle.location.Map.Geoocoding;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class Location {
    private double lat ;
    private double lng ;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public Location setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Location setLng(double lng) {
        this.lng = lng;
        return this;
    }
}
