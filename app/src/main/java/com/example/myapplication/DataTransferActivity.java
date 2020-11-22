package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class DataTransferActivity extends Activity
{
	private static final String TAG = DataTransferActivity.class.getName();
	private static final int UPLOAD_FILES_REQUEST = 0;
	private static final int DOWNLOAD_FILES_REQUEST = 1;
	private static final int UPLOAD_FOLDER_REQUEST = 2;
	private static final int DOWNLOAD_FOLDER_REQUEST = 3;
	private static final int DOWNLOAD_FILE_ALIAS_REQUEST = 4;
	private static final int BROWSE_REQUEST = 5;
	private static final int SEND_REQUEST = 6;
	
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Upload files sample
        Button uploadFilesButton = (Button) findViewById(R.id.button_upload_files_id);
        uploadFilesButton.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				// FTP URL (Starts with ftp://, sftp://, scp:// or ftps:// followed by hostname and port).
				Uri ftpUri = Uri.parse("ftp://192.168.20.128");
				intent.setDataAndType(ftpUri, "vnd.android.cursor.dir/lysesoft.andftp.uri");
				// Upload
				intent.putExtra("command_type", "upload");
				// FTP credentials (optional)
				intent.putExtra("ftp_username", "anonymous");
				intent.putExtra("ftp_password", "test@test.com");
				//intent.putExtra("ftp_keyfile", "/sdcard/rsakey.txt");
				//intent.putExtra("ftp_keypass", "optionalkeypassword");
				// FTP settings (optional)
				intent.putExtra("ftp_pasv", "true");
				//intent.putExtra("ftp_resume", "true");
				//intent.putExtra("ftp_encoding", "UTF-8");
				//intent.putExtra("ftps_mode", "implicit");
				// Activity title
				intent.putExtra("progress_title", "Uploading files ...");
				intent.putExtra("local_file1", "/sdcard/subfolder1/file1.zip");
				intent.putExtra("local_file2", "/sdcard/subfolder2/file2.zip");
				// Optional initial remote folder (it must exist before upload)
				intent.putExtra("remote_folder", "/remotefolder/subfolder");
				//intent.putExtra("close_ui", "true");
				startActivityForResult(intent, UPLOAD_FILES_REQUEST);
			}
        });
                
        // Download files sample.
        Button downloadFilesButton = (Button) findViewById(R.id.button_download_files_id);
        downloadFilesButton.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				// FTP URL (Starts with ftp://, sftp://, ftps:// or scp:// followed by hostname and port).
				Uri ftpUri = Uri.parse("ftp://192.168.20.128");
				intent.setDataAndType(ftpUri, "vnd.android.cursor.dir/lysesoft.andftp.uri");
				// Download
				intent.putExtra("command_type", "download");
				// FTP credentials (optional)
				intent.putExtra("ftp_username", "anonymous");
				intent.putExtra("ftp_password", "test@test.com");
				//intent.putExtra("ftp_keyfile", "/sdcard/dsakey.txt");
				//intent.putExtra("ftp_keypass", "optionalkeypassword");
				// FTP settings (optional)
				intent.putExtra("ftp_pasv", "true");
				//intent.putExtra("ftp_resume", "true");
				//intent.putExtra("ftp_encoding", "UTF-8");
				//intent.putExtra("ftps_mode", "implicit");
				// Activity title
				intent.putExtra("progress_title", "Downloading files ...");
				// Remote files to download.
				intent.putExtra("remote_file1", "/remotefolder/subfolder/file1.zip");
				intent.putExtra("remote_file2", "/remotefolder/subfolder/file2.zip");
				// Target local folder where files will be downloaded.
				intent.putExtra("local_folder", "/sdcard/localfolder");	
				intent.putExtra("close_ui", "true");	
				startActivityForResult(intent, DOWNLOAD_FILES_REQUEST);
			}
        });
        
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