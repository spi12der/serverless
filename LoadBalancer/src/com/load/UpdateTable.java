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

public class UpdateTable 
{
	/**
	 * Method for updating the routing table
	 * @param message
	 */
	public void updateRoutingInfo(JSONObject message)
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
					 String ip=element.getAttribute("ip");
					 if(ip.equalsIgnoreCase((String)message.get("ip")))
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
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
