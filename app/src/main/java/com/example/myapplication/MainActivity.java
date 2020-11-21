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

        backPressCloseHandler = new BackPressCloseHandler(this);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);



                Intent intent2 = new Intent();
                intent2.putExtra("local_file",intent.toUri(0));
				intent.setAction(Intent.ACTION_PICK);
				// FTP URL (Starts with ftp://, sftp://, scp:// or ftps:// followed by hostname and port).
				Uri ftpUri = Uri.parse("sftp://photo.pixtree.com:57556");
				intent2.setDataAndType(ftpUri, "vnd.android.cursor.dir/vnd.pixtree.uri");
				// Upload
				intent2.putExtra("command_type", "upload");
				// FTP credentials (optional)
				intent2.putExtra("ftp_username", "snova");
				intent2.putExtra("ftp_password", "^Snova");
				//intent.putExtra("ftp_keyfile", "/sdcard/rsakey.txt");
				//intent.putExtra("ftp_keypass", "optionalkeypassword");
				// FTP settings (optional)
				intent2.putExtra("ftp_pasv", "true");
				//intent.putExtra("ftp_resume", "true");
				//intent.putExtra("ftp_encoding", "UTF-8");
				//intent.putExtra("ftps_mode", "implicit");
				// Activity title
				intent2.putExtra("progress_title", "Uploading files ...");
				intent2.putExtra("local_file1", "/sdcard/subfolder1/file1.zip");
				//intent.putExtra("local_file2", "/sdcard/subfolder2/file2.zip");
				// Optional initial remote folder (it must exist before upload)
				//intent.putExtra("remote_folder", "/remotefolder/subfolder");
				//intent.putExtra("close_ui", "true");
				startActivityForResult(intent2, UPLOAD_FILES_REQUEST);
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        Button uploadFilesButton = (Button) findViewById(R.id.button2);
        uploadFilesButton.setOnClickListener(new View.OnClickListener()
            {

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
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
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




