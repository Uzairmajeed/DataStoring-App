package com.facebook.database_app;

import android.graphics.Bitmap;

import android.graphics.Bitmap;

import android.graphics.Bitmap;

public class ImageData {
    private String name;
    private String imagePath;
    private String bio;
    private String phone;
    private String address;

    public ImageData(String name, String imagePath, String bio, String phone, String address) {
        this.name = name;
        this.imagePath = imagePath;
        this.bio = bio;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getBio() {
        return bio;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
