package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.File;
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

public class ImageActivity extends AppCompatActivity {

    ImageView imageView1;
    Button button1;
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1 = (ImageView) findViewById(R.id.image1);
                String result = MyAsyncTask();
                Gson gson = new Gson();
                MainActivity.responsess resp = gson.fromJson(result, MainActivity.responsess.class);
                resp.logall();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);
                url = element.getAsJsonObject().get("link").getAsString();
                Log.d("url",url);
                final int waitingtime = element.getAsJsonObject().get("waiting_time").getAsInt();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso
                                .get()
                                .load(url)
                                .into(imageView1);
                    }
                },waitingtime*1000);
            }
        });
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
        TextView textview1 = (TextView) findViewById(R.id.textview1);
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
                    /*if (!response.isSuccessful()) {
                        Log.e("mytag", "connection fail");
                        return "network error";
                    }*/
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            protected void onPostExecute (String str){
                super.onPostExecute(str);
                TextView textview1 = (TextView) findViewById(R.id.textview1);

                if (str != null) {
                    textview1.setText(str);
                    Log.e("response",str);
                    Gson gson = new Gson();
                    MainActivity.responsess respclass = gson.fromJson(str, MainActivity.responsess.class);
                    //respclass.logall();
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
