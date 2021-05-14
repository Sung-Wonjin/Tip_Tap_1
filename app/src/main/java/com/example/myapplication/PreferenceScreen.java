package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PreferenceScreen extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        addPreferencesFromResource(R.xml.activity_setting);

    }
}
