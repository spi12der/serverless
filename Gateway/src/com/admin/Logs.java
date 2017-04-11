package com.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class Logs
 */
@WebServlet("/Logs")
public class Logs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logs() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public JSONObject getObject()
    {
    	JSONObject ob=new JSONObject();
    	ob.put("debug", "INFO");
    	ob.put("module", "DATA SERVICE");
    	ob.put("log", "Request came for creating a table");
    	return ob;
    }
    
    public JSONObject getObject1()
    {
    	JSONObject ob=new JSONObject();
    	ob.put("debug", "INFO");
    	ob.put("module", "LOAD BALANCER");
    	ob.put("log", "Request came for creating a table");
    	return ob;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject message=new JSONObject();	
		JSONArray arr=new JSONArray();
		for(int i=0;i<40;i++)
		{
			if(i%2==0)
				arr.add(getObject());
			else
				arr.add(getObject1());
		}
		message.put("details", arr);
		request.setAttribute("message", message.toJSONString());
		request.getRequestDispatcher("JSP/ADMIN/PlatformLogs.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
