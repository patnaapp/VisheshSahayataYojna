package com.bih.nic.in.visheshsahayata.database;

import android.util.Log;

import com.bih.nic.in.visheshsahayata.entity.BenStatus;
import com.bih.nic.in.visheshsahayata.entity.BenfiList;
import com.bih.nic.in.visheshsahayata.entity.BenificiaryDetail;
import com.bih.nic.in.visheshsahayata.entity.Block;
import com.bih.nic.in.visheshsahayata.entity.District;
import com.bih.nic.in.visheshsahayata.entity.ServerDate;
import com.bih.nic.in.visheshsahayata.entity.State;
import com.bih.nic.in.visheshsahayata.entity.Versioninfo;
import com.bih.nic.in.visheshsahayata.entity.panchayt;
import com.bih.nic.in.visheshsahayata.security.Encriptor;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;


public class WebServiceHelper {

   // public static final String SERVICEURL2 = "http://aapda.bih.nic.in/coronasahayatawebservice.asmx";
    public static final String SERVICEURL2 = "http://10.133.20.159/TestService/VisheshSahayataWebService.asmx";
    //public static final String SERVICENAMESPACE1 = "http://aapda.bih.nic.in/";
    public static final String SERVICENAMESPACE1 = "http://10.133.20.159/";
    public static final String AUTHENTICATE_METHOD = "getAadharDetailsLatest3";
    public static  final String InsertData_Ben ="InsertDataNewLatest";
    public static final String Resend_Otp = "ResendSMS";
    public static  final String Otp_Method ="RegisterUserMobileLatest";
    public static final String UPLOAD_Bank_Details = "UpdateAccountDetails";
    public static final String UPLOAD_PHOTO = "UpdatePhotoNew";
    public static final String UPLOAD_VIDEO = "UpdateVideo";
    public static  final String VALIDATE_IFSC ="ValidateIFSC";
    public static final String REGISTRATIONOTP = "VerifyOTPuserLatest";
    public static final String APPVERSION_METHOD = "getAppLatest";
    private static final String PANCHAYAT_LIST_METHOD="getPanchayatNew";
    public static final String State_List_Method = "getState";
    public static final String Dist_List_Method = "getDistrict";
    public static final String Blk_List_Method = "getBlock";
    public static final String Server_Date = "getDateTime";
    public static final String VIEWAPPLICATION = "BenStatus";
    private static final String API_Key="Kdj1Oh09ANanI0RiwodpQA==";
    static String rest;


    public static Versioninfo CheckVersion(String imei, String version) {
        Versioninfo versioninfo;
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1,APPVERSION_METHOD);
            request.addProperty("IMEI",imei);
            request.addProperty("Ver",version);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,Versioninfo.Versioninfo_CLASS.getSimpleName(),Versioninfo.Versioninfo_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + APPVERSION_METHOD,envelope);
            res1 = (SoapObject) envelope.getResponse();
            SoapObject final_object = (SoapObject) res1.getProperty(0);

            versioninfo = new Versioninfo(final_object);

        } catch (Exception e) {

            return null;
        }
        return versioninfo;

    }

    public static BenificiaryDetail Login(String aadhar, String Password) {
        try {
            SoapObject res1;
            res1=getServerData(AUTHENTICATE_METHOD, BenificiaryDetail.getBenificiaryDetail_CLASS(),"_Aadhar", "_otp", aadhar,Password);
            if (res1 != null) {
                SoapObject obj = (SoapObject) res1.getProperty(0);
                return new BenificiaryDetail(obj);
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String UpdateAadharImage(BenificiaryDetail benificiaryDetail, String adharimgStr, String selfieImgStr,String latitude,String longitude) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, UPLOAD_PHOTO);
            request.addProperty("_Aadhar", benificiaryDetail.getAadhaarNo());
            request.addProperty("_benAadharPath", adharimgStr);
            request.addProperty("_benimagepath", selfieImgStr);
            request.addProperty("_acctno", benificiaryDetail.getAccountNo());
            request.addProperty("_IFSC", benificiaryDetail.getIfsc());
            request.addProperty("_latitude", latitude);
            request.addProperty("_Longitude", longitude);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,
                    BenfiList.getdata.getSimpleName(),
                    BenfiList.getdata);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + UPLOAD_PHOTO, envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String UpdateBankDetails(BenificiaryDetail benificiaryDetail) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, UPLOAD_Bank_Details);
            request.addProperty("_Aadhar", benificiaryDetail.getAadhaarNo());
            request.addProperty("_acctno", benificiaryDetail.getAccountNo());
            request.addProperty("_IFSC", benificiaryDetail.getIfsc());



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,
                    BenfiList.getdata.getSimpleName(),
                    BenfiList.getdata);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + UPLOAD_Bank_Details, envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Registration_Mobile(BenificiaryDetail user) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, Otp_Method);
            request.addProperty("_Mobile",user.getMobileNo());
            request.addProperty("aadharnum",user.getAadhaarNo());



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1, BenificiaryDetail.BenificiaryDetail_CLASS.getSimpleName(), BenificiaryDetail.BenificiaryDetail_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + Otp_Method, envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String ValidateIfscCode(BenificiaryDetail user) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, VALIDATE_IFSC);
            request.addProperty("_IFSC",user.getIfsc());
            //request.addProperty("_Designation",user.getDateTime());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,
                    BenfiList.getdata.getSimpleName(),
                    BenfiList.getdata);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + VALIDATE_IFSC, envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String RegistrationNewBen(BenificiaryDetail user,String versoin) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, InsertData_Ben);
            request.addProperty("_Dist_Code",user.getDistCode());
            request.addProperty("_BlockCode",user.getProjectCode());
            request.addProperty("_panchayatCode",user.getPanchayatCode());
            request.addProperty("_Other_panchayatName",user.getOther_Pan_Name());
            request.addProperty("_benName",user.getBenificiaryName());
            request.addProperty("_Benficiary_BirthYear",user.getDate_Of_Birth());
            request.addProperty("_Gender",user.getGenderCode());
            request.addProperty("_FName_aadhaar",user.getWifeName());
            request.addProperty("_aadharnumber",user.getAadhaarNo());
            request.addProperty("_StateCode",user.getStateCode());
            request.addProperty("_Currentcity",user.getBen_Jagah());
            request.addProperty("_MoblleNo",user.getMobileNo());
            request.addProperty("_Ifsc",user.getIfsc());
            request.addProperty("_Accountnumber",user.getAccountNo());
            request.addProperty("_Photo",user.getPhoto1());
            request.addProperty("_AadharImage",user.getAadhar_Photo());
            request.addProperty("_AadharImgFront",user.getAadhar_front_Photo());
            request.addProperty("_AadharImgBack",user.getAadhar_back_Photo());
            request.addProperty("_latitude",user.getLatitude());
            request.addProperty("_Longitude",user.getLongitude());
            request.addProperty("_app_version",versoin);
            request.addProperty("_encstring",API_Key);
            //request.addProperty("_Designation",user.getDateTime());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,
                    BenfiList.getdata.getSimpleName(),
                    BenfiList.getdata);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + InsertData_Ben, envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String registration_otp_Mobile(BenificiaryDetail data) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, REGISTRATIONOTP);
            request.addProperty("_mob", data.getMobileNo());
            request.addProperty("_otp", data.getRegister_otp());
            request.addProperty("_aadhar", data.getAadhaarNo());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1, BenificiaryDetail.BenificiaryDetail_CLASS.getSimpleName(),BenificiaryDetail.BenificiaryDetail_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + REGISTRATIONOTP,
                    envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                // Log.d("", result.toString());

                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String reg_user_otp_Mobile(BenificiaryDetail userid) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, Resend_Otp);

            request.addProperty("_Mobile", userid.getMobileNo());
            request.addProperty("_aadharnum", userid.getAadhaarNo());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1, BenificiaryDetail.BenificiaryDetail_CLASS.getSimpleName(), BenificiaryDetail.BenificiaryDetail_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + Resend_Otp,
                    envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static String Update_Video(String Aadhar,String Video_Path) {
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1, UPLOAD_VIDEO);
            request.addProperty("_Aadhar", Aadhar);
            request.addProperty("_VideoPath", Video_Path);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1, BenificiaryDetail.BenificiaryDetail_CLASS.getSimpleName(), BenificiaryDetail.BenificiaryDetail_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + UPLOAD_VIDEO,
                    envelope);

            Object result = envelope.getResponse();

            if (result != null) {
                return result.toString();
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<panchayt> loadpanchayatList(String dist_Code) {



        SoapObject request = new SoapObject(SERVICENAMESPACE1,
                PANCHAYAT_LIST_METHOD);

        request.addProperty("BlockCode", dist_Code);

        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            envelope.addMapping(SERVICENAMESPACE1,panchayt.PanchayatWeb_CLASSSS.getSimpleName(), panchayt.PanchayatWeb_CLASSSS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + PANCHAYAT_LIST_METHOD,
                    envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();

        ArrayList<panchayt> pvmArrayList = new ArrayList<panchayt>();

        for (int ii = 0; ii < TotalProperty; ii++) {
            if (res1.getProperty(ii) != null) {
                Object property = res1.getProperty(ii);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    panchayt panchayat = new panchayt(final_object);
                    pvmArrayList.add(panchayat);
                }
            } else
                return pvmArrayList;
        }


        return pvmArrayList;
    }

    public static ArrayList<State> loadStateList() {



        SoapObject request = new SoapObject(SERVICENAMESPACE1, State_List_Method);



        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            envelope.addMapping(SERVICENAMESPACE1,State.District_CLASS.getSimpleName(), State.District_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + State_List_Method,
                    envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();

        ArrayList<State> pvmArrayList = new ArrayList<State>();

        for (int ii = 0; ii < TotalProperty; ii++) {
            if (res1.getProperty(ii) != null) {
                Object property = res1.getProperty(ii);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    State panchayat = new State(final_object);
                    pvmArrayList.add(panchayat);
                }
            } else
                return pvmArrayList;
        }


        return pvmArrayList;
    }
    public static ServerDate loadServerDate() {

        SoapObject request = new SoapObject(SERVICENAMESPACE1, Server_Date);


        ServerDate ben;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,ServerDate.District_CLASS.getSimpleName(), ServerDate.District_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + Server_Date, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            ben = new ServerDate(res1);

        } catch (Exception e) {

            e.printStackTrace();
            if(e.getLocalizedMessage()!=null) {
                Log.e("EXC", e.getLocalizedMessage());
            }
            return null;
        }
        return ben;

    }


    public static SoapObject getServerData(String methodName,Class bindClass,String param1,String param2,String value1,String value2 )
    {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE1,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static BenStatus getApplicationEntry(String aadhano) {

        SoapObject request = new SoapObject(SERVICENAMESPACE1, VIEWAPPLICATION);

        request.addProperty("aadhar_no", aadhano);
        BenStatus userDetails;
        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;



            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE1,BenStatus.VIEWENTRY_CLASS.getSimpleName(), BenStatus.VIEWENTRY_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1+ VIEWAPPLICATION, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new BenStatus(res1);

        } catch (Exception e) {

            return null;
        }
        return userDetails;

    }


//    public static String VerifyAdhaar(BenificiaryDetail benfiList) {
//        try {
//            SoapObject request = new SoapObject("http://tempuri.org/", "DemographicUIDAuth");
//            Encriptor _encrptor = new Encriptor();
//            request.addProperty("mobile_auth_aadhaar",benfiList.getAadhaarNo());
//            request.addProperty("name",benfiList.getBenificiaryName());
//            request.addProperty("dob",benfiList.getDate_Of_Birth());
//            //request.addProperty("Gender","");
//
//
//
//
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//                    SoapEnvelope.VER11);
//            envelope.dotNet = true;
//
//            envelope.setOutputSoapObject(request);
//            HttpTransportSE androidHttpTransport = new HttpTransportSE("Date Over");
//            androidHttpTransport.call("http://tempuri.org/" + "DemographicUIDAuth", envelope);
//            Object result = envelope.getResponse();
//            if (result != null) {
//                return result.toString();
//            } else
//                return null;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }

    public static String VerifyAdhaar(BenificiaryDetail benfiList)
    {
        try
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "DemographicUIDAuth");
            Encriptor _encrptor = new Encriptor();
            request.addProperty("mobile_auth_aadhaar",benfiList.getAadhaarNo());
            request.addProperty("name",benfiList.getBenificiaryName());
            request.addProperty("dob",benfiList.getDate_Of_Birth());
            //request.addProperty("Gender","");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE("http://164.100.37.11/UIDAuthService/UIDAuth.asmx");
            androidHttpTransport.call("http://tempuri.org/" + "DemographicUIDAuth", envelope);
            Object result = envelope.getResponse();
            if (result != null)
            {
                return result.toString();
            }
            else
            {
                return null;
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<District> loadDistList(String statecode) {



        SoapObject request = new SoapObject(SERVICENAMESPACE1, Dist_List_Method);

        request.addProperty("mobile_auth_aadhaar",statecode);

        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            envelope.addMapping(SERVICENAMESPACE1,State.District_CLASS.getSimpleName(), State.District_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + Dist_List_Method,
                    envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();

        ArrayList<District> pvmArrayList = new ArrayList<District>();

        for (int ii = 0; ii < TotalProperty; ii++) {
            if (res1.getProperty(ii) != null) {
                Object property = res1.getProperty(ii);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    District panchayat = new District(final_object);
                    pvmArrayList.add(panchayat);
                }
            } else
                return pvmArrayList;
        }


        return pvmArrayList;
    }



    public static ArrayList<Block> loadBlkList(String blkcode) {



        SoapObject request = new SoapObject(SERVICENAMESPACE1, Blk_List_Method);

        request.addProperty("mobile_auth_aadhaar",blkcode);

        SoapObject res1;
        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            envelope.addMapping(SERVICENAMESPACE1,Block.Block_CLASS.getSimpleName(), Block.Block_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL2);
            androidHttpTransport.call(SERVICENAMESPACE1 + Blk_List_Method,
                    envelope);

            res1 = (SoapObject) envelope.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int TotalProperty = res1.getPropertyCount();

        ArrayList<Block> pvmArrayList = new ArrayList<Block>();

        for (int ii = 0; ii < TotalProperty; ii++) {
            if (res1.getProperty(ii) != null) {
                Object property = res1.getProperty(ii);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Block panchayat = new Block(final_object);
                    pvmArrayList.add(panchayat);
                }
            } else
                return pvmArrayList;
        }


        return pvmArrayList;
    }
}
