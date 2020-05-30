package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class panchayt implements KvmSerializable {

    public static Class<BlockWeb1> PanchayatWeb_CLASSSS= BlockWeb1.class;


    private String BlockCode = "";
    private String PanchCode="";
    private String PanchyatName="";
    private String PanchyatName_HN="";
    private String areaTpte="";

    public panchayt() {

    }

    public panchayt(SoapObject sobj) {
        this.PanchCode=sobj.getProperty("Panchayat_CODE").toString();
        this.PanchyatName=sobj.getProperty("Panchayat_NAME").toString();
        this.PanchyatName_HN=sobj.getProperty("Panchayat_NAME_HN").toString();
        this.areaTpte=sobj.getProperty("AreaType").toString();



    }

    public String getAreaTpte() {
        return areaTpte;
    }

    public void setAreaTpte(String areaTpte) {
        this.areaTpte = areaTpte;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getPanchCode() {
        return PanchCode;
    }

    public void setPanchCode(String panchCode) {
        PanchCode = panchCode;
    }

    public String getPanchyatName() {
        return PanchyatName;
    }

    public void setPanchyatName(String panchyatName) {
        PanchyatName = panchyatName;
    }

    public String getPanchyatName_HN() {
        return PanchyatName_HN;
    }

    public void setPanchyatName_HN(String panchyatName_HN) {
        PanchyatName_HN = panchyatName_HN;
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
