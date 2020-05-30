package com.bih.nic.in.visheshsahayata.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bih.nic.in.visheshsahayata.R;
import com.bih.nic.in.visheshsahayata.database.WebServiceHelper;
import com.bih.nic.in.visheshsahayata.entity.BenStatus;

public class PaymentStatus extends AppCompatActivity {
    EditText et_Aaadharno;
    String aadhano,str_addh_num="";
    TextView status,remarks;
    Button btn_search;
    LinearLayout ll_Bensearch;
    private boolean validAadhaar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        et_Aaadharno = findViewById(R.id.et_Aaadharno);
        et_Aaadharno.addTextChangedListener(inputTextWatcher);
        status = findViewById(R.id.status);
        remarks = findViewById(R.id.remarks);
        btn_search = findViewById(R.id.btn_search);
        ll_Bensearch = findViewById(R.id.ll_Bensearch);
        aadhano=et_Aaadharno.getText().toString();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aadhano=et_Aaadharno.getText().toString();
                if(!aadhano.equals(""))
                {
                    new CheckBenStatus().execute();
                    if(status.equals("")&& remarks.equals(""))
                    {ll_Bensearch.setVisibility(View.GONE);}
                    else {
                        ll_Bensearch.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(PaymentStatus.this, "कृपया आधार नंबर डाले", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private TextWatcher inputTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (et_Aaadharno.getText().length() == 12) {
                try {
                    if (Verhoeff.validateVerhoeff(et_Aaadharno.getText().toString())) {
                        et_Aaadharno.setTextColor(Color.parseColor("#0B610B"));
                        validAadhaar = true;
                    } else {
                        //showMessageDialogue("Invalid Aadhaar Number");
                        Toast.makeText(getApplicationContext(),"आधार नंबर गलत है !",Toast.LENGTH_LONG).show();
                        et_Aaadharno.setTextColor(Color.parseColor("#ff0000"));
                        validAadhaar = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                et_Aaadharno.setTextColor(Color.parseColor("#000000"));
                validAadhaar = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!validAadhaar && str_addh_num.length() == 12) {
//                CommonMethods.showErrorDialog(getResources().getString(R.string.invalid_value),
//                        getResources().getString(R.string.check_aadhaar_no));
            }
        }
    };
    private class CheckBenStatus extends AsyncTask<String, Void, BenStatus> {

        private final ProgressDialog dialog = new ProgressDialog(PaymentStatus.this);

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(PaymentStatus.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("लोड हो रहा है...");
            this.dialog.show();
        }

        @Override
        protected BenStatus doInBackground(String... param) {


            return WebServiceHelper.getApplicationEntry(aadhano);

        }

        @Override
        protected void onPostExecute(BenStatus result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {


                status.setText(result.getStatus());
                remarks.setText(result.getRemarks());

            }
            else {
                Toast.makeText(getApplicationContext(),"कोई रिकॉर्ड नहीं मिला",Toast.LENGTH_LONG).show();

            }
        }

    }
}
