package com.example.dongle.location.Map.Service;

import com.example.dongle.location.Map.Directions.DirectionsRoot;
import com.example.dongle.location.Map.Geoocoding.GeocodingRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DongLe on 01-Dec-17.
 */

public interface ServiceAPI {
    @GET("geocode/json?&key=AIzaSyCYEjSf1L415Nr6ip-JfbncTe_efdf3xC0")
    Call<GeocodingRoot> getLocation(@Query("address") String address);


    @GET("directions/json?&key=AIzaSyCYEjSf1L415Nr6ip-JfbncTe_efdf3xC0")
    Call<DirectionsRoot> getDirection(@Query("origin") String origin, @Query("destination") String destination);
}
