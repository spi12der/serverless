package com.lifecycle;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Connection 
{
	public Session connect(String user,String host,int port,String password) 
	{
		Session session=null;
		try 
		{
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, port);
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
