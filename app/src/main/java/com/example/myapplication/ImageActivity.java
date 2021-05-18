package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    Button button2;
    Button button3;
    Button button4;
    String url;
    TextView textView;
    boolean processing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView1 = (ImageView) findViewById(R.id.image1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        Bitmap bitmap = getBitmapFromCacheDir("pixtreetemp.jpeg");
        imageView1.setImageBitmap(bitmap);
        textView = (TextView) findViewById(R.id.textview1);
        processing = false;
        Intent intent = getIntent();
        int waitingtime = intent.getExtras().getInt("time");
        url = intent.getExtras().getString("url");
        LoadImage();



        imageView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imageView1 = (ImageView) findViewById(R.id.image1);
                int status = event.getAction();

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        Bitmap bmp = getBitmapFromCacheDir("pixtreetemp.jpeg");
                        imageView1.setImageBitmap(bmp);
                        return true;
                    }
                    case MotionEvent.ACTION_UP:{
                        Bitmap bmp = getBitmapFromCacheDir("enhanced.jpeg");
                        imageView1.setImageBitmap(bmp);
                        return false;
                    }
                    default:return false;
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            CheckPermission();
            savePicture();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(ImageActivity.this,Preference.class);
                //startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(ImageActivity.this,ComparePhoto.class);
            //String path = getBitmapPathFromCacheDir("enhanced");
            //intent.putExtra("drawable", path);
            startActivity(intent);
            }
        });
    }

    public void LoadImage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso
                        .get()
                        .load(url)
                        .into(imageView1);
            }
        }, 1200);

        if(imageView1 != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        BitmapDrawable temp = (BitmapDrawable) imageView1.getDrawable();
                        Bitmap bitmap = temp.getBitmap();
                        saveBitmaptoJpeg(bitmap, "enhanced");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    processing = false;
                }
            }, 1500);
        }
        else reload();
    }

    public void reload(){
        if(imageView1 != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        BitmapDrawable temp = (BitmapDrawable) imageView1.getDrawable();
                        Bitmap bitmap = temp.getBitmap();
                        saveBitmaptoJpeg(bitmap, "enhanced");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    processing = false;
                }
            }, 500);
        }
        else reload();
    }

    private String dateName(long dateTaken){
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        return dateFormat.format(date);
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
    }//캐쉬 디렉토리에서 비트맵을 가져오는 함수. 함수를 변형해서 비트맵의 캐쉬상의 경로만 추출하여 가져와 전송하면 된다.


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

   private void savePicture() {
        String date = dateName(System.currentTimeMillis());
        Bitmap bmp = getBitmapFromCacheDir("enhanced.jpeg");
        File dir = new File(Environment.getExternalStorageDirectory(), "Pictures/TipTap/" + date);
        File isdir = new File(Environment.getExternalStorageDirectory(), "Pictures/TipTap");
        if(!isdir.isDirectory()){
            isdir.mkdirs();
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(dir.toString() + ".jpg");
            bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "저장실패", Toast.LENGTH_SHORT).show();
        }
    }


}
