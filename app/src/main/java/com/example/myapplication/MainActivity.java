package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;
import com.bumptech.glide.Glide;
import java.util.Base64.Encoder;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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

        /*MyAsyncTask mProcessTask = null;
        try {
            mProcessTask = new MyAsyncTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mProcessTask.execute();*/


        imageView = (ImageView) findViewById(R.id.image);
        imageView1 = (ImageView) findViewById(R.id.image1);
        final TextView textview1 = (TextView) findViewById(R.id.textview1);
        final String FileUri;
        backPressCloseHandler = new BackPressCloseHandler(this);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        String name = "pixtretemp";
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
                Bitmap bitmap = getBitmapFromCacheDir("pixtreetemp");
                imageView1.setImageBitmap(bitmap);


                //BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                //Bitmap bmap = drawable.getBitmap();
                //saveBitmaptoJpeg(bmap,"pixtretemp");

                /*String imagestring = getBase64String(bmap);
                byte[] decodedByteArray = Base64.decode(imagestring, Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                imageView1.setImageBitmap(decodedBitmap);*/
                //get image from imageview and incode the image to BASE64 stream. and then decode the image to bitmap.
                //the code that makes json file to call rest api and parse response json shuld be in here
                Toast.makeText(MainActivity.this,"butten2 pressed",Toast.LENGTH_SHORT).show();
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
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }
        else return getBitmapFromAsset(getApplicationContext(),"sec/main/assets/images/1111.png");
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", "config_json.json",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("src/main/assets/config_json.json")))
                .addFormDataPart("", "pixtreetemp.jpg",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(getCacheDir().getName())))
                .build();
        Request request = new Request.Builder()
                .url("http://photo.pixtree.com:34569/sr/start/")
                .method("POST", body)
                .addHeader("Authorization", "Bearer supernova")
                .build();
        Response response = client.newCall(request).execute();

        public MyAsyncTask() throws IOException {
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    public void saveBitmaptoJpeg(Bitmap bitmap, String name)
    {
        File tempfile = new File(getCacheDir(),name+".jpg");

        try{
            FileOutputStream out = new FileOutputStream(tempfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.close();
        }
        catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
    }

    /*private Bitmap GetImageFromURL(String strImageURL)
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
    }*/

   /* private void httpconnection() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", "/C:/Users/SUNG/Documents/카카오톡 받은 파일/config_json.json",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("/C:/Users/SUNG/Documents/카카오톡 받은 파일/config_json.json")))
                .addFormDataPart("", "/C:/Users/SUNG/Documents/카카오톡 받은 파일/sample.jpg",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("/C:/Users/SUNG/Documents/카카오톡 받은 파일/sample.jpg")))
                .build();
        Request request = new Request.Builder()
                .url("http://photo.pixtree.com:34569/sr/start/")
                .method("POST", body)
                .addHeader("Authorization", "Bearer supernova")
                .build();
        Response response = client.newCall(request).execute();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                try {
                    InputStream in = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                    Bitmap img = BitmapFactory.decodeStream(in);
                    if (in != null) {
                        in.close();
                    }
                    imageView.setImageBitmap(img);
                    try {
                        saveBitmaptoJpeg(img,"pixtreetemp");
                        Log.e("Mytag","image saved");
                    }
                    catch (Exception e) {
                        Log.e("Mytag","image saved");
                    }
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




