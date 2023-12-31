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
import android.widget.Button; // Added
import java.util.ArrayList;
import java.util.List;

public class ViewRestaurantsActivity extends AppCompatActivity {

    ListView restaurantsListView;
    List<Restaurant> restaurants;
    RestaurantAdapter adapter;
    SearchView searchView;
    DatabaseHelper db;
    Button favButton; // Added
    boolean showingFavorites = false; // Added

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
        favButton = findViewById(R.id.favButton);

        if (restaurants == null) {
            restaurants = new ArrayList<>();
        }

        adapter = new RestaurantAdapter(this, restaurants, true);
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

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowFavorites();
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

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    // Added
    private void toggleShowFavorites() {
        if(showingFavorites) {
            refreshRestaurantList();
            favButton.setText(R.string.show_favorites);
            showingFavorites = false;
        } else {
            restaurants = db.getFavouriteRestaurants();
            adapter.setData(restaurants);
            adapter.notifyDataSetChanged();
            favButton.setText(R.string.show_all);
            showingFavorites = true;
        }
    }
}