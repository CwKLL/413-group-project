package com.example.toeatlistapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditRestaurantActivity extends AppCompatActivity {
    // These would be your actual input fields and buttons
    private EditText nameField, telephoneField, descriptionField, foodTypeField;
    private Spinner districtSpinner;
    private Button saveButton, deleteButton;

    private Restaurant currentRestaurant;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant); // Replace with your actual layout

        // Initialize your DatabaseHelper
        db = new DatabaseHelper(this);

        // Initialize your views. Replace with your actual view IDs
        nameField = findViewById(R.id.nameField);
        telephoneField = findViewById(R.id.telephoneField);
        districtSpinner = findViewById(R.id.districtSpinner);
        descriptionField = findViewById(R.id.descriptionField);
        foodTypeField = findViewById(R.id.foodTypeField);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Retrieve the Restaurant ID passed from the previous activity
        int restaurantId = getIntent().getIntExtra("restaurant_id", -1);

        if (restaurantId != -1) {
            // Retrieve the restaurant data from the database
            currentRestaurant = db.getRestaurant(restaurantId);

            // Check if currentRestaurant is null before using it
            if (currentRestaurant != null) {
                // Display the restaurant data in the fields
                nameField.setText(currentRestaurant.getName());
                telephoneField.setText(currentRestaurant.getTelephone());
                descriptionField.setText(currentRestaurant.getDescription());
                foodTypeField.setText(currentRestaurant.getFoodType());

                // Set the district in the spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.districts_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                districtSpinner.setAdapter(adapter);
                districtSpinner.setSelection(adapter.getPosition(currentRestaurant.getDistrict()));
            } else {
                Toast.makeText(this, "Restaurant not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            Toast.makeText(this, "Error loading restaurant", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String telephone = telephoneField.getText().toString();
                String district = districtSpinner.getSelectedItem().toString();
                String description = descriptionField.getText().toString();
                String foodType = foodTypeField.getText().toString();

                if (name.isEmpty() || telephone.isEmpty() || district.isEmpty() || description.isEmpty() || foodType.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Update the restaurant data with the input from the fields
                    currentRestaurant.setName(name);
                    currentRestaurant.setTelephone(telephone);
                    currentRestaurant.setDistrict(district);
                    currentRestaurant.setDescription(description);
                    currentRestaurant.setFoodType(foodType);

                    // Update the restaurant data in the database
                    db.updateRestaurant(currentRestaurant);

                    // Close the activity and return to the previous screen
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Confirm with the user before deletion
                new AlertDialog.Builder(EditRestaurantActivity.this)
                        .setTitle("Delete Restaurant")
                        .setMessage("Are you sure you want to delete this restaurant?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the restaurant from the database
                                db.deleteRestaurant(currentRestaurant);

                                // Close the activity and start the restaurant list activity
                                Intent intent = new Intent(EditRestaurantActivity.this, ViewRestaurantsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
}