package com.util;

import org.json.simple.JSONObject;

public class SLUtil 
{
	public int registerUser(String username,String password,String email)
	{
		//code for inserting a user into the database
		return 0;	
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject login(String username,String password)
	{
		JSONObject response=new JSONObject();
		//actual code for login will come here
		if(username.equalsIgnoreCase("abc") && password.equalsIgnoreCase("123"))
		{
			response.put("status", "1");
			response.put("usertype", "admin");
			response.put("token", "123");
		}
		else
		{
			response.put("status", "0");
		}	
		return response;
	}
}
