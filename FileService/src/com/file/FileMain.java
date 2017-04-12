package com.file;

import org.json.simple.JSONObject;

import com.file.FileService;
import com.file.FileMain;
import com.message.Message;

public class FileMain {
	
		static Message messageObject;
	
		public static void main(String[] args) 
		{
			FileMain obj=new FileMain();
			messageObject=new Message(args[0],args[1],args[2],args[3]);
			messageObject.recieveMessage();	
			obj.processRequest();
		}
	
		
		
		public void processRequest()
		{
			while(true)
			{
				JSONObject message=Message.messageQueue.poll();
				if(message!=null)
				{
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
		}
		
		
		@SuppressWarnings("unchecked")
		public JSONObject parseMessage(JSONObject m)
		{
			JSONObject response = null;
			JSONObject message = (JSONObject)m.get("parameters");
			FileService fs = new FileService();
			String type=(String)message.get("type");
			switch(type)
			{
				case "createUserDir":	response = fs.createUserDir(message);
										break;
				case "createDir":		response = fs.createDir(message);
										break;
				case "createFile":		response = fs.createFile(message);
										break;
				case "updateFile":		response = fs.updateFile(message);
										break;
				case "deleteFile":		response = fs.deleteFile(message);
										break;
				case "readFile":		response = fs.readFile(message);
										break;
										
			}
			response.put("queue", "gateway");
			response.put("request_id", (String)m.get("request_id"));
			return response;
		}
	}

}