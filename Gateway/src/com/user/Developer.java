package com.user;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.util.SLUtil;

/*import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;*/

/**
 * Servlet implementation class Developer
 */
@WebServlet("/Developer")
public class Developer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 5000 * 1024;
    private int maxMemSize = 4 * 1024;
   	private File file ;
	
   	
   	public void init( )
   	{
        // Get the file location where it would be stored.
        filePath = getServletContext().getInitParameter("file-upload"); 
     }
   	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Developer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("rawtypes")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String serviceName=request.getParameter("name");
		// TODO Auto-generated method stub
		  isMultipart = ServletFileUpload.isMultipartContent(request);
	      response.setContentType("text/html");
	      java.io.PrintWriter out = response.getWriter( );
	      if( !isMultipart )
	      {
	         out.println("<html>");
	         out.println("<head>");
	         out.println("<title>Servlet upload</title>");  
	         out.println("</head>");
	         out.println("<body>");
	         out.println("<p>No file uploaded</p>"); 
	         out.println("</body>");
	         out.println("</html>");
	         return;
	      }
	      DiskFileItemFactory factory = new DiskFileItemFactory();
	      // maximum size that will be stored in memory
	      factory.setSizeThreshold(maxMemSize);
	      // Location to save data that is larger than maxMemSize.
	      factory.setRepository(new File("/home/rohit/temp"));
	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // maximum file size to be uploaded.
	      upload.setSizeMax( maxFileSize );
	      String path=new String();
	      try
	      { 
		      // Parse the request to get file items.
		      List fileItems = upload.parseRequest(request);
			
		      // Process the uploaded file items
		      Iterator i = fileItems.iterator();
	
		      while ( i.hasNext () ) 
		      {
		         FileItem fi = (FileItem)i.next();
		         if ( !fi.isFormField () )	
		         {
		            fi.getFieldName();
		            String fileName = fi.getName();
		            fi.getContentType();
		            fi.isInMemory();
		            fi.getSize();
		            // Write the file
		            if( fileName.lastIndexOf("\\") >= 0 )
		            {
		            	path=filePath +"Developer/"+ fileName.substring( fileName.lastIndexOf("\\"));
		            	file = new File(path) ;
		            }
		            else
		            {
		            	path=filePath +"Developer/"+ fileName.substring(fileName.lastIndexOf("\\")+1);
		            	file = new File(path) ;
		            }
		            fi.write( file ) ;
		         }
		      }
		      SLUtil obj=new SLUtil();
		      obj.deployService(path, serviceName);
		      response.sendRedirect("/Serverless/JSP/USER/Developer.jsp?message=Service deployed sucessfully");
	      }
	      catch(Exception e) 
	      {
	    	  e.printStackTrace();
	    	  response.sendRedirect("/Serverless/JSP/USER/Developer.jsp?message=Unable to deploy service. Retry");
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