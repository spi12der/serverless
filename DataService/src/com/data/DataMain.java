package com.data;

import org.json.simple.JSONObject;
import com.message.Message;

public class DataMain {
	static Message messageObject;
	public static void main(String[] args) 
	{
		messageObject = new Message(args[0],args[1],args[2],args[3]);
		messageObject.recieveMessage();
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
			
		}	  
	}
	@SuppressWarnings("unchecked")
	public JSONObject parseMessage(JSONObject m)
	{
		JSONObject response=null;
		System.out.println(m.toString());
		JSONObject message = (JSONObject)m.get("parameters");
		DataService ds = new DataService();
		//messageObject.logMessage("ERROR", "request ");
		String type=(String)message.get("type");
		switch(type)
		{
			case "create":	response = ds.createTable(message);
									break;
			case "insert":	response = ds.insertRecord(message);
									break;
			case "select":	response = ds.getRecords(message);
									break;
			case "sdlogin": response = ds.SDLoginCheck(message);
									break;
		}
		response.put("queue", "gateway");
		response.put("request_id", (String)m.get("request_id"));
		 messageObject.logMessage("INFO", "Data Service has been served with Exit Status :" + response.get("status"));
		System.out.println(response.toString());
		return response;
	}
}
