package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

/**
 * Created by nicsi on 12-Jul-17.
 */

public class District implements KvmSerializable {
    public static Class<District> District_CLASS = District.class;
    int _id;
    String DistrictCode, StateCode, DistrictName;

    // Empty constructor
    public District() {
    }

    // constructor
    public District(SoapObject obj) {


        this.DistrictCode = obj.getProperty("Dist_CODE").toString();
        this.DistrictName = obj.getProperty("Dist_NAME").toString();
        this.StateCode = obj.getProperty("STATECODE").toString();


    }


    @Override
    public int getPropertyCount() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public Object getProperty(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setProperty(int arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }
}
