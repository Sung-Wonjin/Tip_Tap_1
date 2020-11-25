package com.example.myapplication;

import android.app.Activity;
import android.database.Cursor;
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

import java.io.InputStream;
import java.util.Objects;
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

    private static final int UPLOAD_FILES_REQUEST = 0;

    ImageView imageView;
    ImageView imageView1;
    Button button;

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

                Intent intent4 = new Intent();
				intent4.setAction(Intent.ACTION_PICK);
				// FTP URL (Starts with ftp://, sftp://, scp:// or ftps:// followed by hostname and port).
				Uri ftpUri = Uri.parse("sftp://photo.pixtree.com:57556");
				intent4.setDataAndType(ftpUri, "vnd.android.cursor.dir/lysesoft.andftp.uri");
				// Upload
				intent4.putExtra("command_type", "upload");
				// FTP credentials (optional)
				intent4.putExtra("ftp_username", "snova");
				intent4.putExtra("ftp_password", "^Snova");
				//intent.putExtra("ftp_keyfile", "/sdcard/rsakey.txt");
				//intent.putExtra("ftp_keypass", "optionalkeypassword");
				// FTP settings (optional)
				intent4.putExtra("ftp_pasv", "true");
				//intent.putExtra("ftp_resume", "true");
				//intent.putExtra("ftp_encoding", "UTF-8");
				//intent.putExtra("ftps_mode", "implicit");
				// Activity title
				intent4.putExtra("progress_title", "Uploading files ...");
				intent4.putExtra("local_file1", "/sdcard/subfolder1/file1.zip");
				// Optional initial remote folder (it must exist before upload)
				intent4.putExtra("remote_folder", "/input");
				//intent.putExtra("close_ui", "true");
				startActivityForResult(intent4, UPLOAD_FILES_REQUEST);
            }
        });//listener of the butten for the image call

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                //intent3.putExtra("fileUri", imageView.getImageURI());
                imageActivity.startActivity(intent3);
            }
        });//listener when the user touched the image. when the user click the image, android shows the whole image that can zoom in or zoom out.

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setType("image/*");
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
                    imageView.setImageURI(fileUri);
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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




