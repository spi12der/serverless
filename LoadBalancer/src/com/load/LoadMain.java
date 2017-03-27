package com.load;

import org.json.simple.JSONObject;

import com.message.Message;

public class LoadMain 
{
	public static void main(String[] args) 
	{
		LoadMain obj=new LoadMain();
		obj.monitorRoutingTable();
		while(true)
		{
			Message mObj=new Message();
			obj.processRequest(mObj.recieveMessage());
		}	
	}
	
	/**
	 * Method for monitoring routing table in another thread
	 * @param message
	 */
	public void monitorRoutingTable()
	{
		new Thread(new Runnable() 
	    {
	         public void run() 
	         {
	              MonitorTable obj=new MonitorTable();
	              obj.checkTable();
	         }
	    }).start();
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
			case "updatetable":		UpdateTable uObj=new UpdateTable();
									uObj.updateRoutingInfo(message);
									break;
			case "servicerequest":	ServiceRequest sObj=new ServiceRequest();
									response=sObj.processRequest(message);
									break;						
		}
		return response;
	}
}
