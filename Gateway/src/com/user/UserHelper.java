/*
 * Author : Suryansh Agnihotri :)
 */

package com.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.message.Message;

public class UserHelper 
{
	
	static int count = 0;
	static Message messageObject;
	
	public static Map<String,Thread> requestThMap;
	public static Map<String,JSONObject> responseMap;
	
	static
	{
		requestThMap=new HashMap<String,Thread>();
		responseMap=new HashMap<String,JSONObject>();
		messageObject=new Message("localhost", "gateway", "GATEWAY", "localhost:8114");
		processResponse();
	}
	
	public static void processResponse()
	{
		while(true)
		{
			JSONObject json=Message.messageQueue.poll();
			if(json!=null)
			{
				String req=(String)json.get("request_id");
		        UserHelper.responseMap.put(req, json);
		        Thread resTh=UserHelper.requestThMap.get(req);
		        resTh.notify();
			}	
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public String getFullURL(HttpServletRequest request) {
	    StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
	
	/**  increment counter which will serve as request id (which is shared variable)
	 * 
	 */
    public synchronized int incrementCount() 
    {
        count++;
        if(count==Integer.MAX_VALUE)
        	count=0;
        return count;
    }
    
    /** 
     * 
     * It will parse the given input string and extract the service_name
     * input https://www.aws.com/servlet/service_name?param1=p1 && param2=p2 ....
     */
    public  String parse(String input)
    {
		int start = input.lastIndexOf("/");
		int end = input.indexOf("?");
		if(end==-1)
		{
			// no parameters
			end=input.length();
		}
		return input.substring(start, end);
    }
    
    boolean validate(JSONObject container)
    {
    	
    	return true;
    }
    
    public JSONObject forward_request(JSONObject container) throws InterruptedException
	{

		String serviceName=(String)container.get("service_name");
    	String x= (String)container.get("request_id");
		messageObject.logMessage("INFO", "Request came for "+serviceName+" with request id "+x);
		requestThMap.put(x, Thread.currentThread());
		messageObject.sendMessage(container);
		JSONObject message=getMessage(x);
		messageObject.logMessage("INFO", "Response came for "+serviceName+" with request id "+x);
		return message;
	}
    
    
   
    
    /* For Deployment Assembly: Right click on WAR in eclipse-> Buildpath -> Configure Build path -> Deployment Assembly (left Pane) -> Add -> External file system -> Add -> Select your jar -> Add -> Finish.*/
    
	@SuppressWarnings("unchecked")
	
	// what to do if same parameter name
	// format of req www.aw.com/servlet/service_name?name=hello
	// it should be like this www.aw.com/servlet?name=hello 
	public JSONObject handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, InterruptedException 
	{
		boolean ok= true;
		JSONObject container = new JSONObject();
		String x=new Integer(incrementCount()).toString();
		container.put("request_id", x);
		JSONArray request_parameters = new JSONArray();
		container.put("type", "service_request");
		Enumeration<String> parameterNames = req.getParameterNames();
		while (parameterNames.hasMoreElements()) 
		{
			JSONObject parameter = new JSONObject();
			String paramName = parameterNames.nextElement();
			String[] paramValues = req.getParameterValues(paramName);
			String paramValue = paramValues[0];
			if(ok)
			{
				container.put("service_name", paramValue);
				ok=false;
			}
			else
			{
				parameter.put(paramName, paramValue);
				request_parameters.add(parameter);	
			}
		}
		container.put("request_parameter", request_parameters);
		container.put("queue", "loadbalancer");
		String serviceName=(String)container.get("service_name");
		boolean forward_this_request = true;
		if(!serviceName.equals("login"))
		{
			// user must have a valid token
			if(container.containsKey("token_id"))
			{
				// validate token 
				if(!validate(container))
				{
					// Not Authorised
					forward_this_request=false;
				}
			}
		}
		
		JSONObject msg=null;
		if(forward_this_request)
		{
			 msg = forward_request(container);
		}
		
		return msg;
		
	}
	
	public JSONObject getMessage(String x) throws InterruptedException
	{
		Thread.currentThread().wait();
		JSONObject response=null;
		synchronized (this) 
		{
			response=responseMap.get(x);
			responseMap.remove(x);
			requestThMap.remove(x);
		}
		return response;
	}
	
}
	