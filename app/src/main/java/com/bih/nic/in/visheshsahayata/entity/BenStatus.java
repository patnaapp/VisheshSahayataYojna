package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class BenStatus implements KvmSerializable, Serializable {
    public static Class<BenStatus> VIEWENTRY_CLASS = BenStatus.class;

    private String status;
    private String remarks;


    public BenStatus(){

    }

    public BenStatus(SoapObject obj) {


        if(obj.getProperty("status").toString().equalsIgnoreCase("anyType{}")){
            this.setStatus("");
        }else{
            this.setStatus(obj.getProperty("status").toString());
        }
        if(obj.getProperty("remarks").toString().equalsIgnoreCase("anyType{}")){
            this.setStatus("");
        }else{
            this.setRemarks(obj.getProperty("remarks").toString());
        }


    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
