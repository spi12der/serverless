package com.log;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.message.Message;

public class LogMain 
{
	static Message messageObject;
	static Logger logger = Logger.getLogger(LogMain.class);
    
	public static void main(String[] args)
    {
    	DOMConfigurator.configure("log4j-config.xml");
        messageObject=new Message(args[0],args[1],args[2],args[3]);
        messageObject.recieveMessage();
        processRequest();
    }
	
	@SuppressWarnings("unchecked")
	public static void processRequest()
	{
		while(true)
		{
			JSONObject response=Message.messageQueue.poll();
			if(response!=null)
			{
				if(response.containsKey("logType"))
					LogMain.logMessage((String)response.get("logType"), (String)response.get("message"));
				else
				{
					JSONObject message=new JSONObject();
					message.put("queue", "gateway");
					message.put("status", 1);
					message.put("details", new LogMain().getLogs());
					messageObject.sendMessage(message);
				}	
			}	
		}	
	}
	
	public static void logMessage(String msgType,String message)
	{
		switch(msgType)
		{
			case "INFO": 	logger.info(message);
							break;
			case "ERROR":	logger.error(message);
							break;
			case "DEBUG":	logger.debug(message);
							break;				
			case "FATAL":	logger.fatal(message);
							break;				
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getLogObject(String line)
	{
		JSONObject logObject=new JSONObject();
		logObject.put("debug", line.split(" ")[2]);
		int index=line.indexOf("-")+1;
		line=line.substring(index);
		index=line.indexOf(":");
		String moduleName=line.substring(0, index).trim();
		logObject.put("module", moduleName);
		String log=line.substring(index+1).trim();
		logObject.put("log", log);
		return logObject;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getLogs()
	{
		JSONArray arr=new JSONArray();
		try (BufferedReader br = new BufferedReader(new FileReader("platform.log"))) 
		{
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		    	arr.add(getLogObject(line));
		    }
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println(arr);
		return arr;
	}
}
