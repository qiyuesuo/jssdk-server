package com.qiyuesuo.proxy.json;

import java.util.List;
import java.util.Map;

/**
 * Json处理工具类
 */
public class JSONUtils {
	
	public static String toJson(Object object){
		JSONObject jsonObject = new JSONObject(object);
		return jsonObject.toString();
	}
	
	public static String toJson(Map<?, ?> map){
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}
	
	public static String toJson(List<?> list){
		JSONArray json = new JSONArray();
		
		for(Object object:list){
			JSONObject jsonObject = new JSONObject(object);
			json.put(jsonObject);
		}
		
		return json.toString();
	}
	
}
