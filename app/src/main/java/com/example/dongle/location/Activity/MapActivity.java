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
import com.example.dongle.location.Map.Service.ServiceAPI;
import com.example.dongle.location.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private List<LatLng> latLngs = new ArrayList<>();
    private Marker currmarker;


    private PlaceRepo placeRepo;
    private List<Place> places = new ArrayList<>();
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
        setTitle("Map");
    }
    private void getPlaces() {
        places = placeRepo.getPlaces(CategoryID);

    }
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_saving));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

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
    private void getDirection(LatLng origin, LatLng destination){
        ServiceAPI serviceAPI = retrofit.create(ServiceAPI.class);
        String originAddress = String.valueOf(origin.latitude) + "," + String.valueOf(origin.longitude);
        String destinationAddress = String.valueOf(destination.latitude) + "," + String.valueOf(destination.longitude);
        Call<DirectionsRoot> call = serviceAPI.getDirection(originAddress, destinationAddress);
        call.enqueue(new Callback<DirectionsRoot>() {
            @Override
            public void onResponse(Call<DirectionsRoot> call, Response<DirectionsRoot> response) {
                DirectionsRoot directionsRoot = response.body();
                String polyline = directionsRoot.getRoutes().get(0).getOverview_polyline().getPoints();
                Log.d("Test",polyline);
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
        }else {
            Toast.makeText(this, "Erro get location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

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

                }
                break;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
        AddMarketItem();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                buildAIPClient();
                this.googleMap.setMyLocationEnabled(true);
        }else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},GPS_PERMISSION_REQUEST_CODE);
            }
        }
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
            double latitude = currLocation.getLatitude();
            double longitude = currLocation.getLongitude();
            LatLng currLatLng = new LatLng(latitude, longitude);
            getDirection(currLatLng, marker.getPosition());

        return false;
    }
}
