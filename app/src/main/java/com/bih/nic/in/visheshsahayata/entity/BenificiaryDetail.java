package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class BenificiaryDetail implements KvmSerializable, Serializable {

    public static Class<BenificiaryDetail> BenificiaryDetail_CLASS = BenificiaryDetail.class;

    private boolean isAuthenticated = false;

    private String benId = "";
    private String aadhaarNo = "";
    private String mobileNo = "";
    private String Password = "";
    private String benType = "";
    private String distCode = "";
    private String districtName = "";
    private String projectCode = "";
    private String projName = "";
    private String panchayatCode = "";
    private String panchayatName = "";
    private String AwcCode = "";
    private String AWCName = "";
    private String benificiaryName = "";
    private String encrypt_benName = "";
    private String genderName = "";
    private String categoryDetail = "";
    private String husbandName = "";
    private String wifeName = "";
    private String aadharWife = "";
    private String entryDate = "";
    private String Ifsc = "";
    private String accountNo = "";
    private String bankCode = "";
    private String benAccountTypeId = "";
    private String benAccountTypeName = "";
    private String appVersion = "";
    private String stateCode = "";
    private String stateName = "";
    private String Ben_Jagah = "";
    private String Photo1 = "";
    private String Aadhar_front_Photo = "";
    private String Aadhar_back_Photo = "";
    private String Latitude = "";
    private String Longitude = "";
    private String Aadhar_Photo = "";
    private String Register_otp = "";
    private String GenderCode = "";
    private String ISPaymentdone = "";
    private String IsUpdatedEligible = "";
    private String IsUpdateEligibleAccount = "";
    private String ISPaymentStatus = "";
    private String ISVideoUploaded = "";
    private String ISVideoUploadedElegible = "";
    private String Encrypt_AadharNo = "";
    private String Date_Of_Birth = "";
    private String Other_Pan_Name = "";
    private String ReasonOfRejectionDst = "";
    private String ReasonOfRejectionPfm = "";


    public BenificiaryDetail() {
    }

    public BenificiaryDetail(SoapObject obj) {

        //this.setAuthenticated(true);
        this.isAuthenticated = Boolean.parseBoolean(obj.getProperty("isAuthenticated").toString());
        this.setBenId(obj.getProperty("Slno").toString());
        this.setAadhaarNo(obj.getProperty("AadhaarNo").toString());
        this.setMobileNo(obj.getProperty("MoblleNo").toString());
        this.setBenificiaryName(obj.getProperty("benName").toString());
        this.setIsUpdatedEligible(obj.getProperty("IsUpdateEligible").toString());
        this.setISPaymentStatus(obj.getProperty("PaymentStatus").toString());
        this.setISVideoUploaded(obj.getProperty("isVideoUploaded").toString());
        this.setISVideoUploadedElegible(obj.getProperty("isVideoUploadedEligible").toString());
        this.setIsUpdateEligibleAccount(obj.getProperty("IsUpdateEligibleAccount").toString());
        this.setReasonOfRejectionDst(obj.getProperty("DSTADMRemarks").toString());
        this.setReasonOfRejectionPfm(obj.getProperty("PFMSMRemarks").toString());
//        this.setDistCode(obj.getProperty("Dist_Code").toString());
//        this.setDistrictName(obj.getProperty("District_Name").toString());
//        this.setProjectCode(obj.getProperty("ProjCode").toString());
//        this.setProjName(obj.getProperty("ProjName").toString());
//        this.setPanchayatCode(obj.getProperty("PanchayatCode").toString());
//        this.setPanchayatName(obj.getProperty("PanchayatName").toString());
//        this.setAwcCode(obj.getProperty("Awc_Code").toString());
//        this.setAWCName(obj.getProperty("AWCName").toString());
//        this.setBenificiaryName(obj.getProperty("benName").toString());
//        this.setGenderName(obj.getProperty("GenderName").toString());
//        this.setCategoryDetail(obj.getProperty("category").toString());
//        this.setHusbandName(obj.getProperty("FName_aadhaar").toString());
//        this.setWifeName(obj.getProperty("MName_aadhaar").toString());
//        this.setBenType(obj.getProperty("Aadhaar_MF").toString());
//        this.setEntryDate(obj.getProperty("Entry_Date").toString());
        this.setIfsc(obj.getProperty("IFSC").toString());
        this.setAccountNo(obj.getProperty("acctno").toString());
//        this.setBankCode(obj.getProperty("bankcode").toString());
//        this.setBenAccountTypeId(obj.getProperty("AccountOf").toString());




//        this.setAuthenticated(Boolean.parseBoolean(obj.getProperty("isAuthenticated").toString()));
//        this.setBenId(obj.getProperty("Slno").toString());
//        this.setAadhaarNo(obj.getProperty("AadhaarNo").toString());
//        this.setMobileNo(obj.getProperty("MoblleNo2").toString());
//        this.setBenType(obj.getProperty("ben_type").toString());
//        this.setDistCode(obj.getProperty("Dist_Code").toString());
//        this.setDistrictName(obj.getProperty("District_Name").toString());
//        this.setProjectCode(obj.getProperty("ProjCode").toString());
//        this.setProjName(obj.getProperty("ProjName").toString());
//        this.setPanchayatCode(obj.getProperty("PanchayatCode").toString());
//        this.setPanchayatName(obj.getProperty("PanchayatName").toString());
//        this.setAwcCode(obj.getProperty("Awc_Code").toString());
//        this.setAWCName(obj.getProperty("AWCName").toString());
//        this.setBenificiaryName(obj.getProperty("benName").toString());
//        this.setGenderName(obj.getProperty("GenderName").toString());
//        this.setCategoryDetail(obj.getProperty("category").toString());
//        this.setHusbandName(obj.getProperty("FName_aadhaar").toString());
//        this.setWifeName(obj.getProperty("MName_aadhaar").toString());
//        this.setBenType(obj.getProperty("Aadhaar_MF").toString());
//        this.setEntryDate(obj.getProperty("Entry_Date").toString());
//        this.setIfsc(obj.getProperty("IFSC").toString());
//        this.setAccountNo(obj.getProperty("acctno").toString());
//        this.setBankCode(obj.getProperty("bankcode").toString());
//        this.setBenAccountTypeId(obj.getProperty("AccountOf").toString());
    }

    public static Class<BenificiaryDetail> getBenificiaryDetail_CLASS() {
        return BenificiaryDetail_CLASS;
    }

    public static void setBenificiaryDetail_CLASS(Class<BenificiaryDetail> benificiaryDetail_CLASS) {
        BenificiaryDetail_CLASS = benificiaryDetail_CLASS;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBenAccountTypeId() {
        return benAccountTypeId;
    }

    public void setBenAccountTypeId(String benAccountTypeId) {
        this.benAccountTypeId = benAccountTypeId;
    }

    public String getBenAccountTypeName() {
        return benAccountTypeName;
    }

    public void setBenAccountTypeName(String benAccountTypeName) {
        this.benAccountTypeName = benAccountTypeName;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getBenId() {
        return benId;
    }

    public void setBenId(String benId) {
        this.benId = benId;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBenType() {
        return benType;
    }

    public void setBenType(String benType) {
        this.benType = benType;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getPanchayatCode() {
        return panchayatCode;
    }

    public void setPanchayatCode(String panchayatCode) {
        this.panchayatCode = panchayatCode;
    }

    public String getPanchayatName() {
        return panchayatName;
    }

    public void setPanchayatName(String panchayatName) {
        this.panchayatName = panchayatName;
    }

    public String getAwcCode() {
        return AwcCode;
    }

    public void setAwcCode(String awcCode) {
        AwcCode = awcCode;
    }

    public String getAWCName() {
        return AWCName;
    }

    public void setAWCName(String AWCName) {
        this.AWCName = AWCName;
    }

    public String getBenificiaryName() {
        return benificiaryName;
    }

    public void setBenificiaryName(String benificiaryName) {
        this.benificiaryName = benificiaryName;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(String categoryDetail) {
        this.categoryDetail = categoryDetail;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = wifeName;
    }

    public String getAadharWife() {
        return aadharWife;
    }

    public void setAadharWife(String aadharWife) {
        this.aadharWife = aadharWife;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getIfsc() {
        return Ifsc;
    }

    public void setIfsc(String ifsc) {
        Ifsc = ifsc;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getBen_Jagah() {
        return Ben_Jagah;
    }

    public void setBen_Jagah(String ben_Jagah) {
        Ben_Jagah = ben_Jagah;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAadhar_Photo() {
        return Aadhar_Photo;
    }

    public void setAadhar_Photo(String aadhar_Photo) {
        Aadhar_Photo = aadhar_Photo;
    }

    public String getAadhar_front_Photo() {
        return Aadhar_front_Photo;
    }

    public void setAadhar_front_Photo(String aadhar_front_Photo) {
        Aadhar_front_Photo = aadhar_front_Photo;
    }

    public String getAadhar_back_Photo() {
        return Aadhar_back_Photo;
    }

    public void setAadhar_back_Photo(String aadhar_back_Photo) {
        Aadhar_back_Photo = aadhar_back_Photo;
    }

    public String getRegister_otp() {
        return Register_otp;
    }

    public void setRegister_otp(String register_otp) {
        Register_otp = register_otp;
    }

    public String getGenderCode() {
        return GenderCode;
    }

    public void setGenderCode(String genderCode) {
        GenderCode = genderCode;
    }

    public String getISPaymentdone() {
        return ISPaymentdone;
    }

    public void setISPaymentdone(String ISPaymentdone) {
        this.ISPaymentdone = ISPaymentdone;
    }


    public String getIsUpdatedEligible() {
        return IsUpdatedEligible;
    }

    public void setIsUpdatedEligible(String isUpdatedEligible) {
        IsUpdatedEligible = isUpdatedEligible;
    }

    public String getISPaymentStatus() {
        return ISPaymentStatus;
    }

    public void setISPaymentStatus(String ISPaymentStatus) {
        this.ISPaymentStatus = ISPaymentStatus;
    }

    public String getISVideoUploaded() {
        return ISVideoUploaded;
    }

    public void setISVideoUploaded(String ISVideoUploaded) {
        this.ISVideoUploaded = ISVideoUploaded;
    }

    public String getEncrypt_AadharNo() {
        return Encrypt_AadharNo;
    }

    public void setEncrypt_AadharNo(String encrypt_AadharNo) {
        Encrypt_AadharNo = encrypt_AadharNo;
    }

    public String getEncrypt_benName() {
        return encrypt_benName;
    }

    public void setEncrypt_benName(String encrypt_benName) {
        this.encrypt_benName = encrypt_benName;
    }

    public String getISVideoUploadedElegible() {
        return ISVideoUploadedElegible;
    }

    public void setISVideoUploadedElegible(String ISVideoUploadedElegible) {
        this.ISVideoUploadedElegible = ISVideoUploadedElegible;
    }

    public String getIsUpdateEligibleAccount() {
        return IsUpdateEligibleAccount;
    }

    public void setIsUpdateEligibleAccount(String isUpdateEligibleAccount) {
        IsUpdateEligibleAccount = isUpdateEligibleAccount;
    }

    public String getDate_Of_Birth() {
        return Date_Of_Birth;
    }

    public void setDate_Of_Birth(String date_Of_Birth) {
        Date_Of_Birth = date_Of_Birth;
    }

    public String getOther_Pan_Name() {
        return Other_Pan_Name;
    }

    public void setOther_Pan_Name(String other_Pan_Name) {
        Other_Pan_Name = other_Pan_Name;
    }

    public String getReasonOfRejectionDst() {
        return ReasonOfRejectionDst;
    }

    public void setReasonOfRejectionDst(String reasonOfRejectionDst) {
        ReasonOfRejectionDst = reasonOfRejectionDst;
    }

    public String getReasonOfRejectionPfm() {
        return ReasonOfRejectionPfm;
    }

    public void setReasonOfRejectionPfm(String reasonOfRejectionPfm) {
        ReasonOfRejectionPfm = reasonOfRejectionPfm;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
