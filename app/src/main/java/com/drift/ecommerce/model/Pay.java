package com.drift.ecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pay implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;


    protected Pay(Parcel in) {
        id = in.readInt();
        name = in.readString();

    }

    public static final Creator<Pay> CREATOR = new Creator<Pay>() {
        @Override
        public Pay createFromParcel(Parcel in) {
            return new Pay(in);
        }

        @Override
        public Pay[] newArray(int size) {
            return new Pay[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);

    }
}