package com.example.toeatlistapp;

public class Restaurant {
    private int id;
    private String name;
    private String telephone;
    private String district;
    private String description;
    private String foodType;
    private boolean isFavourite;

    public Restaurant() {
    }

    public Restaurant(int id, String name, String telephone, String district, String description, String foodType) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.district = district;
        this.description = description;
        this.foodType = foodType;
    }

    public Restaurant(String name, String telephone, String district, String description, String foodType) {
        this.name = name;
        this.telephone = telephone;
        this.district = district;
        this.description = description;
        this.foodType = foodType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        this.isFavourite = favourite;
    }

    @Override
    public String toString() {
        return this.name + " " + this.telephone + " " + this.district +
                " " + this.description + " " + this.foodType;
    }
}