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
 * Servlet implementation class ServerDetails
 */
@WebServlet("/ServerDetails")
public class ServerDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServerDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject message=getDetails();
		request.setAttribute("message", message.toJSONString());
		request.getRequestDispatcher("JSP/ADMIN/Server.jsp").forward(request, response);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject dummy()
	{
		JSONObject s1=new JSONObject();
		s1.put("ip", "10.0.2.102");
		s1.put("port", "8080");
		s1.put("status", "Occupied");
		return s1;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getDetails()
	{
		JSONObject message=new JSONObject();
		JSONArray arr=new JSONArray();
		for(int i=0;i<40;i++)
		{
			arr.add(dummy());
		}	
		message.put("details", arr);
		return message;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
