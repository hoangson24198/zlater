package com.example.zlater.Model;

import java.util.ArrayList;

public class Ingredient {
    private int id;
    private String title;
    private String image_url;
    private ArrayList<Dishes> dishes;

    public ArrayList<Dishes> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dishes> dishes) {
        this.dishes = dishes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
