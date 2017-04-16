package com.load;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
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

public class UpdateTable 
{
	/**
	 * Method for updating the routing table
	 * @param message
	 */
	public void updateCpu(JSONObject message,Message messageObject)
	{
		String IP=(String)message.get("ip");
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
					 String ip=element.getAttribute("ip");
					 if(ip.equalsIgnoreCase(IP))
					 {
						 element.setAttribute("cpu", (String)message.get("cpu"));
						 break;
					 }
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult consoleResult = new StreamResult(inputFile);
	        transformer.transform(source, consoleResult);
	        messageObject.logMessage("INFO", "Routing table updated with for ip "+IP);
		}
		catch (Exception e) 
		{
			messageObject.logMessage("ERROR", "Unable to update routing table =>"+e.getLocalizedMessage());
		}
	}
	
	/**
	 * Add a new service in the routing table
	 * @param message
	 */
	public void updateService(JSONObject message,Message messageObject)
	{
		String IP=(String)message.get("ip");
		String name=(String)message.get("service");
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
					 String ip=element.getAttribute("ip");
					 if(ip.equalsIgnoreCase(IP))
					 {
						 Element service=doc.createElement("service");
						 service.setAttribute("name", name);
						 element.appendChild(service);
						 break;
					 }
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        DOMSource source = new DOMSource(doc);
	        StreamResult consoleResult = new StreamResult(inputFile);
	        transformer.transform(source, consoleResult);
	        messageObject.logMessage("INFO", name+" service update in routing table at server "+IP);
		}
		catch (Exception e) 
		{
			messageObject.logMessage("ERROR", "Unable to update routing table =>"+e.getLocalizedMessage());
		}
	}
	
	/**
	 * Add a new service in the routing table
	 * @param message
	 */
	public void updateServer(JSONObject message,Message messageObject)
	{
		String IP=(String)message.get("ip");
		try
		{
			File inputFile = new File("routing.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("root");
			Element server=doc.createElement("server");
			server.setAttribute("ip", IP);
			server.setAttribute("status", "up");
			server.setAttribute("cpu", "20.0");
			Element root=(Element)nList.item(0);			
			root.appendChild(server);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        DOMSource source = new DOMSource(doc);
	        StreamResult consoleResult = new StreamResult(inputFile);
	        transformer.transform(source, consoleResult);
	        messageObject.logMessage("INFO", "Routing table updated with server "+IP);
		}
		catch (Exception e) 
		{
			messageObject.logMessage("ERROR", "Unable to update routing table =>"+e.getLocalizedMessage());
		}
	}
}
