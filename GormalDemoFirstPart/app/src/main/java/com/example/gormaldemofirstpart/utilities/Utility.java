package com.example.gormaldemofirstpart.utilities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by Pawan on 16-05-2018.
 */

public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_SMS = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void checkPermission(final Context mCcontext){
        if(ContextCompat.checkSelfPermission(mCcontext,Manifest.permission.READ_PHONE_STATE)
                + ContextCompat.checkSelfPermission(
                mCcontext,Manifest.permission.READ_PHONE_NUMBERS)
                + ContextCompat.checkSelfPermission(
                mCcontext,Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) mCcontext,Manifest.permission.READ_PHONE_STATE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) mCcontext,Manifest.permission.READ_PHONE_NUMBERS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) mCcontext,Manifest.permission.READ_SMS)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(mCcontext);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                (Activity) mCcontext,
                                new String[]{
                                        Manifest.permission.READ_PHONE_STATE,
                                        Manifest.permission.READ_PHONE_NUMBERS,
                                        Manifest.permission.READ_SMS
                                },
                                MY_PERMISSIONS_REQUEST_READ_SMS
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        (Activity) mCcontext,
                        new String[]{
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_PHONE_NUMBERS,
                                Manifest.permission.READ_SMS
                        },
                        MY_PERMISSIONS_REQUEST_READ_SMS
                );
            }
        }else {
            // Do something, when permissions are already granted
            Toast.makeText(mCcontext,"Permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }

}
