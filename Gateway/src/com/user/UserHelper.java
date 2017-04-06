/*
 * Author : Suryansh Agnihotri :)
 */

package com.user;

import java.io.IOException;
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
	
	public static Map<String,Thread> requestThMap;
	public static Map<String,JSONObject> responseMap;
	
	static
	{
		requestThMap=new HashMap<String,Thread>();
		responseMap=new HashMap<String,JSONObject>();
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
		String serviceName=(String)container.get("service_name");
		new Message().logMessage("INFO", "GATEWAY: Request came for "+serviceName+" with request id "+x);
		container.put("request_parameter", request_parameters);
		container.put("queue", "loadbalancer");
		requestThMap.put(x, Thread.currentThread());
		Message mObj=new Message();
		mObj.sendMessage(container);
		JSONObject message=getMessage(x);
		new Message().logMessage("INFO", "GATEWAY: Response came for "+serviceName+" with request id "+x);
		return message;
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
	