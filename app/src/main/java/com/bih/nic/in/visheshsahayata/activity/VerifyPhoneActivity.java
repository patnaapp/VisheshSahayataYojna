package com.bih.nic.in.visheshsahayata.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bih.nic.in.visheshsahayata.R;
import com.bih.nic.in.visheshsahayata.database.DataBaseHelper;
import com.bih.nic.in.visheshsahayata.database.WebServiceHelper;
import com.bih.nic.in.visheshsahayata.entity.BenificiaryDetail;
import com.bih.nic.in.visheshsahayata.entity.State;
import com.bih.nic.in.visheshsahayata.utility.GlobalVariables;
import com.bih.nic.in.visheshsahayata.utility.Utiilties;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyPhoneActivity extends AppCompatActivity {

    BenificiaryDetail benfiList;

    DataBaseHelper localDBHelper;
    EditText et_aadhar_number,et_Mobile_Number,et_Password,et_Confirm_Password,et_user_name;

    Boolean isDeclerationConfirmed = false;
    String reg_OTP="";
    TextView viewmobile;
    String version="";
    EditText et_OTP;
    Button btn_confirm;
    ArrayList<State> StateList = new ArrayList<State>();
    ArrayAdapter<String> stateadapter;
    Bitmap im1, im2,im3,im4;
    byte[] imageData1,imageData2,image_front_aadhar,img_back_aadhar;
    String str_img="N",str_imagcap="";
    Button btn_reg, btn_cancel, buttonConfirm_OTP, buttonResend_OTP,btn_reg_new;
    EditText et_verify_otp_Number;
    RelativeLayout rl_declaration;
    EditText et_ben_location;
    int ThumbnailSize =500;
    MaterialBetterSpinner sp_state;
    String showmobile="";
    TextView tv_auth_mob;
    CheckBox cb_confirm;
    String StateCode="",StateName="";
    ArrayList<String> stateNameArray;
    String PoiType_Name ="",latitude="",longitude="";
    RelativeLayout rel_selfie;
    android.support.v7.app.AlertDialog.Builder alert1;
    private final static int CAMERA_PIC = 99;
    ImageView img_pic1,img_pic2,img_aadhar_front,img_aadhar_back;
    String Str_et_ben_location,str_aadhar_img="",str_front_aadhar_img="",str_back_aadhar_img="";
    Boolean validIfsc = false;
    TextView tv_verify_otp;
    LinearLayout tv_otp;
    EditText et_ifsc,et_account_number,et_confirm_account_number;
    TextView viewIMG1,viewIMG2,tv_photo,tv_resend_otp;
    String ServerDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        localDBHelper = new DataBaseHelper(this);
        alert1 = new android.support.v7.app.AlertDialog.Builder(this);

        ServerDate = PreferenceManager.getDefaultSharedPreferences(VerifyPhoneActivity.this).getString("ServerDate", "");
        et_aadhar_number = (EditText)findViewById(R.id.et_aadhar_number);
        tv_auth_mob = (TextView) findViewById(R.id.tv_auth_mob);
        tv_verify_otp = (TextView) findViewById(R.id.tv_verify_otp);
        tv_resend_otp = (TextView) findViewById(R.id.tv_resend_otp);
        et_user_name = (EditText)findViewById(R.id.et_user_name);
        et_Mobile_Number = (EditText)findViewById(R.id.et_Mobile_Number);
        et_Password = (EditText)findViewById(R.id.et_Password);
        et_verify_otp_Number = (EditText)findViewById(R.id.et_verify_otp_Number);
        et_ben_location = (EditText)findViewById(R.id.et_ben_location);
        et_Confirm_Password = (EditText)findViewById(R.id.et_Confirm_Password);
        sp_state = (MaterialBetterSpinner)findViewById(R.id.sp_state);
        rel_selfie = (RelativeLayout) findViewById(R.id.rel_selfie);
        img_pic1 = (ImageView) findViewById(R.id.img_pic1);
        img_pic2 = (ImageView) findViewById(R.id.img_pic2);
        img_aadhar_front = (ImageView) findViewById(R.id.img_aadhar_front);
        img_aadhar_back = (ImageView) findViewById(R.id.img_aadhar_back);
        viewIMG1 = (TextView) findViewById(R.id.viewIMG1);
        viewIMG2 = (TextView) findViewById(R.id.viewIMG2);
        tv_photo = (TextView) findViewById(R.id.tv_photo);
        btn_reg_new = (Button) findViewById(R.id.btn_reg_new);
        tv_otp = (LinearLayout) findViewById(R.id.tv_otp);
        tv_otp.setVisibility(View.GONE);
        img_pic1.setEnabled(false);

        et_ifsc = (EditText) findViewById(R.id.et_ifsc);
        et_ifsc.addTextChangedListener(inputTextWatcher1);
        et_ifsc.addTextChangedListener(inputTextWatcher2);
        et_ben_location.addTextChangedListener(inputTextWatcher5);
        et_account_number = (EditText) findViewById(R.id.et_account_number);
        et_confirm_account_number = (EditText) findViewById(R.id.et_confirm_account_number);

        //BlinkTextView(tv_auth_mob);

        rl_declaration = (RelativeLayout)findViewById(R.id.rl_declaration);
        cb_confirm = (CheckBox)findViewById(R.id.cb_confirm);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_reg_new.setVisibility(View.GONE);
        rl_declaration.setVisibility(View.GONE);
        loadstatespinnerdata();
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tv_auth_mob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_Mobile_Number.getText().toString().length()==10){
                    if (Utiilties.isOnline(VerifyPhoneActivity.this)) {
                        registration_Mob();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VerifyPhoneActivity.this);
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
                }else{
                    et_Mobile_Number.setError("कृपया मोबाइल नंबर सही डाले|");
                }
            }
        });

        sp_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("arg2",""+position);
                if (position >= 0) {

                    State district = StateList.get(position);
                    StateCode = district.getStateCode();
                    StateName = district.getStateName();


                } else {
                    StateCode = "";
                    StateName = "";

                }
            }
        });

        getIntentData();

        handleDeclarationConfirmButton();

        cb_confirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDeclerationConfirmed = isChecked;
                handleDeclarationConfirmButton();
            }
        });
//
//        img_aadhar_front.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent iCamera = new Intent(getApplicationContext(),CameraActivity.class);
//                iCamera.putExtra("KEY_PIC", "1");
//                startActivityForResult(iCamera, CAMERA_PIC);
//
//            }
//        });
//        img_aadhar_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent iCamera = new Intent(getApplicationContext(),CameraActivity.class);
//                iCamera.putExtra("KEY_PIC", "2");
//                startActivityForResult(iCamera, CAMERA_PIC);
//
//            }
//        });

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
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeclerationConfirmed) {
                    new RegistrationTask(benfiList).execute();
                    rl_declaration.setVisibility(View.GONE);
                } else {
                    rl_declaration.setVisibility(View.VISIBLE);
                    Toast.makeText(VerifyPhoneActivity.this, "कृपया Declaration/ प्रख्यापन की पुष्टि करें!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        btn_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isDeclerationConfirmed) {
//                    Polygon polygon = Polygon.Builder()
//                    .addVertex(new Point(24.163869f, 86.464733f))
//                    .addVertex(new Point(24.167083f, 87.298434f))
//                    .addVertex(new Point(23.790401f, 86.466274f))
//                    .addVertex(new Point(23.794888f, 87.297577f)).build();
//                    Point point = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitude)));
//                    boolean contains = polygon.contains(point);
//
//
//                    Polygon polygon1 = Polygon.Builder()
//                            .addVertex(new Point(25.629809f, 85.102320f))
//                            .addVertex(new Point(25.629602f, 85.103149f))
//                            .addVertex(new Point(25.627993f, 85.102464f))
//                            .addVertex(new Point(25.628842f, 85.101864f)).build();
//                    Point point1 = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitude)));
//                    boolean contains1 = polygon1.contains(point1);
//
//                    if (contains == false && contains1 == false ) {
//                        new RegistrationTask(benfiList).execute();
//                        rl_declaration.setVisibility(View.GONE);
//                    }else{
//                        AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
//                        ab.setIcon(R.drawable.location);
//                        ab.setTitle("अलर्ट !");
//                        ab.setMessage("आप इस सहायता का लाभ लेने के योग्य नहीं है");
//                        ab.setPositiveButton("[ ओके ]", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                        ab.show();
//                    }
//                } else {
//                    rl_declaration.setVisibility(View.VISIBLE);
//                    Toast.makeText(VerifyPhoneActivity.this, "कृपया Declaration/ प्रख्यापन की पुष्टि करें!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    void handleDeclarationConfirmButton(){
        if(isDeclerationConfirmed){
            btn_confirm.setVisibility(View.VISIBLE);
        }else{
            btn_confirm.setVisibility(View.GONE);
        }
    }

    public void getIntentData(){
        benfiList = (BenificiaryDetail) getIntent().getSerializableExtra("data");
        et_aadhar_number.setText(benfiList.getAadhaarNo());
        et_user_name.setText(benfiList.getBenificiaryName());
        et_aadhar_number.setEnabled(false);
        et_user_name.setEnabled(false);
    }
    private TextWatcher inputTextWatcher1 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_ifsc.getText().length() ==11) {
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
//            if (!validAadhaar && et_father_aadhar_number_new.getText().toString().length() == 12) {
////                CommonMethods.showErrorDialog(getResources().getString(R.string.invalid_value),
////                        getResources().getString(R.string.check_aadhaar_no));
//            }
        }
    };

    public void onCompleteRegistration(View view){
        registration();
//        Intent i = new Intent(this, LoginActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
    }

    public void registration() {
        boolean cancelRegistration = false;
        String isValied = "yes";
        View focusView = null;
        Str_et_ben_location=et_ben_location.getText().toString();

//        if (TextUtils.isEmpty(StateCode)) {
//            sp_state.setError("कृपया आप अभी किस राज्य में है का चयन करे |");
//            focusView = sp_state;
//            cancelRegistration = true;
//        }

        if (TextUtils.isEmpty(Str_et_ben_location)) {
            et_ben_location.setError("आप किस जगह पे है जगह डाले |");
            focusView = et_ben_location;
            cancelRegistration = true;
        }

        if (TextUtils.isEmpty(et_Mobile_Number.getText().toString())) {
            et_Mobile_Number.setError("कृपया मोबाइल नंबर डाले |");
            focusView = et_Mobile_Number;
            cancelRegistration = true;
        } else if (et_Mobile_Number.getText().toString().length() != 10) {
            et_Mobile_Number.setError("मोबाइल नंबर सही नहीं है |");
            focusView = et_Mobile_Number;
            cancelRegistration = true;
        }
        if (TextUtils.isEmpty(et_ifsc.getText().toString())) {
            et_ifsc.setError("कृपया आई०एफ०एस०सी० [IFSC] डाले |");
            focusView = et_ifsc;
            cancelRegistration = true;
        }
        if (TextUtils.isEmpty(et_account_number.getText().toString())) {
            et_account_number.setError("कृपया बैंक खाता संख्या डाले |");
            focusView = et_account_number;
            cancelRegistration = true;
        }else if (et_account_number.getText().toString().length() < 6) {
            et_account_number.setError("कृपया बैंक खाता संख्या सही डाले |");
            focusView = et_account_number;
            cancelRegistration = true;
        }

        if (TextUtils.isEmpty(et_confirm_account_number.getText().toString())) {
            et_confirm_account_number.setError(getString(R.string.field_required));
            focusView = et_confirm_account_number;
            cancelRegistration = true;
        } else if (!(et_account_number.getText().toString().equals(et_confirm_account_number.getText().toString()))) {
            et_confirm_account_number.setError("बैंक खाता संख्या मैच नहीं किया");
            focusView = et_confirm_account_number;
            cancelRegistration = true;
        }

//        if (TextUtils.isEmpty(et_Password.getText().toString())) {
//            et_Password.setError(getString(R.string.field_required));
//            focusView = et_Password;
//            cancelRegistration = true;
//        } else if (et_Password.getText().toString().length() < 4) {
//            et_Password.setError("अवैध पासवर्ड");
//            focusView = et_Password;
//            cancelRegistration = true;
//        }
//
//        if (TextUtils.isEmpty(et_Confirm_Password.getText().toString())) {
//            et_Confirm_Password.setError(getString(R.string.field_required));
//            focusView = et_Confirm_Password;
//            cancelRegistration = true;
//        } else if (!(et_Password.getText().toString().equals(et_Confirm_Password.getText().toString()))) {
//            et_Confirm_Password.setError("पासवर्ड मैच नहीं किया");
//            focusView = et_Confirm_Password;
//            cancelRegistration = true;
//        }

        if (cancelRegistration) {
            // error in login
            focusView.requestFocus();
        } else {
            benfiList.setDistCode(benfiList.getDistCode());
            benfiList.setProjectCode(benfiList.getProjectCode());
            benfiList.setPanchayatCode(benfiList.getPanchayatCode());
            benfiList.setOther_Pan_Name(benfiList.getOther_Pan_Name());
            benfiList.setBenificiaryName(benfiList.getBenificiaryName());
            benfiList.setDate_Of_Birth(benfiList.getDate_Of_Birth());
            benfiList.setGenderCode(benfiList.getGenderCode());
            benfiList.setAadhaarNo(benfiList.getAadhaarNo());
            //benfiList.setHusbandName(benfiList.getHusbandName());
            benfiList.setWifeName(benfiList.getWifeName());
            benfiList.setStateCode(StateCode);
            benfiList.setBen_Jagah(et_ben_location.getText().toString());
            benfiList.setMobileNo(et_Mobile_Number.getText().toString());
            benfiList.setIfsc(et_ifsc.getText().toString());
            benfiList.setAccountNo(et_account_number.getText().toString());
            benfiList.setPhoto1(str_imagcap);
            benfiList.setAadhar_front_Photo(str_front_aadhar_img);
            benfiList.setAadhar_back_Photo(str_back_aadhar_img);

            benfiList.setAadhar_Photo(str_aadhar_img);
            benfiList.setLatitude(latitude);
            benfiList.setLongitude(longitude);
//            benfiList.setPassword(et_Password.getText().toString());
//            benfiList.setPassword(et_Password.getText().toString());
//            benfiList.setPassword(et_Password.getText().toString());
            //userDetails = new UserDetails();

            if (!GlobalVariables.isOffline && !Utiilties.isOnline(this)) {

                AlertDialog.Builder ab = new AlertDialog.Builder(this);
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
                if(str_img.equalsIgnoreCase("Y")) {
                    rl_declaration.setVisibility(View.VISIBLE);
                    // new ValidateIfscTask(benfiList).execute();
                }else{
                    Toast.makeText(VerifyPhoneActivity.this,"कृपया अपना सेल्फी ले !",Toast.LENGTH_SHORT).show();
                }
//                if(isDeclerationConfirmed){
//
//                    new RegistrationTask(benfiList).execute();
//                }else{
//                    rl_declaration.setVisibility(View.VISIBLE);
//                }

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_PIC:
                if (resultCode == RESULT_OK) {
                    byte[] imgData = data.getByteArrayExtra("CapturedImage");

                    //imageData.add(imgData);
                    switch (data.getIntExtra("KEY_PIC", 0)) {

//                        case 1:
//                            im3 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
//                            img_aadhar_front.setScaleType(ImageView.ScaleType.FIT_XY);
//                            img_aadhar_front.setImageBitmap(Utiilties.GenerateThumbnail(im3,
//                                    ThumbnailSize, ThumbnailSize));
//                            //viewIMG1.setVisibility(View.GONE);
//                            image_front_aadhar = imgData;
//                            str_front_aadhar_img = org.kobjects.base64.Base64.encode(imgData);
//                            img_aadhar_back.setEnabled(true);
//                            //latitude = data.getStringExtra("Lat");
//                            //longitude = data.getStringExtra("Lng");
////                            if(getIntent().hasExtra("KeyId")) {
//                            //}
//                            //str_img="Y";
//                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
//                            break;
//                        case 2:
//                            im4 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
//                            img_aadhar_back.setScaleType(ImageView.ScaleType.FIT_XY);
//                            img_aadhar_back.setImageBitmap(Utiilties.GenerateThumbnail(im4,
//                                    ThumbnailSize, ThumbnailSize));
//                            //viewIMG1.setVisibility(View.GONE);
//                            img_back_aadhar = imgData;
//                            str_back_aadhar_img = org.kobjects.base64.Base64.encode(imgData);
//                            img_pic2.setEnabled(true);
//                            //latitude = data.getStringExtra("Lat");
//                            //longitude = data.getStringExtra("Lng");
////                            if(getIntent().hasExtra("KeyId")) {
//                            //}
//                            //str_img="Y";
//                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
//                            break;

                        case 1:
                            im2 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                            img_pic2.setScaleType(ImageView.ScaleType.FIT_XY);
                            // img_pic2.setImageBitmap(Utiilties.GenerateThumbnail(im2,ThumbnailSize, ThumbnailSize));
                            img_pic2.setImageBitmap(im2);
                            viewIMG2.setVisibility(View.VISIBLE);
                            tv_photo.setVisibility(View.VISIBLE);
                            imageData2 = imgData;
                            str_aadhar_img = org.kobjects.base64.Base64.encode(imgData);
                            img_pic1.setEnabled(true);
                            //latitude = data.getStringExtra("Lat");
                            //longitude = data.getStringExtra("Lng");
//                            if(getIntent().hasExtra("KeyId")) {
                            //}
                            //str_img="Y";
                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
                            break;
                        case 2:
                            im1 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                            img_pic1.setScaleType(ImageView.ScaleType.FIT_XY);
                            // img_pic1.setImageBitmap(Utiilties.GenerateThumbnail(im1,ThumbnailSize, ThumbnailSize));
                            img_pic1.setImageBitmap(im1);
                            viewIMG1.setVisibility(View.VISIBLE);
                            imageData1 = imgData;
                            str_imagcap = org.kobjects.base64.Base64.encode(imgData);
                            latitude = data.getStringExtra("Lat");
                            longitude = data.getStringExtra("Lng");
//                            if(getIntent().hasExtra("KeyId")) {
                            //}
                            str_img="Y";
                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
                            break;


                    }


                }

        }

    }
    private TextWatcher inputTextWatcher2 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_ifsc.getText().length() >0) {
                checkForEnglish(et_ifsc);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
    private TextWatcher inputTextWatcher5 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_ben_location.getText().length() >0) {
                checkForEnglish(et_ben_location);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
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

        String regx = "^[A-Z0-9]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txtVal);
        return matcher.find();
    }


//    public void onConfirmDecleration(View view){
//        if(isDeclerationConfirmed){
//            new RegistrationTask(benfiList).execute();
//            rl_declaration.setVisibility(View.GONE);
//        }else{
//            rl_declaration.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "कृपया Declaration/ प्रख्यापन की पुष्टि करें!", Toast.LENGTH_SHORT).show();
//        }
//    }

    private class RegistrationTask extends AsyncTask<BenificiaryDetail, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(VerifyPhoneActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(VerifyPhoneActivity.this).create();

        BenificiaryDetail info;

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Registration...");
            this.dialog.show();
        }

        public RegistrationTask(BenificiaryDetail info) {
            this.info = info;
        }

        @Override
        protected String doInBackground(BenificiaryDetail... param) {

            return WebServiceHelper.RegistrationNewBen(info,version);
        }

        @Override

        protected void onPostExecute(String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result != null) {

                if (result.equals("1")) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setCancelable(false);
                    ab.setIcon(R.drawable.biharlogo);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>मुख्यमंत्री राहत कोष , बिहार से आपदा प्रबंधन विभाग,बिहार पटना के माध्यम से मुख्यमंत्री विशेष सहायता के लिए आपका पंजीकरण सफल रहा है |</font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(getBaseContext(),VarifyActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //setFinishOnTouchOutside(false);
                            finish();
                        }
                    });

                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();

                } else if (result.equals("2")) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>आपने पहले ही इस बैंक खाता को पंजीकृत कर लिया है! </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });

                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();



                } else if (result.equals("3")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>आपने पहले ही इस आधार नंबर से पंजीकृत कर लिया है कृपया दूसरा आधार नंबर प्रयोग करें ! </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });

                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();

                    //Toast.makeText(VerifyPhoneActivity.this, "आपने पहले ही इस आधार नंबर से पंजीकृत कर लिया है कृपया दूसरा आधार नंबर प्रयोग करें !", Toast.LENGTH_SHORT).show();
                } else if (result.trim().equals("4")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>आपने पहले ही इस मोबाइल नंबर से पंजीकृत कर लिया है कृपया दूसरा मोबाइल नंबर प्रयोग करें ! </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();
                }
                else if (result.trim().equals("5")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>सर्वर बिजी है ! कृपया फिर से प्रयास करे ! </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();
                }

                else if (result.trim().equals("6")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>आपका आई०एफ०एस०सी० कोड गलत है | </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();
                }
                else if (result.trim().equals("7")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>कृपया सत्यापित मोबाइल नंबर दर्ज करे| </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();
                }


                else if (result.trim().equals("10")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>यह योजना केवल उन्हीं लोगों के लिए है जो बिहार राज्य के निवासी है तथा बिहार राज्य से बाहर कोरोना वायरस के चलते फसें हुए है  ! </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();

                }else if (result.trim().equals("11")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(VerifyPhoneActivity.this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>कृपया फिर से प्रयास करे ! </font>"));
                    ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();

                    // Toast.makeText(VerifyPhoneActivity.this, "आपने पहले ही इस मोबाइल नंबर से पंजीकृत कर लिया है कृपया दूसरा मोबाइल नंबर प्रयोग करें !", Toast.LENGTH_SHORT).show();
                } else {
                    //unknown return type
                    //Toast.makeText(VerifyPhoneActivity.this,"!! URT !! आपका पंजीकरण असफल रहा है", Toast.LENGTH_SHORT).show();
                    Toast.makeText(VerifyPhoneActivity.this,result.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(VerifyPhoneActivity.this,
                        "इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे: ", Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }

    public void loadstatespinnerdata() {

        StateList = localDBHelper.getStateLocal();
        stateNameArray = new ArrayList<String>();
        //districtNameArray.add("-Select District-");
        int i = 1;
        for (State district : StateList) {
            stateNameArray.add(district.getStateName());
            i++;
        }
        //districtadapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, districtNameArray);
        //districtadapter.setDropDownViewResource(R.layout.dropdownlist);

        stateadapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateNameArray);
        stateadapter.setDropDownViewResource(R.layout.spinner_textview);
        sp_state.setAdapter(stateadapter);

    }
    private class ValidateIfscTask extends AsyncTask<BenificiaryDetail, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(VerifyPhoneActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(VerifyPhoneActivity.this).create();

        BenificiaryDetail info;

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Validating Ifsc Code...");
            this.dialog.show();
        }

        public ValidateIfscTask(BenificiaryDetail info) {
            this.info = info;
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

                    rl_declaration.setVisibility(View.VISIBLE);



                }
                else
                {

                    alertDialog.setTitle("Failed");
                    alertDialog.setMessage("अमान्य IFSC कोड या बैंक खाता बिहार का होना चाहिए | कृपया सही कोड दर्ज करें");
                    alertDialog.show();

                    //unknown return type
                    Toast.makeText(VerifyPhoneActivity.this,"अमान्य IFSC कोड या बैंक खाता बिहार का होना चाहिए | कृपया सही कोड दर्ज करें", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                Toast.makeText(VerifyPhoneActivity.this,
                        "इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे ", Toast.LENGTH_SHORT)
                        .show();
            }

        }


    }
    public String getAppVersion(){
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return version;
    }
    public void registration_Mob() {
        benfiList.setMobileNo(et_Mobile_Number.getText().toString());
        benfiList.setAadhaarNo(benfiList.getAadhaarNo());

        new RegistrationTask_Mobile().execute(benfiList);



    }
    private class RegistrationTask_Mobile extends AsyncTask<BenificiaryDetail, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(VerifyPhoneActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(VerifyPhoneActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Registration...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(BenificiaryDetail... param) {

            return WebServiceHelper.Registration_Mobile(param[0]);
        }

        @Override

        protected void onPostExecute(String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {

                if (result.contains("1")) {

                    String[] v = result.split(",");
                    String uid = "n/a", pwd = "n/a";
                    if (v.length > 2) {
                        uid = v[1];
                        pwd = v[2];
                    }
                    try {
                        tv_otp.setVisibility(View.VISIBLE);
                        tv_auth_mob.setVisibility(View.GONE);
                        et_Mobile_Number.setEnabled(false);
                        BlinkTextView(tv_verify_otp);

                        tv_verify_otp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(et_verify_otp_Number.getText().toString().length()==4) {
                                    confirm_reg_OTP();
                                }else{
                                    Toast.makeText(VerifyPhoneActivity.this,"कृपया 4 अंको प्राप्त OTP डाले !" , Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });

                        tv_resend_otp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Utiilties.isOnline(VerifyPhoneActivity.this)) {
                                    benfiList = new BenificiaryDetail();
                                    benfiList.setAadhaarNo(benfiList.getAadhaarNo());
                                    benfiList.setMobileNo(et_Mobile_Number.getText().toString());

                                    new registrion_resend_otp().execute(benfiList);
                                } else {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VerifyPhoneActivity.this);
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
                        });

                        // confirmOtp();

                    } catch (Exception ex) {
                        Toast.makeText(VerifyPhoneActivity.this,"ERROR-Exception: कुछ त्रुटि !" + ex.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else if (result.contains("3")) {
                    et_Mobile_Number.setEnabled(false);
                    et_Mobile_Number.setTextColor(Color.parseColor("#0B610B"));
                    //btn_confirm.setVisibility(View.VISIBLE);
                    btn_reg_new.setVisibility(View.VISIBLE);
                    et_verify_otp_Number.setVisibility(View.GONE);
                    tv_auth_mob.setVisibility(View.GONE);

                } else if (result.contains("0")) {


                    Toast.makeText(VerifyPhoneActivity.this, "ओटीपी भेजने में विफल रहा  !! कृपया पुन: प्रयास करें !", Toast.LENGTH_LONG).show();

                }
                else if (result.trim().equalsIgnoreCase("2")) {

                    Toast.makeText(VerifyPhoneActivity.this, "!! ओटीपी भेजने में विफल। ! कृपया पुन: प्रयास करें !", Toast.LENGTH_SHORT).show();
                }

                else if (result.trim().equalsIgnoreCase("9")) {

                    Toast.makeText(VerifyPhoneActivity.this, "कृपया ", Toast.LENGTH_SHORT).show();
                }

                else {

                    //unknown return type
                    //   Toast.makeText(VerifyPhoneActivity.this, "!! URT !! आपका पंजीकरण असफल रहा है", Toast.LENGTH_SHORT).show();
                    Toast.makeText(VerifyPhoneActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(VerifyPhoneActivity.this, "इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे ", Toast.LENGTH_SHORT).show();
            }
        }


    }
    public void confirm_reg_OTP() {

        //reg_OTP = et_OTP.getText().toString();
        reg_OTP = et_verify_otp_Number.getText().toString();

        boolean changepassword = false;
        String isValied = "yes";
        View focusView = null;


        if (TextUtils.isEmpty(reg_OTP)) {
            et_OTP.setError("अपना OTP डालें |");
            focusView = et_OTP;
            changepassword = true;
        }

        if (changepassword) {
            // error in login
            focusView.requestFocus();
        } else {
            benfiList.setRegister_otp(reg_OTP);


            new reg_user_otp().execute(benfiList);


        }
    }
    private class reg_user_otp extends AsyncTask<BenificiaryDetail, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(VerifyPhoneActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(VerifyPhoneActivity.this).create();


        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(BenificiaryDetail... param) {
            String res = WebServiceHelper.registration_otp_Mobile(benfiList);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("Responseval", "" + result);
            if (result != null) {
                if (result.contains("1")) {
                    et_Mobile_Number.setEnabled(false);
                    et_Mobile_Number.setTextColor(Color.parseColor("#0B610B"));
                    //btn_confirm.setVisibility(View.VISIBLE);
                    btn_reg_new.setVisibility(View.VISIBLE);
                    tv_auth_mob.setVisibility(View.GONE);

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VerifyPhoneActivity.this);

                    builder.setMessage("Otp verified successfully");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            tv_otp.setVisibility(View.GONE);

                        }
                    });

                    android.support.v7.app.AlertDialog dialog = builder.create();
                    dialog.show();

                } else if (result.contains("0")) {

                    Toast.makeText(VerifyPhoneActivity.this, "सर्वर बिजी है | कृपया फिर से प्रयास करे", Toast.LENGTH_SHORT).show();
                } else if (result.contains("2")) {


                    Toast.makeText(VerifyPhoneActivity.this,
                            "कृपया OTP सही डाले !", Toast.LENGTH_SHORT)
                            .show();
                }
                else if (result.contains("3")) {


                    Toast.makeText(VerifyPhoneActivity.this,
                            " Error", Toast.LENGTH_SHORT)
                            .show();
                }

            }else{
                Toast.makeText(VerifyPhoneActivity.this, "सर्वर बिजी है | कृपया फिर से प्रयास करे", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private class registrion_resend_otp extends AsyncTask<BenificiaryDetail, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(VerifyPhoneActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(VerifyPhoneActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Authenticating...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(BenificiaryDetail... param)
        {
            return WebServiceHelper.reg_user_otp_Mobile(param[0]);
        }

        @Override

        protected void onPostExecute(String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {
                if (result.contains("1")) {


                    try {
                        Toast.makeText(VerifyPhoneActivity.this,
                                "OTP आपके रजिस्टर्ड मोबाइल नंबर पर भेजा गया  !   ", Toast.LENGTH_SHORT)
                                .show();
                        //confirmOtp();
                    } catch (Exception ex) {
                        Toast.makeText(VerifyPhoneActivity.this,
                                "Error" + ex.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else if (result.contains("0")) {

                    Toast.makeText(VerifyPhoneActivity.this,
                            "OTP भेजने में बिफल ! ", Toast.LENGTH_SHORT)
                            .show();
                } else if (result.contains("2")) {

                    Toast.makeText(VerifyPhoneActivity.this,
                            "सर्वर बिजी है !कृपया थोरी देर बाद प्रयास करे !", Toast.LENGTH_SHORT)
                            .show();
                }

            } else {

                Toast.makeText(VerifyPhoneActivity.this,
                        "सर्वर बिजी है !कृपया थोरी देर बाद प्रयास करे !", Toast.LENGTH_LONG)
                        .show();
                ;
            }
        }
    }
    public static class Polygon
    {
        private final BoundingBox _boundingBox;
        private final List<Line> _sides;

        private Polygon(List<Line> sides, BoundingBox boundingBox)
        {
            _sides = sides;
            _boundingBox = boundingBox;
        }


        public static Builder Builder()
        {
            return new Builder();
        }


        public static class Builder
        {
            private List<Point> _vertexes = new ArrayList<Point>();
            private List<Line> _sides = new ArrayList<Line>();
            private BoundingBox _boundingBox = null;

            private boolean _firstPoint = true;
            private boolean _isClosed = false;


            public Builder addVertex(Point point)
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


            public Builder close()
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
                    _boundingBox = new BoundingBox();
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


    public static class Line
    {
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

    public static class Point
    {
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


    public  void BlinkTextView(TextView txt)
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(700); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt.startAnimation(anim);
    }


    public void onClick_ViewImg1(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-आधार कार्ड का फोटो-");


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



    public void onClick_ViewImg(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-आपकी सेल्फी-");


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



}
