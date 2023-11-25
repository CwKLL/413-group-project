package com.example.toeatlistapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

public class RandomRestaurantActivity extends AppCompatActivity {

    ListView restaurantsListView;
    List<Restaurant> restaurants;
    Random random;
    DatabaseHelper db;
    Button randomRestaurantButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_restaurant);

        db = new DatabaseHelper(this);

        restaurantsListView = findViewById(R.id.restaurant_list);

        random = new Random();

        randomRestaurantButton = findViewById(R.id.random_restaurants_button);
        randomRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRandomRestaurant();
            }
        });
    }

    private void pickRandomRestaurant() {
        if (restaurants != null && !restaurants.isEmpty()) {
//            List<Restaurant> shuffledResult = new ArrayList<>(restaurantsListView);
            Collections.shuffle(restaurants);
            Restaurant randomRestaurant = restaurants.get(0);
            Toast.makeText(this, "Randomly picked restaurant: " + randomRestaurant.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No restaurants available", Toast.LENGTH_SHORT).show();
        }
    }
}