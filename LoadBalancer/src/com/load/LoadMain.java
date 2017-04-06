package com.load;

import org.json.simple.JSONObject;

import com.message.Message;

public class LoadMain 
{
	public static void main(String[] args) 
	{
		new LoadMain().monitorRoutingTable();
		Message mObj=new Message();
		mObj.recieveMessage();	
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
			case "update_cpu":		UpdateTable uObj=new UpdateTable();
									uObj.updateCpu(message);
									break;
			case "update_service":	UpdateTable uOb=new UpdateTable();
									uOb.updateService(message);
									break;
			case "service_request":	ServiceRequest sObj=new ServiceRequest();
									response=sObj.processRequest(message);
									break;
			case "update_server":	UpdateTable u=new UpdateTable();
									u.updateServer(message);
									break;						
		}
		return response;
	}
}
