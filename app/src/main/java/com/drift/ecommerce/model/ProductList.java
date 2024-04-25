package com.drift.ecommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductList {

    @SerializedName("data")
    @Expose
    private List<Product> data = null;

    public List<Product> getItems() {
        return data;
    }

    public void setItems(List<Product> items) {
        this.data = items;
    }
}