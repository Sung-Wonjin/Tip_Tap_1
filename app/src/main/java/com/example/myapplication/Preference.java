package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;


public class Preference extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        addPreferencesFromResource(R.xml.activity_setting);

    }
    @SuppressLint("ResourceType")
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.xml.activity_setting, menu);

        return true;

    }

}

