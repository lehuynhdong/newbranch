package com.example.dongle.location.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dongle.location.Database.Model.Place;
import com.example.dongle.location.Database.PlaceRepo;
import com.example.dongle.location.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements
        OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    @BindView(R.id.imgViewDetail_Place)
    ImageView imgViewPlace;
    @BindView(R.id.txtDetail_Name)
    TextView txtName;
    @BindView(R.id.txtDetail_Time)
    TextView txtTime;
    @BindView(R.id.txtDetail_Address)
    TextView txtAddress;
    @BindView(R.id.txtDetail_Price)
    TextView txtPrice;
    @BindView(R.id.txtDetail_Description)
    TextView txtDescription;


    private String placeID;
    private String CategoryID;
    private PlaceRepo placeRepo;
    private ProgressDialog progressDialog;
    private Place place;

    private GoogleMap googleMapDetail;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        init();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_detail);
        mapFragment.getMapAsync(this);
    }
    private void init(){
        placeID = getIntent().getStringExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA);
        CategoryID = getIntent().getStringExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA);
        placeRepo = PlaceRepo.getInstace(this);
//        Log.d("TEST", placeID + " " + categoryID);
        initprogressDialog();
        setPlace();

    }

    private void initprogressDialog(){
        progressDialog = new ProgressDialog(DetailActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_retrieve_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void setPlace(){
        place = placeRepo.getPlace(CategoryID, placeID);
        //place = addTestData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if(place.getPlaceimg() != null){
                    Bitmap bitmap= BitmapFactory.decodeByteArray(place.getPlaceimg(),0,place.getPlaceimg().length);
                    imgViewPlace.setImageBitmap(bitmap);
                }
                txtName.setText(place.getPlaceName());
                txtTime.setText(place.getplaceTime());
                txtAddress.setText(place.getPlaceAddress());
                txtPrice.setText(place.getPlacePrice());
                txtDescription.setText(place.getPlaceDescription());

            }
        },3000);


    }

    private Place addTestData(){
        Place place= new Place.Builder()
                .setPlaceID(UUID.randomUUID().toString())
                .setCategoryID(CategoryID)
                .setPlaceimg(null)
                .setPlaceName("The Coffee House - Bùi Hữu Nghĩa")
                .setPlaceAddress("270 Bùi Hữu Nghĩa, P. 2,  Quận Bình Thạnh, TP. HCM")
                .setPlacePrice("30.000đ - 60.000đ")
                .setPlaceTime("09:00 - 22:00")
                .setPlaceDescription("Mình khá thường xuyên ra quán này vì nằm ngay đầu đường nhà ngoại mình. Mình có ấn tượng rất tốt về nhân viên ở đây. ^^ từ thu ngân cho đến phục vụ và cả quản lí, rất ần cần và chu đáo với khách. Bạn nhân viên mang nước cho mình xong bảo:\" em sẽ quay lại với nước lọc sau ạ\" rồi mới mang tiếp món cho bàn kế bên. Phục vụ chuyên nghiệp. ^^ view cũng rộng rãi thoải mái. Nãy vô mình thấy anh quản lí đang ngồi tháo đồ ra để treo lên cây thông ở quán rồi ^^. Thấy chưa gì quán đã trang trí đón Noel. Bạc xỉu ở đây khá ngon, không quá ngọt mà cái ly lại không có chữ The coffee house, hơi buồn tí. ^^ mình sẽ còn quay lại." +
                        "Follow insta foodholicvietnam để xem thêm nhiều món ăn nhé.\n" +
                        "\n" +
                        "Có ai mê mẩn món trà đen macchiato ở the coffee house không \uD83E\uDD24\uD83E\uDD24? Dù có ra thêm món mới nhưng mình vẫn thích trà đen nhất , được cái ly mới nhìn cute quá đi ak. Lớp macchiato béo thanh kết hợp với trà đen thì xuất xắc luôn rồi.\n" +
                        "Thêm điểm cộng là coffee house lúc nào cũng decor quán thật xinh , thật sang và hiện đại, nhân viên lại nhẹ nhàng lịch sự luôn. Không gian thì đủ loại bàn từ nhỏ tới lớn phù hợp cho cúp bồ hẹn hò hay hội bạn đi chơi tám với nhau hoặc làm việc một mình trong góc yên tĩnh cũng ok nè .\n")
                .setPlaceLng(0)
                .setPlaceLat(0)
                .build();
        return  place;
    }

    @OnClick(R.id.btnDetail_Del)
    public void deletePlace(View view){
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(DetailActivity.this);
        alerDialog.setTitle(getResources().getString(R.string.text_warning));
        alerDialog.setIcon(R.drawable.warning);
        alerDialog.setMessage(getResources().getString(R.string.text_warning_want_to_do)
                + " ' "
                + place.getPlaceName()
                + " ' ?"
        );

        alerDialog.setPositiveButton(getResources().getString(R.string.text_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DetailActivity.this, "Yes", Toast.LENGTH_SHORT).show();
            }
        });

        alerDialog.setNegativeButton(getResources().getString(R.string.text_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DetailActivity.this, "No", Toast.LENGTH_SHORT).show();
            }
        });
        alerDialog.show();

    }
    @OnClick(R.id.btnDetail_Edit)
    public void EditPlace(View view){

        Intent intent= new Intent(DetailActivity.this,AddEditActivity.class);
        intent.putExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA,place.getPlaceID());
        intent.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA,place.getCategoryID());
        startActivity(intent);
    }
    @OnClick(R.id.btnDetail_Description)
    public void DescriptionPlace(View view){

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMapDetail = googleMap;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    LatLng placeLocation = new LatLng(place.getPlaceLat(),place.getPlaceLng());
                    googleMapDetail.addMarker(new MarkerOptions().position(placeLocation)
                            .title(place.getPlaceName()));
                    googleMapDetail.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));
                progressDialog.dismiss();

            }
        },3000);

    }
}
