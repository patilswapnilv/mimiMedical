package mimi.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import mimi.type.Weibo;

public class UserUtils {
	public static final String KEY_USERNAME = "email";
	public static final String KEY_PASSWORD = "password";
	public static final String KET_ACT = "act";
	public static final String LOGIN_METHOD = "doLoginMedical";
	public static final String SUCCED = "﻿OK";
	
	public static final String KEY_REGISTER_USERNAME = "uname";
	public static final String KEY_REGISTER_PASSWORD = "password";
	public static final String KEY_RE_PASSWORD = "re_password";
	public static final String KEY_REGISTER_EMAIL = "email";
	public static final String KET_REGISTER = "act";
	public static final String REGISTER_METHOD = "doRegisterMedical";
	
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	public static ArrayList<Weibo> parserWeibo(String str){
		if(str != null) {
			Log.e("asd", "str = " + str);
			try {
				JSONArray jsonArray = new JSONArray(str);
				Log.e("asd", "jsonArray = " + jsonArray.length());
				if(jsonArray.length() > 0) {
					ArrayList<Weibo> weibos = new ArrayList<Weibo>();
					for (int i = 0; i < jsonArray.length(); i++) {
						Weibo weibo = new Weibo();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						weibo.weibo_id = jsonObject.getInt("weibo_id");
						weibo.uid = jsonObject.getInt("uid");
						weibo.content = jsonObject.getString("content");
						weibo.ctime = jsonObject.getString("ctime");
						weibo.from = jsonObject.getInt("from");
						weibo.comment = jsonObject.getInt("comment");
						weibo.transpond_id = jsonObject.getInt("transpond_id");
						weibo.transpond = jsonObject.getInt("transpond");
						weibo.type = jsonObject.getInt("type");
						weibo.type_data = jsonObject.getString("type_data");
						weibo.from_data = jsonObject.getBoolean("from_data");
						weibo.isdel = jsonObject.getInt("isdel");
						weibo.uname = jsonObject.getString("uname");
						weibo.face = jsonObject.getString("face");
						weibo.transpond_data = jsonObject.getInt("transpond_data");
						weibo.timestamp = jsonObject.getInt("timestamp");
						weibo.favorited = jsonObject.getInt("favorited");
						weibos.add(weibo);
					}
					return weibos;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
