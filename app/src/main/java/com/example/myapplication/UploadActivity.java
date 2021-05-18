package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;


public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Intent intent = getIntent();
        final int key = intent.getExtras().getInt("time");
        final String url = intent.getExtras().getString("url");
        Log.e("time", String.valueOf(key));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(UploadActivity.this,ImageActivity.class);
                intent.putExtra("time",key);
                intent.putExtra("url",url);
                startActivity(intent);
                finish();
            }
        },key*1000);

    }
}
