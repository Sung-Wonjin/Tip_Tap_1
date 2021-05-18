package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageSelect extends AppCompatActivity {

    ImageView image1;
    Button button1;
    Button button2;
    boolean processing;
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select);

        image1 = findViewById(R.id.image2);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        processing = false;
        Bitmap bitmap = getBitmapFromCacheDir("pixtreetemp.jpeg");
        image1.setImageBitmap(bitmap);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (processing == false) {
                    processing = true;
                    Log.e("state",String.valueOf(processing));
                    String result = MyAsyncTask();
                    Gson gson = new Gson();
                    responsess resp = gson.fromJson(result, responsess.class);
                    resp.logall();
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(result);
                    url = element.getAsJsonObject().get("link").getAsString();
                    Log.d("url", url);
                    final int waitingtime = element.getAsJsonObject().get("waiting_time").getAsInt();

                    Intent uploading = new Intent(ImageSelect.this, UploadActivity.class);
                    uploading.putExtra("time", waitingtime);
                    uploading.putExtra("url",url);
                    startActivityForResult(uploading, 1);
                }
                if (processing == true) {
                    Log.e("state",String.valueOf(processing));
                    Toast.makeText(getApplicationContext(), "이미지 처리중입니다.", Toast.LENGTH_LONG);
                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageSelect.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public Bitmap getBitmapFromCacheDir(String name) {

        ArrayList<String> arrays = new ArrayList<>();

        File file = new File(getCacheDir().toString());
        File[] files = file.listFiles();
        for(File tempFile : files) {
            Log.d("File_List",tempFile.getName());
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

    public void saveBitmaptoJpeg(Bitmap bitmap, String name)
    {
        File tempfile = new File(getCacheDir(),name+".jpeg");

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

    private String getBitmapPathFromCacheDir(String name){
        ArrayList<String> arrays = new ArrayList<>();

        File file = new File(getCacheDir().toString());
        File[] files = file.listFiles();
        for(File tempFile : Objects.requireNonNull(files)) {
            Log.d("file_name",tempFile.getName());
            if(tempFile.getName().contains(name)) {
                arrays.add(tempFile.getName());
            }
        }
        if(arrays.size() > 0) {
            int randomPosition = new Random().nextInt(arrays.size());
            String path = getCacheDir() + "/" + arrays.get(randomPosition);
            Log.d("Path",path);
            return path;
        }
        else return null;
    }

    public String MyAsyncTask(){
        //TextView textview1 = (TextView) findViewById(R.id.textview1);
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .followRedirects(false)
                .build();
        final MediaType mediaType = MediaType.parse("text/plain");
        final RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("json", "jsonfile.json",
                        RequestBody.create(MediaType.parse("application/json"),
                                new File(Objects.requireNonNull(getBitmapPathFromCacheDir("jsonfile.json")))))
                .addFormDataPart("image", "pixtreetemp.jpeg",
                        RequestBody.create(MediaType.parse("image/jpeg"),
                                new File(Objects.requireNonNull(getBitmapPathFromCacheDir("pixtreetemp.jpeg")))))
                .build();
        final Request request = new Request.Builder()
                .url("http://photo.pixtree.com:34569/sr/start")
                .method("POST", body)
                .addHeader("Authorization", "Bearer supernova")
                .build();
        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @SuppressLint("StaticFieldLeak")
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            protected void onPostExecute (String str){
                super.onPostExecute(str);

                if (str != null) {
                    Log.e("response",str);
                    Gson gson = new Gson();
                    responsess respclass = gson.fromJson(str, responsess.class);
                }
            }
        };
        try {
            String result = asyncTask.execute().get();
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static class responsess {
        private String description;
        private String id;
        private String link;
        private int version;
        private int waiting_number;
        private int waiting_time;
        private int result_code;
        public void logall(){
            Log.e("description",description);
            Log.e("id",id);
            Log.e("link",link);
            Log.e("version",Integer.toString(version));
            Log.e("waiting number",Integer.toString(waiting_number));
            Log.e("waiting time",Integer.toString(waiting_time));
            Log.e("result code",Integer.toString(result_code));
        }
        public String getDescription(){ return description; }
        public String getId(){
            return id;
        }
        public String getLink(){
            return link;
        }
        public int getVersion(){
            return version;
        }
        public int getWaiting_number(){
            return waiting_number;
        }
        public int getWaiting_time(){
            return waiting_time;
        }
        public int getResunt_code(){ return result_code; }
        public void setDescription(){
            this.description = description;
        }
        public void setId(){
            this.id = id;
        }
        public void setLink(){
            this.link = link;
        }
        public void setVersion(){
            this.version = version;
        }
        public void setWaiting_number(){
            this.waiting_number = waiting_number;
        }
        public void setWaiting_time(){
            this.waiting_time = waiting_time;
        }
        public void setResult_code(){ this.result_code = result_code; }
    }
}
