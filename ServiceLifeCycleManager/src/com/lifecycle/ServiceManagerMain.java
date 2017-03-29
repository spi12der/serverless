package com.lifecycle;

import org.json.simple.JSONObject;

/*import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;*/
import com.message.Message;

public class ServiceManagerMain 
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception 
	{
		ServiceManagerMain obj=new ServiceManagerMain();
		/*while(true)
		{
			Message mObj=new Message();
			obj.processRequest(mObj.recieveMessage());
		}*/
		JSONObject r=new JSONObject();
		r.put("service_name", "file");
		obj.deployJar(r);
	}
	
	/**
	 * Method to process Request in separate thread
	 * @param message
	 */
	public void processRequest(JSONObject message)
	{
	    new Thread(new Runnable() 
	    {
	         public void run() 
	         {
	              JSONObject response=parseMessage(message);
	              if(response!=null)
	              {
	            	  Message obj=new Message();
	            	  obj.sendMessage(response);
	              }  
	         }
	    }).start();  
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
			case "create_service" :  response=createService(message);
									break;
			case "stop_service" :    response=stopService(message);
									break;
		}
		return response;
	}
	
	/**
	 * Method to get the path for repository along with ip
	 * @param serviceName
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getRepository(String serviceName)
	{
		JSONObject repoDetails=new JSONObject();
		repoDetails.put("password", "ro123hit");
		repoDetails.put("username", "rohit");
		repoDetails.put("ip", "localhost");
		repoDetails.put("path", "/home/rohit/IIIT/Sem2/table2.csv");
		return repoDetails;
	}
	
	public void deployJar(JSONObject message) throws Exception
	{
		/*String serviceName=(String) message.get("service_name");
		JSONObject repoDetails=getRepository(serviceName);
		String command="sshpass -p "+((String)repoDetails.get("password"))+" scp "+((String)repoDetails.get("ip"))+"@"+((String)repoDetails.get("username"))+":"+((String)repoDetails.get("path"));
	    JSch jsch=new JSch();
	    Session session=jsch.getSession("rohit", "127.0.0.1", 22);
	    session.connect();
	    Channel channel=session.openChannel("exec");
	    ((ChannelExec)channel).setCommand(command);
	    channel.setInputStream(null);
	    ((ChannelExec)channel).setErrStream(System.err);
	    BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
	    channel.connect();
	    String msg=null;
	    while((msg=in.readLine())!=null){
	      System.out.println(msg);
	    }*/
	}
	
	public JSONObject createService(JSONObject message)
	{
		if(message.containsKey("server"))
		{
			//here server IP are already provided by the load balancer
			//fetch other details
		}
		else
		{
			//fetch the details of available from server lifecycle manager
		}
		//copy the jar file to a new server (SCP)
		//make a new docker instance and execute the jar
		//send the request message to the service message
		return null;
	}
	
	public JSONObject stopService(JSONObject message)
	{
		//check for the service to stop
		//stop the virtual machines and remove the jars
		//if no other VM is running on that machine update the servers.xml
		return null;
	}
}
