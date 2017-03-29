package com.load;

import java.awt.SecondaryLoop;
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
	public JSONObject processRequest(JSONObject message)
	{
		JSONObject response = new JSONObject();
		String servicename=(String)message.get("service_name");
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
								 //response.put("ip",element.getAttribute("ip"));
								 //response.put("port",element.getAttribute("port"));
								 flag=true;
							 }		 
						 }	 
					 }
				}
			}
			if(!flag)
			{
				//write code for sending it manager server lifecycle
				response.put("queue","service_manager");
				response.put("type","create_service");
			}
			else
			{
				response.put("queue",servicename);
				response.put("type","service_request");
			}
			response.put("service_name",servicename);
			response.put("parameters", message.get("parameters"));
			Message mObj=new Message();
			mObj.sendMessage(response);
			System.out.println(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return response;
	}
}
