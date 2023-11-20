package com.example.toeatlistapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ViewRestaurantsActivity extends AppCompatActivity {

    ListView restaurantsListView;
    List<String> restaurants;
    ArrayAdapter<String> adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurants);

        db = new DatabaseHelper(this);

        restaurantsListView = findViewById(R.id.restaurant_list);

        restaurants = db.getAllRestaurants();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                restaurants
        );

        restaurantsListView.setAdapter(adapter);
    }
}