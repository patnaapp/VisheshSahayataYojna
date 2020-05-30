package com.bih.nic.in.visheshsahayata.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class BlockWeb1 implements KvmSerializable {

	public static Class<BlockWeb1> BlockWeb_CLASS= BlockWeb1.class;

	private String code;
	private String value;
	private String districtcode;
	private String DistCode = "";
	private String BlockCode = "";
	private String BlockName = "";
	private String BlockName_HN = "";
	private String PacsBankId = "";
	private String UserId;




	public BlockWeb1() {

	}

	public BlockWeb1(SoapObject sobj) {
		this.BlockCode=sobj.getProperty("Block_CODE").toString();
		this.BlockName=sobj.getProperty("Block_NAME").toString();
		this.BlockName_HN=sobj.getProperty("Block_NAME_HN").toString();



	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDistrictcode() {
		return districtcode;
	}

	public void setDistrictcode(String districtcode) {
		this.districtcode = districtcode;
	}

	public String getDistCode() {
		return DistCode;
	}

	public void setDistCode(String distCode) {
		DistCode = distCode;
	}

	public String getBlockCode() {
		return BlockCode;
	}

	public void setBlockCode(String blockCode) {
		BlockCode = blockCode;
	}

	public String getBlockName() {
		return BlockName;
	}

	public void setBlockName(String blockName) {
		BlockName = blockName;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getBlockName_HN() {
		return BlockName_HN;
	}

	public void setBlockName_HN(String blockName_HN) {
		BlockName_HN = blockName_HN;
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
