package com.load;

import org.json.simple.JSONObject;

import com.message.Message;


public class LoadMain 
{
	static Message messageObject;
	
	public static void main(String[] args) 
	{
		messageObject=new Message(args[0],args[1],args[2],args[3]);
		messageObject.recieveMessage();	
		LoadMain obj=new LoadMain();
		//obj.monitorRoutingTable();
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
	              obj.checkTable(messageObject);
	              
	         }
	    }).start();
	}
	
	/**
	 * Method to process Request in separate thread
	 * @param message
	 */
	public void processRequest(JSONObject message)
	{
		if(message!=null)
		{
			System.out.println("message recieved");
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
									uObj.updateCpu(message,messageObject);
									break;
			case "update_service":	UpdateTable uOb=new UpdateTable();
									uOb.updateService(message,messageObject);
									break;
			case "service_request":	ServiceRequest sObj=new ServiceRequest();
									response=sObj.processRequest(message,messageObject);
									break;
			case "update_server":	UpdateTable u=new UpdateTable();
									u.updateServer(message,messageObject);
									break;						
		}
		return response;
	}
}
