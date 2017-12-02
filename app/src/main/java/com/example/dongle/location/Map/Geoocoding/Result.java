package com.example.dongle.location.Map.Geoocoding;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class Result {
    private Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }

    public Result setGeometry(Geometry geometry) {
        this.geometry = geometry;
        return this;
    }
}
