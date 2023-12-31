package com.example.toeatlistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "restaurants_db";
    private static final String TABLE_NAME = "restaurants";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FOOD_TYPE = "food_type";
    private static final String KEY_FAVOURITE = "favourite";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_TELEPHONE + " TEXT,"
                + KEY_DISTRICT + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_FOOD_TYPE + " TEXT,"
                + KEY_FAVOURITE + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    long addRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, restaurant.getName());
        values.put(KEY_TELEPHONE, restaurant.getTelephone());
        values.put(KEY_DISTRICT, restaurant.getDistrict());
        values.put(KEY_DESCRIPTION, restaurant.getDescription());
        values.put(KEY_FOOD_TYPE, restaurant.getFoodType());
        values.put(KEY_FAVOURITE, 0);

        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public Restaurant getRestaurant(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KEY_ID, KEY_NAME, KEY_TELEPHONE, KEY_DISTRICT, KEY_DESCRIPTION, KEY_FOOD_TYPE},
                KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Restaurant restaurant = new Restaurant();
            int columnIndex;

            columnIndex = cursor.getColumnIndex(KEY_ID);
            if (columnIndex != -1) {
                restaurant.setId(cursor.getInt(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(KEY_NAME);
            if (columnIndex != -1) {
                restaurant.setName(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(KEY_TELEPHONE);
            if (columnIndex != -1) {
                restaurant.setTelephone(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(KEY_DISTRICT);
            if (columnIndex != -1) {
                restaurant.setDistrict(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
            if (columnIndex != -1) {
                restaurant.setDescription(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(KEY_FOOD_TYPE);
            if (columnIndex != -1) {
                restaurant.setFoodType(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(KEY_FAVOURITE);
            if (columnIndex != -1) {
                restaurant.setFavourite(cursor.getInt(columnIndex) != 0);
            }

            cursor.close();
            return restaurant;
        }

        return null;
    }

    public int updateRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, restaurant.getName());
        values.put(KEY_TELEPHONE, restaurant.getTelephone());
        values.put(KEY_DISTRICT, restaurant.getDistrict());
        values.put(KEY_DESCRIPTION, restaurant.getDescription());
        values.put(KEY_FOOD_TYPE, restaurant.getFoodType());
        values.put(KEY_FAVOURITE, restaurant.getFavourite() ? 1 : 0);

        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(restaurant.getId())});
    }

    public void deleteRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(restaurant.getId())});
        db.close();
    }

    public void setFavourite(long id, boolean favourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FAVOURITE, favourite ? 1 : 0);
        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Restaurant> getFavouriteRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_FAVOURITE + " = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                int columnIndex;

                columnIndex = cursor.getColumnIndex(KEY_ID);
                if (columnIndex != -1) {
                    restaurant.setId(cursor.getInt(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_NAME);
                if (columnIndex != -1) {
                    restaurant.setName(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_TELEPHONE);
                if (columnIndex != -1) {
                    restaurant.setTelephone(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_DISTRICT);
                if (columnIndex != -1) {
                    restaurant.setDistrict(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
                if (columnIndex != -1) {
                    restaurant.setDescription(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_FOOD_TYPE);
                if (columnIndex != -1) {
                    restaurant.setFoodType(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_FAVOURITE);
                if (columnIndex != -1) {
                    restaurant.setFavourite(cursor.getInt(columnIndex) != 0);
                }

                restaurantList.add(restaurant);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return restaurantList;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                int columnIndex;

                columnIndex = cursor.getColumnIndex(KEY_ID);
                if (columnIndex != -1) {
                    restaurant.setId(cursor.getInt(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_NAME);
                if (columnIndex != -1) {
                    restaurant.setName(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_TELEPHONE);
                if (columnIndex != -1) {
                    restaurant.setTelephone(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_DISTRICT);
                if (columnIndex != -1) {
                    restaurant.setDistrict(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
                if (columnIndex != -1) {
                    restaurant.setDescription(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_FOOD_TYPE);
                if (columnIndex != -1) {
                    restaurant.setFoodType(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(KEY_FAVOURITE);
                if (columnIndex != -1) {
                    restaurant.setFavourite(cursor.getInt(columnIndex) != 0);
                }

                restaurantList.add(restaurant);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return restaurantList;
    }
}