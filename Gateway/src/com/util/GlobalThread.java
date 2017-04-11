package com.util;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.message.Message;

public class GlobalThread 
{
	private static GlobalThread obj;
	
	private GlobalThread()
	{
		
	}
	
	public static void getInstance()
	{
        if(obj == null)
        {
            obj = new GlobalThread();
            obj.processResponse();
        }
    }
	
	private void processResponse()
	{
		new Thread(new Runnable() 
	    {
	         public void run() 
	         {
		        	RequestUtil.requestThMap=new HashMap<String,Thread>();
		     		RequestUtil.responseMap=new HashMap<String,JSONObject>();
		     		RequestUtil.messageObject=new Message("localhost", "gateway", "GATEWAY", "localhost:8114");
		     		while(true)
		     		{
		     			JSONObject json=Message.messageQueue.poll();
		     			if(json!=null)
		     			{
		     				String req=(String)json.get("request_id");
		     				RequestUtil.responseMap.put(req, json);
		     		        Thread resTh=RequestUtil.requestThMap.get(req);
		     		        resTh.notify();
		     			}	
		     		}  
	         }	
	    }).start();
	}
}
