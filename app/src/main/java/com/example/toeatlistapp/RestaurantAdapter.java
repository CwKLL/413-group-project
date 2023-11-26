package com.example.toeatlistapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends BaseAdapter implements Filterable {
    private List<Restaurant> originalData = null;
    private List<Restaurant> filteredData = null;
    private LayoutInflater layoutInflater;
    private ItemFilter itemFilter = new ItemFilter();
    private Context context;
    private DatabaseHelper database;
    private PopupMenu currentPopup;

    private boolean isButtonVisible;

    public RestaurantAdapter(Context context, List<Restaurant> data, boolean isButtonVisible) {
        this.originalData = data;
        this.filteredData = data;
        this.context = context;
        this.database = new DatabaseHelper(context);
        this.isButtonVisible = isButtonVisible;
        layoutInflater = LayoutInflater.from(context);
    }

    private void sendFavoriteRemovedBroadcast() {
        Intent intent = new Intent("favorite-removed");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void sendFavoriteAddedBroadcast() {
        Intent intent = new Intent("favorite-added");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void setData(List<Restaurant> data) {
        this.originalData = data;
        this.filteredData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_restaurant, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.restaurantName);
            holder.description = convertView.findViewById(R.id.restaurantDescription);
            holder.telephone = convertView.findViewById(R.id.restaurantTelephone);
            holder.district = convertView.findViewById(R.id.restaurantDistrict);
            holder.foodType = convertView.findViewById(R.id.restaurantFoodType);
            holder.optionsButton = convertView.findViewById(R.id.optionsButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Restaurant restaurant = filteredData.get(position);

        if (restaurant.getFavourite()) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        if (isButtonVisible) {
            holder.optionsButton.setVisibility(View.VISIBLE);
        } else {
            holder.optionsButton.setVisibility(View.GONE);
        }

        holder.name.setText(restaurant.getName());
        holder.description.setText(restaurant.getDescription());
        holder.telephone.setText(restaurant.getTelephone());
        holder.district.setText(restaurant.getDistrict());
        holder.foodType.setText(restaurant.getFoodType());

        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPopup = new PopupMenu(context, v);
                currentPopup.getMenuInflater().inflate(R.menu.popup_menu, currentPopup.getMenu());

                updatePopupMenu(restaurant, currentPopup);

                MenuItem removeFromFavouriteItem = currentPopup.getMenu().findItem(R.id.remove_from_favourite);
                if (restaurant.getFavourite()) {
                    removeFromFavouriteItem.setVisible(true);
                } else {
                    removeFromFavouriteItem.setVisible(false);
                }

                currentPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.edit) {
                            Intent intent = new Intent(context, EditRestaurantActivity.class);
                            intent.putExtra("RESTAURANT_ID", restaurant.getId());
                            intent.putExtra("IS_FAVOURITE", restaurant.getFavourite());
                            context.startActivity(intent);
                            return true;
                        } else if (id == R.id.add_to_favourite) {
                            database.setFavourite(restaurant.getId(), true);
                            setData(database.getAllRestaurants());
                            updatePopupMenu(restaurant, currentPopup);
                            sendFavoriteAddedBroadcast();
                            return true;
                        } else if (id == R.id.remove_from_favourite) {
                            database.setFavourite(restaurant.getId(), false);
                            setData(database.getAllRestaurants());
                            updatePopupMenu(restaurant, currentPopup);
                            sendFavoriteRemovedBroadcast();
                            return true;
                        }
                        return false;
                    }
                });

                currentPopup.show();
            }
        });

        return convertView;
    }

    private void updatePopupMenu(Restaurant restaurant, PopupMenu currentPopup) {
        MenuItem removeFromFavouriteItem = currentPopup.getMenu().findItem(R.id.remove_from_favourite);
        MenuItem addToFavouriteItem = currentPopup.getMenu().findItem(R.id.add_to_favourite);

        if (restaurant.getFavourite()) {
            removeFromFavouriteItem.setVisible(true);
            addToFavouriteItem.setVisible(false);
        } else {
            removeFromFavouriteItem.setVisible(false);
            addToFavouriteItem.setVisible(true);
        }
    }

    public Filter getFilter() {
        return itemFilter;
    }

    private class ViewHolder {
        TextView name;
        TextView description;
        TextView telephone;
        TextView district;
        TextView foodType;
        Button optionsButton;
    }

    private class ItemFilter extends Filter {
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final List<Restaurant> list = originalData;

            int count = list.size();
            final ArrayList<Restaurant> newFilteredList = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                if (list.get(i).getName().toLowerCase().contains(filterString)
                        || list.get(i).getTelephone().toLowerCase().contains(filterString)
                        || list.get(i).getDistrict().toLowerCase().contains(filterString)
                        || list.get(i).getDescription().toLowerCase().contains(filterString)
                        || list.get(i).getFoodType().toLowerCase().contains(filterString)) {
                    newFilteredList.add(list.get(i));
                }
            }

            results.values = newFilteredList;
            results.count = newFilteredList.size();

            return results;
        }


        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Restaurant>) results.values;
            notifyDataSetChanged();
        }
    }
}