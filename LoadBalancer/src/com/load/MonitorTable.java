package com.load;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.message.Message;

public class MonitorTable 
{
	@SuppressWarnings("unchecked")
	public void checkTable(Message messageObject)
	{
		while(true)
		{
			try
			{
				File inputFile = new File("routing.xml");
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
						 double cpu=Double.parseDouble(element.getAttribute("cpu"));
						 if(status.equalsIgnoreCase("up") && cpu>=10.0)
						 {
							 element.setAttribute("status", "down");
							 JSONObject response=new JSONObject();
							 response.put("type", "stopservice");
							 JSONObject server=new JSONObject();
							 server.put("ip", element.getAttribute("ip"));
							 response.put("serverdetails",server);
							 response.put("queue", "service_manager");
							 messageObject.sendMessage(response);
							 TransformerFactory transformerFactory = TransformerFactory.newInstance();
						     Transformer transformer = transformerFactory.newTransformer();
						     DOMSource source = new DOMSource(doc);
						     StreamResult consoleResult = new StreamResult(inputFile);
						     transformer.transform(source, consoleResult);
						 }
					}
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}	
	}
	
	public String getFreeServer()
	{
		try
		{
			File inputFile = new File("routing.xml");
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
					 String IP=element.getAttribute("ip");
					 double cpu=Double.parseDouble(element.getAttribute("cpu"));
					 if(status.equalsIgnoreCase("up") && cpu<=50.0 && cpu>=10.0)
					 {
						 return IP;
					 }
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
