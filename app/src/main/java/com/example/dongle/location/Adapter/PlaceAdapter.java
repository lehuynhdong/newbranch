package com.example.dongle.location.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dongle.location.Database.Model.Place;
import com.example.dongle.location.R;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class PlaceAdapter extends BaseAdapter{
    private Context context;
    private List<Place> places = new ArrayList<>();

    public PlaceAdapter(Context context, List<Place> places) {
        this.context = context;
    }
    public void setPlaces(List<Place> places){
        this.places = places;
    }

    public void updatePlaces(List<Place> places){
        this.places = places;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return places.size() != 0? places.size():0;
    }

    @Override
    public Object getItem(int i) {
        return places.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PlaceViewHolder viewHolder ;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Place place= (Place) getItem(i);
        if(view == null){
            viewHolder= new PlaceViewHolder();
            view =inflater.inflate(R.layout.item_places,viewGroup,false);
            ButterKnife.bind(viewHolder,view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (PlaceViewHolder) view.getTag();
        }


        if(place.getPlaceimg() != null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(place.getPlaceimg(),0,place.getPlaceimg().length);
            viewHolder.imgPlace.setImageBitmap(bitmap);
        }
        viewHolder.txtName.setText(place.getPlaceName());

        viewHolder.txtAdrress.setText(place.getPlaceAddress());

        viewHolder.txtPrice.setText(place.getPlacePrice());

        return view;
    }

    class PlaceViewHolder{
        @BindView(R.id.imgItemPlace)
        ImageView imgPlace;
        @BindView(R.id.txtItemPlace_Name)
        TextView txtName;
        @BindView(R.id.txtItemPlace_Address)
        TextView txtAdrress;
        @BindView(R.id.txtItemPlace_Price2)
        TextView txtPrice;
        @BindView(R.id.imageView)
        RoundedImageView imgView;

        

    }

}
