package com.example.toeatlistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.widget.TextView;
import java.util.List;
import java.util.Collections;

public class RandomRestaurantActivity extends AppCompatActivity {
    List<Restaurant> restaurants;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_restaurant);

        db = new DatabaseHelper(this);
        restaurants = db.getAllRestaurants();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pickRandomRestaurant();
    }

    private void pickRandomRestaurant() {
        TextView resultTextTextView = findViewById(R.id.random_restaurant_text);
        TextView resultTextView = findViewById(R.id.random_restaurant_result);
        if (restaurants != null && !restaurants.isEmpty()) {
            List<Restaurant> shuffledResult = restaurants;
            Collections.shuffle(shuffledResult);
            Restaurant randomRestaurant = shuffledResult.get(0);
            resultTextTextView.setText("Randomly picked restaurant: \n");
            resultTextView.setText(randomRestaurant.getName());
        } else {
            resultTextTextView.setText("No restaurants available");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}