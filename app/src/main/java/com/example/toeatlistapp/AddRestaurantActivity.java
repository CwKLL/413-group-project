package com.example.toeatlistapp;

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

public class AddRestaurantActivity extends AppCompatActivity {
    EditText nameField, telephoneField, descriptionField;
    Spinner districtSpinner, foodTypeSpinner;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        nameField = findViewById(R.id.restaurant_name);
        telephoneField = findViewById(R.id.telephone);
        districtSpinner = findViewById(R.id.district);
        descriptionField = findViewById(R.id.description);
        foodTypeSpinner = findViewById(R.id.food_type);
        saveButton = findViewById(R.id.save_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.districts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> foodTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_type_array, android.R.layout.simple_spinner_item);
        foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodTypeSpinner.setAdapter(foodTypeAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                    Restaurant restaurant = new Restaurant(name, telephone, district, description, foodType);
                    DatabaseHelper db = new DatabaseHelper(AddRestaurantActivity.this);
                    db.addRestaurant(restaurant);
                    Toast.makeText(getApplicationContext(), "Restaurant Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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