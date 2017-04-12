package com.load;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.message.Message;

public class ServiceRequest 
{
	/**
	 * Method for processing a request for a service according to load
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject processRequest(JSONObject message,Message messageObject)
	{
		JSONObject response = new JSONObject();
		String servicename=(String)message.get("service_name");
		String x=(String)message.get("request_id");
		if(!servicename.equalsIgnoreCase("logging"))
			messageObject.logMessage("INFO", "Request came for "+servicename+" with request id "+x);
		try
		{
			File inputFile = new File("routing.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("server");
			boolean flag=false;
			for(int i=0;i<nList.getLength();i++)
			{
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					 Element element = (Element) node;
					 String status=element.getAttribute("status");
					 double cpu=Double.parseDouble(element.getAttribute("cpu"));
					 if(status.equalsIgnoreCase("up") && cpu<=75.0)
					 {
						 NodeList childList=element.getElementsByTagName("service");
						 for(int j=0;j<childList.getLength();j++)
						 {
							 Element cE = (Element)childList.item(j);
							 String name=cE.getAttribute("name");
							 if(servicename.equalsIgnoreCase(name))
							 {
								 flag=true;
							 }		 
						 }	 
					 }
				}
			}
			if(!flag)
			{
				String IP=new MonitorTable().getFreeServer();
				if(IP!=null)
				{
					JSONObject j=new JSONObject();
					j.put("ip", IP);
					response.put("server", j);
				}	
				response.put("queue","service_manager");
				response.put("type","create_service");
				if(!servicename.equalsIgnoreCase("logging"))
					messageObject.logMessage("INFO", "Request forwarded to service manager for "+servicename+" with request id "+x);
			}
			else
			{
				response.put("queue",servicename);
				response.put("type","service_request");
				response.put("parameters",message.get("request_parameter"));
				if(!servicename.equalsIgnoreCase("logging"))
					messageObject.logMessage("INFO", "Request forwarded to "+servicename+" with request id "+x);
			}
			response.put("service_name",servicename);
			response.put("request_id", x);
		}
		catch (Exception e) 
		{
			if(!servicename.equalsIgnoreCase("logging"))
				messageObject.logMessage("ERROR", "Unable to forward service request =>"+e.getLocalizedMessage());
		}
		return response;
	}
}
