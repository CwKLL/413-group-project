package com.example.toeatlistapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button addRestaurantButton;
    Button randomRestaurantButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    ListView favoriteRestaurantListView;
    ListView restaurantListView;
    RestaurantAdapter favoriteAdapter;
    RestaurantAdapter adapter;
    List<Restaurant> favoriteRestaurants = new ArrayList<>();
    List<Restaurant> restaurants = new ArrayList<>();

    private final BroadcastReceiver mBroadcastReceiverRemoved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onFavoriteRemoved();
        }
    };

    private final BroadcastReceiver mBroadcastReceiverAdded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onFavoriteAdded();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            showMainMenu();
        return true;
    }

    private void showMainMenu() {
        setContentView(R.layout.activity_main);

        addRestaurantButton = findViewById(R.id.addRestaurantButton);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        favoriteRestaurantListView = findViewById(R.id.favoriteRestaurantListView);
        restaurantListView = findViewById(R.id.restaurantListView);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiverRemoved,
                new IntentFilter("favorite-removed"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiverAdded,
                new IntentFilter("favorite-added"));


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == navigationView.getMenu().findItem(R.id.nav_view_restaurants).getItemId()) {
                    Intent intent = new Intent(MainActivity.this, ViewRestaurantsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

                item.setChecked(false);

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        addRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRestaurantActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button viewRestaurantsButton = findViewById(R.id.view_restaurants_button);
        viewRestaurantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewRestaurantsActivity.class);
                startActivity(intent);
            }
        });

        randomRestaurantButton = findViewById(R.id.random_restaurants_button);
        randomRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RandomRestaurantActivity.class);
                startActivity(intent);
            }
        });

        favoriteAdapter = new RestaurantAdapter(this, favoriteRestaurants, false);
        favoriteRestaurantListView.setAdapter(favoriteAdapter);

        adapter = new RestaurantAdapter(this, restaurants, false);
        restaurantListView.setAdapter(adapter);

        refreshData();
    }

    public void onFavoriteRemoved() {
        refreshData();
    }

    public void onFavoriteAdded() {
        refreshData();
    }

    private void refreshData() {
        new FetchDataTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiverRemoved);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiverAdded);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchDataTask extends AsyncTask<Void, Void, Pair<List<Restaurant>, List<Restaurant>>> {
        @Override
        protected Pair<List<Restaurant>, List<Restaurant>> doInBackground(Void... params) {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            List<Restaurant> allRestaurants = db.getAllRestaurants();
            List<Restaurant> favoriteRestaurants = new ArrayList<>();
            List<Restaurant> otherRestaurants = new ArrayList<>();

            for (Restaurant restaurant : allRestaurants) {
                if (restaurant.isFavourite()) {
                    favoriteRestaurants.add(restaurant);
                } else {
                    otherRestaurants.add(restaurant);
                }
            }
            return new Pair<>(favoriteRestaurants, otherRestaurants);
        }

        @Override
        protected void onPostExecute(Pair<List<Restaurant>, List<Restaurant>> pair) {
            favoriteRestaurants.clear();
            restaurants.clear();

            TextView favoriteSuggestionTextView = findViewById(R.id.favoriteSuggestionTextView);

            if(!pair.first.isEmpty()) {
                Restaurant favRestaurant = pair.first.get(new Random().nextInt(pair.first.size()));
                favoriteRestaurants.add(favRestaurant);
                favoriteSuggestionTextView.setText("Want to go your favorite restaurant again?");
            } else {
                favoriteSuggestionTextView.setText("You have no favorite restaurant right, lets add some");
            }

            if(!pair.second.isEmpty()) {
                Restaurant nonFavRestaurant = pair.second.get(new Random().nextInt(pair.second.size()));
                restaurants.add(nonFavRestaurant);
            }

            favoriteAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }
}