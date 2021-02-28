package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
	private static final int UPLOAD_FILES_REQUEST = 0;
	private static final int DOWNLOAD_FILES_REQUEST = 1;
	private static final int UPLOAD_FOLDER_REQUEST = 2;
	private static final int DOWNLOAD_FOLDER_REQUEST = 3;
	private static final int DOWNLOAD_FILE_ALIAS_REQUEST = 4;
	private static final int BROWSE_REQUEST = 5;
	private static final int SEND_REQUEST = 6;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
		Log.i(TAG, "Result: "+resultCode+ " from request: "+requestCode);
		if (intent != null)
		{
			String transferredBytesStr = intent.getStringExtra("TRANSFERSIZE");
			String transferTimeStr = intent.getStringExtra("TRANSFERTIME");
			Log.i(TAG, "Transfer status: " + intent.getStringExtra("TRANSFERSTATUS"));
			Log.i(TAG, "Transfer amount: " + intent.getStringExtra("TRANSFERAMOUNT") + " file(s)");
			Log.i(TAG, "Transfer size: " + transferredBytesStr + " bytes");
			Log.i(TAG, "Transfer time: " + transferTimeStr + " milliseconds");
			// Compute transfer rate.
			if ((transferredBytesStr != null) && (transferTimeStr != null))
			{
				try
				{
					long transferredBytes = Long.parseLong(transferredBytesStr);
					long transferTime = Long.parseLong(transferTimeStr);
					double transferRate = 0.0;
					if (transferTime > 0) transferRate = ((transferredBytes) * 1000.0) / (transferTime * 1024.0);
					Log.i(TAG, "Transfer rate: " + transferRate + " KB/s");
				} 
				catch (NumberFormatException e)
				{
					// Cannot parse string.
				}
			}
		}
	}
}