package com.example.toeatlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class RestaurantDetailsActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView telephoneTextView;
    private TextView districtTextView;
    private TextView descriptionTextView;
    private TextView foodTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurants);

        nameTextView = findViewById(R.id.name);
        telephoneTextView = findViewById(R.id.telephone);
        districtTextView = findViewById(R.id.district);
        descriptionTextView = findViewById(R.id.description);
        foodTypeTextView = findViewById(R.id.foodType);

        // Get the passed restaurantId
        int restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);
        Log.d("RestaurantDetails", "Restaurant ID is: " + restaurantId);

        if (restaurantId != -1) {
            DatabaseHelper db = new DatabaseHelper(this);
            Restaurant restaurant = db.getRestaurant(restaurantId);

            if (restaurant != null) {
                nameTextView.setText(restaurant.getName());
                telephoneTextView.setText(restaurant.getTelephone());
                districtTextView.setText(restaurant.getDistrict());
                descriptionTextView.setText(restaurant.getDescription());
                foodTypeTextView.setText(restaurant.getFoodType());
            }
        }

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailsActivity.this, EditRestaurantActivity.class);
                intent.putExtra("restaurant_id", restaurantId);
                startActivity(intent);
            }
        });
    }
}