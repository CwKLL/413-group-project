package com.example.toeatlistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ViewRestaurantsActivity extends AppCompatActivity {

    ListView restaurantsListView;
    List<Restaurant> restaurants;
    ArrayAdapter<String> adapter;
    DatabaseHelper db;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurants);

        db = new DatabaseHelper(this);

        restaurantsListView = findViewById(R.id.restaurant_list);

        restaurants = db.getAllRestaurants();

        List<String> restaurantNames = new ArrayList<>();
        for (Restaurant r : restaurants) {
            restaurantNames.add(r.getName());
        }

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                restaurantNames
        );

        restaurantsListView.setAdapter(adapter);

        restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant selectedRestaurant = restaurants.get(position);
                Intent intent = new Intent(ViewRestaurantsActivity.this, RestaurantDetailsActivity.class);
                intent.putExtra("RESTAURANT_ID", (int) selectedRestaurant.getId());
                startActivity(intent);
            }
        });

    }
    
    
}