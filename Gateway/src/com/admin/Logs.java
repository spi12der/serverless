package com.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setAttribute("message", "{'details':[{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Available'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Available'},{'port':'8080','ip':'10.0.2.102','status':'Available'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Available'},{'port':'8080','ip':'10.0.2.102','status':'Available'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Available'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Available'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'},{'port':'8080','ip':'10.0.2.102','status':'Occupied'}]}");
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
