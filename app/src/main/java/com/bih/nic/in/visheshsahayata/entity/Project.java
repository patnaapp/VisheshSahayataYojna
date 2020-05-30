package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Project implements KvmSerializable {

    public static Class<Project> Project_CLASS = Project.class;
    int _id;
    String ProjCode, ProjName, DistCode, BlockCode;

    public Project() {
    }

    // constructor
    public Project(SoapObject obj) {
        this.ProjCode = obj.getProperty("ProjCode").toString();
        this.ProjName = obj.getProperty("ProjName").toString();
        this.DistCode = obj.getProperty("DistCode").toString();
        this.BlockCode = obj.getProperty("BlockCode").toString();
    }

    public static Class<Project> getProject_CLASS() {
        return Project_CLASS;
    }

    public static void setProject_CLASS(Class<Project> project_CLASS) {
        Project_CLASS = project_CLASS;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getProjCode() {
        return ProjCode;
    }

    public void setProjCode(String projCode) {
        ProjCode = projCode;
    }

    public String getProjName() {
        return ProjName;
    }

    public void setProjName(String projName) {
        ProjName = projName;
    }

    public String getDistCode() {
        return DistCode;
    }

    public void setDistCode(String distCode) {
        DistCode = distCode;
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
