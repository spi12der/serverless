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
 		RequestUtil.messageObject=new Message("10.1.34.155", "gateway", "GATEWAY", "10.1.34.155:8114");
 		System.out.println("sfsdfsdf");
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
		if(json!=null)
		{
			System.out.println("Message recieved");
			String req=(String)json.get("request_id");
			RequestUtil.responseMap.put(req, json);
	        Thread resTh=RequestUtil.requestThMap.get(req);
	        if(resTh!=null)
	        {
	        	synchronized (resTh) 
		        {
		        	resTh.notify();
		        }
	        }	
		}
	}
}
