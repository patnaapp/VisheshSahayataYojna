package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

/**
 * Created by NICSI on 3/15/2018.
 */

public class UserDetails implements KvmSerializable {

    public static Class<UserDetails> USER_CLASS = UserDetails.class;


    private boolean isAuthenticated = false;

    private String _UserID = "";
    private String _UserName = "";
    private String _UserPassword = "";
    private String _DistrictCode = "";
    private String _DistrictName = "";
    private String _UserRole = "";
    private String _UserRoleDesc = "";
    private String _IsLock = "";
    private String _StateCode = "";
    private String _StateName = "";
    private String _IMEI = "";
    private String _Login = "";

    private String Name = "";
    private String MobileNumber = "";
    private String Thana = "";
    private String Designation = "";
    private String Dist_Code = "";
    private String Dist_Name = "";
    private String DateTime = "";

    private String Register_otp = "";


    public UserDetails() {
    }

    public UserDetails(SoapObject obj) {

        this.isAuthenticated = Boolean.parseBoolean(obj.getProperty("isAuthenticated").toString());
        this.setName(obj.getProperty("Name").toString());
        this.setThana(obj.getProperty("ThanaNumb").toString());
        this.setMobileNumber(obj.getProperty("MobileNo").toString());
        this.setDist_Code(obj.getProperty("DistCode").toString());
        this.setDist_Name(obj.getProperty("DistName").toString());
        this.setDesignation(obj.getProperty("Designation").toString());
        this.setDateTime(obj.getProperty("UploadDate").toString());


    }


    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }

    public String get_UserPassword() {
        return _UserPassword;
    }

    public void set_UserPassword(String _UserPassword) {
        this._UserPassword = _UserPassword;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String get_UserID() {
        return _UserID;
    }

    public void set_UserID(String _UserID) {
        this._UserID = _UserID;
    }

    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public String get_DistrictCode() {
        return _DistrictCode;
    }

    public void set_DistrictCode(String _DistrictCode) {
        this._DistrictCode = _DistrictCode;
    }

    public String get_DistrictName() {
        return _DistrictName;
    }

    public void set_DistrictName(String _DistrictName) {
        this._DistrictName = _DistrictName;
    }

    public String get_UserRole() {
        return _UserRole;
    }

    public void set_UserRole(String _UserRole) {
        this._UserRole = _UserRole;
    }

    public String get_UserRoleDesc() {
        return _UserRoleDesc;
    }

    public void set_UserRoleDesc(String _UserRoleDesc) {
        this._UserRoleDesc = _UserRoleDesc;
    }

    public String get_IsLock() {
        return _IsLock;
    }

    public void set_IsLock(String _IsLock) {
        this._IsLock = _IsLock;
    }

    public String get_StateCode() {
        return _StateCode;
    }

    public void set_StateCode(String _StateCode) {
        this._StateCode = _StateCode;
    }

    public String get_StateName() {
        return _StateName;
    }

    public void set_StateName(String _StateName) {
        this._StateName = _StateName;
    }

    public String get_IMEI() {
        return _IMEI;
    }

    public void set_IMEI(String _IMEI) {
        this._IMEI = _IMEI;
    }

    public String get_Login() {
        return _Login;
    }

    public void set_Login(String _Login) {
        this._Login = _Login;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getThana() {
        return Thana;
    }

    public void setThana(String thana) {
        Thana = thana;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getDist_Code() {
        return Dist_Code;
    }

    public void setDist_Code(String dist_Code) {
        Dist_Code = dist_Code;
    }

    public String getDist_Name() {
        return Dist_Name;
    }

    public void setDist_Name(String dist_Name) {
        Dist_Name = dist_Name;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getRegister_otp() {
        return Register_otp;
    }

    public void setRegister_otp(String register_otp) {
        Register_otp = register_otp;
    }
}
