package com.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.message.Message;

public class DataMain {
	public static void main(String[] args) 
	{
		Message mObj=new Message();
		mObj.recieveMessage();	
	}
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
		Message msg = new Message();
		response.put("ip", msg.getGatewayAddress());
		return response;
	}
}
