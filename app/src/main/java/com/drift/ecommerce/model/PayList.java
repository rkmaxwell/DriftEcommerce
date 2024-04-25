package com.drift.ecommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayList {

    @SerializedName("data")
    @Expose
    private List<Pay> data = null;

    public List<Pay> getItems() {
        return data;
    }

    public void setItems(List<Pay> items) {
        this.data = items;
    }
}