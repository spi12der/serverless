package com.lifecycle;

import org.json.simple.JSONObject;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class ServiceUtils 
{
	
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
	
	public void copyJarHere(JSONObject source,Session session) throws Exception
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
	
	public void deployJar(Session session,String serviceName,JSONObject destination) throws Exception
	{
		JSONObject source=getRepository(serviceName);
		String path=(String)source.get("path");
		String jarName=path.substring(path.indexOf("/")+1);
		copyJarHere(source, session);
		copyJarTo(destination, session, jarName);
		Connection con=new Connection();
		Session desSession=con.connect(destination);
		ChannelExec channelExec = (ChannelExec)desSession.openChannel("exec");
		channelExec.setCommand("sudo docker run --name my1 --rm -v '$PWD':/tmp -w /tmp openjdk:8 java -jar "+jarName);
		desSession.disconnect();
	}
}
