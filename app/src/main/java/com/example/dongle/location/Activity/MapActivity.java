package com.example.dongle.location.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dongle.location.Database.Model.Place;
import com.example.dongle.location.Database.PlaceRepo;
import com.example.dongle.location.Map.Directions.DirectionsRoot;
import com.example.dongle.location.Map.Geoocoding.GeocodingRoot;
import com.example.dongle.location.Map.Geoocoding.Location;
import com.example.dongle.location.Map.Service.ServiceAPI;
import com.example.dongle.location.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private List<LatLng> latLngs = new ArrayList<>();
    private Marker marker;


    private PlaceRepo placeRepo;
    private List<Place> places;

    private String CategoryID;
    private ProgressDialog progressDialog;

    private Retrofit retrofit;
    private android.location.Location currLocation;

    private static final int GPS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        init();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void init() {
        CategoryID = getIntent().getStringExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA);
        placeRepo = PlaceRepo.getInstace(this);
        initProgressDialog();
        initRetrofit();
        getPlaces();
    }

    private void getPlaces() {
        places = placeRepo.getPlaces(CategoryID);

    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_saving));
        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

    }
    private void initRetrofit(){
        retrofit  = new Retrofit.Builder()
                .baseUrl(ActivityUtils.BASE_URL_MAP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    private  void buildAIPClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
        googleApiClient.connect();

    }
    private void AddMarketItem(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(Place place : places) {
                    LatLng placeLocation = new LatLng(place.getPlaceLat(),place.getPlaceLng());
                    googleMap.addMarker(new MarkerOptions().position(placeLocation)
                            .title(place.getPlaceName()));
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));
                }
                progressDialog.dismiss();

            }
        },3000);

    }
    private void getDirection(LatLng origin, final LatLng destination){
        ServiceAPI serviceAIP = retrofit.create(ServiceAPI.class);
        String originAddress = String.valueOf(origin.latitude) +","+ String.valueOf(origin.longitude);
        String destinationAddress = String.valueOf(destination.latitude) +","+ String.valueOf(destination.longitude);
        Call<DirectionsRoot> call = serviceAIP.getDirection(originAddress, destinationAddress);
        call.enqueue(new Callback<DirectionsRoot>() {
            @Override
            public void onResponse(Call<DirectionsRoot> call, Response<DirectionsRoot> response) {
                DirectionsRoot directionsRoot = response.body();
                String polyline = directionsRoot.getRoutes().get(0).getOverview_polyline().getPoints();
                Log.d("TEST",polyline);

                List<LatLng> decodePath = PolyUtil.decode(polyline);
                googleMap.addPolyline( new PolylineOptions().addAll(decodePath));

            }

            @Override
            public void onFailure(Call<DirectionsRoot> call, Throwable t) {

            }
        });

    }
    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    currLocation = location;
                    if(googleApiClient != null){
                        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case GPS_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(googleApiClient == null){
                            buildAIPClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }else {
                        Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show();
                    }

                }break;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
        AddMarketItem();
        //
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
                buildAIPClient();
                this.googleMap.setMyLocationEnabled(true);
        }else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},GPS_PERMISSION_REQUEST_CODE);
            }
        }

        //
//        LatLng sydney = new LatLng(10.8021703,106.7146415);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        // real time move
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);// do uu tien == % pin
        getLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng currLatLng = new LatLng(currLocation.getLatitude(),currLocation.getLongitude());
        getDirection(currLatLng, marker.getPosition());

        return false;
    }
}
