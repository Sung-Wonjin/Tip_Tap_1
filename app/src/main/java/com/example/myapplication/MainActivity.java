package com.example.myapplication;

import android.app.Activity;
import android.database.Cursor;
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
import android.util.Log;
import android.net.Uri;
import android.content.Intent;
import android.widget.Toast;

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

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            url = new URL("https://photo.pixtree.com:34569/sr/start/");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        HttpsURLConnection connection = null;
                        try {
                            connection = (HttpsURLConnection) url.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
                        try {
                            if (connection.getResponseCode() == 200) {
                                Toast.makeText(getApplicationContext(), "Connection seccess", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
                                // Error handling code goes here
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
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




