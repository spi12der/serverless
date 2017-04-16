package com.lifecycle;

import java.io.ByteArrayOutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.jcraft.jsch.Session;
import com.message.Message;

public class ServiceUtils 
{
	
	/**
	 * Method to get the path for repository along with ip
	 * @param serviceName
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getRepository(String serviceName,Message messageObject)
	{
		JSONObject repoDetails=new JSONObject();
		repoDetails.put("password", "ro123hit");
		repoDetails.put("username", "rohit");
		repoDetails.put("ip", "localhost");
		JSONObject message=messageObject.callServiceURL("http://"+Message.getGateWayAddr()+"/Serverless/UserServlet?service_name=dataservice&&type=select&&db_name=admin&&tbl_name=serviceInfo&&attributes=*&&conditions=service~"+serviceName);
		JSONObject row=(JSONObject) ((JSONArray) message.get("result")).get(0);
		String path=(String) row.get("path");
		String type=(String) row.get("type");
		repoDetails.put("path", path);
		repoDetails.put("type", type);
		repoDetails.put("dev", path.substring(0,path.lastIndexOf("/")+1)+"developer.jar");
		return repoDetails;
	}
	
	public String runScript(String command) throws Exception
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    CommandLine commandline = CommandLine.parse(command);
	    DefaultExecutor exec = new DefaultExecutor();
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	    exec.setStreamHandler(streamHandler);
	    exec.execute(commandline);
	    return outputStream.toString();
	}
	
	public void deployJar(Session session,String serviceName,JSONObject destination, Message messageObject) throws Exception
	{
		JSONObject source=getRepository(serviceName,messageObject);
		String type=(String)source.get("type");
		String i1=(String)source.get("ip");
		String u1=(String)source.get("username");
		String p1=(String)source.get("password");
		String i2=(String)destination.get("ip");
		String u2=(String)destination.get("username");
		String p2=(String)destination.get("password");
		String devPath=new String();
		if(type.equalsIgnoreCase("dev"))
		{
			String path=(String)source.get("path");
			devPath=(String)source.get("dev");
			String cmd1="bash copy.sh "+i1+" "+u1+" "+p1+" "+i2+" "+u2+" "+p2+" "+devPath;
			String cmd2="bash copy.sh "+i1+" "+u1+" "+p1+" "+i2+" "+u2+" "+p2+" "+path;
			runScript(cmd1);
			runScript(cmd2);
		}
		else
		{
			devPath=(String)source.get("path");
			String cmd="bash copy.sh "+i1+" "+u1+" "+p1+" "+i2+" "+u2+" "+p2+" "+devPath;
			runScript(cmd);
		}
		messageObject.logMessage("INFO", serviceName+" jar copied sucessfully ");
		String depCmd="bash deploy.sh "+i2+" "+u2+" "+p2+" "+devPath.substring(devPath.lastIndexOf("/")+1)+" "+messageObject.getRabbitIP()+" "+serviceName+" "+serviceName+" "+Message.getGateWayAddr();
		runScript(depCmd);
		messageObject.logMessage("INFO", serviceName+" jar deployed sucessfully ");
	}	
	
	/*public void copyJarHere(JSONObject source,Session session) throws Exception
	{
		String cmd=new String();
		String password=(String)source.get("password");
		String username=(String)source.get("username");
		String ip=(String)source.get("ip");
		String path=(String)source.get("path");
		ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
		cmd="sshpass -p "+password+" scp "+username+"@"+ip+":"+path+" .";
		channelExec.setCommand(cmd);
	}
	
	public void copyJarTo(JSONObject destination,Session session,String jarName) throws Exception
	{
		String cmd=new String();
		String password=(String)destination.get("password");
		String username=(String)destination.get("username");
		String ip=(String)destination.get("ip");
		ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
		cmd="sshpass -p "+password+" scp "+jarName+" "+username+"@"+ip+":~";
		channelExec.setCommand(cmd);
	}
	
	public void deployJar(Session session,String serviceName,JSONObject destination, Message messageObject) throws Exception
	{
		JSONObject source=getRepository(serviceName,messageObject);
		String path=(String)source.get("path");
		String jarName=path.substring(path.indexOf("/")+1);
		copyJarHere(source, session);
		copyJarTo(destination, session, jarName);
		Connection con=new Connection();
		Session desSession=con.connect(destination);
		ChannelExec channelExec = (ChannelExec)desSession.openChannel("exec");
		channelExec.setCommand("sudo docker run --name my1 --rm -v '$PWD':/tmp -w /tmp openjdk:8 java -jar "+jarName);
		desSession.disconnect();
	}*/
}
