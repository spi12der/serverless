package com.util;

import org.json.simple.JSONObject;

public class SLUtil 
{
	@SuppressWarnings("unchecked")
	public int registerUser(String username,String password,String email) throws Exception
	{
		JSONObject message=new JSONObject();
		message.put("db_name", "ias");
		message.put("tbl_name", "login");
		message.put("type", "insert");
		message.put("service_name", "dataservice");
		RequestUtil obj=new RequestUtil();
		JSONObject response=obj.process_request(message);
		return (int)response.get("status");	
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject login(String username,String password) throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject message=new JSONObject();
		message.put("username", username);
		message.put("password", password);
		message.put("type", "sd_login");
		message.put("service_name", "security");
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject logout(String token) throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject message=new JSONObject();
		message.put("token", token);
		message.put("type", "logout");
		message.put("service_name", "login");
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject logs() throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject message=new JSONObject();
		message.put("service_name", "logging");
		message.put("request_id", "1");
		return obj.process_request(message);
	}
}
