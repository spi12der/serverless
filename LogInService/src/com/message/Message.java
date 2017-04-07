package com.message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.security.Security;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


public class Message 
{
	private final static String RECIEVE_QUEUE_NAME = "security";
	private final static String RECEIVEHOSTADDRESS="10.2.128.180";

	public void recieveMessage()
	{
		try
		{
			ConnectionFactory factory = new ConnectionFactory();
			//factory.setUri("amq://test:test@RECEIVEHOSTADDRESS:5672");
		    factory.setHost(RECEIVEHOSTADDRESS);
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    channel.queueDeclare(RECIEVE_QUEUE_NAME, false, false, false, null);
		    //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		    Consumer consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException 
		      {
			        String message=  new String(body, "UTF-8");
			        JSONParser parser=new JSONParser();
			        try 
			        {
						JSONObject json=(JSONObject)parser.parse(message);
						Security obj=new Security();
						obj.processRequest(json);
					} 
			        catch (ParseException e) 
			        {
						e.printStackTrace();
					}
		        }
		    };
		    channel.basicConsume(RECIEVE_QUEUE_NAME, false, consumer);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void sendMessage(JSONObject response)
	{
		final String SEND_QUEUE_NAME = (String) response.get("queue");
		final String SENDHOSTADDRESS= (String) response.get("ip");
		try
		{
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("10.2.128.180");
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
		}
	}
	
	@SuppressWarnings("unchecked")
	public void logMessage(String messageType,String message)
	{
		//callServiceURL("http://"+getGatewayAddress()+"/Serverless/logging?logType="+messageType+"message="+message);
		JSONObject logObject=new JSONObject();
		logObject.put("queue", "logging");
		logObject.put("ip", "localhost");
		logObject.put("logType", messageType);
		logObject.put("message", message);
		sendMessage(logObject);
	}
	
	public String getGatewayAddress()
	{
		return "localhost:8114";
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
		}
		return message;
	}
}