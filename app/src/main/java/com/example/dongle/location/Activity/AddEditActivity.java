package com.example.dongle.location.Activity;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dongle.location.Database.Model.Place;
import com.example.dongle.location.Database.PlaceRepo;
import com.example.dongle.location.Map.Geoocoding.GeocodingRoot;
import com.example.dongle.location.Map.Geoocoding.Location;
import com.example.dongle.location.Map.Service.ServiceAPI;
import com.example.dongle.location.R;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEditActivity extends AppCompatActivity {
    @BindView(R.id.imgAdd_Edit_Place)
    ImageView imgAddEditPlace;
    @BindView(R.id.EditAdd_Edit_Name)
    EditText editName;
    @BindView(R.id.EditAdd_Edit_Time)
    EditText editTime;
    @BindView(R.id.EditAdd_Edit_Address)
    EditText editAddress;
    @BindView(R.id.EditAdd_Edit_Price)
    EditText editPrice;
    @BindView(R.id.EditAdd_Edit_Description)
    EditText editDescription;


    private String placeID;
    private String CategoryID;
    private PlaceRepo placeRepo;
    private ProgressDialog progressDialog;
    private Place place;
    private Location location;
    private boolean hasImg = false;
    private boolean allowSave = false;

    private static final int PICK_IMAGE_REQUEST =1;

    private Retrofit retrofit;
    //

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ){

            if(data == null ){
                if(placeID == null){
                    hasImg = false;
                    allowSave = false;
                }else {
                    hasImg = true; //  cập nhập
                }

            }else {

                hasImg = true;
                allowSave = true;
                Uri picUri = data.getData();

                filePath = getPath(picUri);

                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);

                imgAddEditPlace.setImageURI(picUri);
            }


        }
    }

    private void init() {
        placeID = getIntent().getStringExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA);
        CategoryID = getIntent().getStringExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA);
        placeRepo = PlaceRepo.getInstace(this);
        initRetrofit();
        initProgressDialog();

        if(placeID != null){
            hasImg = true;
            setPlace(placeID,CategoryID);
        }
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(AddEditActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_saving));
        progressDialog.setCanceledOnTouchOutside(false);

    }
    private void initRetrofit(){
        retrofit  = new Retrofit.Builder()
                .baseUrl(ActivityUtils.BASE_URL_MAP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    private void setPlace(String placeID, String CategoryID){
        Place place = placeRepo.getPlace(CategoryID,placeID);
        Bitmap bitmapsetplace = BitmapFactory.decodeByteArray(place.getPlaceimg(),0,place.getPlaceimg().length);
        imgAddEditPlace.setImageBitmap(bitmapsetplace);
        editName.setText(place.getPlaceName());
        editTime.setText(place.getplaceTime());
        editAddress.setText(place.getPlaceAddress());
        editPrice.setText(place.getPlacePrice());
        editDescription.setText(place.getPlaceDescription());


    }
    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void getLayLng(String address){
        ServiceAPI serviceAIP = retrofit.create(ServiceAPI.class);
        Call<GeocodingRoot> call = serviceAIP.getLocation(address);
        call.enqueue(new Callback<GeocodingRoot>() {
            @Override
            public void onResponse(Call<GeocodingRoot> call, retrofit2.Response<GeocodingRoot> response) {
                GeocodingRoot geocodingRoot = response.body();
                double lat = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLat();
                double lng = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLng();

                location = new Location(lat,lng);

            }

            @Override
            public void onFailure(Call<GeocodingRoot> call, Throwable t) {

            }
        });

    }
    private  byte[] convertImgToByteArray(ImageView imageView) {
        Bitmap bitmap= ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        return baos.toByteArray();
    }

    private void redirectPlacesAct(){
        Intent placesActIntent= new Intent(AddEditActivity.this,PlacesActivity.class);
        placesActIntent.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA, CategoryID);
        startActivity(placesActIntent);
        finish();
    }
    private void redirectDetailAct(){
        Intent intent= new Intent(AddEditActivity.this,DetailActivity.class);
        intent.putExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA, placeID);
        intent.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA,CategoryID);
        startActivity(intent);
        finish();

    }
    @OnClick(R.id.btnAdd_Edit_Save)
    public void savePlace(View view){
        final String placeName = editName.getText().toString();
        final String placeTime = editTime.getText().toString();
        final String placeAddress = editAddress.getText().toString();
        final String placePrice = editPrice.getText().toString();
        final String placeDescription = editDescription.getText().toString();


        if(Place.validateInput(placeName, placeTime, placeAddress, placePrice, placeDescription, CategoryID)){
            allowSave = true;
            getLayLng(placeName+ ", " + placeAddress);


        } else {

        }

        if(allowSave){
            // new
            progressDialog.show();
            if(hasImg && placeID == null){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Place place= new Place.Builder()
                                .setPlaceID(UUID.randomUUID().toString())
                                .setCategoryID(CategoryID)
                                .setPlaceName(placeName)
                                .setPlacePrice(placePrice)
                                .setPlaceAddress(placeAddress)
                                .setPlaceTime(placeTime)
                                .setPlaceDescription(placeDescription)
                                .setPlaceimg(convertImgToByteArray(imgAddEditPlace))
                                .setPlaceLat(location.getLat())
                                .setPlaceLng(location.getLng())
                                .build();
                        placeRepo.insert(place);
                        progressDialog.dismiss();
                        redirectPlacesAct();

                    }
                },2000);

            }
            // cập nhập
            if(placeID  != null){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Place place= new Place.Builder()
                                .setPlaceID(placeID)
                                .setCategoryID(CategoryID)
                                .setPlaceName(placeName)
                                .setPlacePrice(placePrice)
                                .setPlaceAddress(placeAddress)
                                .setPlaceTime(placeTime)
                                .setPlaceDescription(placeDescription)
                                .setPlaceimg(convertImgToByteArray(imgAddEditPlace))
                                .setPlaceLat(location.getLat())
                                .setPlaceLng(location.getLng())
                                .build();
                        placeRepo.update(place);
                        progressDialog.dismiss();
                        redirectDetailAct();

                    }
                },2000);

            }


        }
    }










    @OnClick(R.id.EditAdd_Img_Browse)
    public void BrowseImg(View view){
        imageBrowse();

    }


}
