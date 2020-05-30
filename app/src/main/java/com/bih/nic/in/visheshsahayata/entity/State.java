package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class State implements KvmSerializable {
    public static Class<State> District_CLASS = State.class;
    int _id;
    String StateCode, StateName, StateNameHn;

    // Empty constructor
    public State() {
    }

    // constructor
    public State(SoapObject obj) {


        this.StateCode = obj.getProperty("State_CODE").toString();
        this.StateName = obj.getProperty("State_NAME").toString();
        //this.StateNameHn = obj.getProperty("State_Code").toString();


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
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getStateNameHn() {
        return StateNameHn;
    }

    public void setStateNameHn(String stateNameHn) {
        StateNameHn = stateNameHn;
    }
}
