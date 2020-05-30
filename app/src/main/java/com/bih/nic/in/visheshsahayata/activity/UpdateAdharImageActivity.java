package com.bih.nic.in.visheshsahayata.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bih.nic.in.visheshsahayata.R;
import com.bih.nic.in.visheshsahayata.activity.Videofile.MediaControllernew;
import com.bih.nic.in.visheshsahayata.activity.file.FileUtils;
import com.bih.nic.in.visheshsahayata.database.WebServiceHelper;
import com.bih.nic.in.visheshsahayata.entity.BenificiaryDetail;
import com.bih.nic.in.visheshsahayata.utility.GlobalVariables;
import com.bih.nic.in.visheshsahayata.utility.Utiilties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateAdharImageActivity extends AppCompatActivity implements LocationListener {

    EditText et_aadhar_number,et_name,et_Mobile_Number,et_ifsc,et_account,et_confirm_account;
    LinearLayout ll_account;
    RelativeLayout rl_adhar_image,rel_selfie;
    ImageView img_pic1,img_pic2;
    TextView viewIMG1,viewIMG2;
    Button btn_reg_new;
    String destPath="";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    private final static int CAMERA_PIC = 99;


    BenificiaryDetail benificiaryDetail;

    Bitmap im1,im2;
    int ThumbnailSize =500;

    byte[] imageData1,imageData2;
    String str_aadhar_img,str_imagcap;

    CheckBox cb_account,cb_adhar,cb_selfie;

    Boolean isAccount = false;
    Boolean isAadhar = false;
    Boolean isSelfie = false;

    Boolean validIfsc = false;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager mlocManager = null;
    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;

    final String TAG = "GPS";
    static Location LastLocation = null;
    Button btn_reg_video_new;

    String latitude="",longitud="";
    String str_img="N";

    private File tempFile;
    private ProgressBar progressBar;
    private MediaController mediaController;
    String videoData="";
    byte[] videobytedata;
    ImageView img_firstPhoto,img_secondPhoto,img_videorecord,playbutton,pausebutton;
    VideoView view_Video;
    private Uri uri;
    private String pathToStoredVideo;
    private static final int VIDEO_CAPTURE = 300;
    private boolean isContinuously = false;
    TextView tv_indicator;
    LinearLayout ll_video,ll_main;
    String Str_aadhar="",IsUpdatedEligible="",ISPaymentStatus="",ISVideoUploaded="",ISVideoUploadedElegible="",IsUpdateEligibleAccount,_ReasonOfRejection_dst="",_ReasonOfRejection_pfms="";
    TextView tv_mssg;
    RelativeLayout rl_selfie_img,rl_account_checkbox,rl_aadhar_img_chekbox;
    String ServerDate="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_adhar_image);

        ServerDate = PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("ServerDate", "");

        et_aadhar_number = (EditText) findViewById(R.id.et_aadhar_number);
        et_name = (EditText) findViewById(R.id.et_name);
        et_Mobile_Number = (EditText) findViewById(R.id.et_Mobile_Number);

        IsUpdatedEligible = PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("IsUpdatedEligible", "");
        IsUpdateEligibleAccount = PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("IsUpdateEligibleAccount", "");
        ISPaymentStatus= PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("ISPaymentStatus", "");
        ISVideoUploaded= PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("ISVideoUploaded", "");
        ISVideoUploadedElegible= PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("ISVideoUploadedElegible", "");
        _ReasonOfRejection_dst= PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("ReasonOfRejection_Dst", "");
        _ReasonOfRejection_pfms= PreferenceManager.getDefaultSharedPreferences(UpdateAdharImageActivity.this).getString("ReasonOfRejection_PFMS", "");

        ll_account = (LinearLayout) findViewById(R.id.ll_account);
        rl_adhar_image = (RelativeLayout) findViewById(R.id.rl_adhar_image);
        rl_selfie_img = (RelativeLayout) findViewById(R.id.rl_selfie_img);
        rl_aadhar_img_chekbox = (RelativeLayout) findViewById(R.id.rl_aadhar_img_chekbox);
        rl_account_checkbox = (RelativeLayout) findViewById(R.id.rl_account_checkbox);
        rel_selfie = (RelativeLayout) findViewById(R.id.rel_selfie);

        btn_reg_video_new = (Button) findViewById(R.id.btn_reg_video_new);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //  lin_camera = (LinearLayout) findViewById(R.id.lin_camera);
        playbutton=(ImageView) findViewById(R.id.playbutton);
        pausebutton=(ImageView) findViewById(R.id.pausebutton);
        view_Video = (VideoView)findViewById(R.id.img_videorecord);
        view_Video.setZOrderOnTop(true);

        et_ifsc = (EditText) findViewById(R.id.et_ifsc);
        et_account = (EditText) findViewById(R.id.et_account);
        et_confirm_account = (EditText) findViewById(R.id.et_confirm_account);

        cb_account = (CheckBox)findViewById(R.id.cb_account);
        cb_adhar = (CheckBox)findViewById(R.id.cb_adhar);
        cb_selfie = (CheckBox)findViewById(R.id.cb_selfie);

        img_pic2 = (ImageView) findViewById(R.id.img_pic2);
        img_pic1 = (ImageView) findViewById(R.id.img_pic1);
        tv_indicator=(TextView) findViewById(R.id.tv_indicator);
        tv_mssg=(TextView) findViewById(R.id.tv_mssg);

        viewIMG1 = (TextView) findViewById(R.id.viewIMG1);
        viewIMG2 = (TextView)findViewById(R.id.viewIMG2);

        btn_reg_new  = (Button) findViewById(R.id.btn_reg_new);

        benificiaryDetail = (BenificiaryDetail) getIntent().getSerializableExtra("data");
        //benificiaryDetail = new BenificiaryDetail();

        et_aadhar_number.setText(benificiaryDetail.getAadhaarNo());
        Str_aadhar=benificiaryDetail.getAadhaarNo();
        et_name.setText(benificiaryDetail.getBenificiaryName());
        et_Mobile_Number.setText(benificiaryDetail.getMobileNo());
        et_ifsc.setText(benificiaryDetail.getIfsc());
        et_account.setText(benificiaryDetail.getAccountNo());
        et_confirm_account.setText(benificiaryDetail.getAccountNo());

        Log.e("name", benificiaryDetail.getBenificiaryName());

        et_aadhar_number.setEnabled(false);
        et_name.setEnabled(false);
        et_Mobile_Number.setEnabled(false);

        ll_account.setVisibility(View.GONE);
        rl_adhar_image.setVisibility(View.GONE);
        rel_selfie.setVisibility(View.GONE);

        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        ll_video.setVisibility(View.GONE);
        rl_aadhar_img_chekbox.setVisibility(View.GONE);
        rl_selfie_img.setVisibility(View.GONE);
        rl_account_checkbox.setVisibility(View.GONE);
        btn_reg_new.setVisibility(View.GONE);
        // ll_main.setVisibility(View.GONE);


        if(IsUpdatedEligible.equalsIgnoreCase("Y")||IsUpdateEligibleAccount.equalsIgnoreCase("Y")&&ISPaymentStatus.equalsIgnoreCase("N")&&ISVideoUploaded.equalsIgnoreCase("N")&&ISVideoUploadedElegible.equalsIgnoreCase("N"))
        {


            ll_video.setVisibility(View.GONE);
            if (IsUpdatedEligible.equalsIgnoreCase("Y")&&IsUpdateEligibleAccount.equalsIgnoreCase("N"))
            {
                showMessageDstReject();
                rl_aadhar_img_chekbox.setVisibility(View.VISIBLE);
                rl_selfie_img.setVisibility(View.VISIBLE);
                rl_account_checkbox.setVisibility(View.GONE);
                btn_reg_new.setVisibility(View.VISIBLE);
            }
            else if (IsUpdateEligibleAccount.equalsIgnoreCase("Y")&&IsUpdatedEligible.equalsIgnoreCase("N"))
            {
                showMessageforbankupdate();
                cb_account.setChecked(true);
                rl_aadhar_img_chekbox.setVisibility(View.GONE);
                rl_selfie_img.setVisibility(View.GONE);
                rl_account_checkbox.setVisibility(View.VISIBLE);
                ll_account.setVisibility(View.VISIBLE);
                btn_reg_new.setVisibility(View.VISIBLE);
            }
            else if (IsUpdateEligibleAccount.equalsIgnoreCase("Y")&&IsUpdatedEligible.equalsIgnoreCase("Y"))
            {
                showMessageforBoth();
                rl_aadhar_img_chekbox.setVisibility(View.VISIBLE);
                rl_selfie_img.setVisibility(View.VISIBLE);
                rl_account_checkbox.setVisibility(View.VISIBLE);
                btn_reg_new.setVisibility(View.VISIBLE);
            }
            //ll_main.setVisibility(View.VISIBLE);
        }
        else if(IsUpdatedEligible.equalsIgnoreCase("N")&&ISPaymentStatus.equalsIgnoreCase("Y")&&ISVideoUploaded.equalsIgnoreCase("N")&&ISVideoUploadedElegible.equalsIgnoreCase("Y")){
            ll_video.setVisibility(View.VISIBLE);
            ll_main.setVisibility(View.GONE);
        }



//        if(IsUpdatedEligible.equalsIgnoreCase("Y")){
//            ll_video.setVisibility(View.GONE);
//            ll_main.setVisibility(View.VISIBLE);
//        }else if(ISPaymentStatus.equalsIgnoreCase("Y")){
//            ll_video.setVisibility(View.VISIBLE);
//            ll_main.setVisibility(View.GONE);
//            //BlinkTextView(tv_mssg);
//        }



        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pausebutton.setVisibility(View.VISIBLE);
                    playbutton.setVisibility(View.GONE);
                    isContinuously = true;
                    view_Video.setMediaController(mediaController);
                    view_Video.setVideoURI(uri);
                    view_Video.requestFocus();
                    view_Video.start();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }}
        });


        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    pausebutton.setVisibility(View.GONE);
                    playbutton.setVisibility(View.VISIBLE);
                    view_Video.pause();
                }
                catch (Exception e){

                    e.printStackTrace();
                }}

        });

        view_Video.setOnTouchListener(new View.OnTouchListener() {
                                          @Override
                                          public boolean onTouch(View v, MotionEvent event) {
                                              Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                              intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                              intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 83886080L);// 10*1024*1024 = 10MB,20971520=20 MB8,38,86,080‬
                                              intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                              intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
                                              intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//                                              intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
//                                              intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
//                                              intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                              startActivityForResult(intent, VIDEO_CAPTURE);
                                              return false;
                                          }
                                      }
        );

        et_ifsc.addTextChangedListener(inputTextWatcher1);
        handleSubmitButtonVisibility();

        img_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(getApplicationContext(),CameraActivity.class);
                iCamera.putExtra("KEY_PIC", "1");
                startActivityForResult(iCamera, CAMERA_PIC);

            }
        });

        img_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(getApplicationContext(),CameraActivity.class);
                iCamera.putExtra("KEY_PIC", "2");
                startActivityForResult(iCamera, CAMERA_PIC);

            }
        });

        cb_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleAccountChecked(isChecked);
            }
        });

        cb_adhar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleAadharImageChecked(isChecked);
            }
        });

        cb_selfie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleSelfieChecked(isChecked);
            }
        });

//        btn_reg_video_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String isvalid = validateRecordBeforeSaving();
//
//                if (isvalid.equalsIgnoreCase("yes")) {
//                    new Update_Video().execute();
//                    if (Utiilties.isOnline(UpdateAdharImageActivity.this)) {
//                      //  new Update_Video().execute();
//
//                    }else{
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateAdharImageActivity.this);
//                        alertDialog.setTitle("अलर्ट डायलॉग !");
//                        alertDialog.setMessage("इंटरनेट कनेक्शन उपलब्ध नहीं है .. कृपया नेटवर्क कनेक्शन चालू करें?");
//                        alertDialog.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                                startActivity(I);
//                            }
//                        });
//                        alertDialog.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        AlertDialog alert = alertDialog.create();
//                        alert.show();
//                    }
//                    //updateChayanAamsabha();
//                }
//            }
//        });
        mediaController = new MediaController(this);
        mediaController.setAnchorView(view_Video);
        view_Video.setMediaController(mediaController);


        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isNetwork && !isGPS) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();
            //getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }else {

                }

            }

            getLocation();
        }
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private TextWatcher inputTextWatcher1 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_ifsc.getText().length() <= 12) {
                if (et_ifsc.getText().length() >0) {
                    checkForEnglish(et_ifsc);
                }
                try {
                    if (Utiilties.isIfscCodeValid(et_ifsc.getText().toString())) {
                        et_ifsc.setTextColor(Color.parseColor("#0B610B"));
                        validIfsc = true;
                    } else {
                        //showMessageDialogue("Invalid Aadhaar Number");
                        //Toast.makeText(getApplicationContext(),"Invalid Aadhaar Number",Toast.LENGTH_LONG).show();
                        et_ifsc.setTextColor(Color.parseColor("#ff0000"));
                        validIfsc = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                et_ifsc.setTextColor(Color.parseColor("#000000"));
                validIfsc = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void checkForEnglish(EditText etxt) {
        if (etxt.getText().length() > 0) {
            String s = etxt.getText().toString();
            if (isInputInEnglish(s)) {
                //OK
            } else {
                Toast.makeText(this, "कृपया अंग्रेजी में लिखे", Toast.LENGTH_SHORT).show();
                etxt.setText("");
            }
        }
    }
    public static boolean isInputInEnglish(String txtVal) {

        String regx = "^[A-Z0-9 ]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(txtVal);
        return matcher.find();
    }

    void handleAccountChecked(Boolean isChecked){
        if(isChecked){
            ll_account.setVisibility(View.VISIBLE);
        }else{
            ll_account.setVisibility(View.GONE);
        }

        handleSubmitButtonVisibility();
    }

    void handleAadharImageChecked(Boolean isChecked){
        if(isChecked){
            rl_adhar_image.setVisibility(View.VISIBLE);
        }else{
            rl_adhar_image.setVisibility(View.GONE);
        }

        handleSubmitButtonVisibility();
    }

    void handleSelfieChecked(Boolean isChecked){
        if(isChecked){
            rel_selfie.setVisibility(View.VISIBLE);
        }else{
            rel_selfie.setVisibility(View.GONE);
        }

        handleSubmitButtonVisibility();
    }

    public void handleSubmitButtonVisibility(){
        if(cb_account.isChecked() || cb_adhar.isChecked() || cb_selfie.isChecked()){
            btn_reg_new.setVisibility(View.VISIBLE);
        }else{
            btn_reg_new.setVisibility(View.INVISIBLE);
        }
    }

    public void onUpdateAadharimage(View view){

        if (Utiilties.isOnline(this)) {
            if (cb_account.isChecked()==true && cb_adhar.isChecked() == false && cb_selfie.isChecked() == false) {
                validateData();
            } else {
                if (latitude.equalsIgnoreCase("")) {
                    Toast.makeText(this, "आप अपना लोकेशन ऑन कीजिये उसके बाद लॉग इन कीजिये !", Toast.LENGTH_SHORT).show();
                    getLocation();
                } else {

                    Polygon polygon = Polygon.Builder()
                            .addVertex(new Point(24.163869f, 86.464733f))
                            .addVertex(new Point(23.790401f, 86.466274f))
                            .addVertex(new Point(23.794888f, 87.297577f))
                            .addVertex(new Point(24.167083f, 87.298434f)).build();  //Jamtara Location


                    Point point = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
                    boolean contains = polygon.contains(point);


                    Polygon polygon1 = Polygon.Builder()
                            .addVertex(new Point(27.64995f, 83.78421f))
                            .addVertex(new Point(26.57064f, 88.557203f))
                            .addVertex(new Point(25.866319f, 88.024204f))
                            .addVertex(new Point(25.499882f, 88.219319f))
                            .addVertex(new Point(24.281598f, 86.744054f))
                            .addVertex(new Point(24.252534f, 83.408781f))
                            .addVertex(new Point(25.185739f, 83.241732f))
                            .addVertex(new Point(25.888924f, 84.151631f))
                            .addVertex(new Point(26.186357f, 83.824455f)).build();  //Bihar Location Clockwise

                    Polygon polygon3 = Polygon.Builder()
                            .addVertex(new Point(27.64995f, 83.78421f))
                            .addVertex(new Point(26.186357f, 83.824455f))
                            .addVertex(new Point(25.888924f, 84.151631f))
                            .addVertex(new Point(25.185739f, 83.241732f))
                            .addVertex(new Point(24.252534f, 83.408781f))
                            .addVertex(new Point(24.281598f, 86.744054f))
                            .addVertex(new Point(25.499882f, 88.219319f))
                            .addVertex(new Point(25.866319f, 88.024204f))
                            .addVertex(new Point(26.57064f, 88.557203f)).build();  //Bihar Location AntiClockwise


                    Point point3 = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
                    boolean contains3 = polygon3.contains(point3);


                    Point point1 = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
                    boolean contains1 = polygon1.contains(point1);
                    Polygon polygon2 = Polygon.Builder()
                            .addVertex(new Point(30.551864f, 81761214f))
                            .addVertex(new Point(28.526323f, 85.640303f))
                            .addVertex(new Point(27.872436f, 88.230474f))
                            .addVertex(new Point(27.116858f, 88.011613f))
                            .addVertex(new Point(26.903611f, 88.214915f))
                            .addVertex(new Point(26.006473f, 88.126840f))
                            .addVertex(new Point(27.139097f, 83.294154f))
                            .addVertex(new Point(28.804404f, 80.005744f))
                            .addVertex(new Point(29.440910f, 80.219152f))
                            .addVertex(new Point(29.767055f, 80.877981f))
                            .addVertex(new Point(30.327259f, 80.991486f)).build();  //Nepal Location


                    Point point2 = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
                    boolean contains2 = polygon2.contains(point2);


                    if (contains == false && contains1 == false && contains2 == false&& contains3 == false) {
                    //if (contains == false && contains2 == false) {
                        //if (contains == false) {
                        validateData();
                    } else {
                        AlertDialog.Builder ab = new AlertDialog.Builder(this);
                        ab.setIcon(R.drawable.location);
                        ab.setTitle("अलर्ट !");
                        ab.setMessage("क्षमा कीजिये ! यह मोबाइल एप्प बिहार के लोग जो बिहार के बाहर कोरोना के वजह से फसे हुए है ये उनके सहायता के लिए है|");
                        ab.setPositiveButton("[ ओके ]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                        ab.show();
                    }

                }
            }
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("अलर्ट डायलॉग !");
            alertDialog.setMessage("इंटरनेट कनेक्शन उपलब्ध नहीं है .. कृपया नेटवर्क कनेक्शन चालू करें?");
            alertDialog.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(I);
                }
            });
            alertDialog.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    public void validateData(){
        boolean cancelRegistration = false;
        String isValied = "yes";
        View focusView = null;

        if(cb_account.isChecked()){
            if (TextUtils.isEmpty(et_ifsc.getText().toString())) {
                et_ifsc.setError("कृपया Ifsc कोड डाले |");
                focusView = et_ifsc;
                cancelRegistration = true;
            }

            if (TextUtils.isEmpty(et_account.getText().toString())) {
                et_account.setError("कृपया खाता संख्या डाले |");
                focusView = et_account;
                cancelRegistration = true;
            }else if (et_account.getText().length() < 6) {
                et_account.setError("कृपया सही खाता संख्या डाले |");
                focusView = et_account;
                cancelRegistration = true;
            }

            if (TextUtils.isEmpty(et_confirm_account.getText().toString())) {
                et_confirm_account.setError(getString(R.string.field_required));
                focusView = et_confirm_account;
                cancelRegistration = true;
            } else if (!(et_confirm_account.getText().toString().equals(et_account.getText().toString()))) {
                et_confirm_account.setError("खाता संख्या मैच नहीं किया");
                focusView = et_confirm_account;
                cancelRegistration = true;
            }
        }

        if(cb_adhar.isChecked() && str_aadhar_img.isEmpty()){

            Toast.makeText(this, "कृपया आधार फोटो डाले", Toast.LENGTH_SHORT).show();
            cancelRegistration = true;
        }

        if(cb_selfie.isChecked() && str_imagcap.isEmpty()){

            Toast.makeText(this, "कृपया सेल्फी फोटो डाले", Toast.LENGTH_SHORT).show();
            cancelRegistration = true;
        }


        if(!cancelRegistration){
            String aadharImg="", selfie = "";

            if(cb_account.isChecked()){
                benificiaryDetail.setIfsc(et_ifsc.getText().toString());
                benificiaryDetail.setAccountNo(et_account.getText().toString());
            }

            if(cb_adhar.isChecked()){
                aadharImg = str_aadhar_img;
            }

            if(cb_selfie.isChecked()){
                selfie = str_imagcap;
            }

            updateBenDetail(aadharImg, selfie);
        }
    }

    public void updateBenDetail(String straadharImg, String selfieImg){

        if(cb_account.isChecked())
        {
            new ValidateIfscTask(benificiaryDetail, straadharImg,selfieImg).execute();
        }
        else
        {
            new UpdateAadharImageTask(benificiaryDetail, straadharImg, selfieImg,latitude,longitud).execute();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case CAMERA_PIC:
                if (resultCode == RESULT_OK)
                {
                    byte[] imgData = data.getByteArrayExtra("CapturedImage");
                    latitude = data.getStringExtra("Lat");
                    longitud = data.getStringExtra("Lng");

                    //imageData.add(data);
                    //  if (isOpen.equals("2")) btnOk.setEnabled(true);

                    switch (data.getIntExtra("KEY_PIC", 0))
                    {

                        case 1:
                            im2 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                            img_pic2.setScaleType(ImageView.ScaleType.FIT_XY);
                            //img_pic2.setImageBitmap(Utiilties.GenerateThumbnail(im2,ThumbnailSize, ThumbnailSize));
                            img_pic2.setImageBitmap(im2);
                            viewIMG2.setVisibility(View.VISIBLE);
                            imageData2 = imgData;
                            str_aadhar_img = org.kobjects.base64.Base64.encode(imgData);
                            break;

                        case 2:
                            im1 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                            img_pic1.setScaleType(ImageView.ScaleType.FIT_XY);
                            //  img_pic1.setImageBitmap(Utiilties.GenerateThumbnail(im1,ThumbnailSize, ThumbnailSize));
                            img_pic1.setImageBitmap(im1);
                            viewIMG1.setVisibility(View.VISIBLE);
                            imageData1 = imgData;
                            str_imagcap = org.kobjects.base64.Base64.encode(imgData);
                            latitude = data.getStringExtra("Lat");
                            longitud = data.getStringExtra("Lng");
//                            if(getIntent().hasExtra("KeyId")) {
                            //}
                            str_img="Y";
                            break;

                    }

                    return;
                }
                break;

            case VIDEO_CAPTURE:

                if (requestCode == VIDEO_CAPTURE) {
                    if (resultCode == RESULT_OK) {
                        uri = data.getData();
                        pathToStoredVideo = getRealPathFromURIPath(uri, UpdateAdharImageActivity.this);

                        if (uri != null) {
                            Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);

                            try {
                                if (cursor != null && cursor.moveToFirst()) {

                                    String displayName = cursor.getString(
                                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                    // Log.i(TAG, "Display Name: " + displayName);
                                    //Toast.makeText(this, "display"+ displayName, Toast.LENGTH_SHORT).show();

                                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                                    String size = null;
                                    if (!cursor.isNull(sizeIndex)) {
                                        size = cursor.getString(sizeIndex);
                                    } else {
                                        size = "Unknown";
                                    }
                                    // Log.i(TAG, "Size: " + size);

                                    tempFile = FileUtils.saveTempFile(displayName, this, uri);
                                    //  Toast.makeText(this, "temp file"+tempFile, Toast.LENGTH_SHORT).show();
                                    new VideoCompressor().execute();
                                    // editText.setText(tempFile.getPath());

                                }
                            } finally {
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                        }

                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "Video recording cancelled.",  Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(this, "Failed to record video",  Toast.LENGTH_LONG).show();
                    }
                }
                return;
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case CAMERA_PIC:
//                if (resultCode == RESULT_OK) {
//                    byte[] imgData = data.getByteArrayExtra("CapturedImage");
//
//                    //imageData.add(imgData);
//                    switch (data.getIntExtra("KEY_PIC", 0)) {
//
//                        case 1:
//                            im2 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
//                            img_pic2.setScaleType(ImageView.ScaleType.FIT_XY);
//                            img_pic2.setImageBitmap(Utiilties.GenerateThumbnail(im2,
//                                    ThumbnailSize, ThumbnailSize));
//                            viewIMG2.setVisibility(View.VISIBLE);
//                            imageData2 = imgData;
//                            str_aadhar_img = org.kobjects.base64.Base64.encode(imgData);
//                            //img_pic1.setEnabled(true);
//                            //latitude = data.getStringExtra("Lat");
//                            //longitude = data.getStringExtra("Lng");
////                            if(getIntent().hasExtra("KeyId")) {
//                            //}
//                            //str_img="Y";
//                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
//                            break;
//                        case 2:
//                            im1 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
//                            img_pic1.setScaleType(ImageView.ScaleType.FIT_XY);
//                            img_pic1.setImageBitmap(Utiilties.GenerateThumbnail(im1,
//                                    ThumbnailSize, ThumbnailSize));
//                            viewIMG1.setVisibility(View.VISIBLE);
//                            imageData1 = imgData;
//                            str_imagcap = org.kobjects.base64.Base64.encode(imgData);
//                            latitude = data.getStringExtra("Lat");
//                            longitud = data.getStringExtra("Lng");
////                            if(getIntent().hasExtra("KeyId")) {
//                            //}
//                            str_img="Y";
//                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
//
//                            break;
//                    }
//
//
//                }
//
//        }
//
//    }

    public void onClick_ViewImg(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-सेल्फी फोटो-");


        Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView imgview = (ImageView) dialogView.findViewById(R.id.imgview);
        if (imageData1 != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imageData1, 0, imageData1.length);


            imgview.setImageBitmap(bmp);

        }

        dialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });


        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void onClick_ViewImg1(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-आधार फोटो-");


        Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView imgview = (ImageView) dialogView.findViewById(R.id.imgview);
        if (imageData2 != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imageData2, 0, imageData2.length);

            imgview.setImageBitmap(bmp);
        }

        dialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });


        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private class ValidateIfscTask extends AsyncTask<BenificiaryDetail, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(UpdateAdharImageActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(UpdateAdharImageActivity.this).create();

        BenificiaryDetail info;
        String aadharImgStr;
        String selfieImgStr;

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Validating Ifsc Code...");
            this.dialog.show();
        }

        public ValidateIfscTask(BenificiaryDetail info, String aadharImgStr, String selfieImgStr) {
            this.info = info;
            this.aadharImgStr = aadharImgStr;
            this.selfieImgStr = selfieImgStr;
        }

        @Override
        protected String doInBackground(BenificiaryDetail... param) {

            return WebServiceHelper.ValidateIfscCode(info);
        }

        @Override

        protected void onPostExecute(String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result != null) {

                if (result.contains("1")) {
                    //Toast.makeText(HomeActivity.this,"valid", Toast.LENGTH_SHORT).show();

//                    Intent i = new Intent(VerifyPhoneActivity.this, RegisterMemberActivity.class);
//                    i.putExtra("data",benfiList);
//                    startActivity(i);
                    if (cb_account.isChecked()==true && cb_adhar.isChecked() == false && cb_selfie.isChecked() == false) {
                        new UpdateAadharImageTask_New(benificiaryDetail).execute();
                    }
                    else if (cb_account.isChecked()==true && cb_adhar.isChecked() == true && cb_selfie.isChecked() == true) {
                        new UpdateAadharImageTask(benificiaryDetail, str_aadhar_img, selfieImgStr,latitude,longitud).execute();
                    }
//                    new UpdateAadharImageTask(benificiaryDetail, str_aadhar_img, selfieImgStr,latitude,longitud).execute();

//                    if(isDeclerationConfirmed){
//                        new RegistrationTask(benfiList).execute();
//                    }else{
//                        rl_declaration.setVisibility(View.VISIBLE);
//                    }
                    //new RegistrationTask(benfiList).execute();

                } else {

                    alertDialog.setTitle("Failed");
                    alertDialog.setMessage("अमान्य ifsc कोड, कृपया सही कोड दर्ज करें");
                    alertDialog.show();

                    //unknown return type
                    Toast.makeText(UpdateAdharImageActivity.this,"अमान्य ifsc कोड, कृपया सही कोड दर्ज करें", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(UpdateAdharImageActivity.this,
                        "अपने इंटरनेट कनेक्शन की जाँच करें और पुन: प्रयास करें : ", Toast.LENGTH_SHORT)
                        .show();
            }

        }


    }

    private class UpdateAadharImageTask extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(UpdateAdharImageActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(UpdateAdharImageActivity.this).create();

        BenificiaryDetail benDetail;
        String aadharImgStr;
        String selfieImgStr;
        String Latitude;
        String Longitude;

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("डेटा अपलोड हो रहा है...");
            this.dialog.show();
        }

        public UpdateAadharImageTask(BenificiaryDetail benDetail, String aadharImgStr, String selfieImgStr,String latitude,String longitude) {
            this.benDetail = benDetail;
            this.aadharImgStr = aadharImgStr;
            this.selfieImgStr = selfieImgStr;
            this.Latitude = latitude;
            this.Longitude = longitude;
        }

        @Override
        protected String doInBackground(String... param) {

            return WebServiceHelper.UpdateAadharImage(benDetail, aadharImgStr, selfieImgStr,Latitude,Longitude);
        }

        @Override

        protected void onPostExecute(String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result != null) {

                if (result.contains("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAdharImageActivity.this);
                    builder.setTitle("Successfull");
                    builder.setCancelable(false);
                    // Ask the final question
                    builder.setMessage("डेटा सफलतापूर्वक अपलोड हो गया");

                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getBaseContext(), PhotoViewer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    //Toast.makeText(UpdateAdharImageActivity.this,"आधार फ़ोटो सफलतापूर्वक अपलोड की गई", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(VerifyPhoneActivity.this, "Error Occured while inserting in Local Database ", Toast.LENGTH_SHORT).show();

                }
//                else if (result.contains("2")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this,
//                            "आपने पहले ही इस आधार नंबर से पंजीकृत कर लिया है कृपया दूसरा आधार नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
//                            .show();
//                } else if (result.trim().equalsIgnoreCase("3")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this,"आपने पहले ही इस मोबाइल नंबर से पंजीकृत कर लिया है कृपया दूसरा मोबाइल नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
//                            .show();
//
//                }else if (result.contains("4")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this, "आधार नंबर दुसरे अकाउंट से पंजीकृत है", Toast.LENGTH_SHORT).show();
//                }
                else {
                    //unknown return type
                    Toast.makeText(UpdateAdharImageActivity.this,"आधार फ़ोटो सफलतापूर्वक अपलोड असफल रहा है", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(UpdateAdharImageActivity.this,
                        "अपने इंटरनेट कनेक्शन की जाँच करें और पुन: प्रयास करें : ", Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }


    private class UpdateAadharImageTask_New extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(UpdateAdharImageActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(UpdateAdharImageActivity.this).create();

        BenificiaryDetail benDetail;
        String aadharImgStr;
        String selfieImgStr;
        String Latitude;
        String Longitude;

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("डेटा अपलोड हो रहा है...");
            this.dialog.show();
        }

        public UpdateAadharImageTask_New(BenificiaryDetail benDetail) {
            this.benDetail = benDetail;

        }

        @Override
        protected String doInBackground(String... param) {

            return WebServiceHelper.UpdateBankDetails(benDetail);
        }

        @Override

        protected void onPostExecute(String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result != null) {

                if (result.contains("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAdharImageActivity.this);
                    builder.setTitle("Successfull");
                    builder.setCancelable(false);
                    // Ask the final question
                    builder.setMessage("डेटा सफलतापूर्वक अपलोड हो गया");

                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getBaseContext(), PhotoViewer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    //Toast.makeText(UpdateAdharImageActivity.this,"आधार फ़ोटो सफलतापूर्वक अपलोड की गई", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(VerifyPhoneActivity.this, "Error Occured while inserting in Local Database ", Toast.LENGTH_SHORT).show();

                }
//                else if (result.contains("2")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this,
//                            "आपने पहले ही इस आधार नंबर से पंजीकृत कर लिया है कृपया दूसरा आधार नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
//                            .show();
//                } else if (result.trim().equalsIgnoreCase("3")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this,"आपने पहले ही इस मोबाइल नंबर से पंजीकृत कर लिया है कृपया दूसरा मोबाइल नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
//                            .show();
//
//                }else if (result.contains("4")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this, "आधार नंबर दुसरे अकाउंट से पंजीकृत है", Toast.LENGTH_SHORT).show();
//                }
                else {
                    //unknown return type
                    Toast.makeText(UpdateAdharImageActivity.this,"बैंक डिटेल्स सफलतापूर्वक अपलोड नही हुआ है", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(UpdateAdharImageActivity.this,
                        "अपने इंटरनेट कनेक्शन की जाँच करें और पुन: प्रयास करें : ", Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }


    @Override
    public void onLocationChanged(Location location) {
        updateUI(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public static class Polygon {
        private final Polygon.BoundingBox _boundingBox;
        private final List<Line> _sides;

        private Polygon(List<Line> sides, Polygon.BoundingBox boundingBox)
        {
            _sides = sides;
            _boundingBox = boundingBox;
        }


        public static Polygon.Builder Builder()
        {
            return new Polygon.Builder();
        }


        public static class Builder
        {
            private List<Point> _vertexes = new ArrayList<Point>();
            private List<Line> _sides = new ArrayList<Line>();
            private Polygon.BoundingBox _boundingBox = null;

            private boolean _firstPoint = true;
            private boolean _isClosed = false;


            public Polygon.Builder addVertex(Point point)
            {
                if (_isClosed)
                {
                    // each hole we start with the new array of vertex points
                    _vertexes = new ArrayList<Point>();
                    _isClosed = false;
                }

                updateBoundingBox(point);
                _vertexes.add(point);

                // add line (edge) to the polygon
                if (_vertexes.size() > 1)
                {
                    Line Line = new Line(_vertexes.get(_vertexes.size() - 2), point);
                    _sides.add(Line);
                }

                return this;
            }


            public Polygon.Builder close()
            {
                validate();

                // add last Line
                _sides.add(new Line(_vertexes.get(_vertexes.size() - 1), _vertexes.get(0)));
                _isClosed = true;

                return this;
            }


            public Polygon build()
            {
                validate();

                // in case you forgot to close
                if (!_isClosed)
                {
                    // add last Line
                    _sides.add(new Line(_vertexes.get(_vertexes.size() - 1), _vertexes.get(0)));
                }

                Polygon polygon = new Polygon(_sides, _boundingBox);
                return polygon;
            }


            private void updateBoundingBox(Point point)
            {
                if (_firstPoint)
                {
                    _boundingBox = new Polygon.BoundingBox();
                    _boundingBox.xMax = point.x;
                    _boundingBox.xMin = point.x;
                    _boundingBox.yMax = point.y;
                    _boundingBox.yMin = point.y;

                    _firstPoint = false;
                }
                else
                {
                    // set bounding box
                    if (point.x > _boundingBox.xMax)
                    {
                        _boundingBox.xMax = point.x;
                    }
                    else if (point.x < _boundingBox.xMin)
                    {
                        _boundingBox.xMin = point.x;
                    }
                    if (point.y > _boundingBox.yMax)
                    {
                        _boundingBox.yMax = point.y;
                    }
                    else if (point.y < _boundingBox.yMin)
                    {
                        _boundingBox.yMin = point.y;
                    }
                }
            }

            private void validate()
            {
                if (_vertexes.size() < 3)
                {
                    throw new RuntimeException("Polygon must have at least 3 points");
                }
            }
        }

        public boolean contains(Point point)
        {
            if (inBoundingBox(point))
            {
                Line ray = createRay(point);
                int intersection = 0;
                for (Line side : _sides)
                {
                    if (intersect(ray, side))
                    {
                        // System.out.println("intersection++");
                        intersection++;
                    }
                }

                /*
                 * If the number of intersections is odd, then the point is inside the polygon
                 */
                if (intersection % 2 == 1)
                {
                    return true;
                }
            }
            return false;
        }

        public List<Line> getSides()
        {
            return _sides;
        }


        private boolean intersect(Line ray, Line side)
        {
            Point intersectPoint = null;

            // if both vectors aren't from the kind of x=1 lines then go into
            if (!ray.isVertical() && !side.isVertical())
            {
                // check if both vectors are parallel. If they are parallel then no intersection point will exist
                if (ray.getA() - side.getA() == 0)
                {
                    return false;
                }

                float x = ((side.getB() - ray.getB()) / (ray.getA() - side.getA())); // x = (b2-b1)/(a1-a2)
                float y = side.getA() * x + side.getB(); // y = a2*x+b2
                intersectPoint = new Point(x, y);
            }

            else if (ray.isVertical() && !side.isVertical())
            {
                float x = ray.getStart().x;
                float y = side.getA() * x + side.getB();
                intersectPoint = new Point(x, y);
            }

            else if (!ray.isVertical() && side.isVertical())
            {
                float x = side.getStart().x;
                float y = ray.getA() * x + ray.getB();
                intersectPoint = new Point(x, y);
            }

            else
            {
                return false;
            }

            // System.out.println("Ray: " + ray.toString() + " ,Side: " + side);
            // System.out.println("Intersect point: " + intersectPoint.toString());

            if (side.isInside(intersectPoint) && ray.isInside(intersectPoint))
            {
                return true;
            }

            return false;
        }


        private Line createRay(Point point)
        {
            // create outside point
            float epsilon = (_boundingBox.xMax - _boundingBox.xMin) / 100f;
            Point outsidePoint = new Point(_boundingBox.xMin - epsilon, _boundingBox.yMin);
            Line vector = new Line(outsidePoint, point);
            return vector;
        }


        private boolean inBoundingBox(Point point)
        {
            if (point.x < _boundingBox.xMin || point.x > _boundingBox.xMax || point.y < _boundingBox.yMin || point.y > _boundingBox.yMax)
            {
                return false;
            }
            return true;
        }

        private static class BoundingBox
        {
            public float xMax = Float.NEGATIVE_INFINITY;
            public float xMin = Float.NEGATIVE_INFINITY;
            public float yMax = Float.NEGATIVE_INFINITY;
            public float yMin = Float.NEGATIVE_INFINITY;
        }
    }


    public static class Line {
        private final Point _start;
        private final Point _end;
        private float _a = Float.NaN;
        private float _b = Float.NaN;
        private boolean _vertical = false;

        public Line(Point start, Point end)
        {
            _start = start;
            _end = end;

            if (_end.x - _start.x != 0)
            {
                _a = ((_end.y - _start.y) / (_end.x - _start.x));
                _b = _start.y - _a * _start.x;
            }

            else
            {
                _vertical = true;
            }
        }


        public boolean isInside(Point point)
        {
            float maxX = _start.x > _end.x ? _start.x : _end.x;
            float minX = _start.x < _end.x ? _start.x : _end.x;
            float maxY = _start.y > _end.y ? _start.y : _end.y;
            float minY = _start.y < _end.y ? _start.y : _end.y;

            if ((point.x >= minX && point.x <= maxX) && (point.y >= minY && point.y <= maxY))
            {
                return true;
            }
            return false;
        }


        public boolean isVertical()
        {
            return _vertical;
        }

        public float getA()
        {
            return _a;
        }

        public float getB()
        {
            return _b;
        }


        public Point getStart()
        {
            return _start;
        }

        public Point getEnd()
        {
            return _end;
        }

        @Override
        public String toString()
        {
            return String.format("%s-%s", _start.toString(), _end.toString());
        }
    }

    public static class Point {
        public Point(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        public float x;
        public float y;

        @Override
        public String toString()
        {
            return String.format("(%.2f,%.2f)", x, y);
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        alertDialog.show();
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        //loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, (float) 0.01, mlistener);
                        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, (float) 0.01, mlistener);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private final LocationListener mlistener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            //A new location update is received. Do something useful with it.
            //Update the UI with
            //the location update.
            if (Utiilties.isGPSEnabled(UpdateAdharImageActivity.this)) {
                LastLocation = location;
                GlobalVariables.glocation = location;
                updateUI(location);
                //updateUILocation(GlobalVariables.glocation);
                //  if (getIntent().getStringExtra("KEY_PIC").equals("2")) {
//                if (location.getLatitude() > 0.0) {
//                    //long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
//                    if (location.getAccuracy() > 0 && location.getAccuracy() < 150) {
//
//                        takePhoto.setText(" Take Photo ");
//                        progress_finding_location.setVisibility(View.GONE);
//                        takePhoto.setEnabled(true);
//                    } else {
//
//                        takePhoto.setText(" Wait for GPS to Stable ");
//                        progress_finding_location.setVisibility(View.VISIBLE);
//                        takePhoto.setEnabled(false);
//
//                    }
//
//                }

//                } else {
//                    GlobalVariables.glocation.setLatitude(0.0);
//                    GlobalVariables.glocation.setLongitude(0.0);
//                    GlobalVariables.glocation.setTime(0);
//                    updateUILocation(GlobalVariables.glocation);
//                    takePhoto.setText(" Take Photo ");
//                    progress_finding_location.setVisibility(View.GONE);
//                    takePhoto.setEnabled(true);
//                }
            } else {
                updateUI(location);
            }
            //Toast.makeText(getApplicationContext(), latitude + " WORKS offline " + longitude + "", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };

    private void updateUI(Location loc) {
        boolean isMockLocation = loc.isFromMockProvider();
        Log.d("TAGLOCATION",String.valueOf(isMockLocation));
        if(!isMockLocation) {
            //if(loc.getAccuracy() < 250) {
            if (loc.getLatitude() > 0.0) {
                latitude = Double.toString(loc.getLatitude());
                longitud = Double.toString(loc.getLongitude());


//            btn_save.setEnabled(true);
//            pbar.setVisibility(View.GONE);
//            btn_save.setText("Make Attendence");
                if(latitude.equalsIgnoreCase("")){
                    btn_reg_new.setEnabled(false);
                    btn_reg_new.setText("कृपया GPS के लिए प्रतिक्षा कीजिये !");
                }else{
                    btn_reg_new.setEnabled(true);
                    //pbar.setVisibility(View.GONE);
                    btn_reg_new.setText("सत्यापित करे");
                }
            }

        }
        else {
            AlertDialog.Builder ab = new AlertDialog.Builder(UpdateAdharImageActivity.this);
            ab.setIcon(R.drawable.alert);
            ab.setTitle("अलर्ट");
            ab.setCancelable(false);
            ab.setMessage("आपका लोकेशन गलत है, कृपया फिर से प्रयास कीजिये");
            ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();

                }
            });

            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            ab.show();

        }
    }
    class VideoCompressor extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressBar1.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            // tv_indicator.setText("Video Compressing Wait...");
            tv_indicator.setText("कृपया थोड़ा इंतजार कीजिये,वीडियो कंप्रेस हो रहा है ...");
            tv_indicator.setVisibility(View.VISIBLE);

        }



        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaControllernew.getInstance().convertVideo(tempFile.getPath(),UpdateAdharImageActivity.this);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            progressBar.setVisibility(View.GONE);
            tv_indicator.setVisibility(View.GONE);
            view_Video.setClickable(false);
            if(compressed){
                String timejavaclass= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("TIMESTAMP", "");
                destPath = Environment.getExternalStorageDirectory()+  File.separator+ Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME
                        + Config.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR+"VIDEO_" +timejavaclass+ ".mp4";

                File file=new File(destPath);
                //Uri myUri = Uri.parse(destPath);
                try {
                    view_Video.setVideoURI(Uri.parse(destPath));
                    view_Video.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                InputStream inputStream = null;
                try
                {
                    inputStream = getContentResolver().openInputStream(Uri.fromFile(file));
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                int len = 0;
                try
                {
                    while ((len = inputStream.read(buffer)) != -1)
                    {
                        byteBuffer.write(buffer, 0, len);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                videobytedata=byteBuffer.toByteArray();

                videoData = android.util.Base64.encodeToString(byteBuffer.toByteArray(), android.util.Base64.DEFAULT);
                Log.d("VideoData**>  " , videoData);
                pausebutton.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }
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
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {

        }catch (Exception e){
            e.printStackTrace();
        }

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(UpdateAdharImageActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }

    }
    private String validateRecordBeforeSaving()
    {
        String isvalid = "yes";

        if(destPath.equals(""))
        {
            Toast.makeText(getApplicationContext(),"कृपया 1 मिनट का वीडियो बनायें  ",Toast.LENGTH_LONG).show();
            return "no";
        }

        return isvalid;
    }

    private class Update_Video extends AsyncTask<BenificiaryDetail, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(UpdateAdharImageActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UpdateAdharImageActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("विडियो अपलोड हो रहा है !कृपया इंतज़ार कीजिये !...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(BenificiaryDetail... param) {

            return WebServiceHelper.Update_Video(Str_aadhar,videoData);

        }

        @Override

        protected void onPostExecute(String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {

                if (result.contains("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAdharImageActivity.this);
                    builder.setTitle("सफल");
                    builder.setCancelable(false);
                    // Ask the final question
                    builder.setMessage("विडियो सफलतापूर्वक अपलोड हो गया");

                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getBaseContext(), PhotoViewer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    //Toast.makeText(UpdateAdharImageActivity.this,"आधार फ़ोटो सफलतापूर्वक अपलोड की गई", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(VerifyPhoneActivity.this, "Error Occured while inserting in Local Database ", Toast.LENGTH_SHORT).show();

                }
//                else if (result.contains("2")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this,
//                            "आपने पहले ही इस आधार नंबर से पंजीकृत कर लिया है कृपया दूसरा आधार नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
//                            .show();
//                } else if (result.trim().equalsIgnoreCase("3")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this,"आपने पहले ही इस मोबाइल नंबर से पंजीकृत कर लिया है कृपया दूसरा मोबाइल नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
//                            .show();
//
//                }else if (result.contains("4")) {
//
//                    Toast.makeText(VerifyPhoneActivity.this, "आधार नंबर दुसरे अकाउंट से पंजीकृत है", Toast.LENGTH_SHORT).show();
//                }
                else {
                    //unknown return type
                    Toast.makeText(UpdateAdharImageActivity.this,"विडियो सफलतापूर्वक अपलोड असफल रहा है", Toast.LENGTH_SHORT).show();
                }
            } else
            {

                Toast.makeText(UpdateAdharImageActivity.this,
                        "अपने इंटरनेट कनेक्शन की जाँच करें और पुन: प्रयास करें : ", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

//    private class Update_Video extends AsyncTask<String, Void, ArrayList<BenificiaryDetail>> {
//
//        private final ProgressDialog dialog = new ProgressDialog(UpdateAdharImageActivity.this);
//
//        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UpdateAdharImageActivity.this).create();
//
//        @Override
//        protected void onPreExecute() {
//
//            this.dialog.setCanceledOnTouchOutside(false);
//            this.dialog.setMessage("विडियो अपलोड हो रहा है !कृपया इंतज़ार कीजिये !");
//            this.dialog.show();
//        }
//
//        @Override
//        protected ArrayList<BenificiaryDetail> doInBackground(String... param) {
//
//
//            return WebServiceHelper.Update_Video(Str_aadhar,videoData);
//
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<BenificiaryDetail> result) {
//            if (this.dialog.isShowing()) {
//                this.dialog.dismiss();
//            }
//
//            if (result != null) {
//
//                if (result.contains("1")) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAdharImageActivity.this);
//                    builder.setTitle("सफल");
//                    builder.setCancelable(false);
//                    // Ask the final question
//                    builder.setMessage("विडियो सफलतापूर्वक अपलोड हो गया");
//
//                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//
//                            Intent intent = new Intent(getBaseContext(), PhotoViewer.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//
//                    //Toast.makeText(UpdateAdharImageActivity.this,"आधार फ़ोटो सफलतापूर्वक अपलोड की गई", Toast.LENGTH_SHORT).show();
////                    Toast.makeText(VerifyPhoneActivity.this, "Error Occured while inserting in Local Database ", Toast.LENGTH_SHORT).show();
//
//                }
////                else if (result.contains("2")) {
////
////                    Toast.makeText(VerifyPhoneActivity.this,
////                            "आपने पहले ही इस आधार नंबर से पंजीकृत कर लिया है कृपया दूसरा आधार नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
////                            .show();
////                } else if (result.trim().equalsIgnoreCase("3")) {
////
////                    Toast.makeText(VerifyPhoneActivity.this,"आपने पहले ही इस मोबाइल नंबर से पंजीकृत कर लिया है कृपया दूसरा मोबाइल नंबर प्रयोग करें !", Toast.LENGTH_SHORT)
////                            .show();
////
////                }else if (result.contains("4")) {
////
////                    Toast.makeText(VerifyPhoneActivity.this, "आधार नंबर दुसरे अकाउंट से पंजीकृत है", Toast.LENGTH_SHORT).show();
////                }
//                else {
//                    //unknown return type
//                    Toast.makeText(UpdateAdharImageActivity.this,"विडियो सफलतापूर्वक अपलोड असफल रहा है", Toast.LENGTH_SHORT).show();
//                }
//            } else
//                {
//
//                Toast.makeText(UpdateAdharImageActivity.this,
//                        "अपने इंटरनेट कनेक्शन की जाँच करें और पुन: प्रयास करें : ", Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//
//        }
//
//    }


    public void Video_Upload(View view){
        String isvalid = validateRecordBeforeSaving();

        if (isvalid.equalsIgnoreCase("yes")) {
            new Update_Video().execute();
            if (Utiilties.isOnline(UpdateAdharImageActivity.this)) {
                //  new Update_Video().execute();

            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateAdharImageActivity.this);
                alertDialog.setTitle("अलर्ट डायलॉग !");
                alertDialog.setMessage("इंटरनेट कनेक्शन उपलब्ध नहीं है .. कृपया नेटवर्क कनेक्शन चालू करें?");
                alertDialog.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(I);
                    }
                });
                alertDialog.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
            //updateChayanAamsabha();
        }
    }

    public  void BlinkTextView(TextView txt)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(10000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(40);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt.startAnimation(anim);
    }



    public  void showMessageDstReject()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(UpdateAdharImageActivity.this);
        ab.setIcon(R.drawable.usermanual);
        ab.setCancelable(false);
        ab.setTitle(Html.fromHtml("<font color='#990000'>अस्वीकृति का कारण:-</font>"));
        // ab.setMessage("1).If the checkbox is checked and beneficiary name is written in GREEN colour it means attendance is above 75%. \n\n 2).If the checkbox is unchecked and beneficiary name is written in RED colour it means attendance is below 75%.  \n\n 3). If the checkbox is unchecked and beneficiary name is written in BLACK colour it means attendance is unmarked till now.");
        //ab.setMessage(Html.fromHtml("<font color='#0000FF'>(I).यदि आप आधार का फोटो अपडेट करना कहते ह तो आधार का फोटो ले कर सुरक्षित करें पे क्लिक करे |<BR><BR>(II).यदि आप अपना सेल्फी  अपडेट करना कहते है तो अपना सेल्फी  ले कर सुरक्षित करें पे क्लिक करे|<BR><BR></font>"));
        ab.setMessage(_ReasonOfRejection_dst);
        ab.setNegativeButton("[ ओके ]",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog,int whichButton)
            {

                dialog.dismiss();

            }
        });
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        ab.show();


    }


    public  void showMessageforbankupdate()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(UpdateAdharImageActivity.this);
        ab.setIcon(R.drawable.usermanual);
        ab.setCancelable(false);
        ab.setTitle(Html.fromHtml("<font color='#990000'>अस्वीकृति का कारण:-</font>"));
        // ab.setMessage("1).If the checkbox is checked and beneficiary name is written in GREEN colour it means attendance is above 75%. \n\n 2).If the checkbox is unchecked and beneficiary name is written in RED colour it means attendance is below 75%.  \n\n 3). If the checkbox is unchecked and beneficiary name is written in BLACK colour it means attendance is unmarked till now.");
     //   ab.setMessage(Html.fromHtml("<font color='#0000FF'>(I).अपना बैंक का विवरण अपडेट करे (बैंक )|<BR><BR>"));
        ab.setMessage(_ReasonOfRejection_pfms);
        ab.setNegativeButton("[ ओके ]",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog,int whichButton)
            {

                dialog.dismiss();

            }
        });
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        ab.show();


    }

    public  void showMessageforBoth()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(UpdateAdharImageActivity.this);
        ab.setIcon(R.drawable.usermanual);
        ab.setCancelable(false);
        ab.setTitle(Html.fromHtml("<font color='#990000'>अस्वीकृति का कारण:-</font>"));
        // ab.setMessage("1).If the checkbox is checked and beneficiary name is written in GREEN colour it means attendance is above 75%. \n\n 2).If the checkbox is unchecked and beneficiary name is written in RED colour it means attendance is below 75%.  \n\n 3). If the checkbox is unchecked and beneficiary name is written in BLACK colour it means attendance is unmarked till now.");
        //   ab.setMessage(Html.fromHtml("<font color='#0000FF'>(I).अपना बैंक का विवरण अपडेट करे (बैंक )|<BR><BR>"));
        ab.setMessage(_ReasonOfRejection_pfms+" and "+_ReasonOfRejection_dst);
        ab.setNegativeButton("[ ओके ]",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog,int whichButton)
            {

                dialog.dismiss();

            }
        });
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        ab.show();


    }

}
