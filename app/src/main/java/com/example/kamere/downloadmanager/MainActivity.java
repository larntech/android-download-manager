package com.example.kamere.downloadmanager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

Button btnDownload;

//download manager
DownloadManager downloadManager;

//Uri for download
Uri downloadUri;

//change this to point your url
final String downloadLink = "http://192.168.43.254/test.pdf";

//name of your item
String itemName="test";

//unique id
long downloadId;
    public static final int RequestPermissionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button
        btnDownload = (Button) findViewById(R.id.btnDownload);

        //download manager
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        //click event to the button
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(itemName, downloadLink);
            }
        });

        //checking permissions to write and read external storage
        if(!checkPermission()){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            READ_EXTERNAL_STORAGE,
                            WRITE_EXTERNAL_STORAGE
                    }, RequestPermissionCode);

        }


    }


    public void downloadFile(String name, String url){


        //download link
        downloadUri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        //allow download to take place over wifi, mobile network and roaming
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE ).setAllowedOverRoaming(true);
        request.setAllowedOverRoaming(false);

        //name to show while downloading
        request.setTitle(name + ".pdf");

        //description to show while downloading
        request.setDescription("Downloading " + name + ".pdf");

        //show on navigation
        request.setVisibleInDownloadsUi(true);

        //download path
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS.toString(), "/" + itemName + ".pdf");

        //file open when item on navigation is clicked
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadId = downloadManager.enqueue(request);

    }




    //check if permission is granted
    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE );
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean ReadStoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (ReadStoragePermission && WriteStoragePermission) {

//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        finish();

                    }
                }

                break;
        }
    }



}
