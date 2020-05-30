package com.bih.nic.in.visheshsahayata.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bih.nic.in.visheshsahayata.R;
import com.bih.nic.in.visheshsahayata.database.DataBaseHelper;
import com.bih.nic.in.visheshsahayata.database.WebServiceHelper;
import com.bih.nic.in.visheshsahayata.entity.BenificiaryDetail;
import com.bih.nic.in.visheshsahayata.entity.ServerDate;
import com.bih.nic.in.visheshsahayata.entity.UserDetails;
import com.bih.nic.in.visheshsahayata.utility.GlobalVariables;
import com.bih.nic.in.visheshsahayata.utility.MarshmallowPermission;
import com.bih.nic.in.visheshsahayata.utility.Utiilties;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    ConnectivityManager cm;
    String version;
    TelephonyManager tm;
    private static String imei;
    DataBaseHelper localDBHelper;
    Context context;
    String uid = "";
    String pass = "";
    AutoCompleteTextView userName;

    EditText et_aadhar_number,et_Mobile_Number,et_Password;
    String[] param;
    Button btn_Sign_In;
    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    String aadhar="",password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        localDBHelper = new DataBaseHelper(LoginActivity.this);

        //userName = (AutoCompleteTextView) findViewById(R.id.et_User_Id);
        et_aadhar_number = (EditText) findViewById(R.id.et_aadhar_number);
        //et_Mobile_Number = (EditText) findViewById(R.id.et_Mobile_Number);
        et_Password = (EditText) findViewById(R.id.et_Password);

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }


    @SuppressLint("MissingPermission")
    private void getIMEI() {
        context = this;
        //Database Opening
        localDBHelper = new DataBaseHelper(LoginActivity.this);
        localDBHelper = new DataBaseHelper(this);

        MarshmallowPermission permission = new MarshmallowPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permission.result == -1 || permission.result == 0) {
            try {
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if (tm != null) imei = tm.getDeviceId();
                String mPhoneNumber = tm.getLine1Number();
            } catch (Exception e) {
            }
        } else if (permission.result == 1) {
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) imei = tm.getDeviceId();
            String mPhoneNumber = tm.getLine1Number();
                    /* Intent i=new Intent(this,LoginActivity.class);
                     startActivity(i);
	            	 finish();*/
        }
        //AutoCompleteTextView userName = (AutoCompleteTextView) findViewById(R.id.et_User_Id);
        //userName.setText(CommonPref.getUserDetails(getApplicationContext()).get_UserName());
        try {

            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView tv = (TextView) findViewById(R.id.txtVersion);
            tv.setText(getResources().getString(R.string.app_version) + version +"");

        } catch (PackageManager.NameNotFoundException e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getIMEI();

    }

    public void onRegistration(View view){
        Intent intent = new Intent(this, VarifyActivity.class);
        startActivity(intent);
    }

    public void Login(View view) {
        if (!GlobalVariables.isOffline && !Utiilties.isOnline(LoginActivity.this)) {

            AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
            ab.setMessage(Html.fromHtml(
                    "<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection </font>"));
            ab.setPositiveButton("Turn On Network Connection", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(I);
                }
            });

            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            ab.show();

        }else{
            initLogin();
        }

    }

    public void initLogin() {

//        String[] param = new String[3];
//        param[0] = et_aadhar_number.getText().toString();
//        param[1] = et_Password.getText().toString();
//        param[2] = et_Mobile_Number.getText().toString();

        aadhar = et_aadhar_number.getText().toString();
        //String mobile = et_Mobile_Number.getText().toString();
        password = et_Password.getText().toString();

        if (aadhar.isEmpty()) {
            et_aadhar_number.setError("This field is required");
        }
//        else if (mobile.isEmpty()) {
//            et_Mobile_Number.setError("This field is required");
//        }
        else if (password.isEmpty()) {
            et_Password.setError("This field is required");
        }else {
//            Intent i = new Intent(this, HomeActivity.class);
//            startActivity(i);
            new ServerDateTime().execute();
            //new LoginTask(aadhar,password).execute(param);
        }


//        edt_username.setError(null);
//        edt_password.setError(null);
//
//        String email = edt_username.getText().toString();
//        String password = edt_password.getText().toString();
//
//        boolean cancelLogin = false;
//        View focusView = null;
//
//        if (TextUtils.isEmpty(password)) {
//            edt_password.setError(getString(R.string.field_required));
//            focusView = edt_password;
//            cancelLogin = true;
//        }
//
//        if (TextUtils.isEmpty(email)) {
//            edt_username.setError(getString(R.string.field_required));
//            focusView = edt_username;
//            cancelLogin = true;
//        }
//        if (cancelLogin) {
//            // error in login
//            focusView.requestFocus();
//        } else {
//
//            if (!GlobalVariables.isOffline && !Utiilties.isOnline(LoginActivity.this)) {
//
//                AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
//                ab.setMessage(Html.fromHtml(
//                        "<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection </font>"));
//                ab.setPositiveButton("Turn On Network Connection", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                        startActivity(I);
//                    }
//                });
//
//                ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                ab.show();
//
//            } else {
//
//
//            param = new String[3];
//            param[0] = email;
//            param[1] = password;
//            new LoginTask().execute(param);
//
//
//        }
//
//
//        }
    }

    private class LoginTask extends AsyncTask<String, Void, BenificiaryDetail> {

        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();

        String aadhar, mobile, password;

        public LoginTask(String aadhar, String password) {
            this.aadhar = aadhar;
            this.mobile = mobile;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("प्रमाणित हो रहा है...");
            this.dialog.show();
        }

        @Override
        protected BenificiaryDetail doInBackground(String... param) {


//            if (!Utiilties.isOnline(LoginActivity.this))
//                return OfflineLogin(param[0],param[1]);
//            else
            return WebServiceHelper.Login(aadhar,password);
            //return WebServiceHelper.Authenticate(aadhar,password);

            //return WebServiceHelper.Authenticate( param[0], param[1]);

        }

        @Override
        protected void onPostExecute(final BenificiaryDetail result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

           /* final EditText userPass = (EditText) findViewById(R.id.input_Password);
            final AutoCompleteTextView userName = (AutoCompleteTextView) findViewById(R.id.email);*/

            if (!(result != null) || !result.isAuthenticated()) {

                alertDialog.setTitle("Failed");
                alertDialog.setMessage("आपका आधार नंबर और OTP गलत है");
                alertDialog.show();

            } /*else if(!(result.get_IMEI().trim().equalsIgnoreCase(imei)))
            {

                Toast.makeText(getApplicationContext(),
                        "Your Device is not Registered", Toast.LENGTH_SHORT).show();

            }*/ else {

                try {
                    //UsercodeToken = result.getGeneratedUserId() + "@" + result.getToken();
//                    GlobalVariables.LoggedUser = result;
//                    GlobalVariables.UserId = result.getBenId();
//                    CommonPref.setUserDetails1(getApplicationContext(), GlobalVariables.LoggedUser);
//                    SQLiteDatabase db = localDBHelper.getReadableDatabase();
//                    long c = localDBHelper.insertBenDetails(result);
                    // if (c > 0) {
//                        insertinShared(result);
//                        GlobalVariables.isLogged = true;
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("IsUpdatedEligible", result.getIsUpdatedEligible() ).commit();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ISPaymentStatus", result.getISPaymentStatus() ).commit();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ISVideoUploaded", result.getISVideoUploaded() ).commit();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ISVideoUploadedElegible", result.getISVideoUploadedElegible() ).commit();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("IsUpdateEligibleAccount", result.getIsUpdateEligibleAccount() ).commit();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ReasonOfRejection_Dst", result.getReasonOfRejectionDst() ).commit();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ReasonOfRejection_PFMS", result.getReasonOfRejectionPfm() ).commit();

                    if(result.getIsUpdatedEligible().equalsIgnoreCase("Y")||result.getIsUpdateEligibleAccount().equalsIgnoreCase("Y")&&result.getISPaymentStatus().equalsIgnoreCase("N")&&result.getISVideoUploaded().equalsIgnoreCase("N")&&result.getISVideoUploadedElegible().equalsIgnoreCase("N")) {
                        Intent iUserHome = new Intent(getApplicationContext(), UpdateAdharImageActivity.class);
                        iUserHome.putExtra("data", result);
                        iUserHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(iUserHome);
                        finish();
                    }

                    if(result.getIsUpdatedEligible().equalsIgnoreCase("N")&&result.getISPaymentStatus().equalsIgnoreCase("N")&&result.getISVideoUploaded().equalsIgnoreCase("N")&&result.getISVideoUploadedElegible().equalsIgnoreCase("N")&&result.getIsUpdateEligibleAccount().equalsIgnoreCase("N")) {
                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                        ab.setCancelable(false);
                        ab.setMessage(Html.fromHtml(
                                "<font color=#000000>आपका पंजीकरण हो चुका है तथा पैसा भेजने की प्रक्रिया हेतु सत्यापन जारी है ! कृपया इंतजार करे ! </font>"));
                        ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                        ab.show();
                    }

                    if(result.getIsUpdatedEligible().equalsIgnoreCase("N")&&result.getISPaymentStatus().equalsIgnoreCase("Y")&&result.getISVideoUploaded().equalsIgnoreCase("N")&&result.getISVideoUploadedElegible().equalsIgnoreCase("N")&&result.getIsUpdateEligibleAccount().equalsIgnoreCase("N")) {
                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                        ab.setCancelable(false);
                        ab.setMessage(Html.fromHtml(
                                "<font color=#000000>आपके बैंक खाता में आपदा विभाग,बिहार सरकार द्वारा रु 1,000 की राशि भेजी जा रही है जो आपके दिए खाते में पहुंचेगी | कृपया आपके बैंक से  आने वाले SMS का इंतजार करें | </font>"));
                        ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                        ab.show();
                    }

                    if(result.getIsUpdatedEligible().equalsIgnoreCase("N")&&result.getISPaymentStatus().equalsIgnoreCase("Y")&&result.getISVideoUploaded().equalsIgnoreCase("N")&&result.getISVideoUploadedElegible().equalsIgnoreCase("Y")&&result.getIsUpdateEligibleAccount().equalsIgnoreCase("N")) {
                        Intent iUserHome = new Intent(getApplicationContext(), UpdateAdharImageActivity.class);
                        iUserHome.putExtra("data", result);
                        iUserHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(iUserHome);
                        finish();
                    }

                    if(result.getIsUpdatedEligible().equalsIgnoreCase("N")&&result.getISPaymentStatus().equalsIgnoreCase("Y")&&result.getISVideoUploaded().equalsIgnoreCase("Y")&&result.getISVideoUploadedElegible().equalsIgnoreCase("Y")&&result.getIsUpdateEligibleAccount().equalsIgnoreCase("N")) {
                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                        ab.setCancelable(false);
                        ab.setMessage(Html.fromHtml(
                                "<font color=#000000>आपने धन्यवाद् का विडियो अपलोड कर दिया है ! </font>"));
                        ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                        ab.show();
                    }


//
//                    if(result.getIsUpdatedEligible().equalsIgnoreCase("Y")) {
//                        Intent iUserHome = new Intent(getApplicationContext(), UpdateAdharImageActivity.class);
//                        iUserHome.putExtra("data", result);
//                        iUserHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(iUserHome);
//                        finish();
//                    }else if (result.getIsUpdatedEligible().equalsIgnoreCase("N")&& (result.getISPaymentStatus().equalsIgnoreCase("N"))){
//
//                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
//                        ab.setMessage(Html.fromHtml(
//                                "<font color=#000000>आपकी भुगतान की प्रक्रिया अंडर प्रोसेस है!कृपया प्रतिक्षा करे ! </font>"));
//                        ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        });
//                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                        ab.show();
//
//                    }else if (result.getIsUpdatedEligible().equalsIgnoreCase("N")&& (result.getISPaymentStatus().equalsIgnoreCase("Y"))&& (result.getISVideoUploaded().equalsIgnoreCase("N"))){
//                        Intent iUserHome = new Intent(getApplicationContext(), UpdateAdharImageActivity.class);
//                        iUserHome.putExtra("data", result);
//                        iUserHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(iUserHome);
//                        finish();
//                    }else if (result.getIsUpdatedEligible().equalsIgnoreCase("N")&& (result.getISPaymentStatus().equalsIgnoreCase("Y"))&& (result.getISVideoUploaded().equalsIgnoreCase("Y"))){
//                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
//                        ab.setMessage(Html.fromHtml(
//                                "<font color=#000000>आपने धन्यवाद् का विडियो अपलोड कर दिया है !आपका सहायता राशि का भुगतान हो चूका है ! </font>"));
//                        ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        });
//                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                        ab.show();
//                    }
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Error Occured while inserting in Local Database ", Toast.LENGTH_SHORT).show();
//                    }

                } catch (Exception ex) {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    protected UserDetails OfflineLogin(String User_ID, String Pwd) {

        DataBaseHelper placeData = new DataBaseHelper(LoginActivity.this);
        SQLiteDatabase db = placeData.getReadableDatabase();

        String[] params = new String[]{User_ID, Pwd};
        //Cursor cur = db.rawQuery("Select [UserID],[UserPassword],[UserName],[UserRole],[UserRoleDesc],[DistrictCode],[DistrictName],[StateCode],[StateName],[IsLock],[IMEI] from UserDetail where UserID=? AND UserPassword=?", params);
        //CREATE TABLE "UserDetail" ( `UserID` TEXT NOT NULL, `UserName` TEXT, `UserRole` TEXT, `StateCode` TEXT, `StateName` TEXT, `DistrictCode` TEXT, `DistrictName` TEXT,
        // `IsLock` TEXT, `UserRoleDesc` TEXT, `UserPassword` TEXT, `IMEI` TEXT, PRIMARY KEY(`UserID`) )
        Cursor cur = db.rawQuery("SELECT * FROM UserDetail where UserID=? AND UserPassword=?", params);

        UserDetails userInfo = new UserDetails();

        if (cur.moveToNext()) {
            userInfo.set_UserID(cur.getString(cur.getColumnIndex("UserID")));
            userInfo.set_UserPassword(cur.getString(cur.getColumnIndex("UserPassword")));
            userInfo.set_UserName(cur.getString(cur.getColumnIndex("UserName")));
            userInfo.set_UserRole(cur.getString(cur.getColumnIndex("UserRole")));
            userInfo.set_UserRoleDesc(cur.getString(cur.getColumnIndex("UserRoleDesc")));
            userInfo.set_DistrictCode(cur.getString(cur.getColumnIndex("DistrictCode")));
            userInfo.set_DistrictName(cur.getString(cur.getColumnIndex("DistrictName")));
            userInfo.set_StateCode(cur.getString(cur.getColumnIndex("StateCode")));
            userInfo.set_StateName(cur.getString(cur.getColumnIndex("StateName")));
            userInfo.set_IsLock(cur.getString(cur.getColumnIndex("IsLock")));
            userInfo.set_IMEI(cur.getString(cur.getColumnIndex("IMEI")));
            //userInfo.isAuthenticated(true);

        }

        else {

            //userInfo.isAuthenticated(false);
        }
        cur.close();
        db.close();
        return userInfo;

    }

    public void insertinShared(BenificiaryDetail result){

        String Userid=result.getBenId().trim();
        //String Userrole=result.getUserRole();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("BenId", Userid ).commit();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Aadhar", result.getAadhaarNo() ).commit();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Mobile", result.getMobileNo() ).commit();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("password", et_Password.getText().toString() ).commit();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("username", true ).commit();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("password", true).commit();
        //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USER_ROLE", Userrole ).commit();

    }


    private class ServerDateTime extends AsyncTask<String, Void, ServerDate> {

        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LoginActivity.this).create();

        @Override
        protected void onPreExecute() {

//            this.dialog.setCanceledOnTouchOutside(false);
//            this.dialog.setMessage("Loading...");
//            this.dialog.show();
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


                new LoginTask(aadhar,password).execute(param);

            }


        }

    }

}
