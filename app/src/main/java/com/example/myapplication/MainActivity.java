package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

public class MainActivity extends AppCompatActivity {



    ImageView imageView;
    ImageView imageView1;
    Button button;
    Button button2;
    Button button3;
    Button button4;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckPermission();

        imageView = (ImageView) findViewById(R.id.image);
        imageView1 = (ImageView) findViewById(R.id.image1);
        final TextView textview1 = (TextView) findViewById(R.id.textview1);
        final String FileUri;
        backPressCloseHandler = new BackPressCloseHandler(this);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        String name = "pixtretemp";
        JsonToFile();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);


            }
        });//listener of the butten for the image call

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1 = (ImageView) findViewById(R.id.image1);
                String json = JsonToString();
                textview1.setText(json);

                Toast.makeText(MainActivity.this,"butten2 pressed",Toast.LENGTH_SHORT).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = MyAsyncTask();
                textview1.setText(result);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temppath = getBitmapPathFromCacheDir("pixtreetemp.jpg");
                textview1.setText(temppath);
            }
        });
    }

    public void CheckPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
                Toast.makeText(this,"정상적인 앱 실행을 위해서는 권한을 설정해야합니다",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

    }

    private String JsonToString(){
        String json = null;
        try {
            InputStream is = getAssets().open("jsons/config_json.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Log.e("jsontostring",json);
        return json;
    }

    private void JsonToFile(){
        File jsonfile = new File(getCacheDir(),"jsonfile.json");
        String json = null;
        try {
            InputStream is = getAssets().open("jsons/config_json.json");
            OutputStream os = new FileOutputStream(jsonfile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            os.write(buffer);
            is.close();
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        else return getBitmapFromAsset(getApplicationContext(),"images/1111.png");
    }//캐쉬 디렉토리에서 비트맵을 가져오는 함수. 함수를 변형해서 비트맵의 캐쉬상의 경로만 추출하여 가져와 전송하면 된다.


    private String getBitmapPathFromCacheDir(String name){
        ArrayList<String> arrays = new ArrayList<>();

        File file = new File(getCacheDir().toString());
        File[] files = file.listFiles();
        for(File tempFile : files) {
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
                        //사용자가 이미지를 선택함과 동시에 캐쉬에 비트맵을 저장한다 "pixtreetemp.jpg"

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
        { }
    }

    public String MyAsyncTask(){

        final OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        final MediaType mediaType = MediaType.parse("text/plain");
        final RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("json", "jsonfile.json",
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                                new File(getBitmapPathFromCacheDir("jsonfile.json"))))
                .addFormDataPart("image", "pixtreetemp.jpg",
                        RequestBody.create(MediaType.parse("image/*"),
                                new File(getBitmapPathFromCacheDir("pixtreetemp.jpg"))))
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
                    if (!response.isSuccessful()) {
                        Log.e("mytag","connection fail");
                        return "network error";
                    }
                    TextView textView2 = (TextView) findViewById(R.id.textview1);
                    textView2.setText(response.body().string());
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "exception";
                }
            }
        };
        asyncTask.execute();
        return "eeeee";
    }

    public class response {
        private String description;
        private int id;
        private String link;
        private int version;
        private int waiting_number;
        private int waiting_time;
        private int result_code;
        public String getDescription(){ return description; }
        public int getId(){
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




