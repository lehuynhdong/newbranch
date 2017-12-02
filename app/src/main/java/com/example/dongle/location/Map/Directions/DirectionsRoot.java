package com.example.dongle.location.Map.Directions;


import java.util.List;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class DirectionsRoot {

    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public DirectionsRoot setRoutes(List<Route> routes) {
        this.routes = routes;
        return this;
    }
}
