package com.lifecycle;

import org.json.simple.JSONObject;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Connection 
{
	public Session connect(JSONObject details) 
	{
		String user=(String)details.get("username");
		String host=(String)details.get("ip");
		String password=(String)details.get("password");
		Session session=null;
		try 
		{
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, 22);
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.setPassword(password);
	        session.connect();
	        
		} 
		catch (JSchException e) 
		{
			e.printStackTrace();
		}
		return session;
	}
	
	public void disconnect(Session session) 
	{
		session.disconnect();
	}
}
