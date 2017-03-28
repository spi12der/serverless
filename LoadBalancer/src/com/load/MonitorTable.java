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

public class MonitorTable 
{
	@SuppressWarnings("unchecked")
	public void checkTable()
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
					 if(status.equalsIgnoreCase("up") && cpu>=75.0)
					 {
						 element.setAttribute("status", "down");
						 JSONObject response=new JSONObject();
						 response.put("type", "stopservice");
						 JSONObject server=new JSONObject();
						 server.put("ip", element.getAttribute("ip"));
						 server.put("port", element.getAttribute("port"));
						 response.put("serverdetails",server);
						 /*response.put("ip", "10.2.23.23");
						 response.put("port", "8080");
						 System.out.println(response);*/
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
