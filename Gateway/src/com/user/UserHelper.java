/*
 * Author : Suryansh Agnihotri :)
 */

package com.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.message.Message;

public class UserHelper 
{
	
	public static int count = 0;
	
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
    	if(count==Integer.MAX_VALUE)
        	count=0;
    	count++;
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
	
	public JSONObject handleRequest(HttpServletRequest req, HttpServletResponse res,int requestId) throws IOException 
	{
		boolean ok= true;
		JSONObject container = new JSONObject();
		JSONArray request_parameters = new JSONArray();
		container.put("type", "service_request");
		container.put("request_id", requestId);
		PrintWriter out = res.getWriter();
		res.setContentType("text/plain");

		Enumeration<String> parameterNames = req.getParameterNames();
		
		while (parameterNames.hasMoreElements()) {
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
		container.put("ip", "10.3.0.233");
		System.out.println(container.toJSONString());
		out.close();
		Message m = new Message();
		m.sendMessage(container);
		m.recieveMessage(requestId);
		String x=m.msg;
		return null;
	}
}
	