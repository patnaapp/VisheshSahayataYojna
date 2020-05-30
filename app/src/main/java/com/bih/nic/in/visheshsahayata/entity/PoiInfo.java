package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class PoiInfo implements KvmSerializable,Serializable {

    private static final long serialVersionUID = 1L;

    public static Class<PoiInfo> PoiInfo_CLASS = PoiInfo.class;




    String slno;

    String statecode;
    private String stateName;
    String districtcode;
    String districtName;
    String blockcode;
    String blockName;
    String poiName;
    String details;
    String remarks;
    String Image;
    String Latitude="";
    String Longitude="";
    String gps_date="";
    String entry_Date="";
    String Upload_by="";
    String Upload_date;
    String Mobile_Number;
    String Thana_Name;
    String Designation;
    String POI;
    String Poi_Type;
    String Poi_SubType;
    String Poi_SubType_Name;
    byte[] photo;
    byte[] photo1;
    byte[] photo2;



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

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictcode() {
        return districtcode;
    }

    public void setDistrictcode(String districtcode) {
        this.districtcode = districtcode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getBlockcode() {
        return blockcode;
    }

    public void setBlockcode(String blockcode) {
        this.blockcode = blockcode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public String getGps_date() {
        return gps_date;
    }

    public void setGps_date(String gps_date) {
        this.gps_date = gps_date;
    }

    public String getEntry_Date() {
        return entry_Date;
    }

    public void setEntry_Date(String entry_Date) {
        this.entry_Date = entry_Date;
    }

    public String getUpload_by() {
        return Upload_by;
    }

    public void setUpload_by(String upload_by) {
        Upload_by = upload_by;
    }

    public String getUpload_date() {
        return Upload_date;
    }

    public void setUpload_date(String upload_date) {
        Upload_date = upload_date;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getMobile_Number() {
        return Mobile_Number;
    }

    public void setMobile_Number(String mobile_Number) {
        Mobile_Number = mobile_Number;
    }

    public String getThana_Name() {
        return Thana_Name;
    }

    public void setThana_Name(String thana_Name) {
        Thana_Name = thana_Name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getPoi_Type() {
        return Poi_Type;
    }

    public void setPoi_Type(String poi_Type) {
        Poi_Type = poi_Type;
    }

    public String getPoi_SubType() {
        return Poi_SubType;
    }

    public void setPoi_SubType(String poi_SubType) {
        Poi_SubType = poi_SubType;
    }

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public byte[] getPhoto2() {
        return photo2;
    }

    public void setPhoto2(byte[] photo2) {
        this.photo2 = photo2;
    }

    public String getPoi_SubType_Name() {
        return Poi_SubType_Name;
    }

    public void setPoi_SubType_Name(String poi_SubType_Name) {
        Poi_SubType_Name = poi_SubType_Name;
    }

    public String getPOI() {
        return POI;
    }

    public void setPOI(String POI) {
        this.POI = POI;
    }
}
