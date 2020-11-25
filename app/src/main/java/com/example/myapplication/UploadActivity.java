package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UploadActivity extends AppCompatActivity {

    private static final int UPLOAD_FILES_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

       
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		// FTP URL (Starts with ftp://, sftp://, scp:// or ftps:// followed by hostname and port).
		Uri ftpUri = Uri.parse("sftp://photo.pixtree.com");
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
		//intent.putExtra("progress_title", "Uploading files ...");
		intent.putExtra("local_file1", "/sdcard/subfolder1/file1.zip");
		//intent.putExtra("local_file2", "/sdcard/subfolder2/file2.zip");
		// Optional initial remote folder (it must exist before upload)
		intent.putExtra("remote_folder", "/remotefolder/subfolder");
		//intent.putExtra("close_ui", "true");
		startActivityForResult(intent, UPLOAD_FILES_REQUEST);
			
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