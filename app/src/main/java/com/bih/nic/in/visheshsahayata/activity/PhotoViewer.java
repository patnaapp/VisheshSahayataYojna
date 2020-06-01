package com.bih.nic.in.visheshsahayata.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bih.nic.in.visheshsahayata.R;

public class PhotoViewer extends AppCompatActivity {
    Button btn_ok,btn_update_aadhar,btn_status;
    String version="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        //showinstructions();

        btn_ok = (Button) findViewById(R.id.btn_confirm);
        btn_status = (Button) findViewById(R.id.btn_status);
        btn_update_aadhar = (Button) findViewById(R.id.btn_update_aadhar);

        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView tv = (TextView) findViewById(R.id.txtVersion);
            tv.setText(getResources().getString(R.string.app_version)  + version +"");

        } catch (PackageManager.NameNotFoundException e) {

        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ob =new Intent(PhotoViewer.this,VarifyActivity.class);
                startActivity(ob);
//                AlertDialog.Builder ab = new AlertDialog.Builder(PhotoViewer.this);
//                ab.setMessage(Html.fromHtml(
//                        "<font color=#000000>बिहार मुख्यमंत्री विशेष सहायता योजना के लिए आवेदन की अंतिम तिथि खत्म हो गई है | </font>"));
//                ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                });
//                ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                ab.show();

            }
        });

        btn_update_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ob =new Intent(PhotoViewer.this,LoginActivity.class);
                startActivity(ob);
            }
        });
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ob =new Intent(PhotoViewer.this,PaymentStatus.class);
                startActivity(ob);
            }
        });
    }



    public  void showinstructions()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(PhotoViewer.this);
        ab.setIcon(R.drawable.usermanual);
        ab.setCancelable(false);

        ab.setTitle(Html.fromHtml("<font color='#990000'>आवश्यक सूचना:-</font>"));
        TextView myMsg = new TextView(this);
        myMsg.setPadding(20, 10, 0, 0);
        myMsg.setGravity(Gravity.LEFT);
        myMsg.setText(Html.fromHtml("<font color='#0000FF'>(I). मै बिहार का/की निवासी हूं |<BR><BR>(II).मेरा बैंक खाता बिहार राज्य के बैंक ब्रांच में है |<BR><BR>(III).उत्तर बिहार ग्रामीण बैंक का  आई०एफ०एस०सी० कोड-<B>(CBIN0R10001)</B> डाले|<BR><BR>(IV).दक्षिण बिहार ग्रामीण बैंक , मध्य बिहार ग्रामीण बैंक ,  बिहार क्षेत्रीय ग्रामीण बैंक का  आई०एफ०एस०सी० कोड-<B>(PUNB0MBGB06)</B> डाले|<BR><BR>(V). वैसे लोग जो पंजीकरण नहीं करा पाए थे वे पुनः अब प्रयास करे |<BR><BR>(VI).कृपया फोटो का सेल्फी न ले अन्यथा आपका आवेदन अस्वीकृत कर दिया जाएगा और कानूनी कानूनी कार्रवाई की जा सकती है |</font>"));
        // ab.setTitle(Html.fromHtml("<font color='#990000'>आवश्यक सूचना:-</font>"));
        // ab.setMessage("1).If the checkbox is checked and beneficiary name is written in GREEN colour it means attendance is above 75%. \n\n 2).If the checkbox is unchecked and beneficiary name is written in RED colour it means attendance is below 75%.  \n\n 3). If the checkbox is unchecked and beneficiary name is written in BLACK colour it means attendance is unmarked till now.");
        // ab.setMessage(Html.fromHtml("<font color='#0000FF'>(I). मै बिहार का निवासी हूं |<BR><BR>(II).मेरा बैंक खाता बिहार राज्य के बैंक ब्रांच में है |<BR><BR>(III).उत्तर बिहार ग्रामीण बैंक का  आई०एफ०एस०सी० कोड-<B>(CBIN0R10001)</B> डाले|<BR><BR>(IV).दक्षिण बिहार ग्रामीण बैंक , मध्य बिहार ग्रामीण बैंक ,  बिहार क्षेत्रीय ग्रामीण बैंक का  आई०एफ०एस०सी० कोड-<B>(PUNB0MBGB06)</B> डाले|<BR><BR></font>"));
        ab.setView(myMsg);
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

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        showinstructions();
//    }
    //    @Override
//    protected void onResume() {
//        super.onResume();
//        showinstructions();
//    }
}
