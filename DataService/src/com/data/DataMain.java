package com.data;

import org.json.simple.JSONObject;
import com.message.Message;

public class DataMain {
	static Message messageObject;
	public static void main(String[] args) 
	{
		DataMain obj = new DataMain();
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
		DataService ds = new DataService();
		String type=(String)message.get("type");
		switch(type)
		{
			case "create":	response = ds.createTable(message);
									break;
			case "insert":	response = ds.insertRecord(message);
									break;
			case "select":	response = ds.getRecords(message);
									break;
		}
		response.put("queue", "gateway");
		response.put("request_id", (String)m.get("request_id"));
		return response;
	}
}
