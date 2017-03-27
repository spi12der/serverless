package com.message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.log.LogMain;
import com.rabbitmq.client.*;
import java.io.IOException;


public class Message 
{
	private final static String RECIEVE_QUEUE_NAME = "logging";
	private final static String RECEIVEHOSTADDRESS="localhost";

	public void recieveMessage()
	{
		try
		{
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(RECEIVEHOSTADDRESS);
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    channel.queueDeclare(RECIEVE_QUEUE_NAME, false, false, false, null);
		    //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		    Consumer consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		          throws IOException {
			        String message=  new String(body, "UTF-8");
			        //System.out.println(message);
			        //write(message);
			        JSONParser parser=new JSONParser();
			        try 
			        {
						JSONObject response = (JSONObject)parser.parse(message);
						LogMain.logMessage((String)response.get("logType"), (String)response.get("message"));
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
		}
	}
}