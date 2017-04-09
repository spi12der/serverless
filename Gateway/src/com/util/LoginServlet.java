package com.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		SLUtil slObj=new SLUtil();
		JSONObject message=slObj.login(username, password);
		String status=(String)message.get("status");
		if(status.equalsIgnoreCase("1"))
		{
			String type=(String)message.get("usertype");
			if(type.equalsIgnoreCase("admin"))
			{
				response.sendRedirect("/Serverless/JSP/ADMIN/AdminHome.jsp");
			}
			else
			{
				response.sendRedirect("/Serverless/JSP/USER/UserHome.jsp");
			}
		}	
		else
		{
			response.sendRedirect("/Serverless/JSP/login.jsp?message=Invalid credentials");
		}	
	}

}
