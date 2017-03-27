package com.log;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.message.Message;

public class LogMain 
{
	static Logger logger = Logger.getLogger(LogMain.class);
    
	public static void main(String[] args)
    {
    	DOMConfigurator.configure("log4j-config.xml");
        Message mObj=new Message();
        mObj.recieveMessage();
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
