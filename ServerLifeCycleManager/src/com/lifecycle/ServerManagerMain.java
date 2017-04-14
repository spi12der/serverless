package com.lifecycle;

import java.io.File;
import java.net.InetAddress;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.message.Message;

public class ServerManagerMain 
{
	static Message messageObject;
	
	public static void main(String[] args) 
	{
		messageObject=new Message(args[0],args[1],args[2],args[3]);
		messageObject.recieveMessage();	
	}
	
	/**
	 * Method to process Request in separate thread
	 * @param message
	 */
	public void processRequest(JSONObject message)
	{
		if(message!=null)
    	{
    		new Thread(new Runnable() 
		    {
		         public void run() 
		         {
		              JSONObject response=parseMessage(message);
		              System.out.println(response.toJSONString());
		              if(response!=null)
		              {
		            	  messageObject.sendMessage(response);
		              }  
		         }
		    }).start();
    	}
	}
	
	/**
	 * Method to parse the message and return response message for a request
	 */
	public JSONObject parseMessage(JSONObject request)
	{
		System.out.println("Message recieved");
		JSONObject response=null;
		JSONObject message=(JSONObject)request.get("parameters");
		String type=(String)message.get("type");
		switch(type)
		{
			case "server_request":	response=getAvailableServer(request);
									break;
			case "server_details":	response=getServerDetails(request);
									break;
			case "update_server": 	response=updateServer(request);
									break;
			case "add_server":		response=addServer(request);
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
	public JSONObject getAvailableServer(JSONObject message)
	{
		JSONObject response=new JSONObject();
		response.put("type", "server_request");
		response.put("queue", "gateway");
		response.put("request_id", message.get("request_id"));
		JSONObject parameters=(JSONObject)message.get("parameters");
		if(parameters!=null && parameters.containsKey("ip"))
			response=getServerWithIP(response, (String)message.get("ip"));
		else
			response=getServer(response);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getServer(JSONObject response)
	{
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("server");
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
						 if(inet.isReachable(22))
						 {
							 response.put("status","1");
							 response.put("server_ip",element.getAttribute("ip"));
							 response.put("server_username",element.getAttribute("username"));
							 response.put("server_password",element.getAttribute("password"));
							 messageObject.logMessage("INFO", "Idle server details sent to gateway");
							 return response;
						 }	 
					 }	 
				}
			}
			response.put("status","0");
			messageObject.logMessage("INFO", "No idle server available");
		}
		catch (Exception e) 
		{
			response.put("status","0");
			e.printStackTrace();
			messageObject.logMessage("ERROR", "Error in finding idle server =>"+e.getLocalizedMessage());
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getServerWithIP(JSONObject response,String IP)
	{
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("server");
			for(int i=0;i<nList.getLength();i++)
			{
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					 Element element = (Element) node;
					 String ip=element.getAttribute("ip");
					 if(ip.equalsIgnoreCase(IP))
					 {
						 response.put("status","1");
						 response.put("server_ip",element.getAttribute("ip"));
						 response.put("server_username",element.getAttribute("username"));
						 response.put("server_password",element.getAttribute("password"));
						 messageObject.logMessage("INFO", "Idle server details sent to gateway");
						 return response;	 
					 }	 
				}
			}
			response.put("status","0");
			messageObject.logMessage("INFO", "No such server available");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			messageObject.logMessage("ERROR", "Error in finding idle server =>"+e.getLocalizedMessage());
			response.put("status","0");
		}
		return response;
	}
	
	/**
	 * Method to fetch all the details of server available with the platform
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getServerDetails(JSONObject message)
	{
		JSONObject response=new JSONObject();
		response.put("request_id", message.get("request_id"));
		response.put("queue", "gateway");
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("server");
			JSONArray serverArray=new JSONArray();
			for(int i=0;i<nList.getLength();i++)
			{
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					 Element element = (Element) node;
					 JSONObject server=new JSONObject();
					 server.put("ip",element.getAttribute("ip"));
					 server.put("username",element.getAttribute("username"));
					 server.put("status",element.getAttribute("status"));
					 serverArray.add(server);	 
				}
			}
			response.put("type", "server_details");
			response.put("details", serverArray);
			messageObject.logMessage("INFO", "Server details sent to Gateway");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			messageObject.logMessage("ERROR", "Error in fetching server details =>"+e.getLocalizedMessage());
		}
		return response;
	}
	
	
	/**
	 * Method to update the status of a server
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject updateServer(JSONObject request)
	{
		JSONObject response=new JSONObject();
		response.put("request_id", request.get("request_id"));
		JSONObject message=(JSONObject)request.get("parameters");
		String ip=(String)message.get("server_ip");
		String status=(String)message.get("status");
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("server");
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
			messageObject.logMessage("INFO", "Server file updated sucessfully");
			response.put("status", "1");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			messageObject.logMessage("ERROR", "Error in updating servers file =>"+e.getLocalizedMessage());
			response.put("status", "0");
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject addServer(JSONObject request)
	{
		JSONObject response=new JSONObject();
		response.put("request_id", request.get("request_id"));
		JSONObject message=(JSONObject)request.get("parameters");
		String ip=(String)message.get("ip");
		String username=(String)message.get("username");
		String password=(String)message.get("password");
		try
		{
			File inputFile = new File("servers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("root");
			Element server=doc.createElement("server");
			server.setAttribute("ip", ip);
			server.setAttribute("username", username);
			server.setAttribute("password", password);
			server.setAttribute("status", "A");
			Element root=(Element)nList.item(0);			
			root.appendChild(server);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult consoleResult = new StreamResult(inputFile);
	        transformer.transform(source, consoleResult);
	        messageObject.logMessage("INFO", "Server added successfully ");
	        response.put("status", "1");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			messageObject.logMessage("ERROR", "Unable to add server =>"+e.getLocalizedMessage());
			response.put("status", "0");
		}
		return response;
	}
}
