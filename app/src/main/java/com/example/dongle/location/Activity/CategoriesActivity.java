package com.example.dongle.location.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dongle.location.Database.Model.Category;
import com.example.dongle.location.Database.PlaceRepo;
import com.example.dongle.location.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriesActivity extends AppCompatActivity {
    @BindView(R.id.txtCategories_coffee)
    TextView txtCoffee;
    @BindView(R.id.txtCategories_restaurant)
    TextView txtRestaurant;
    @BindView(R.id.txtCategories_cinema)
    TextView txtCinema;
    @BindView(R.id.txtCategories_atm)
    TextView txtATM;

    @BindView(R.id.viewCategories_coffee)
    TextView txtvCoffee;
    @BindView(R.id.viewCategories_restaurant)
    TextView txtvRestaurant;
    @BindView(R.id.viewCategories_cinema)
    TextView txtvCinema;
    @BindView(R.id.viewCategories_atm)
    TextView txtvATM;

    private PlaceRepo placeRepo;
    private List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setTitle("Categories");
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        placeRepo= placeRepo.getInstace(this);
        categories = placeRepo.getCategories();
    }
    private void startPlacesAct(String categoryID){
        Intent placesActIntent= new Intent(CategoriesActivity.this,PlacesActivity.class);
        placesActIntent.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA, categoryID);
        startActivity(placesActIntent);
    }




    @OnClick(R.id.layoutCategories_coffee)
    public void ClickOnCoffee(View view){
        String categoryID= categories.get(0).getCategoryID();
        startPlacesAct(categoryID);

    }

    @OnClick(R.id.layoutCategories_restaurant)
    public void ClickOnRestaurant(View view){
        String categoryID= categories.get(1).getCategoryID();
        startPlacesAct(categoryID);

    }

    @OnClick(R.id.layoutCategories_cinema)
    public void ClickOnCinema(View view){
        String categoryID= categories.get(2).getCategoryID();
        startPlacesAct(categoryID);

    }

    @OnClick(R.id.layoutCategories_atm)
    public void ClickOnATM(View view){
        String categoryID= categories.get(3).getCategoryID();
        startPlacesAct(categoryID);

    }
}
