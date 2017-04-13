package com.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class registration
 */
@WebServlet("/registration")
public class RegisterServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RegisterServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		SLUtil slObj=new SLUtil();
		try
		{
			String res=slObj.registerUser(username, password, email);
			if(res.equalsIgnoreCase("1"))
			{
				response.sendRedirect("/Serverless/JSP/login.jsp?message=User added successfully");
			}
			else
			{
				response.sendRedirect("/Serverless/JSP/registration.jsp?message=Unable to add user");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			response.sendRedirect("/Serverless/JSP/registration.jsp?message=Unable to add user");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);	
	}

}
