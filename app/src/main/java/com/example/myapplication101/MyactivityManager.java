package com.example.myapplication101;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class MyactivityManager {
    private static MyactivityManager sInstance = new MyactivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;

    public static MyactivityManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<>(activity);
    }
}
