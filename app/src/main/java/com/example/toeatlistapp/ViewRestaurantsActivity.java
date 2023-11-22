package com.example.toeatlistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ViewRestaurantsActivity extends AppCompatActivity {

    ListView restaurantsListView;
    List<Restaurant> restaurants;
    RestaurantAdapter adapter;
    SearchView searchView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurants);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        restaurantsListView = findViewById(R.id.restaurant_list);
        searchView = findViewById(R.id.searchView);

        adapter = new RestaurantAdapter(
                this,
                new ArrayList<>()
        );

        restaurantsListView.setAdapter(adapter);

        refreshRestaurantList();

        restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant selectedRestaurant = restaurants.get(position);
                Intent intent = new Intent(ViewRestaurantsActivity.this, RestaurantDetailsActivity.class);
                intent.putExtra("RESTAURANT_ID", (int) selectedRestaurant.getId());
                startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRestaurantList();
    }

    private void refreshRestaurantList() {
        restaurants = db.getAllRestaurants();
        adapter.setData(restaurants);
        adapter.notifyDataSetChanged();
    }
}