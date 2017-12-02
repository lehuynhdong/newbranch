package com.example.dongle.location.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dongle.location.Adapter.PlaceAdapter;
import com.example.dongle.location.Database.Model.Place;
import com.example.dongle.location.Database.PlaceRepo;
import com.example.dongle.location.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlacesActivity extends AppCompatActivity {
    @BindView(R.id.lvPlacse)
    ListView lvPlace;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;



    private String CategoryID;
    private PlaceRepo placeRepo;
    private List<Place> places = new ArrayList<>();
    private PlaceAdapter placeAdapter;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        setTitle("Place");
        ButterKnife.bind(this);
        init();

    }
    private void init() {
        CategoryID = getIntent().getStringExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA);
        placeRepo = PlaceRepo.getInstace(this);
        placeAdapter =  new PlaceAdapter(this,places);
        placeAdapter.notifyDataSetChanged();
        initProgressDialog();
        getPlaces();
        onPlaceClick();



    }
    private void getPlaces(){
        places = placeRepo.getPlaces(CategoryID);
        //addTestData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if(!places.isEmpty()){
                    txtNoData.setVisibility(View.GONE);
                }
                lvPlace.setAdapter(placeAdapter);
                placeAdapter.setPlaces(places);
                placeAdapter.notifyDataSetChanged();

            }
        },2000);

    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(PlacesActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_retrieve_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    private void onPlaceClick(){
        lvPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Place place = places.get(i);
                Intent intent= new Intent(PlacesActivity.this,DetailActivity.class);
                intent.putExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA,place.getPlaceID());
                intent.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA,place.getCategoryID());
                startActivity(intent);
            }
        });
    }
    private void addTestData(){
        Place place= new Place.Builder()
                .setPlaceID(UUID.randomUUID().toString())
                .setCategoryID(CategoryID)
                .setPlaceimg(null)
                .setPlaceName("The Coffee House - Bùi Hữu Nghĩa")
                .setPlaceAddress("270 Bùi Hữu Nghĩa, P. 2,  Quận Bình Thạnh, TP. HCM")
                .setPlacePrice("30.000đ - 60.000đ")
                .setPlaceTime("09:00 AM - 10:00 PM")
                .setPlaceDescription("Mình khá thường xuyên ra quán này vì nằm ngay đầu đường nhà ngoại mình. Mình có ấn tượng rất tốt về nhân viên ở đây. ^^ từ thu ngân cho đến phục vụ và cả quản lí, rất ần cần và chu đáo với khách. Bạn nhân viên mang nước cho mình xong bảo:\" em sẽ quay lại với nước lọc sau ạ\" rồi mới mang tiếp món cho bàn kế bên. Phục vụ chuyên nghiệp. ^^ view cũng rộng rãi thoải mái. Nãy vô mình thấy anh quản lí đang ngồi tháo đồ ra để treo lên cây thông ở quán rồi ^^. Thấy chưa gì quán đã trang trí đón Noel. Bạc xỉu ở đây khá ngon, không quá ngọt mà cái ly lại không có chữ The coffee house, hơi buồn tí. ^^ mình sẽ còn quay lại." +
                        "Follow insta foodholicvietnam để xem thêm nhiều món ăn nhé.\n" +
                        "\n" +
                        "Có ai mê mẩn món trà đen macchiato ở the coffee house không \uD83E\uDD24\uD83E\uDD24? Dù có ra thêm món mới nhưng mình vẫn thích trà đen nhất , được cái ly mới nhìn cute quá đi ak. Lớp macchiato béo thanh kết hợp với trà đen thì xuất xắc luôn rồi.\n" +
                        "Thêm điểm cộng là coffee house lúc nào cũng decor quán thật xinh , thật sang và hiện đại, nhân viên lại nhẹ nhàng lịch sự luôn. Không gian thì đủ loại bàn từ nhỏ tới lớn phù hợp cho cúp bồ hẹn hò hay hội bạn đi chơi tám với nhau hoặc làm việc một mình trong góc yên tĩnh cũng ok nè .\n")
                .setPlaceLng(0)
                .setPlaceLat(0)
                .build();
        places.add(place);
    }
    @OnClick(R.id.fab)
    public void add(View view){
        Intent i = new Intent(PlacesActivity.this,AddEditActivity.class);
        i.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA,CategoryID);
        startActivity(i);

    }
    @OnClick(R.id.btnMap)
    public void map(View view){
        Intent imap = new Intent(PlacesActivity.this,MapActivity.class);
        imap.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA,CategoryID);
        startActivity(imap);

    }
}
