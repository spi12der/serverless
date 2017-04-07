package com.lifecycle;

import org.json.simple.JSONObject;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
/*import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;*/
import com.message.Message;

public class ServiceManagerMain 
{
	static Message messageObject;
	
	public static void main(String[] args) throws Exception 
	{
		messageObject=new Message(args[0],args[1],args[2],args[3]);
		messageObject.recieveMessage();
		new ServiceManagerMain().processRequest();
	}
	
	/**
	 * Method to process Request in separate thread
	 * @param message
	 */
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
	    	            JSONObject response;
	    				try 
	    				{
	    					response = parseMessage(message);
	    					if(response!=null)
	    		            {
	    						messageObject.sendMessage(response);
	    		            }
	    				} 
	    				catch (JSchException e) 
	    				{
	    					e.printStackTrace();
	    				}
	    	                
	    	         }
	    	    }).start();
	    	}	
	    }	
	}
	
	/**
	 * Method to parse the message and return response message for a request
	 * @throws JSchException 
	 */
	public JSONObject parseMessage(JSONObject message) throws JSchException
	{
		JSONObject response=null;
		String type=(String)message.get("type");
		switch(type)
		{
			case "create_service" :  	createService(message);
										break;
			case "stop_service" :    	stopService(message);
										break;
		}
		return response;
	}
	
	/**
	 * Method to get the path for repository along with ip
	 * @param serviceName
	 */
	public JSONObject getRepository(String serviceName)
	{
		/*JSONObject repoDetails=new JSONObject();
		repoDetails.put("password", "ro123hit");
		repoDetails.put("username", "rohit");
		repoDetails.put("ip", "localhost");
		repoDetails.put("path", "/home/rohit/IIIT/Sem2/table2.csv");
		return repoDetails;*/
		//Fetch repo details
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void createService(JSONObject message) throws JSchException
	{
		JSONObject serverDetails=null;
		Session session=null;
		if(message.containsKey("server"))
		{
			String IP=(String)((JSONObject)message.get("server")).get("IP");
			serverDetails=messageObject.callServiceURL("http://"+Message.getGateWayAddr()+"/Serverless/Userservlet?servicename=server_manager&&type=server_request&&ip="+IP);
		}
		else
		{
			serverDetails=messageObject.callServiceURL("http://"+Message.getGateWayAddr()+"/Serverless/Userservlet?servicename=server_manager&&type=server_request");	
		}
		JSONObject destination=new JSONObject();
		destination.put("server_ip", serverDetails.get("server_ip"));
		destination.put("server_port", serverDetails.get("server_port"));
		destination.put("server_username", serverDetails.get("server_username"));
		destination.put("server_password", serverDetails.get("server_password"));
		JSONObject source=getRepository((String)message.get("service_name"));
		if(!message.containsKey("server"))
		{
			JSONObject agent=getRepository("agent");
			copyJar(agent, destination);
		}
		copyJar(source, destination);
		deployJar(session, "");
		message.put("queue", message.get("service_name"));
		messageObject.sendMessage(message);
	}
	
	public void copyJar(JSONObject source,JSONObject destination)
	{
		
	}
	
	public void deployJar(Session session,String jarPath) throws JSchException
	{
		ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
		channelExec.setCommand("java -jar "+jarPath);
		channelExec.setCommand("echo $!");
	}
	
	public void stopService(JSONObject message)
	{
		
		//if no other VM is running on that machine update the servers.xml
	}
}
