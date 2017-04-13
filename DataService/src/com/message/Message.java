package com.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.data.DataMain;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Message 
{
	private static String RECIEVE_QUEUE_NAME;
	private static String rabbitIP;
	private static String moduleName;
	private static String gateWayAddr;
	
	public static Queue<JSONObject> messageQueue;
	
	public Message(String ip,String queue_name,String modulename,String gatewayAddr) 
	{
		rabbitIP=ip;
		RECIEVE_QUEUE_NAME=queue_name;
		moduleName=modulename;
		gateWayAddr=gatewayAddr;
		messageQueue=new LinkedList<JSONObject>();
	}
	
	

	public static String getRECIEVE_QUEUE_NAME() {
		return RECIEVE_QUEUE_NAME;
	}



	public static void setRECIEVE_QUEUE_NAME(String rECIEVE_QUEUE_NAME) {
		RECIEVE_QUEUE_NAME = rECIEVE_QUEUE_NAME;
	}



	public static String getModuleName() {
		return moduleName;
	}



	public static void setModuleName(String moduleName) {
		Message.moduleName = moduleName;
	}



	public static void setRabbitIP(String rabbitIP) {
		Message.rabbitIP = rabbitIP;
	}



	public String getRabbitIP()
	{
		return rabbitIP;
	}
	

	public static String getGateWayAddr() {
		return gateWayAddr;
	}



	public static void setGateWayAddr(String gateWayAddr) {
		Message.gateWayAddr = gateWayAddr;
	}
	
	public void recieveMessage()
	{
		try
		{
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(getRabbitIP());
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    channel.queueDeclare(RECIEVE_QUEUE_NAME, false, false, false, null);
		    Consumer consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		          throws IOException {
			        String message=  new String(body, "UTF-8");
			        JSONParser parser=new JSONParser();
			        try 
			        {
			        	JSONObject json=(JSONObject)parser.parse(message);
						DataMain obj=new DataMain();
			        	obj.processRequest(json);
					} 
			        catch (ParseException e) 
			        {
						e.printStackTrace();
					}
		        }
		    };
		    channel.basicConsume(RECIEVE_QUEUE_NAME, true, consumer);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			logMessage("ERROR", "Error in recieving messages from messaging queue =>"+exception.getLocalizedMessage());
		}
	}
	
	public void sendMessage(JSONObject response)
	{
		final String SEND_QUEUE_NAME = (String) response.get("queue");
		final String SENDHOSTADDRESS= getRabbitIP();
		try
		{
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(SENDHOSTADDRESS);
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    channel.queueDeclare(SEND_QUEUE_NAME, false, false, false, null);
		    String message = response.toJSONString();
		    channel.basicPublish("", SEND_QUEUE_NAME, null, message.getBytes("UTF-8"));
		    //System.out.println(" [x] Sent '" + message + "'");
			channel.close();
		    connection.close();	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logMessage("ERROR", "Error in sending messages on messaging queue =>"+e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void logMessage(String messageType,String message)
	{
		message=moduleName+" : "+message;
		JSONObject logObject=new JSONObject();
		logObject.put("queue", "logging");
		logObject.put("logType", messageType);
		logObject.put("message", message);
		sendMessage(logObject);
	}
	
	@SuppressWarnings("unused")
	public JSONObject callServiceURL(String urlString)
	{
		JSONObject message=new JSONObject();
		try
		{
			//If the we are connect a PC in other network
			//System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		    //System.setProperty("http.proxyPort", "8080");
			URL url = new URL(urlString);
			HttpURLConnection h = (HttpURLConnection)url.openConnection();
			h.setRequestMethod("POST");
			h.setDoOutput(true);
			BufferedReader reader = new BufferedReader( new InputStreamReader(h.getInputStream() ) );
			String response = reader.readLine();
			while( null != response )
			{	
				//System.out.println( response );
				response = reader.readLine();
				
			}
			if(response!=null)
			{
				JSONParser parser=new JSONParser();
				message=(JSONObject)parser.parse(response);
			}	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logMessage("ERROR", "Error in call any service URL =>"+e.getLocalizedMessage());
		}
		return message;
	}
	
}

