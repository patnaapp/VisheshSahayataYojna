package com.bih.nic.in.visheshsahayata.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bih.nic.in.visheshsahayata.R;
import com.bih.nic.in.visheshsahayata.database.DataBaseHelper;
import com.bih.nic.in.visheshsahayata.database.WebServiceHelper;
import com.bih.nic.in.visheshsahayata.entity.BenificiaryDetail;
import com.bih.nic.in.visheshsahayata.entity.Block;
import com.bih.nic.in.visheshsahayata.entity.BlockWeb1;
import com.bih.nic.in.visheshsahayata.entity.District;
import com.bih.nic.in.visheshsahayata.entity.Project;
import com.bih.nic.in.visheshsahayata.entity.ServerDate;
import com.bih.nic.in.visheshsahayata.entity.State;
import com.bih.nic.in.visheshsahayata.entity.panchayt;
import com.bih.nic.in.visheshsahayata.utility.GetLocation;
import com.bih.nic.in.visheshsahayata.utility.GlobalVariables;
import com.bih.nic.in.visheshsahayata.utility.Utiilties;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarifyActivity extends AppCompatActivity {
    //implements LocationListener
    EditText et_father_Name_new,et_father_aadhar_number_new;
    TextView tv_dist_inst,tv_blck_inst;
    Button btn_verify;
    String latitude="",longitud="";
    ProgressBar pbar;

    String DistCode="",DistName="",Block_Code="",Block_Name="",PanCode="",PanName="",Awc_Code="",Awc_Name="";
    String father_aadhar_name="",father_aadhar_no_name,Ben_Id="",Ben_Name="",Ben_aadhar_Name="",Ben_aadhar_Id="",_birth_year;
    private boolean validAadhaar;
    BenificiaryDetail benfiList;
    final String TAG = "GPS";
    String latitude_new;
    GetLocation gps;
    String Gender_Name="",Gender_Code="";
    MaterialBetterSpinner sp_ben,sp_aadhar_num,sp_aadhar_num_mahila;
    String ben_type[] = {"-चयन करे-","आंगनवाड़ी केंद्र के बच्चे","स्तनपान कराने वाली महिला","गर्भवती महिला"};
    //String ben_type_aangan[] = {"-चयन करे-","बच्चे के पिता का","बच्चे के माता का"};
    String ben_type_mahila[] = {"-चयन करे-","महिला के पति का","स्वयं महिला का"};
    String ben_type_aangan[] = {"-चयन करे-","पुरुष","महिला","अन्य"};
    //String ben_type_mahila[] = {"-चयन करे-","महिला के पति का","स्वयं महिला का"};
    ArrayAdapter benarray_array,ben_type_aangan_aaray,ben_type_aangan_mahila_aaray;
    TextView login_title;

    MaterialBetterSpinner sp_project,sp_anganwadi,sp_category;

    ArrayList<District> DistrictList = new ArrayList<District>();
    Spinner sp_district,sp_block,sp_panchayat;
    ArrayList<BlockWeb1> BlockList = new ArrayList<BlockWeb1>();
    ArrayList<Project> ProjectList = new ArrayList<Project>();
    ArrayList<panchayt> PanchayatList = new ArrayList<panchayt>();
    ArrayList<String> stateNameArray;

    ArrayAdapter<String> districtadapter;
    ArrayAdapter<String> panchayatadapter;
    ArrayAdapter<String> blockadapter;
    ArrayAdapter<String> awcadapter;

    ArrayList<String> districtNameArray;
    static ArrayList<String> BlockStringList, PanchayatStringList, AwcStringList;

    DataBaseHelper localDBHelper;

    EditText et_husband_Name,et_wife_Name;
    LocationManager mlocManager = null;
    Spinner spn_gender;
    EditText et_panchayat_name;

    ArrayAdapter category_array;
    String Category_Code="",Category_Name="",_et_panchayat_name="";
    String category[] = {"-चयन करे-","समान्य","ओबीसी","इबीसी","एससी","एसटी","एससी मुशहर भुइया"};
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;

    EditText et_date,et_year_birth;
    String _ed_dob="";
    LinearLayout ll_pan;
    Spinner sp_state;
    ArrayList<State> StateList = new ArrayList<State>();
    ArrayAdapter<String> stateadapter;
    String StateCode="",StateName="";
    DataBaseHelper databaseHelper;
    ImageView sync_dist,sync_blk;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varify);
        initialize();

        databaseHelper=new DataBaseHelper(getApplicationContext());
        try {
            databaseHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }

        try {

            databaseHelper.openDataBase();
            // modifyTable();
        } catch (SQLException sqle) {

            throw sqle;
        }

        loadstatespinnerdata();

        btn_verify.setEnabled(true);
        pbar.setVisibility(View.GONE);
        btn_verify.setText("सत्यापित करे");

//        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
//        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        permissionsToRequest = findUnAskedPermissions(permissions);
        //getLocation();

        //if (!isGPS && !isNetwork) {
//        if (!isNetwork) {
//            Log.d(TAG, "Connection off");
//            showSettingsAlert();
//            //getLastLocation();
//        } else {
//            Log.d(TAG, "Connection on");
//            // check permissions
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (permissionsToRequest.size() > 0) {
//                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
//                            ALL_PERMISSIONS_RESULT);
//                    Log.d(TAG, "Permission requests");
//                    canGetLocation = false;
//                }else {
//
//                }
//
//            }
//
//            // get location
//            getLocation();
//        }

        // loadDistrictSpinnerdata();
        sp_aadhar_num.setVisibility(View.GONE);

        category_array = new ArrayAdapter(this, R.layout.spinner_textview, category);
        sp_category.setAdapter(category_array);




        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                int pos = position;
                if (pos > 0) {
                    State district = StateList.get(position-1);
                    StateCode = district.getStateCode();
                    StateName = district.getStateName();

                    sync_dist.setVisibility(View.VISIBLE);
                    tv_dist_inst.setVisibility(View.VISIBLE);
                    DistrictList = localDBHelper.getDistrictLocal(StateCode);

                    if (DistrictList.size() <= 0) {
                        new distData().execute();

                    } else {
                        loadDistrictSpinnerdata(StateCode);
                    }
                }
                else {
                    StateCode = "";
                    StateName = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        sp_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                int pos = position;
                if (pos > 0) {
                    District district = DistrictList.get(position-1);
                    DistCode = district.getDistrictCode();
                    DistName = district.getDistrictName();
                    sync_blk.setVisibility(View.VISIBLE);
                    tv_blck_inst.setVisibility(View.VISIBLE);
                    sp_block.setSelection(0);
                    sp_panchayat.setSelection(0);

                    ProjectList = localDBHelper.getBlocFromLocal(DistCode);

                    if (ProjectList.size() <= 0) {
                        new dblkData().execute();

                    } else {
                        loadBlockSpinnerData(DistCode);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
        sp_block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                int pos = position;
                if (pos > 0) {

                    Project district = ProjectList.get(position-1);
                    Block_Code = district.getBlockCode();
                    Block_Name = district.getProjName();

                    //setPanchayatSpinnerData(Block_Code);
                    Log.e("proejct", Block_Code);

                } else {
                    Block_Code = "";
                    Block_Name = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


        sp_panchayat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                int pos = position;
                if (pos > 0) {

                    panchayt district = PanchayatList.get(position-1);
                    PanCode = district.getPanchCode();
                    PanName = district.getPanchyatName();
                    if(PanName.equalsIgnoreCase("अन्य")){
                        ll_pan.setVisibility(View.VISIBLE);
                    }else{
                        ll_pan.setVisibility(View.GONE);
                    }

                } else {
                    PanCode = "";
                    PanName = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


        spn_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("arg2",""+position);
                if (position > 0) {
                    Gender_Name = ben_type_aangan[position].toString();

                    if (Gender_Name.equals("पुरुष")) {

                        Gender_Code = "M";
                    } else if (Gender_Name.equals("महिला")) {

                        Gender_Code = "F";
                    }
                    else if (Gender_Name.equals("अन्य")) {

                        Gender_Code = "T";
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
        sync_dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new distData().execute();

            }
        });

        sync_blk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new dblkData().execute();

            }
        });


        btn_verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utiilties.isOnline(VarifyActivity.this)) {
                    registration();
//                    if (latitude.equalsIgnoreCase("")) {
//
//                    } else {
//
//                        Polygon polygon = Polygon.Builder()
//                                .addVertex(new Point(24.163869f, 86.464733f))
//                                .addVertex(new Point(23.790401f, 86.466274f))
//                                .addVertex(new Point(23.794888f, 87.297577f))
//                                .addVertex(new Point(24.167083f, 87.298434f)).build(); //jamtara
//
//
//
//
//                        Point point = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
//                        boolean contains = polygon.contains(point);
//
//
//                        Polygon polygon1 = Polygon.Builder()
//                                .addVertex(new Point(27.64995f, 83.78421f))
//                                .addVertex(new Point(26.57064f, 88.557203f))
//                                .addVertex(new Point(25.866319f, 88.024204f))
//                                .addVertex(new Point(25.499882f, 88.219319f))
//                                .addVertex(new Point(24.281598f, 86.744054f))
//                                .addVertex(new Point(24.252534f, 83.408781f))
//                                .addVertex(new Point(25.185739f, 83.241732f))
//                                .addVertex(new Point(25.888924f, 84.151631f))
//                                .addVertex(new Point(26.186357f, 83.824455f)).build();  //Bihar Clockwiswe
//
//
//                        Point point1 = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
//                        boolean contains1 = polygon1.contains(point1);
//
//                        Polygon polygon3 = Polygon.Builder()
//                                .addVertex(new Point(27.64995f, 83.78421f))
//                                .addVertex(new Point(26.186357f, 83.824455f))
//                                .addVertex(new Point(25.888924f, 84.151631f))
//                                .addVertex(new Point(25.185739f, 83.241732f))
//                                .addVertex(new Point(24.252534f, 83.408781f))
//                                .addVertex(new Point(24.281598f, 86.744054f))
//                                .addVertex(new Point(25.499882f, 88.219319f))
//                                .addVertex(new Point(25.866319f, 88.024204f))
//                                .addVertex(new Point(26.57064f, 88.557203f)).build();//Bihar AntiClockwiswe
//
//
//
//                        Point point3 = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
//                        boolean contains3 = polygon3.contains(point3);
//
//                        Polygon polygon2 = Polygon.Builder()
//                                .addVertex(new Point(30.551864f, 81761214f))
//                                .addVertex(new Point(28.526323f, 85.640303f))
//                                .addVertex(new Point(27.872436f, 88.230474f))
//                                .addVertex(new Point(27.116858f, 88.011613f))
//                                .addVertex(new Point(26.903611f, 88.214915f))
//                                .addVertex(new Point(26.006473f, 88.126840f))
//                                .addVertex(new Point(27.139097f, 83.294154f))
//                                .addVertex(new Point(28.804404f, 80.005744f))
//                                .addVertex(new Point(29.440910f, 80.219152f))
//                                .addVertex(new Point(29.767055f, 80.877981f))
//                                .addVertex(new Point(30.327259f, 80.991486f)).build();  //Nepal Location
//
////                                .addVertex(new Point(30.802144f, 81.281905f))
////                                .addVertex(new Point(28.526323f, 85.640303f))
////                                .addVertex(new Point(27.862453f, 88.507553f))
////                                .addVertex(new Point(26.006473f, 88.12684f))
////                                .addVertex(new Point(27.139097f, 83.294154f))
////                                .addVertex(new Point(28.954626f, 79.591713f)).build(); //  old_Nepal Location
//
//
//                        Point point2 = new Point(Float.valueOf(String.valueOf(latitude)), Float.valueOf(String.valueOf(longitud)));
//                        boolean contains2 = polygon2.contains(point2);
//
//
//                   if (contains == false && contains1 == false && contains2 == false&& contains3 == false) {
//                            //if (contains == false  && contains2 == false) {
//                            registration();
//                        } else {
//                            AlertDialog.Builder ab = new AlertDialog.Builder(VarifyActivity.this);
//                            ab.setIcon(R.drawable.location);
//                            ab.setTitle("अलर्ट !");
//                            ab.setMessage("क्षमा कीजिये ! यह मोबाइल एप्प बिहार के लोग जो बिहार के बाहर इस संकट के वजह से फसे हुए है ये उनके सहायता के लिए है|");
//                            ab.setPositiveButton("[ ओके ]", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.dismiss();
//
//                                }
//                            });
//
//                            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                            ab.show();
//                        }
//
//                    }
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VarifyActivity.this);
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

    }


    public void setPanchayatSpinnerData(String blockCode) {
        DataBaseHelper placeData = new DataBaseHelper(VarifyActivity.this);
        PanchayatList = placeData.getPanchayatLocal(blockCode);
        if(PanchayatList.size()>0)
            loadPanchayatSpinnerData(PanchayatList);

        else
        {
            if (Utiilties.isOnline(VarifyActivity.this)) {
                new PANCHAYATDATA().execute();
            }
            else{


                final AlertDialog alertDialog = new AlertDialog.Builder(VarifyActivity.this).create();
                alertDialog.setTitle(getResources().getString(R.string.no_internet_connection));
                alertDialog.setMessage("No internet connection. Please turn on internet connection.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        alertDialog.cancel();
                        Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(I);
                    }
                });

                alertDialog.show();
            }
        }

    }
    public void loadBlockSpinnerData(String distid) {
        ProjectList = localDBHelper.getBlocFromLocal(distid);
        String[] typeNameArray = new String[ProjectList.size() + 1];
        typeNameArray[0] = "-प्रखंड चयन करे-";
        int i = 1;
        for (Project type : ProjectList) {
            typeNameArray[i] = type.getProjName();
            i++;
        }


        blockadapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, typeNameArray);
        blockadapter.setDropDownViewResource(R.layout.spinner_textview);
        sp_block.setAdapter(blockadapter);


    }

    private void loadPanchayatSpinnerData(ArrayList<panchayt> dList) {
        PanchayatStringList = new ArrayList<String>();

        PanchayatStringList.add("-पंचायत चयन करें-");

        for (int i = 0; i < dList.size(); i++) {
            PanchayatStringList.add(dList.get(i).getPanchyatName());

            panchayatadapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, PanchayatStringList);
            panchayatadapter.setDropDownViewResource(R.layout.spinner_textview);
            sp_panchayat.setAdapter(panchayatadapter);

            //panchayatadapter = new ArrayAdapter(this, R.layout.listview, PanchayatStringList);
            //sp_panchayat.setAdapter(panchayatadapter);

        }
        //PanchayatStringList.add(PanchayatStringList.size(),"अन्य");



    }


    public void loadDistrictSpinnerdata(String statecode) {

        DistrictList = localDBHelper.getDistrictLocal(statecode);
        districtNameArray = new ArrayList<String>();
        districtNameArray.add("-जिला का नाम-");
        int i = 1;
        for (District district : DistrictList) {
            districtNameArray.add(district.getDistrictName());
            i++;
        }
        //districtadapter = new ArrayAdapter<String>(this, R.layout.dropdownlist, districtNameArray);
        //districtadapter.setDropDownViewResource(R.layout.dropdownlist);

        districtadapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, districtNameArray);
        districtadapter.setDropDownViewResource(R.layout.spinner_textview);
        sp_district.setAdapter(districtadapter);

    }

//    @Override
//    public void onLocationChanged(Location location) {
//        updateUI(location);
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        getLocation();
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        if (locationManager != null) {
//            locationManager.removeUpdates(this);
//        }
//    }


    private class PANCHAYATDATA extends AsyncTask<String, Void, ArrayList<panchayt>> {

        private final ProgressDialog dialog = new ProgressDialog(VarifyActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(VarifyActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<panchayt> doInBackground(String... param) {


            return WebServiceHelper.loadpanchayatList(Block_Code);

        }

        @Override
        protected void onPostExecute(ArrayList<panchayt> result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {

                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());


                long i = helper.insertPANCHAYATData(result, Block_Code);// block id pass hoga
                if (i > 0) {

                    DataBaseHelper placeData = new DataBaseHelper(VarifyActivity.this);
                    PanchayatList = placeData.getPanchayatLocal(Block_Code);
                    if(PanchayatList.size()>0)
                        loadPanchayatSpinnerData(PanchayatList);

                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                } else {
                    // Toast.makeText(getApplicationContext(), "FailPanchayat", Toast.LENGTH_SHORT).show();
                }

            }


        }

    }
    private class stateData extends AsyncTask<String, Void, ArrayList<State>> {

        private final ProgressDialog dialog = new ProgressDialog(VarifyActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(VarifyActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<State> doInBackground(String... param) {


            return WebServiceHelper.loadStateList();

        }

        @Override
        protected void onPostExecute(ArrayList<State> result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {

                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());


                long i = helper.insertStateData(result);
                if(i>0){
                    new ServerDateTime().execute();
                    //Toast.makeText(getApplicationContext(), "State Loaded", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(getApplicationContext(), "इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे", Toast.LENGTH_LONG).show();
            }


        }

    }


    private TextWatcher inputTextWatcher2 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_husband_Name.getText().length() >0) {
                checkForEnglish(et_husband_Name);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
    private TextWatcher inputTextWatcher3 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_wife_Name.getText().length() >0) {
                checkForEnglish(et_wife_Name);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    public void registration() {
        //Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
        father_aadhar_name = et_father_Name_new.getText().toString();
        father_aadhar_no_name = et_father_aadhar_number_new.getText().toString();
        _birth_year = et_year_birth.getText().toString();
        _et_panchayat_name = et_panchayat_name.getText().toString();
        boolean cancelRegistration = false;
        String isValied = "yes";
        View focusView = null;
        if (TextUtils.isEmpty(StateCode)) {
            Toast.makeText(getApplicationContext(), "कृपया राज्य का नाम का चयन करे |", Toast.LENGTH_LONG).show();
            // sp_district.setError("कृपया जिला का नाम का चयन करे |");
            focusView = sp_state;
            cancelRegistration = true;
        }

        if (TextUtils.isEmpty(DistCode)) {
            Toast.makeText(getApplicationContext(), "कृपया जिला का नाम का चयन करे |", Toast.LENGTH_LONG).show();
            // sp_district.setError("कृपया जिला का नाम का चयन करे |");
            focusView = sp_district;
            cancelRegistration = true;
        }
        if (TextUtils.isEmpty(Block_Code)) {
            Toast.makeText(getApplicationContext(), "कृपया प्रखंड का नाम का चयन करे |", Toast.LENGTH_LONG).show();
            //sp_block.setError("कृपया प्रखंड का नाम का चयन करे |");
            focusView = sp_block;
            cancelRegistration = true;
        }
//        if (TextUtils.isEmpty(PanCode)) {
//            Toast.makeText(getApplicationContext(), "कृपया पंचायत का नाम का चयन करे |", Toast.LENGTH_LONG).show();
//            //sp_panchayat.setError("कृपया पंचायत का नाम का चयन करे |");
//            focusView = sp_panchayat;
//            cancelRegistration = true;
//        }
        if(PanName.equalsIgnoreCase("अन्य")){
            if (TextUtils.isEmpty(et_panchayat_name.getText().toString())) {
                et_panchayat_name.setError("कृपया पंचायत का नाम दर्ज करे|");
                focusView = et_panchayat_name;
                cancelRegistration = true;
            }
        }
//        if (TextUtils.isEmpty(Awc_Code)) {
//            sp_anganwadi.setError("कृपया आंगनवाड़ी केंद्र का नाम का चयन करे |");
//            focusView = sp_anganwadi;
//            cancelRegistration = true;
//        }

        if (TextUtils.isEmpty(et_husband_Name.getText().toString())) {
            et_husband_Name.setError("कृपया लाभार्थी का नाम (आधार के अनुसार) डाले |");
            focusView = et_husband_Name;
            cancelRegistration = true;
        }
        if (TextUtils.isEmpty(_birth_year)) {
            et_year_birth.setError("कृपया जन्म का वर्ष डाले |");
            focusView = et_year_birth;
            cancelRegistration = true;
        }else if (et_year_birth.getText().toString().length() < 4) {
            et_year_birth.setError("कृपया जन्म का वर्ष सही डाले |");
            focusView = et_year_birth;
            cancelRegistration = true;
        }
//        if (TextUtils.isEmpty(et_date.getText().toString())) {
//            et_date.setError("कृपया लाभार्थी अपना जन्म तिथि का चयन (आधार के अनुसार) करे |");
//            focusView = et_date;
//            cancelRegistration = true;
//        }

        if (TextUtils.isEmpty(Gender_Code)) {
            Toast.makeText(getApplicationContext(), "कृपया लाभार्थी अपना लिंग का चयन करे |", Toast.LENGTH_LONG).show();
            //sp_panchayat.setError("कृपया पंचायत का नाम का चयन करे |");
            focusView = spn_gender;
            cancelRegistration = true;
        }

        if (TextUtils.isEmpty(et_wife_Name.getText().toString())) {
            et_wife_Name.setError("कृपया पिता /पति का नाम डाले |");
            focusView = et_wife_Name;
            cancelRegistration = true;
        }
        if (TextUtils.isEmpty(father_aadhar_no_name)) {
            et_father_aadhar_number_new.setError("कृपया आधार नंबर नाम डाले |");
            focusView = et_father_aadhar_number_new;
            cancelRegistration = true;
        }


//        if (TextUtils.isEmpty(Category_Code)) {
//            sp_category.setError("कृपया श्रेणी का चयन करे |");
//            focusView = sp_category;
//            cancelRegistration = true;
//        }

//        if (TextUtils.isEmpty(Ben_aadhar_Id)) {
//            sp_aadhar_num.setError("कृपया आधार नंबर किनका है का चयन करे |");
//            focusView = sp_aadhar_num;
//            cancelRegistration = true;
//        }

        if(!validAadhaar){
            if(!Verhoeff.validateVerhoeff(father_aadhar_no_name)){
                et_father_aadhar_number_new.setError("कृपया आधार नंबर सही डाले |");
                focusView = et_father_aadhar_number_new;
                cancelRegistration = true;
            }
        }
        if (cancelRegistration) {
            // error in login
            focusView.requestFocus();
        } else {
            //userDetails = new UserDetails();
//            if(Ben_aadhar_Id == "1"){
//                benfiList.setBen_father(et_husband_Name.getText().toString().trim());
//            }else{
//                benfiList.setBen_father(et_wife_Name.getText().toString().trim());
//            }
            benfiList.setBenificiaryName(et_husband_Name.getText().toString().trim());
            benfiList.setGenderCode(Gender_Code);
            benfiList.setWifeName(et_wife_Name.getText().toString().trim());
            benfiList.setAadhaarNo(et_father_aadhar_number_new.getText().toString().trim());
            benfiList.setDate_Of_Birth(et_year_birth.getText().toString().trim());
            benfiList.setOther_Pan_Name(et_panchayat_name.getText().toString().trim());
            try{
                //benfiList.setEncrypt_benName(_encrptor.Encrypt(et_husband_Name.getText().toString().trim(), CommonPref.CIPER_KEY));
                //benfiList.setEncrypt_AadharNo(_encrptor.Encrypt(et_father_aadhar_number_new.getText().toString().trim(), CommonPref.CIPER_KEY));
            }
            catch (Exception ex){
                ex.printStackTrace();
            }


            // PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USER_ID", MobileNumber).commit();

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
                //new stateData().execute();
                new ValidateAdhhar(benfiList).execute();
            }

        }
    }
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

        String regx = "^[a-zA-Z ]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txtVal);
        return matcher.find();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private class ValidateAdhhar extends AsyncTask<String, Void, String> {

        String pid = "-1";
        BenificiaryDetail data;
        private final ProgressDialog dialog = new ProgressDialog(
                VarifyActivity.this);

        ValidateAdhhar(BenificiaryDetail data) {
            this.data = data;
            //pid = data.getId();
            Log.e("Pid  ", pid + " ");
        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage(getResources().getString(R.string.uploading));
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {


            String res = WebServiceHelper.VerifyAdhaar(this.data);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();

            }

            try {
                if (result != null) {
                    if (result.equalsIgnoreCase("Authentication Success")) {


                        showSuccessMessageDialogue(result.toString());

                    } else {
                        showMessageDialogue("आधार और नाम सही नहीं", "कृपया सही नाम और आधार संख्या डालें");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        public void showMessageDialogue(String messageTxt, String essage) {
            // MainActi.this.runOnUiThread(new Runnable() {
            // @Override
            //  public void run() {
            new AlertDialog.Builder(VarifyActivity.this)
                    .setCancelable(false)
                    .setTitle("Error")
                    .setMessage(essage)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            // }
            // });
        }

        public void showSuccessMessageDialogue(String messageTxt) {
            // MainActi.this.runOnUiThread(new Runnable() {
            // @Override
            //  public void run() {
            new AlertDialog.Builder(VarifyActivity.this)
                    .setCancelable(false)
                    .setTitle("Success")
                    .setMessage("आपका आधार सत्यापन सफल हुआ !")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.cancel();

                            moveToNext();
                            //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Dist_Code", benfiList.getDist_Code() ).commit();
                        }
                    })
                    .show();
            // }
            // });
        }

    }

    public void moveToNext(){
        BenificiaryDetail info = new BenificiaryDetail();

        info.setStateCode(StateCode);
        info.setStateName(StateName);
        info.setDistCode(DistCode);
        info.setDistrictName(DistName);
        info.setProjectCode(Block_Code);
        info.setProjName(Block_Name);
        info.setPanchayatCode(PanCode);
        info.setPanchayatName(PanName);
        //info.setAwcCode(Awc_Code);
        //info.setAWCName(Awc_Name);
        info.setBenificiaryName(et_husband_Name.getText().toString());
        info.setOther_Pan_Name(et_panchayat_name.getText().toString());
        info.setGenderCode(Gender_Code);
        info.setWifeName(et_wife_Name.getText().toString());
        //info.setCategoryDetail(Category_Code);
        // info.setBenType(Ben_aadhar_Id);
        info.setAadhaarNo(et_father_aadhar_number_new.getText().toString());
        info.setDate_Of_Birth(et_year_birth.getText().toString());
        Intent i = new Intent(VarifyActivity.this,VerifyPhoneActivity.class);
        i.putExtra("data", info);
        startActivity(i);
//        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Ben_Id",Ben_Id).commit();
//        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("BenAadhar_Id",Ben_aadhar_Id).commit();
//        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("FatherName",et_father_Name_new.getText().toString()).commit();
//        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AaadharNumber",et_father_aadhar_number_new.getText().toString()).commit();
    }

    private TextWatcher inputTextWatcher1 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (et_father_aadhar_number_new.getText().length() == 12) {
                try {
                    if (Verhoeff.validateVerhoeff(et_father_aadhar_number_new.getText().toString())) {
                        et_father_aadhar_number_new.setTextColor(Color.parseColor("#0B610B"));
                        validAadhaar = true;
                    } else {
                        //showMessageDialogue("Invalid Aadhaar Number");
                        Toast.makeText(getApplicationContext(),"आधार नंबर गलत है ",Toast.LENGTH_LONG).show();
                        et_father_aadhar_number_new.setTextColor(Color.parseColor("#ff0000"));
                        validAadhaar = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                et_father_aadhar_number_new.setTextColor(Color.parseColor("#000000"));
                validAadhaar = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!validAadhaar && et_father_aadhar_number_new.getText().toString().length() == 12) {
//                CommonMethods.showErrorDialog(getResources().getString(R.string.invalid_value),
//                        getResources().getString(R.string.check_aadhaar_no));
            }
        }
    };



    public static class Polygon
    {
        private final Polygon.BoundingBox _boundingBox;
        private final List<Line> _sides;

        private Polygon(List<Line> sides,Polygon.BoundingBox boundingBox)
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


        private boolean intersect(Line ray,Line side)
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

        public Line(Point start,Point end)
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
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

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

    //    private void getLocation() {
//        try {
//            if (canGetLocation) {
//                Log.d(TAG, "Can get location");
//                if (isGPS) {
//                    // from GPS
//                    Log.d(TAG, "GPS on");
//                    locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//
//                    if (locationManager != null) {
//                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        //loc = getLocation(LocationManager.GPS_PROVIDER);
//                        if (loc != null)
//                            updateUI(loc);
//                    }
//                } else if (isNetwork) {
//                    // from Network Provider
//                    Log.d(TAG, "NETWORK_PROVIDER on");
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//
//                    if (locationManager != null) {
//                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (loc != null)
//                            updateUI(loc);
//                    }
//                } else {
//                    loc.setLatitude(0);
//                    loc.setLongitude(0);
//                    updateUI(loc);
//                }
//            } else {
//                Log.d(TAG, "Can't get location");
//            }
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
    private void updateUI(Location loc) {

        //if(loc.getAccuracy() < 250) {
        boolean isMockLocation = loc.isFromMockProvider();
        Log.d("TAGLOCATION",String.valueOf(isMockLocation));
        if(!isMockLocation) {

            if (loc.getLatitude() > 0.0) {
                latitude = Double.toString(loc.getLatitude());
                longitud = Double.toString(loc.getLongitude());





//            btn_save.setEnabled(true);
//            pbar.setVisibility(View.GONE);
//            btn_save.setText("Make Attendence");
                if (latitude.equalsIgnoreCase("")) {
                    btn_verify.setEnabled(false);
                    btn_verify.setText("कृपया GPS के लिए प्रतिक्षा कीजिये !");
                } else {
                    btn_verify.setEnabled(true);
                    pbar.setVisibility(View.GONE);
                    btn_verify.setText("सत्यापित करे");
                }


            }
        }
        else {
            AlertDialog.Builder ab = new AlertDialog.Builder(VarifyActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();

        // getLocation();
    }


    private class ServerDateTime extends AsyncTask<String, Void, ServerDate> {

        private final ProgressDialog dialog = new ProgressDialog(VarifyActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(VarifyActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("लोड हो रहा है...");
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
                new ValidateAdhhar(benfiList).execute();




            }


        }

    }

//    public  Location getLocation(String provider){
//        if(locationManager.isProviderEnabled(provider)){
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
//                    locationManager.requestLocationUpdates(provider,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,VarifyActivity.this);
//                    if(locationManager!=null){
//                        loc=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        return loc;
//                    }
//
//                }
//            }
//        }
//        return null;
//    }

    public void initialize(){
        btn_verify=(Button)findViewById(R.id.btn_verify_new);
        btn_verify.setEnabled(false);
        pbar = (ProgressBar) findViewById(R.id.spinner);
        pbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#80DAEB"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        pbar.setVisibility(View.VISIBLE);

        localDBHelper= new DataBaseHelper(this);
        benfiList = new BenificiaryDetail();
        et_father_Name_new=(EditText)findViewById(R.id.et_father_Name_new);
        et_father_aadhar_number_new=(EditText)findViewById(R.id.et_father_aadhar_number_new);

        et_husband_Name=(EditText)findViewById(R.id.et_husband_Name);
        et_wife_Name=(EditText)findViewById(R.id.et_wife_Name);
        et_panchayat_name=(EditText)findViewById(R.id.et_panchayat_name);
        et_year_birth=(EditText)findViewById(R.id.et_year_birth);
        ll_pan=(LinearLayout) findViewById(R.id.ll_pan);
        ll_pan.setVisibility(View.GONE);
        et_husband_Name.addTextChangedListener(inputTextWatcher2);
        et_wife_Name.addTextChangedListener(inputTextWatcher3);
        login_title=(TextView) findViewById(R.id.login_title);
        tv_dist_inst=(TextView) findViewById(R.id.tv_dist_inst);
        tv_blck_inst=(TextView) findViewById(R.id.tv_blck_inst);

        sp_aadhar_num=(MaterialBetterSpinner)findViewById(R.id.sp_aadhar_num);
        sp_aadhar_num.setVisibility(View.GONE);
        sp_aadhar_num_mahila=(MaterialBetterSpinner)findViewById(R.id.sp_aadhar_num_mahila);
        sp_aadhar_num_mahila.setVisibility(View.GONE);
        spn_gender = (Spinner) findViewById(R.id.spn_gender);
        sp_state = (Spinner)findViewById(R.id.sp_state);
        sync_dist = (ImageView) findViewById(R.id.sync_dist);
        sync_blk = (ImageView) findViewById(R.id.sync_blk);

        et_father_aadhar_number_new.addTextChangedListener(inputTextWatcher1);
        benarray_array = new ArrayAdapter(this, R.layout.spinner_textview, ben_type);
        ben_type_aangan_aaray = new ArrayAdapter(this, R.layout.spinner_textview, ben_type_aangan);
        ben_type_aangan_mahila_aaray = new ArrayAdapter(this, R.layout.spinner_textview, ben_type_mahila);
        spn_gender.setAdapter(ben_type_aangan_aaray);
        sp_aadhar_num_mahila.setAdapter(ben_type_aangan_mahila_aaray);
        et_father_Name_new.setEnabled(false);
        sync_dist.setVisibility(View.GONE);
        sync_blk.setVisibility(View.GONE);
        tv_dist_inst.setVisibility(View.GONE);
        tv_blck_inst.setVisibility(View.GONE);




        sp_district = (Spinner) findViewById(R.id.sp_district);
        sp_block = (Spinner) findViewById(R.id.sp_block);
        sp_panchayat = (Spinner) findViewById(R.id.sp_panchayat);
        sp_project = (MaterialBetterSpinner) findViewById(R.id.sp_project);

        sp_category = (MaterialBetterSpinner) findViewById(R.id.sp_category);
    }


    public void loadstatespinnerdata()
    {

        StateList = localDBHelper.getStateLocal();
        stateNameArray = new ArrayList<String>();
        stateNameArray.add("-राज्य का नाम-");

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


    private class distData extends AsyncTask<String, Void, ArrayList<District>> {

        private final ProgressDialog dialog = new ProgressDialog(VarifyActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(VarifyActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<District> doInBackground(String... param) {


            return WebServiceHelper.loadDistList(StateCode);

        }

        @Override
        protected void onPostExecute(ArrayList<District> result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null)
            {

                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());


                long i = helper.insertDistData(result);
                if(i>0)
                {
                    loadDistrictSpinnerdata(StateCode);
                    //new ServerDateTime().execute();
                    //Toast.makeText(getApplicationContext(), "State Loaded", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(), "इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे", Toast.LENGTH_LONG).show();
            }


        }

    }



    private class dblkData extends AsyncTask<String, Void, ArrayList<Block>> {

        private final ProgressDialog dialog = new ProgressDialog(VarifyActivity.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(VarifyActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<Block> doInBackground(String... param) {


            return WebServiceHelper.loadBlkList(DistCode);

        }

        @Override
        protected void onPostExecute(ArrayList<Block> result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {

                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());


                long i = helper.insertBlkData(result);
                if(i>0){
                    loadBlockSpinnerData(DistCode);
                    //new ServerDateTime().execute();
                    //Toast.makeText(getApplicationContext(), "State Loaded", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(getApplicationContext(), "इंटरनेट की स्पीड स्लो है | कृपया कुछ समय बाद प्रयास करे", Toast.LENGTH_LONG).show();
            }


        }

    }
}
