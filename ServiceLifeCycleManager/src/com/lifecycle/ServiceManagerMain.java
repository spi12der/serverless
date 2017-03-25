package com.lifecycle;

import org.json.simple.JSONObject;

import com.message.Message;

public class ServiceManagerMain 
{
	public static void main(String[] args) 
	{
		ServiceManagerMain obj=new ServiceManagerMain();
		while(true)
		{
			Message mObj=new Message();
			obj.processRequest(mObj.recieveMessage());
		}	
	}
	
	/**
	 * Method to process Request in separate thread
	 * @param message
	 */
	public void processRequest(JSONObject message)
	{
	    new Thread(new Runnable() 
	    {
	         public void run() 
	         {
	              JSONObject response=parseMessage(message);
	              if(response!=null)
	              {
	            	  Message obj=new Message();
	            	  obj.sendMessage(response);
	              }  
	         }
	    }).start();  
	}
	
	/**
	 * Method to parse the message and return response message for a request
	 */
	public JSONObject parseMessage(JSONObject message)
	{
		JSONObject response=null;
		String type=(String)message.get("type");
		switch(type)
		{
			case "createservice" :  response=createService(message);
									break;
			case "stopservice" :    response=stopService(message);
									break;
		}
		return response;
	}
	
	public JSONObject createService(JSONObject message)
	{
		if(message.containsKey("server"))
		{
			//here server IP are already provided by the load balancer
			//fetch other details
		}
		else
		{
			//fetch the details of available from server lifecycle manager
		}
		//copy the jar file to a new server (SCP)
		//make a new docker instance and execute the jar
		//send the request message to the service message
		return null;
	}
	
	public JSONObject stopService(JSONObject message)
	{
		//check for the service to stop
		//stop the virtual machines and remove the jars
		//if no other VM is running on that machine update the servers.xml
		return null;
	}
}
