package com.example.dongle.location.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dongle.location.Database.Model.Category;
import com.example.dongle.location.Database.Model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class PlaceRepo {
    private static PlaceRepo INSTANCE;
    private PlaceSqliteHelper placeSqliteHepler;

    private PlaceRepo(Context context){
        placeSqliteHepler = new PlaceSqliteHelper(context);
    }

    public static PlaceRepo getInstace(Context context){
        if (INSTANCE == null){
            return new PlaceRepo(context);
        }else {
            return INSTANCE;
        }

    }

    public List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase= placeSqliteHepler.getReadableDatabase();

        String[] columns = {
                DataUlits.COLUMN_CATEGORY_ID,
                DataUlits.COLUMN_CATEGORY_NAME
        };

        Cursor cursor =sqLiteDatabase.query(DataUlits.CATEGORY_TBL_NAME, columns, null,null,null,null,null);
        if ( cursor!=null && cursor.getCount() >0){
            while (cursor.moveToNext()){
                String categoryID = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_CATEGORY_ID));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_CATEGORY_NAME));

                categories.add(new Category(categoryID,categoryName));
            }
        }

        if (cursor!=null){
            cursor.close();
        }
        sqLiteDatabase.close();
        return categories;
    }

    public List<Place> getPlaces(String cateID){
        List<Place> places = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase= placeSqliteHepler.getReadableDatabase();

        String[] columns = {
                DataUlits.COLUMN_PLACE_ID,
                DataUlits.COLUMN_PLACE_CATEGORY_ID,
                DataUlits.COLUMN_PLACE_NAME,
                DataUlits.COLUMN_PLACE_ADDRESS,
                DataUlits.COLUMN_PLACE_PRICE,
                DataUlits.COLUMN_PLACE_TIME,
                DataUlits.COLUMN_PLACE_DESCRIPITION,
                DataUlits.COLUMN_PLACE_IMGAE,
                DataUlits.COLUMN_PLACE_LAT,
                DataUlits.COLUMN_PLACE_LNG,
        };
        String selection = DataUlits.COLUMN_PLACE_CATEGORY_ID + " =?";
        String [] selectionArr={cateID};

        Cursor cursor =sqLiteDatabase.query(DataUlits.PLACE_TBL_NAME, columns, selection,selectionArr,null,null,null);
        if ( cursor!=null && cursor.getCount() >0){
            while (cursor.moveToNext()){
                String placeID = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_ID));
                String placeCategoryID = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_CATEGORY_ID));
                String placeName = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_NAME));
                String placeAddress = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_ADDRESS));
                String placePrice = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_PRICE));
                String placeTime = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_TIME));
                String placeDescription = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_DESCRIPITION));
                byte[] placeimg = cursor.getBlob(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_IMGAE));
                double placeLat = cursor.getDouble(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_LAT));
                double placeLng = cursor.getDouble(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_LNG));

                Place place = new Place.Builder()
                        .setPlaceID(placeID)
                        .setCategoryID(placeCategoryID)
                        .setPlaceName(placeName)
                        .setPlaceAddress(placeAddress)
                        .setPlacePrice(placePrice)
                        .setPlaceTime(placeTime)
                        .setPlaceDescription(placeDescription)
                        .setPlaceimg(placeimg)
                        .setPlaceLat(placeLat)
                        .setPlaceLng(placeLng)
                        .build();
                places.add(place);
            }
        }

        if (cursor!=null){
            cursor.close();
        }
        sqLiteDatabase.close();
        return places;
    }

    public Place getPlace(String cateID,String plID){
        Place place = null;

        SQLiteDatabase sqLiteDatabase= placeSqliteHepler.getReadableDatabase();

        String[] columns = {
                DataUlits.COLUMN_PLACE_ID,
                DataUlits.COLUMN_PLACE_CATEGORY_ID,
                DataUlits.COLUMN_PLACE_NAME,
                DataUlits.COLUMN_PLACE_ADDRESS,
                DataUlits.COLUMN_PLACE_PRICE,
                DataUlits.COLUMN_PLACE_TIME,
                DataUlits.COLUMN_PLACE_DESCRIPITION,
                DataUlits.COLUMN_PLACE_IMGAE,
                DataUlits.COLUMN_PLACE_LAT,
                DataUlits.COLUMN_PLACE_LNG,


        };


        String selection = DataUlits.COLUMN_PLACE_ID + " = ?" + " AND " + DataUlits.COLUMN_PLACE_CATEGORY_ID + " = ?";
        String [] selectionArr={plID,cateID};

        Cursor cursor =sqLiteDatabase.query(DataUlits.PLACE_TBL_NAME, columns, selection,selectionArr,null,null,null);
        if ( cursor!=null && cursor.getCount() >0){
            cursor.moveToFirst();
            String placeID = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_ID));
            String placeCategoryID = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_CATEGORY_ID));
            String placeName = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_NAME));
            String placeAddress = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_ADDRESS));
            String placePrice = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_PRICE));
            String placeTime = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_TIME));
            String placeDescription = cursor.getString(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_DESCRIPITION));
            byte[] placeimg = cursor.getBlob(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_IMGAE));
            double placeLat = cursor.getDouble(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_LAT));
            double placeLng = cursor.getDouble(cursor.getColumnIndexOrThrow(DataUlits.COLUMN_PLACE_LNG));

            place  = new Place.Builder()
                    .setPlaceID(placeID)
                    .setCategoryID(placeCategoryID)
                    .setPlaceName(placeName)
                    .setPlaceAddress(placeAddress)
                    .setPlacePrice(placePrice)
                    .setPlaceTime(placeTime)
                    .setPlaceDescription(placeDescription)
                    .setPlaceimg(placeimg)
                    .setPlaceLat(placeLat)
                    .setPlaceLng(placeLng)
                    .build();

        }

        if (cursor!=null){
            cursor.close();
        }
        sqLiteDatabase.close();
        return place;
    }

    public void insert(Place place){

        SQLiteDatabase sqLiteDatabase= placeSqliteHepler.getWritableDatabase();

        ContentValues contentValues= new ContentValues();
        contentValues.put(DataUlits.COLUMN_PLACE_ID, place.getPlaceID());
        contentValues.put(DataUlits.COLUMN_PLACE_CATEGORY_ID, place.getCategoryID());
        contentValues.put(DataUlits.COLUMN_PLACE_NAME, place.getPlaceName());
        contentValues.put(DataUlits.COLUMN_PLACE_ADDRESS, place.getPlaceAddress());
        contentValues.put(DataUlits.COLUMN_PLACE_PRICE, place.getPlacePrice());
        contentValues.put(DataUlits.COLUMN_PLACE_TIME, place.getplaceTime());
        contentValues.put(DataUlits.COLUMN_PLACE_DESCRIPITION, place.getPlaceDescription());
        contentValues.put(DataUlits.COLUMN_PLACE_IMGAE, place.getPlaceimg());
        contentValues.put(DataUlits.COLUMN_PLACE_LAT, place.getPlaceLat());
        contentValues.put(DataUlits.COLUMN_PLACE_LNG, place.getPlaceLng());

        sqLiteDatabase.insert(DataUlits.PLACE_TBL_NAME, null, contentValues);
        sqLiteDatabase.close();


    }

    public void update(Place place){

        SQLiteDatabase sqLiteDatabase= placeSqliteHepler.getWritableDatabase();

        ContentValues contentValues= new ContentValues();
        contentValues.put(DataUlits.COLUMN_PLACE_ID, place.getPlaceID());
        contentValues.put(DataUlits.COLUMN_PLACE_CATEGORY_ID, place.getCategoryID());
        contentValues.put(DataUlits.COLUMN_PLACE_NAME, place.getPlaceName());
        contentValues.put(DataUlits.COLUMN_PLACE_ADDRESS, place.getPlaceAddress());
        contentValues.put(DataUlits.COLUMN_PLACE_PRICE, place.getPlacePrice());
        contentValues.put(DataUlits.COLUMN_PLACE_TIME, place.getplaceTime());
        contentValues.put(DataUlits.COLUMN_PLACE_DESCRIPITION, place.getPlaceDescription());
        contentValues.put(DataUlits.COLUMN_PLACE_IMGAE, place.getPlaceimg());
        contentValues.put(DataUlits.COLUMN_PLACE_LAT, place.getPlaceLat());
        contentValues.put(DataUlits.COLUMN_PLACE_LNG, place.getPlaceLng());


        String selection = DataUlits.COLUMN_PLACE_ID + " =?";
        String [] selectionArr={place.getPlaceID()};
        sqLiteDatabase.update(DataUlits.PLACE_TBL_NAME, contentValues,selection,selectionArr);
        sqLiteDatabase.close();


    }

    public void delete(String plID){

        SQLiteDatabase sqLiteDatabase= placeSqliteHepler.getWritableDatabase();
        String selection = DataUlits.COLUMN_PLACE_ID + " =?";
        String [] selectionArr={plID};
        sqLiteDatabase.delete(DataUlits.PLACE_TBL_NAME,selection,selectionArr);
        sqLiteDatabase.close();


    }

}
