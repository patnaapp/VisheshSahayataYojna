package com.bih.nic.in.visheshsahayata.activity;/*
* By Jorge E. Hernandez (@lalongooo) 2015
* */

import android.app.Application;

import com.bih.nic.in.visheshsahayata.activity.file.FileUtils;




public class VideoCompressorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.createApplicationFolder();
    }

}