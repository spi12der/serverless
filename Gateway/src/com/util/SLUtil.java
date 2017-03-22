package com.util;

public class SLUtil 
{
	public int registerUser(String username,String password,String email)
	{
		//code for inserting a user into the database
		return 0;	
	}
	
	public int login(String username,String password)
	{
		//actual code for login will come here
		if(username.equalsIgnoreCase("abc") && password.equalsIgnoreCase("123"))
			return 1;
		return 0;
	}
}
