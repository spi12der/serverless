package com;

import org.json.simple.JSONObject;

import com.Email;
import com.message.Message;

public class EmailMain {
	public void processRequest(JSONObject message)
	{
	    new Thread(new Runnable() 
	    {
	         public void run() 
	         {
	              JSONObject response = parseMessage(message);
	              if(response!=null)
	              {
	            	  Message obj=new Message();
	            	  obj.sendMessage(response);
	              }  
	         }
	    }).start();  
	}
	@SuppressWarnings("unchecked")
	public JSONObject parseMessage(JSONObject message)
	{
		JSONObject response=null;
		Email e = new Email();
		String type=(String)message.get("type");
		switch(type)
		{
			case "send":	response = e.SendEmail(message);
									break;
			
		}
		response.put("queue", "gateway");
		Message m = new Message();
		response.put("ip", m.getGatewayAddress());
		return response;
	}
}
