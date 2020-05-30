package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class ServerDate implements KvmSerializable {
    public static Class<State> District_CLASS = State.class;
    int _id;
    String ServerDate;

    // Empty constructor
    public ServerDate() {
    }

    // constructor
    public ServerDate(SoapObject obj) {


        this.ServerDate = obj.getProperty("Datetimecurrent").toString();



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

    public String getServerDate() {
        return ServerDate;
    }

    public void setServerDate(String serverDate) {
        ServerDate = serverDate;
    }
}
