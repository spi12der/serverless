package com.lifecycle;

import java.io.File;
import java.net.InetAddress;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.message.Message;

public class ServerManagerMain 
{
	public static void main(String[] args) 
	{
		Message mObj=new Message();
		mObj.recieveMessage();	
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
			case "server_request":	response=getAvailableServer();
									break;
			case "server_details":	response=getServerDetails();
									break;
			case "update_server": 	updateServer(message);
									break;
		}
		return response;
	}
	
	/**
	 * Method to check for any free server available and return its ip, port 
	 * and user credentials
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getAvailableServer()
	{
		JSONObject response=new JSONObject();
		response.put("type", "server_request");
		response.put("queue", "gateway");
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("servers");
			for(int i=0;i<nList.getLength();i++)
			{
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					 Element element = (Element) node;
					 String status=element.getAttribute("status");
					 if(status.equalsIgnoreCase("A"))
					 {
						 InetAddress inet = InetAddress.getByName(element.getAttribute("ip"));
						 if(inet.isReachable(Integer.parseInt(element.getAttribute("port"))))
						 {
							 response.put("status","yes");
							 response.put("server_ip",element.getAttribute("ip"));
							 response.put("server_port",element.getAttribute("port"));
							 response.put("server_username",element.getAttribute("username"));
							 response.put("server_password",element.getAttribute("password"));
							 return response;
						 }	 
					 }	 
				}
			}
			response.put("status","no");
			new Message().logMessage("INFO", "SERVER MANAGER : Idle server details sent to gateway");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			new Message().logMessage("ERROR", "SERVER MANAGER : Error in finding idle server =>"+e.getLocalizedMessage());
		}
		return response;
	}
	
	/**
	 * Method to fetch all the details of server available with the platform
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getServerDetails()
	{
		JSONObject response=new JSONObject();
		response.put("queue", "gateway");
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("servers");
			JSONArray serverArray=new JSONArray();
			for(int i=0;i<nList.getLength();i++)
			{
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					 Element element = (Element) node;
					 JSONObject server=new JSONObject();
					 server.put("ip",element.getAttribute("ip"));
					 server.put("port",element.getAttribute("port"));
					 server.put("status",element.getAttribute("status"));
					 serverArray.add(server);	 
				}
			}
			response.put("type", "server_details");
			response.put("details", serverArray);
			new Message().logMessage("INFO", "SERVER MANAGER : Server details sent to Gateway");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			new Message().logMessage("ERROR", "SERVER MANAGER : Error in fetching server details =>"+e.getLocalizedMessage());
		}
		return response;
	}
	
	
	/**
	 * Method to update the status of a server
	 * @param message
	 * @return
	 */
	public void updateServer(JSONObject message)
	{
		String ip=(String)message.get("server_ip");
		String status=(String)message.get("status");
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("servers");
			for(int i=0;i<nList.getLength();i++)
			{
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					 Element element = (Element) node;
					 if(ip.equalsIgnoreCase(element.getAttribute("ip")))
					 {
						 element.setAttribute("status", status);
						 break;
					 }	 
				}
			}
			new Message().logMessage("INFO", "SERVER MANAGER : Server file updated sucessfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			new Message().logMessage("ERROR", "SERVER MANAGER : Error in updating servers file =>"+e.getLocalizedMessage());
		}
	}
}
