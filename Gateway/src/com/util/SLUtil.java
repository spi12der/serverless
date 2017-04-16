package com.util;

import org.json.simple.JSONObject;

public class SLUtil 
{
	@SuppressWarnings("unchecked")
	public String registerUser(String username,String password,String email) throws Exception
	{
		JSONObject details=new JSONObject();
		details.put("db_name", "admin");
		details.put("tbl_name", "sd_details");
		details.put("type", "insert");
		details.put("attributes", "username,password,email_id,user_type");
		details.put("values", username+","+password+","+email+",user");
		JSONObject message=new JSONObject();
		message.put("service_name", "dataservice");
		message.put("parameters", details);
		RequestUtil obj=new RequestUtil();
		JSONObject response=obj.process_request(message);
		return (String)response.get("status");	
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject login(String username,String password) throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject details=new JSONObject();
		details.put("username", username);
		details.put("password", password);
		details.put("type", "sd_login");
		JSONObject message=new JSONObject();
		message.put("service_name", "security");
		message.put("parameters", details);
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject logout(String token) throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject details=new JSONObject();
		details.put("token", token);
		details.put("type", "logout");
		JSONObject message=new JSONObject();
		message.put("service_name", "security");
		message.put("parameters", details);
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject logs() throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject message=new JSONObject();
		message.put("service_name", "logging");
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getServerDetails() throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject message=new JSONObject();
		message.put("service_name", "server_manager");
		JSONObject parameters=new JSONObject();
		parameters.put("type", "server_details");
		message.put("parameters", parameters);
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject addServer(String username,String password,String ip) throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject message=new JSONObject();
		message.put("service_name", "server_manager");
		JSONObject parameters=new JSONObject();
		parameters.put("type", "add_server");
		parameters.put("username", username);
		parameters.put("password", password);
		parameters.put("ip", ip);
		message.put("parameters", parameters);
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getVMInfo() throws Exception
	{
		RequestUtil obj=new RequestUtil();
		JSONObject parameter=new JSONObject();
		parameter.put("type", "vm_details");
		JSONObject message=new JSONObject();
		message.put("service_name", "vm_manager");
		message.put("parameters", parameter);
		return obj.process_request(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject deployService(String path,String serviceName) throws Exception
	{
		JSONObject details=new JSONObject();
		details.put("db_name", "admin");
		details.put("tbl_name", "serviceInfo");
		details.put("type", "insert");
		details.put("attributes", "service,path,type");
		details.put("values", serviceName+","+path+",dev");
		JSONObject message=new JSONObject();
		message.put("service_name", "dataservice");
		message.put("parameters", details);
		RequestUtil obj=new RequestUtil();
		return obj.process_request(message);
	}
}
