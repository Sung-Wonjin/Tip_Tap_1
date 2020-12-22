package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
/*import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;*/


public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    ImageView imageView1;
    Button button;
    Button button2;


    private BackPressCloseHandler backPressCloseHandler;
    ImageActivity imageActivity;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);
        imageView1 = (ImageView) findViewById(R.id.image1);

        final String FileUri;
        backPressCloseHandler = new BackPressCloseHandler(this);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

            }
        });//listener of the butten for the image call

        /*imageView.setOnClickListener(new View.OnClickListener() {
        });//listener when the user touched the image. when the user click the image, android shows the whole image that can zoom in or zoom out.*/

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1 = (ImageView) findViewById(R.id.image1);
                Bitmap imgBitmap = GetImageFromURL("http://photo.pixtree.com:44569/236F2C1E-42FD-11EB-9055-002590D363D8.JPG");
                imageView1.setImageBitmap(imgBitmap);
                //Glide.with(MainActivity.this).load(url).into(imageView1);
                Toast.makeText(MainActivity.this,"butten2 pressed",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private Bitmap GetImageFromURL(String strImageURL)
    {
        Bitmap imgBitmap = null;
        try
        {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();
            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);

            bis.close();
        }
            catch (Exception e)
        {
            e.printStackTrace();
        }
        return imgBitmap;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView1 = findViewById(R.id.image1);
        
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                
                try {
                    InputStream in = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                    Bitmap img = BitmapFactory.decodeStream(in);
                    if (in != null) {
                        in.close();
                    }
                    Uri fileUri = data.getData();
                    String contentUri = getRealPathFromURI(fileUri);
                    data.putExtra("fileuri",contentUri);
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(requestCode == 0)
        {
            
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage"))
        {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) { return cursor.getString(columnIndex); }
        }
        finally
        {
            cursor.close();
        }
        return null;
    }
}




