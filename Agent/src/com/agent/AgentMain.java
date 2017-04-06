package com.agent;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import com.message.Message;

public class AgentMain 
{
	public static void main(String[] args) 
	{
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
			Message m=new Message();
			m.sendMessage(response);
			Thread.currentThread().sleep(10000);
		}
	}
	
	/**
	 * Method to get cpu load of current server
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getCPULoad()
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
		try 
		{
            InetAddress ipAddr = InetAddress.getLocalHost();
            response.put("ip", ipAddr.getHostAddress());
        } 
		catch (UnknownHostException ex) 
		{
            ex.printStackTrace();
        }
		return response;
	}
}
