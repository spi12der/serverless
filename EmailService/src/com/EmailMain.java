package com;

import org.json.simple.JSONObject;

import com.Email;
import com.message.Message;

public class EmailMain {
	static Message messageObject;
	public static void main(String[] args) 
	{
		EmailMain obj = new EmailMain();
		messageObject = new Message(args[0],args[1],args[2],args[3]);
		messageObject.recieveMessage();
		obj.processRequest();
	}
	public void processRequest()
	{
		while(true)
		{
			JSONObject message=Message.messageQueue.poll();
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
			}
		}
	}
	@SuppressWarnings("unchecked")
	public JSONObject parseMessage(JSONObject m)
	{
		JSONObject response=null;
		JSONObject message = (JSONObject)m.get("parameters");
		Email e = new Email();
		String type=(String)message.get("type");
		switch(type)
		{
			case "send":	response = e.SendEmail(message);
									break;
			
		}
		response.put("queue", "gateway");
		return response;
	}
}
