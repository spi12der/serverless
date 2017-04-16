package com.util;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.message.Message;

public class GlobalThread 
{
	private static GlobalThread obj;
	
	private GlobalThread()
	{
		RequestUtil.requestThMap=new HashMap<String,Thread>();
 		RequestUtil.responseMap=new HashMap<String,JSONObject>();
 		RequestUtil.messageObject=new Message("10.42.0.38", "gateway", "GATEWAY", "10.1.34.155:8114");
 		RequestUtil.messageObject.recieveMessage();
	}
	
	public static void getInstance()
	{
        if(obj == null)
        {
            obj = new GlobalThread();
        }
    }
	
	public static void processResponse(JSONObject json)
	{
		System.out.println("Message recieved");
		if(json!=null)
		{
			String req=(String)json.get("request_id");
			System.out.println("request_id "+req);
			RequestUtil.responseMap.put(req, json);
	        Thread resTh=RequestUtil.requestThMap.get(req);
	        if(resTh!=null)
	        {
	        	System.out.println("Notifying thread");
	        	synchronized (resTh) 
		        {
		        	resTh.notify();
		        }
	        }	
		}
		else
		{
			System.out.println("Empty json");
		}
	}
}
