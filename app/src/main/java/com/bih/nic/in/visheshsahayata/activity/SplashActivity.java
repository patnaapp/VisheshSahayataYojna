package com.bih.nic.in.visheshsahayata.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.bih.nic.in.visheshsahayata.R;
import com.bih.nic.in.visheshsahayata.activity.file.FileUtils;
import com.bih.nic.in.visheshsahayata.database.DataBaseHelper;
import com.bih.nic.in.visheshsahayata.database.WebServiceHelper;
import com.bih.nic.in.visheshsahayata.entity.ServerDate;
import com.bih.nic.in.visheshsahayata.entity.Versioninfo;
import com.bih.nic.in.visheshsahayata.utility.CommonPref;
import com.bih.nic.in.visheshsahayata.utility.GlobalVariables;
import com.bih.nic.in.visheshsahayata.utility.MarshmallowPermission;
import com.bih.nic.in.visheshsahayata.utility.Utiilties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends Activity {

    Context context;
    MarshmallowPermission permission;
    long isDataDownloaded=-1;
    public static SharedPreferences prefs;
    @SuppressLint("NewApi")
    ActionBar actionBar;
    @SuppressLint("NewApi")
    DataBaseHelper localDBHelper;
    private static int SPLASH_TIME_OUT = 2000;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private static final int PERMISSION_ALL = 0;
    String version="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        localDBHelper = new DataBaseHelper(SplashActivity.this);
        FileUtils.createApplicationFolder();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        context=this;
        try {
            localDBHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            localDBHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {

        }
    }


    private void checkAppUseMode()
    {
            if (Utiilties.isOnline(SplashActivity.this)) {
                new CheckUpdate().execute();
            }  else {

                AlertDialog.Builder ab = new AlertDialog.Builder(
                        SplashActivity.this);
                ab.setTitle("Alert Dialog !!!");
                ab.setMessage(Html.fromHtml("<font color=#000000>इंटरनेट कनेक्शन अभी नहीं है... \n नेटवर्क कनेक्शन चालू करने के लिए हां बटन दबाएं .</font>"));
                ab.setPositiveButton("हां", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        GlobalVariables.isOffline = false;
                        Intent I = new Intent(
                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(I);
                    }
                });

                ab.show();

            }
    }

    private void start() {

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, PhotoViewer.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }


    private void showDailog(AlertDialog.Builder ab, final Versioninfo versioninfo) {

        if (versioninfo.isVerUpdated()) {

            if (versioninfo.getPriority() == 0) {

                start();
            } else if (versioninfo.getPriority() == 1) {

                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());

                ab.setPositiveButton(getResources().getString(R.string.update),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

								Intent myWebLink = new Intent(
										android.content.Intent.ACTION_VIEW);
								myWebLink.setData(Uri.parse(versioninfo
										.getAppUrl()));

								startActivity(myWebLink);

								dialog.dismiss();
								finish();


                                Intent launchIntent = getPackageManager()
                                        .getLaunchIntentForPackage(
                                                "com.android.vending");
                                ComponentName comp = new ComponentName(
                                        "com.android.vending",
                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package
                                // name
                                // and
                                // activity
                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri
                                        .parse("market://details?id="
                                                + getApplicationContext()
                                                .getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(
                                            Intent.ACTION_VIEW, Uri
                                            .parse(versioninfo
                                                    .getAppUrl())));
                                    finish();
                                }

                                dialog.dismiss();
                            }


                        });
                ab.setNegativeButton(getResources().getString(R.string.ignore),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();

                                start();
                            }

                        });

                ab.show();

            } else if (versioninfo.getPriority() == 2) {

                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());
                ab.setPositiveButton(getResources().getString(R.string.update),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

								Intent myWebLink = new Intent(
										android.content.Intent.ACTION_VIEW);
								myWebLink.setData(Uri.parse(versioninfo
										.getAppUrl()));

								startActivity(myWebLink);

								dialog.dismiss();

                                Intent launchIntent = getPackageManager()
                                        .getLaunchIntentForPackage(
                                                "com.android.vending");
                                ComponentName comp = new ComponentName(
                                        "com.android.vending",
                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package
                                // name
                                // and
                                // activity
                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri
                                        .parse("market://details?id="
                                                + getApplicationContext()
                                                .getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(
                                            Intent.ACTION_VIEW, Uri
                                            .parse(versioninfo
                                                    .getAppUrl())));
                                    finish();
                                }

                                dialog.dismiss();
                                // finish();
                            }
                        });
                ab.show();
            }
        } else {

            start();
        }

    }


    private class CheckUpdate extends AsyncTask<Void, Void, Versioninfo> {

        CheckUpdate() {

        }

        @Override
        protected void onPreExecute() {

        }

        @SuppressLint("MissingPermission")
        @Override
        protected Versioninfo doInBackground(Void... Params) {

            TelephonyManager tm = null;

            String imei = null;

//            permission=new MarshmallowPermission(SplashActivity.this,Manifest.permission.READ_PHONE_STATE);
//            if(permission.result==-1 || permission.result==0)
//            {
//
//            }

            try
            {
                tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if(tm!=null) imei = tm.getDeviceId();
            }catch(Exception e){}

            String version = null;
            try {
                version = getPackageManager().getPackageInfo(getPackageName(),
                        0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Versioninfo versioninfo = WebServiceHelper.CheckVersion(imei, version);

            return versioninfo;

        }

        @Override
        protected void onPostExecute(final Versioninfo versioninfo) {

            final AlertDialog.Builder ab = new AlertDialog.Builder(
                    SplashActivity.this);
            ab.setCancelable(false);
            if (versioninfo != null ) {
//                if (versioninfo.getAppVersion().equalsIgnoreCase(version)){
//
//                    //new ServerDateTime().execute();
//                    start();
//
//                }else{
//                    AlertDialog.Builder ab1 = new AlertDialog.Builder(SplashActivity.this);
//                    ab1.setIcon(R.drawable.alert);
//                    ab1.setTitle("अलर्ट");
//                    ab1.setCancelable(false);
//                    ab1.setMessage("कृपया अपडेटेड एप्प aapda.bih.nic.in से डाउनलोड करे|");
//                    ab1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int whichButton) {
//
//                            finish();
//                        }
//                    });
//
//                    ab1.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                    ab1.show();
//                }


                CommonPref.setCheckUpdate(getApplicationContext(), System.currentTimeMillis());

                if (versioninfo.getAdminMsg().trim().length() > 0 && !versioninfo.getAdminMsg().trim().equalsIgnoreCase("anyType{}")) {

                    ab.setTitle(versioninfo.getAdminTitle());
                    ab.setMessage(Html.fromHtml(versioninfo.getAdminMsg()));
                    ab.setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();

                                    showDailog(ab, versioninfo);

                                }
                            });
                    ab.show();
                } else {
                    showDailog(ab, versioninfo);
                }
            } else {
                AlertDialog.Builder ab1 = new AlertDialog.Builder(SplashActivity.this);
                ab1.setIcon(R.drawable.alert);
                ab1.setTitle("अलर्ट");
                ab1.setCancelable(false);
                ab1.setMessage("इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे");
                ab1.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();
                    }
                });

                ab1.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                try {
                    ab1.show();;
                }
                catch (WindowManager.BadTokenException e) {
                    //use a log message
                }

            }

        }
    }

//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//
//        permission = new MarshmallowPermission(this, Manifest.permission.READ_PHONE_STATE);
//        permission = new MarshmallowPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        permission = new MarshmallowPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//
//        checkAppUseMode();
//
//        if (permission.result == -1 || permission.result == 0) {
//            try {
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (permission.result == 1) {
//        } else {
//            finish();
//        }
//        super.onResume();
//    }

    @Override
    protected void onResume() {
        requestRequiredPermission();
        super.onResume();
    }

    private void requestRequiredPermission(){
        String[] PERMISSIONS = {
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                CAMERA,
                WRITE_EXTERNAL_STORAGE,
                READ_PHONE_STATE
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{
            checkAppUseMode();
        }
    }

    public  boolean hasPermissions(Context context, String... allPermissionNeeded)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context != null && allPermissionNeeded != null)
            for (String permission : allPermissionNeeded)
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions)
        {
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if(PermissionsMap.get(ACCESS_FINE_LOCATION) != null && PermissionsMap.get(CAMERA) != null&& PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != null){
            if((PermissionsMap.get(ACCESS_FINE_LOCATION) != 0)||PermissionsMap.get(ACCESS_COARSE_LOCATION) != 0){
                Toast.makeText(this, "Location permissions is required", Toast.LENGTH_SHORT).show();
                requestRequiredPermission();
            }else if(PermissionsMap.get(CAMERA) != 0){
                Toast.makeText(this, "Camera permissions is required", Toast.LENGTH_SHORT).show();
                requestRequiredPermission();
            }else if(PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != 0){
                Toast.makeText(this, "External Storage permissions is required", Toast.LENGTH_SHORT).show();
                requestRequiredPermission();
            }else if(PermissionsMap.get(READ_PHONE_STATE) != 0){
                Toast.makeText(this, "Read Phone State permissions is required", Toast.LENGTH_SHORT).show();
                requestRequiredPermission();
            }
            else
            {
                checkAppUseMode();
            }
        }else{
            finish();
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private class ServerDateTime extends AsyncTask<String, Void,ServerDate> {

        private final ProgressDialog dialog = new ProgressDialog(SplashActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(SplashActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected ServerDate doInBackground(String... param) {


            return WebServiceHelper.loadServerDate();

        }

        @Override
        protected void onPostExecute(ServerDate result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {

                Log.d("Resultgfg", "" + result);

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ServerDate", result.getServerDate() ).commit();




            }


        }

    }


}
