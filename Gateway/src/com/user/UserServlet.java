/*
 * Author : Suryansh Agnihotri :)
 */

package com.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.util.RequestUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() 
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		RequestUtil helper=new RequestUtil();
		JSONObject msg=null;
		try 
		{
			msg = helper.handleRequest(request, response);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if(msg!=null)
		{
			out.print(msg.toJSONString());
		}
		else
		{
		    out.println("<html>");
		    out.println("<head>");
		    out.println("<title>Unauthorised User</title>");
		    out.println("</head>");
		    out.println("<body>");
		    out.println("<h1>You are not authorised to access this service !</h1>");
		    out.println("</body>");
		    out.println("</html>");
		}
//		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
}
