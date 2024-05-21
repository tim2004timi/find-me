package com.example.mobileproject;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("5cc96ce2-3351-46e7-8a01-87763819a535");
        MapKitFactory.initialize(this);
    }
}
