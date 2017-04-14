package com.dev;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.message.Message;

public class DevMain 
{
	static Message messageObject;
	static String jarPath;
	
	
	/*public static void main(String[] args) 
	{
		jarPath=args[4];
		messageObject = new Message(args[0],args[1],args[2],args[3]);
		messageObject.recieveMessage();
	}*/
	
	public void processRequest(JSONObject message)
	{
		if(message!=null)
		{
			new Thread(new Runnable() 
		    {
		         @SuppressWarnings("unchecked")
				public void run() 
		         {
		                JSONObject response;
						try 
						{
							response = parseMessage(message);
							if(response!=null)
				            {
								messageObject.sendMessage(response);
				            }
							else
							{
								throw new Exception();
							}
						} 
						catch (Exception e) 
						{
							response=new JSONObject();
							response.put("status", "0");
							response.put("queue", "gateway");
							e.printStackTrace();
						}
			            
		         }
		    }).start();
			
		}	  
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		jarPath=args[0];
		JSONObject message=new JSONObject();
		message.put("queue", "ClimateService");
		JSONObject parameters=new JSONObject();
		parameters.put("inCelsius", "true");
		parameters.put("type", "getWeather");
		message.put("parameters", parameters);
		DevMain obj=new DevMain();
		obj.parseMessage(message);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject parseMessage(JSONObject message) throws Exception
	{
		JSONObject parameters=(JSONObject)message.get("parameters");
		List<Object> obj=new ArrayList<Object>();
		URL url=new URL("file:"+jarPath);
		String methodName=new String();
		URLClassLoader cloader = new URLClassLoader (new URL[] {url});
		for (Object key : parameters.keySet()) 
		{
			if(((String)key).equalsIgnoreCase("type"))
			{
				methodName=(String)parameters.get(key);
			}
			else
			{
				obj.add(parameters.get(key));
			}	
		}		
		Class<?> params[] = new Class[obj.size()];
        for (int i = 0; i < obj.size(); i++) 
        {
        	Object temp=obj.get(i);
            if (temp instanceof Integer) 
            	params[i] = int.class; 
            else if (temp instanceof String) 
            	params[i] = String.class;
            else if (temp instanceof JSONObject) 
            	params[i] = JSONObject.class; 
        }
        Class<?> cls = Class.forName ((String)message.get("queue"), true, cloader);
        Object _instance = cls.newInstance();
        Method myMethod = cls.getDeclaredMethod(methodName, params);
        String result=(String) myMethod.invoke(_instance, obj);
        JSONObject response=new JSONObject();
        response.put("message", result);
		return response;
	}
}
