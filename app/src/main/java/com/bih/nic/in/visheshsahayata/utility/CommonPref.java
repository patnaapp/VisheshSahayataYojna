package com.bih.nic.in.visheshsahayata.utility;


import android.content.Context;
import android.content.SharedPreferences;

import com.bih.nic.in.visheshsahayata.entity.BenificiaryDetail;
import com.bih.nic.in.visheshsahayata.entity.UserDetails;


public class CommonPref {

	static Context context;

	CommonPref() {

	}
	CommonPref(Context context) {
		CommonPref.context = context;
	}


	public static void setUserDetails(Context context, UserDetails userInfo) {

		String key = "_USER_DETAILS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();

		editor.putString("UserID", userInfo.get_UserID());
		editor.putString("UserPassword", userInfo.get_UserPassword());
		editor.putString("UserName", userInfo.get_UserName());
		editor.putString("IsLock", userInfo.get_IsLock());
		editor.putString("UserRole", userInfo.get_UserRole());
		editor.putString("UserRoleDesc", userInfo.get_UserRoleDesc());
		editor.putString("StateCode", userInfo.get_StateCode());
		editor.putString("StateName", userInfo.get_StateName());
		editor.putString("Dist_Code", userInfo.get_DistrictCode());
		editor.putString("District_Name", userInfo.get_DistrictName());
		editor.putString("IMEI", userInfo.get_IMEI());
		editor.putString("Logedin", "yes");


		editor.commit();

	}


	public static UserDetails getUserDetails(Context context) {

		String key = "_USER_DETAILS";
		UserDetails userInfo = new UserDetails();
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		userInfo.set_UserID(prefs.getString("UserID", ""));
		userInfo.set_UserPassword(prefs.getString("UserPassword", ""));
		userInfo.set_UserName(prefs.getString("UserName", ""));
		userInfo.set_IsLock(prefs.getString("IsLock", ""));
		userInfo.set_UserRole(prefs.getString("UserRole", ""));
		userInfo.set_UserRoleDesc(prefs.getString("UserRoleDesc", ""));
		userInfo.set_StateCode(prefs.getString("StateCode", ""));
		userInfo.set_StateName(prefs.getString("StateName", ""));
		userInfo.set_DistrictCode(prefs.getString("Dist_Code", ""));
		userInfo.set_DistrictName(prefs.getString("District_Name", ""));
		userInfo.set_IMEI(prefs.getString("IMEI", ""));
		userInfo.set_Login(prefs.getString("Logedin", ""));



		return userInfo;
	}


	/*public static void setUserDetails(Context context, DataDetails userInfo) {

		String key = "_USER_DETAILS";
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("UserName", userInfo.get_name());
		editor.putString("MobNo", userInfo.getMobno());
		editor.putString("Email", userInfo.getEmail());

		editor.commit();

	}

	public static DataDetails getUserDetails(Context context) {
		String key = "_USER_DETAILS";
		DataDetails userInfo = new DataDetails();
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		userInfo.set_name(prefs.getString("UserName", ""));
		userInfo.setMobno(prefs.getString("MobNo", ""));
		userInfo.setEmail(prefs.getString("Email", ""));

		return userInfo;
	}*/



	public static void setCheckUpdate(Context context, long dateTime) {

		String key = "_CheckUpdate";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();


		dateTime=dateTime+1*3600000;
		editor.putLong("LastVisitedDate", dateTime);

		editor.commit();

	}

	public static int getCheckUpdate(Context context) {

		String key = "_CheckUpdate";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		long a = prefs.getLong("LastVisitedDate", 0);


		if(System.currentTimeMillis()>a)
			return 1;
		else
			return 0;
	}
	public static void setUserDetails1(Context context, BenificiaryDetail userInfo) {

		String key = "_USER_DETAILS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();

//		editor.putString("UserID", userInfo.get_UserID());
//		editor.putString("UserPassword", userInfo.get_UserPassword());
//		editor.putString("UserName", userInfo.get_UserName());
//		editor.putString("IsLock", userInfo.get_IsLock());
//		editor.putString("UserRole", userInfo.get_UserRole());
//		editor.putString("UserRoleDesc", userInfo.get_UserRoleDesc());
//		editor.putString("StateCode", userInfo.get_StateCode());
//		editor.putString("StateName", userInfo.get_StateName());
		editor.putString("Dist_Code", userInfo.getDistCode());
		editor.putString("District_Name", userInfo.getDistrictName());
//		editor.putString("IMEI", userInfo.get_IMEI());
//		editor.putString("Logedin", "yes");


		editor.commit();

	}


	public static BenificiaryDetail getUserDetails1(Context context) {

		String key = "_USER_DETAILS";
		BenificiaryDetail userInfo = new BenificiaryDetail();
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
//		userInfo.set_UserID(prefs.getString("UserID", ""));
//		userInfo.set_UserPassword(prefs.getString("UserPassword", ""));
//		userInfo.set_UserName(prefs.getString("UserName", ""));
//		userInfo.set_IsLock(prefs.getString("IsLock", ""));
//		userInfo.set_UserRole(prefs.getString("UserRole", ""));
//		userInfo.set_UserRoleDesc(prefs.getString("UserRoleDesc", ""));
//		userInfo.set_StateCode(prefs.getString("StateCode", ""));
//		userInfo.set_StateName(prefs.getString("StateName", ""));
		userInfo.setDistCode(prefs.getString("Dist_Code", ""));
		userInfo.setDistrictName(prefs.getString("District_Name", ""));
//		userInfo.set_IMEI(prefs.getString("IMEI", ""));
//		userInfo.set_Login(prefs.getString("Logedin", ""));



		return userInfo;
	}

}
