package com.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class FileServices 
{
	private String home = "/home/rama/Desktop/20162029/IAS";
	static Logger logger = Logger.getLogger(FileServices.class.getName());
	public JSONObject createUserDir(JSONObject request)throws Exception
	{
		JSONObject response=new JSONObject();
		try{
			String directoryName = request.getString("username");
			if(directoryName == null) {
				logger.error("error, username is empty");
			}
			File f=new File(home + File.separator + directoryName);
			if(!f.isDirectory())
			{
				f.mkdir();
				response.put("status", "1");
				response.put("message", "User Directory created successfully");
				logger.info("directory for the app developer created successfully");
			} 
			else{
				response.put("status", "0");
				response.put("message", "User Directory creation failed");
				logger.info("directory creation failed");
			}
		}
		catch(Exception e){
			logger.error("Exception caught : " + e);
			response.put("status", "0");
			response.put("message", "Unable to Directory");
			e.printStackTrace();
		}
		return response;
	}
	
	public JSONObject createDir(JSONObject request)throws Exception
	{
		JSONObject response=new JSONObject();
		try{
			String username = request.getString("username");
			String directoryName = request.getString("directoryName");
			File f=new File(home + File.separator + username + File.separator + directoryName);
			if(!f.isDirectory())
			{
				f.mkdir();
				response.put("status", "1");
				response.put("message", "File create successfully");
			}
			else{
				response.put("status", "0");
				response.put("message", "User Directory create failed");
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception caught : " + e);
			response.put("status", "0");
			response.put("message", "Unable to Directory");
			e.printStackTrace();
		}
		return response;
	}
	
	public JSONObject createFile(JSONObject request)throws Exception
	{
		JSONObject response = null;
		try {
			response=new JSONObject();
			String username = request.getString("username");
			String directoryName = request.getString("directoryName");
			String fileName = request.getString("fileName");
			File f=new File(home + File.separator + username + 
					File.separator + directoryName + File.separator + fileName);
			if(!f.exists())
			{
				f.createNewFile();
				response.put("status", "1");
				response.put("message", "File create successfully");
			}
		}
		catch(IOException e) 
		{
			System.out.println("Exception caught : " + e);
			response.put("status", "0");
			response.put("message", "Unable to create file");
			System.out.println("Exception caught : " + e);
			e.printStackTrace();
		}
		return response;
	}

	
	public JSONObject updateFile(JSONObject request)throws Exception
	{
		JSONObject response = null;
		try {
			response=new JSONObject();
			String username = request.getString("username");
			String directoryName = request.getString("directoryName");
			String fileName = request.getString("fileName");
			String file = home + File.separator + username + 
					File.separator + directoryName + File.separator + fileName;
			File f=new File(file);
			if(!f.exists()) {
				f.delete();
			}
			f.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(request.getString("content"));
			bw.close();
			response.put("status", "1");
			response.put("message", "File updated successfully");
		} 
		catch (IOException e) 
		{
			response.put("status", "0");
			response.put("message", "Unable to update file");
			System.out.println("Exception caught : " + e);
			e.printStackTrace();
		}
		return response;
	}

	
	public JSONObject deleteFile(JSONObject request)throws Exception
	{
		JSONObject response = null;
		try {
			response=new JSONObject();
			String username = request.getString("username");
			String directoryName = request.getString("directoryName");
			String fileName = request.getString("fileName");
			String file = home + File.separator + username + 
					File.separator + directoryName + File.separator + fileName;
			File f=new File(file);
			if(f.exists())
			{
				f.delete();
				response.put("status", "1");
				response.put("message", "File Deleted successfully");
			}
			else
			{
				response.put("status", "0");
				response.put("message", "Unable to delete file");
			}	
		}
		catch (Exception e) 
		{
			response.put("status", "0");
			response.put("message", "Unable to update file");
			System.out.println("Exception caught : " + e);
			e.printStackTrace();
		}
		return response;
	}

	
	public JSONObject readFile(JSONObject request)throws Exception
	{
		JSONObject response=new JSONObject();
		String username = request.getString("username");
		String directoryName = request.getString("directoryName");
		String fileName = request.getString("fileName");
		String file = home + File.separator + username + 
				File.separator + directoryName + File.separator + fileName;
		File f=new File(file);
		if(f.exists())
		{
			try 
			{
				BufferedReader br = new BufferedReader(new FileReader(file));
				String fileContent=new String();
				String line;
				while ((line = br.readLine()) != null) 
					fileContent=fileContent+line;
				response.put("fileContent", fileContent);
				response.put("status", "1");
				response.put("message", "File read successful");
				br.close();
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Exception caught : " + e);
				e.printStackTrace();
				response.put("status", "0");
				response.put("message", "Unable to locate file");
			}
			catch (IOException e) 
			{
				System.out.println("Exception caught : " + e);
				e.printStackTrace();
				response.put("status", "0");
				response.put("message", "Unable to read file");
			}
		}
		else
		{
			response.put("status", "0");
		}
		return response;
	}
}