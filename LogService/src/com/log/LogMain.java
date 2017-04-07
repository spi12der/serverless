package com.log;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
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
	
	public static void processRequest()
	{
		while(true)
		{
			JSONObject response=Message.messageQueue.poll();
			if(response!=null)
				LogMain.logMessage((String)response.get("logType"), (String)response.get("message"));
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
}
