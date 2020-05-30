package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class BenFamilyMember implements KvmSerializable, Serializable {

    private int id = 0;

    private String memberId = "";
    private String BenificiaryId = "";
    private String BenificiaryName = "";
    private String benTypeId = "";
    private String benTypeName = "";
    private String gendarId = "";
    private String gendarName = "";
    private String statusId = "";
    private String statusName = "";
    private String ageId = "";
    private String ageName = "";
    private String benMemeberName = "";
    private String isUpdated = "";
    private String entryDate = "";

    public BenFamilyMember() {
    }

    public BenFamilyMember(SoapObject obj) {
        this.memberId = obj.getProperty("a_id").toString();
        this.BenificiaryId = obj.getProperty("BenDetails_Slno").toString();
        this.benTypeId = obj.getProperty("TypeId").toString();
        this.benMemeberName = obj.getProperty("benName").toString();
        this.gendarId = obj.getProperty("gender").toString();
        this.ageId = obj.getProperty("Age").toString();
        this.statusId = obj.getProperty("Active").toString();
    }

    public String getBenificiaryName() {
        return BenificiaryName;
    }

    public void setBenificiaryName(String benificiaryName) {
        BenificiaryName = benificiaryName;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBenificiaryId() {
        return BenificiaryId;
    }

    public void setBenificiaryId(String benificiaryId) {
        BenificiaryId = benificiaryId;
    }

    public String getBenTypeId() {
        return benTypeId;
    }

    public void setBenTypeId(String benTypeId) {
        this.benTypeId = benTypeId;
    }

    public String getBenTypeName() {
        return benTypeName;
    }

    public void setBenTypeName(String benTypeName) {
        this.benTypeName = benTypeName;
    }

    public String getGendarId() {
        return gendarId;
    }

    public void setGendarId(String gendarId) {
        this.gendarId = gendarId;
    }

    public String getGendarName() {
        return gendarName;
    }

    public void setGendarName(String gendarName) {
        this.gendarName = gendarName;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getAgeId() {
        return ageId;
    }

    public void setAgeId(String ageId) {
        this.ageId = ageId;
    }

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }

    public String getBenMemeberName() {
        return benMemeberName;
    }

    public void setBenMemeberName(String benMemeberName) {
        this.benMemeberName = benMemeberName;
    }

    public String getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
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
