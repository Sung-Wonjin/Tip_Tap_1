package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class ComparePhoto extends AppCompatActivity {

    PhotoView photoView;
    Button button_change;
    boolean state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        Bitmap bmp = getBitmapFromCacheDir("enhanced.jpeg");
        Drawable drawable = new BitmapDrawable(bmp);
        photoView = findViewById(R.id.photoView);
        button_change = findViewById(R.id.button_change);
        photoView.setImageDrawable(drawable);

        state = true;

        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state == true){
                    Bitmap bmp = getBitmapFromCacheDir("pixtreetemp.jpeg");
                    Drawable drawable1 = new BitmapDrawable(bmp);
                    photoView.setImageDrawable(drawable1);
                    state = false;
                }else if(state == false){
                    Bitmap bmp = getBitmapFromCacheDir("enhanced.jpeg");
                    Drawable drawable1 = new BitmapDrawable(bmp);
                    photoView.setImageDrawable(drawable1);
                    state = true;
                }
            }
        });

    }



    private Bitmap getBitmapFromCacheDir(String name) {

        ArrayList<String> arrays = new ArrayList<>();

        File file = new File(getCacheDir().toString());
        File[] files = file.listFiles();
        for(File tempFile : files) {
            Log.d("MyTag",tempFile.getName());
            if(tempFile.getName().contains(name)) {
                arrays.add(tempFile.getName());
            }
        }
        if(arrays.size() > 0) {
            int randomPosition = new Random().nextInt(arrays.size());
            String path = getCacheDir() + "/" + arrays.get(randomPosition);
            return BitmapFactory.decodeFile(path);
        }
        else return getBitmapFromAsset(getApplicationContext(),"images/1111.png");
    }

    public static Bitmap getBitmapFromAsset(Context context, String filename) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filename);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }
        return bitmap;
    }
}
