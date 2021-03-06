package com.security;

import org.json.simple.JSONObject;
import com.message.Message;
import com.security.Security;

public class SecurityMain {
	
static Message messageObject;
	
	public static void main(String[] args) 
	{
		//SecurityMain obj=new SecurityMain();
		messageObject=new Message(args[0],args[1],args[2],args[3]);
		messageObject.recieveMessage();	
		//obj.processRequest();
	}
	
	public void processRequest(JSONObject message)
	{
		if(message!=null)
		{
			new Thread(new Runnable() 
		    {
		         public void run() 
		         {
		              JSONObject response=parseMessage(message);
		              if(response!=null)
		              {
		            	  messageObject.sendMessage(response);
		              }  
		         }
		    }).start();
			    /*JSONObject response=parseMessage(message);
		        if(response!=null)
		        {
		      	  messageObject.sendMessage(response);
		        }*/
		}  
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject parseMessage(JSONObject message)
	{
		JSONObject response=null;
		System.out.println("m: " + message);
		try
		{
			JSONObject parameters = (JSONObject) message.get("parameters");
			String type = (String) parameters.get("type");
			
			switch(type)
			{
				case "sd_login":		Security sc1 = new Security();
										response = sc1.Sd_logIn(parameters,messageObject);
										break;
				case "authorization":	Security sc2 = new Security();
										response = sc2.Authorization(parameters,messageObject);
										break;
										
				case "logout":			Security sc3 = new Security();
										response = sc3.logout(parameters);
										break;
				case "userplz":			Security sc4 = new Security();
										response = sc4.logout(parameters);
										break;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			response.put("status", "0");
		}
		response.put("queue", "gateway");
		response.put("request_id", (String)message.get("request_id"));
		return response;
	}
	

}
