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
		ServerManagerMain obj=new ServerManagerMain();
		while(true)
		{
			Message mObj=new Message();
			obj.processRequest(mObj.recieveMessage());
		}	
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
			case "serverrequest":	response=getAvailableServer();
									break;
			case "serverdetails":	response=getServerDetails();
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
							 response.put("type", "serverrequest");
							 response.put("status","yes");
							 response.put("ip",element.getAttribute("ip"));
							 response.put("port",element.getAttribute("port"));
							 response.put("username",element.getAttribute("username"));
							 response.put("password",element.getAttribute("password"));
							 return response;
						 }	 
					 }	 
				}
			}
			response.put("type", "serverdetails");
			response.put("status","no");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
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
			response.put("type", "serverdetails");
			response.put("details", serverArray);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return response;
	}
}
