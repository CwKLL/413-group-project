package com.example.toeatlistapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

//        restaurantsListView = findViewById(R.id.restaurant_list);

        random = new Random();
        restaurants = db.getAllRestaurants();
        pickRandomRestaurant();
    }

    private void pickRandomRestaurant() {
        TextView resultTextView = findViewById(R.id.random_restaurant_text);
        if (restaurants != null && !restaurants.isEmpty()) {
            List<Restaurant> shuffledResult = restaurants;
            Collections.shuffle(shuffledResult);
            Restaurant randomRestaurant = shuffledResult.get(0);
            resultTextView.setText("Randomly picked restaurant: \n \n" + randomRestaurant.getName());
        } else {
            resultTextView.setText("No restaurants available");
        }
    }
}