package com.example.toeatlistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends BaseAdapter implements Filterable {
    private List<Restaurant> originalData = null;
    private List<Restaurant> filteredData = null;
    private LayoutInflater layoutInflater;
    private ItemFilter itemFilter = new ItemFilter();

    public RestaurantAdapter(Context context, List<Restaurant> data) {
        this.originalData = data;
        this.filteredData = data;
        layoutInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_restaurant, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.restaurantName);
            holder.description = convertView.findViewById(R.id.restaurantDescription);
            holder.telephone = convertView.findViewById(R.id.restaurantTelephone);
            holder.district = convertView.findViewById(R.id.restaurantDistrict);
            holder.foodType = convertView.findViewById(R.id.restaurantFoodType);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Restaurant restaurant = filteredData.get(position);
        holder.name.setText(restaurant.getName());
        holder.description.setText(restaurant.getDescription());
        holder.telephone.setText(restaurant.getTelephone());
        holder.district.setText(restaurant.getDistrict());
        holder.foodType.setText(restaurant.getFoodType());

        return convertView;
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