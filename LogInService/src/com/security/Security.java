package com.security;

import org.json.simple.JSONObject;
/*import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
*/
import com.message.Message;
/*import java.io.File;
import java.io.FileReader;
import java.io.IOException;*/
import java.math.BigInteger;
import java.security.MessageDigest;
/*import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;*/
import java.util.HashMap;

public class Security {
	
	static HashMap <String , String> Token_Hash = new HashMap <>();
	static int hash_value = 0;
	
	public static void main(String[] args) 
	{
		Message mObj=new Message();
		mObj.recieveMessage();	
	}
	
	
	public void processRequest(JSONObject message)
	{
	    new Thread(new Runnable() 
	    {
	         @SuppressWarnings("unchecked")
			public void run() 
	         {
	              JSONObject response=parseMessage(message);
	              response.put("queue", "gateway");
	              Message m = new Message();
	              response.put("ip", m.getGatewayAddress());
	              if(response!=null)
	              {
	            	  Message obj=new Message();
	            	  obj.sendMessage(response);
	              }  
	         }
	    }).start();  
	}
	
/*	public static String readFile() throws IOException
    {
        String content = null;
        File file = new File("/home/muzammil/Desktop/message.txt"); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){reader.close();}
        }
        return content;
    }
	
	public static void main(String[] args) throws IOException
	{
		//String t="{"service_name":"security","ip":"localhost","type":"service_request","parameters":[{"type":"sd_login"},{"username":"rohit"},{"password":"123"}],"request_id":"1","queue":"security"}";
		String t = readFile();
		System.out.println(t);
		JSONParser parser=new JSONParser();
        try 
        {
			JSONObject json=(JSONObject)parser.parse(t);
			parseMessage(json);
		} 
        catch (ParseException e) 
        {
			e.printStackTrace();
		}
        
	}*/
	
	@SuppressWarnings({ "unchecked"})
	public JSONObject parseMessage(JSONObject message)
	{
		JSONObject response=null;
		System.out.println("m: " + message);
		try
		{
			JSONObject parameters = (JSONObject) message.get("parameters");
			String type = (String) parameters.get("type");
			
			switch(type)
			{
				case "sd_login":		response=Sd_logIn(parameters);
										break;
				case "authorization":	response=Authorization(parameters);
										break;
				case "logout":			response=logout(parameters);
										break;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			response.put("status", "0");
			Message m=new Message();
			m.logMessage("ERROR", "SECURITY: Exception occured "+e.getMessage());
		}
		return response;
	}
	
	@SuppressWarnings({ "unchecked" })
	public JSONObject Sd_logIn(JSONObject message) throws Exception
	{
		String url = "http://localhost:8080/Serverless/";
		String server_name =  "xyzservlet?";
		url += server_name;
		String urlString = "";
		
		JSONObject response=new JSONObject();
	    int user_flag = 0;
	    String username=(String)message.get("username");
	    String password=(String)message.get("password");
	    
	    /* Check credentials in the database for sd and call Data_Service(DB_name,Table_name,username,password)
	     * return output boolean
	     * if boolean = true then userflag = 1
	     * else user_flag = 0
	     */
	    
	    urlString += "type=select_query"; 
	    //urlString += "db_name="+DB_name;
	    //urlString += "table_name"+table_name;
	    urlString += "&username="+username;
	    urlString += "&password="+password;
	    urlString = url + urlString;
	    Message m = new Message();
	    JSONObject getDataService = m.callServiceURL(urlString);
	    if((String)getDataService.get("status")== "1")
	    	user_flag = 1;
	    else
	    	user_flag = 0;
	    	
	    if(user_flag == 1)
	    {
	    	response.put("status","1");
	    	String token  = token_creation(username);
	    	response.put("token",token);
	    	
	    	// Call DataService to insert token w.r.t sd giving required parameters if needed 
		   
        }
        
	    else
	    	response.put("status","0");
	    
	    return response;
	
	}
	
	
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	public JSONObject Authorization(JSONObject message)
	{
		String url = "http://localhost:8080/Serverless/";
		String server_name =  "xyzservlet?";
		url += server_name;
		String username;
		String urlString = "";
		JSONObject response=new JSONObject();
		
	    String Token=(String)message.get("token");
	    String service=(String)message.get("service_name");
	    
		if(Token_Hash.get(Token) == null)
			response.put("status","0");
		else if(Token_Hash.get(Token) != null)
		{
			
			/* Call Data Service for checking services available for particular user.
			 * With parameters as username and service_name.
			 * returns status as boolen true or false
			 * */
			username = Token_Hash.get(Token);
			urlString+="type=select_query"; 
			urlString += "&token=" + Token;
			urlString += "&username=" + username;
			urlString = url + urlString; 
			
			Message m = new Message();
		    JSONObject getDataService = m.callServiceURL(urlString); 
		    
		    if((String)getDataService.get("status")== "1")
		    	response.put("status","1");
		    
		    else
		    	response.put("status","0");
			
		}
	return response;
	}
	
	
	
	public synchronized static int incrementHash() 
    {
        hash_value++;
        if(hash_value==Integer.MAX_VALUE)
        	hash_value=0;
        return hash_value;
    }
	
	
	
	public String token_creation(String input) throws Exception
	{
		MessageDigest msg;
		String token = null;
		int x = incrementHash();
		try 
		{
			msg = MessageDigest.getInstance("MD5");
			msg.reset();
			String temp = String.valueOf(x);
			msg.update(temp.getBytes());
			byte[] digest = msg.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			//padding is here to get 32 bit token
			while(hashtext.length() < 32 )
			  hashtext = "0"+hashtext;
			
			token = hashtext;
			
		}
		catch (java.security.NoSuchAlgorithmException e)
		{
			throw e;
		}
		Token_Hash.put(token,input);
		return token;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public JSONObject logout(JSONObject message)
	{
		JSONObject response=new JSONObject();
	    String Token=(String)message.get("token");
	    
	    if(Token_Hash.get(Token) == null)
			response.put("status","1");
		else if(Token_Hash.get(Token) != null)
		{
			Token_Hash.remove(Token);
			response.put("status","1");
		}	
	    
		return response;
	}


}
