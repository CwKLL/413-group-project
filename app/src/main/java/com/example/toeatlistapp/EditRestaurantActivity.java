package com.example.toeatlistapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditRestaurantActivity extends AppCompatActivity {

    private EditText nameField, telephoneField, descriptionField;
    private Spinner districtSpinner, foodTypeSpinner;
    private Button saveButton, deleteButton;

    private Restaurant currentRestaurant;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        nameField = findViewById(R.id.nameField);
        telephoneField = findViewById(R.id.telephoneField);
        districtSpinner = findViewById(R.id.districtSpinner);
        descriptionField = findViewById(R.id.descriptionField);
        foodTypeSpinner = findViewById(R.id.foodTypeSpinner);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        int restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);

        if (restaurantId != -1) {
            currentRestaurant = db.getRestaurant(restaurantId);
            if (currentRestaurant != null) {
                nameField.setText(currentRestaurant.getName());
                telephoneField.setText(currentRestaurant.getTelephone());
                descriptionField.setText(currentRestaurant.getDescription());

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.districts_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                districtSpinner.setAdapter(adapter);
                districtSpinner.setSelection(adapter.getPosition(currentRestaurant.getDistrict()));

                ArrayAdapter<CharSequence> foodTypeAdapter = ArrayAdapter.createFromResource(this,
                        R.array.food_type_array, android.R.layout.simple_spinner_item);
                foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                foodTypeSpinner.setAdapter(foodTypeAdapter);
                foodTypeSpinner.setSelection(foodTypeAdapter.getPosition(currentRestaurant.getFoodType()));

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
                String foodType = foodTypeSpinner.getSelectedItem().toString();

                if (name.isEmpty() || telephone.isEmpty() || district.isEmpty() || description.isEmpty() || foodType.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (telephone.length() != 8) {
                    Toast.makeText(getApplicationContext(), "Telephone number must be exactly 8 digits", Toast.LENGTH_SHORT).show();
                } else {
                    currentRestaurant.setName(name);
                    currentRestaurant.setTelephone(telephone);
                    currentRestaurant.setDistrict(district);
                    currentRestaurant.setDescription(description);
                    currentRestaurant.setFoodType(foodType);

                    db.updateRestaurant(currentRestaurant);

                    Intent intent = new Intent(EditRestaurantActivity.this, ViewRestaurantsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditRestaurantActivity.this)
                        .setTitle("Delete Restaurant")
                        .setMessage("Are you sure you want to delete this restaurant?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteRestaurant(currentRestaurant);

                                Intent intent = new Intent(EditRestaurantActivity.this, ViewRestaurantsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

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