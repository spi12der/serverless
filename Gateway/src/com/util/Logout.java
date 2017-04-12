package com.util;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String type=(String)session.getAttribute("usertype");
		SLUtil ob=new SLUtil();
		try
		{
			JSONObject message=ob.logout((String)session.getAttribute("token"));
			String status=(String)message.get("status");
			if(status.equalsIgnoreCase("1"))
			{
				response.sendRedirect("/Serverless/JSP/login.jsp");
			}
			else
			{
				if(type.equalsIgnoreCase("admin"))
					response.sendRedirect("/Serverless/JSP/ADMIN/AdminHome.jsp?message=Unable to logout. Retry..");
				else
					response.sendRedirect("/Serverless/JSP/USER/UserHome.jsp?message=Unable to logout. Retry..");
			}
		}
		catch (Exception e) 
		{
			if(type.equalsIgnoreCase("admin"))
				response.sendRedirect("/Serverless/JSP/ADMIN/AdminHome.jsp?message=Unable to logout. Retry..");
			else
				response.sendRedirect("/Serverless/JSP/USER/UserHome.jsp?message=Unable to logout. Retry..");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
