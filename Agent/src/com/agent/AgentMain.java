package com.agent;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.json.simple.JSONObject;

import com.message.Message;

public class AgentMain 
{
	static Message messageObject;
	
	public static void main(String[] args) 
	{
		messageObject=new Message(args[0], args[1], args[2], args[3]);
		try 
		{
			new AgentMain().updateStatus();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to send updated status to load balancer
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void updateStatus() throws Exception
	{
		while(true)
		{
			JSONObject response=getCPULoad();
			response.put("type", "update_cpu");
			response.put("queue", "loadbalancer");
			messageObject.sendMessage(response);
			Thread.currentThread().sleep(10000);
		}
	}
	
	/**
	 * Method to get cpu load of current server
	 * @return
	 * @throws SocketException 
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getCPULoad() throws SocketException
	{
		JSONObject response=new JSONObject();
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) 
		{
		    method.setAccessible(true);
		    if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) 
		    {
		        Object value;
		        try 
		        {
		            value = method.invoke(operatingSystemMXBean);
		        } 
		        catch (Exception e) 
		        {
		            value = e;
		        }
                String str="";
                str=method.getName() + " = " + value;
                if(str.contains("getSystemCpuLoad"))
                {
                	response.put("cpu", str);
                	break;
                }       
		    }
		}
//            InetAddress ipAddr = InetAddress.getLocalHost();
//            response.put("ip", ipAddr.getHostAddress());
            @SuppressWarnings("rawtypes")
    		Enumeration e = NetworkInterface.getNetworkInterfaces();
    		int flag=0;
    		while(e.hasMoreElements())
    		{
    		    NetworkInterface n = (NetworkInterface) e.nextElement();
    		    Enumeration ee = n.getInetAddresses();
    		    while (ee.hasMoreElements())
    		    {
    		        InetAddress i = (InetAddress) ee.nextElement();
    		        String check = i.getHostAddress();
    		        if(flag==1)
    		        {
//    		        	System.out.println("IP = "+check);
    		        	response.put("ip", check);
    		        	flag=0;
    		        }
    		        if(check.contains("%"))
    		        {
    		        	int ind = check.indexOf("%");
    		        	if(check.charAt(ind+1)=='w')
    		        	{
//    				        System.out.println(i.getHostAddress());
    				        flag=1;
    		        	}
    		        		
    		        }
    		    }
    		}

		return response;
	}
}
