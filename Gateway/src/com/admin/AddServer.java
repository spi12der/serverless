package com.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.util.SLUtil;

/**
 * Servlet implementation class AddServer
 */
@WebServlet("/AddServer")
public class AddServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddServer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String ip=request.getParameter("ip");
		SLUtil obj=new SLUtil();
		try
		{
			JSONObject message=obj.addServer(username, password, ip);
			String status=(String)message.get("status");
			if(status.equalsIgnoreCase("1"))
				response.sendRedirect("/Serverless/JSP/ADMIN/AddServer.jsp?message=Server added sucessfully");
			else
				response.sendRedirect("/Serverless/JSP/ADMIN/AddServer.jsp?message=Unable to add server");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			response.sendRedirect("/Serverless/JSP/ADMIN/AddServer.jsp?message=Unable to add server");
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
