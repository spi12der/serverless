package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataService {
	// JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/";

	   //  Database credentials
	   static final String USER = "admin";
	   static final String PASS = "root";
	   @SuppressWarnings("unchecked")
	   JSONArray convert( ResultSet rs ) throws Exception{
		     JSONArray json = new JSONArray();
		     ResultSetMetaData rsmd = rs.getMetaData();

		     while(rs.next()) {
				   int numColumns = rsmd.getColumnCount();
				   JSONObject obj = new JSONObject();
				
				   for (int i=1; i<numColumns+1; i++) {
					   	String column_name = rsmd.getColumnName(i);
				
					     if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
					    	 obj.put(column_name, rs.getArray(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
					    	 obj.put(column_name, rs.getInt(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
					    	 obj.put(column_name, rs.getBoolean(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
					    	 obj.put(column_name, rs.getBlob(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
					    	 obj.put(column_name, rs.getDouble(column_name)); 
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
					    	 obj.put(column_name, rs.getFloat(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
					    	 obj.put(column_name, rs.getInt(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
					    	 obj.put(column_name, rs.getNString(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
					    	 obj.put(column_name, rs.getString(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
					    	 obj.put(column_name, rs.getInt(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
					    	 obj.put(column_name, rs.getInt(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
					    	 obj.put(column_name, rs.getDate(column_name));
					     }
					     else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
					    	 obj.put(column_name, rs.getTimestamp(column_name));   
					     }
					     else{
					    	 obj.put(column_name, rs.getObject(column_name));
					     }
				   }
				   json.add(obj);
		     }
		     return json;
	   }
	   Connection getConnection(String db_name) throws Exception{
		   
		   Connection conn = null;
		   
	      //STEP 2: Register JDBC driver
		   Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
		   System.out.println("Connecting to database...");
		   conn = DriverManager.getConnection(DB_URL + db_name, USER, PASS);
		   return conn;
	   }
	   
	   @SuppressWarnings("unchecked")
	   public JSONObject createTable(JSONObject message){
			   
			   JSONObject response = new JSONObject();
			   String dbname = (String)message.get("db_name");
			   String tblname = (String)message.get("tbl_name");
			   String attList = (String) message.get("attributes");
			   String[] attributes = attList.split(",");
			   String[] type = ((String) message.get("attributes_type")).split(",");
			   try {
				   Connection con = getConnection(dbname);
				   Statement stmt;
				   stmt = con.createStatement();
				   String query = "CREATE TABLE " + tblname + " ";
				   query += "(";
				   for(int i=0; i< attributes.length; i++){
					   query += attributes[i] + " " + type[i] + " ,";
				   }
				   query = query.substring(0, query.length() -1) + ")";
				   stmt.execute(query);
				   response.put("status", "1");
				   response.put("message", "Table successfully created");
			   }
			   catch (Exception e) {
					// TODO Auto-generated catch block
				   	System.out.println("Table Creation Failed");
				   	//System.out.println(e.getMessage());
				   	response.put("status", "0");
				   	response.put("message",e.getMessage());
					//e.printStackTrace();
			   }
			   return response;
	   }
	   
	   @SuppressWarnings("unchecked")
	   public JSONObject getRecords(JSONObject message){
		   //JSONArray responseArray = new JSONArray();
		   JSONObject response = new JSONObject();
		   String dbname = (String)message.get("db_name");
		   String tblname = (String)message.get("tbl_name");
		   String attList = (String) message.get("attributes");
		   String[] attributes = attList.split(",");
		   Statement stmt = null;
		   try {
		   		Connection con = getConnection(dbname);
		   		stmt = con.createStatement();
		   		String query = "SELECT ";
			   for(int i=0; i< attributes.length; i++){
				   query += attributes[i] + " ,";
			   }
			   query = query.substring(0, query.length() -1);
			   query += "FROM " + tblname;
			   
			   if(message.containsKey("conditions")){
				   query += " WHERE ";
				   String[] conditions = ((String) message.get("conditions")).split("#");
				   for(int i=0; i<conditions.length; i++)
					   query += conditions[i] + " AND";
				   query = query.substring(0, query.length() -3);
			   }
			   
			   ResultSet rs = stmt.executeQuery(query);
			   response.put("result", convert(rs));
			   response.put("status", "1");
			   response.put("message", "Successfully fetched the Result");
		   }
		   catch (Exception e) {
				// TODO Auto-generated catch block
			   	System.out.println("Table Fetching Failed");
			   	System.out.println(e.getMessage());
			   	response.put("status", "0");
			   	response.put("message",e.getMessage());
				//e.printStackTrace();
		   }
		   finally {
			    if (stmt != null) { 
			    	try {
			    		stmt.close();
			    	} catch (SQLException e) {
					// TODO Auto-generated catch block
			    		e.printStackTrace();
			    	} 
			    }
		   }
		   return response;
	   }
	   
	   @SuppressWarnings("unchecked")
	   public JSONObject insertRecord(JSONObject message){
		   JSONObject response = new JSONObject();
		   String dbname = (String)message.get("db_name");
		   String tblname = (String)message.get("tbl_name");
		   String attList = (String) message.get("attributes");
		   String[] attributes = attList.split(",");
		   String[] values = ((String) message.get("values")).split(",");
		   
		   Statement stmt = null;
		   try{
			   Connection con = getConnection(dbname);
			   stmt = con.createStatement();
			   String query = "INSERT INTO " + tblname + " (";
			   for(int i=0; i< attributes.length; i++){
				   query += attributes[i] + " ,";
			   }
			   query = query.substring(0, query.length() -1) + ")";
			   query += " VALUES (";
			   for(int i=0; i< values.length; i++){
				   query += '"' +values[i] + '"' + " ,";
			   }
			   query = query.substring(0, query.length() -1) + ")";
		   
			   if (stmt.executeUpdate(query) == 1){
				   response.put("status", "1");
				   response.put("message", "Successfully inserted record");
			   }
			   else{
				   response.put("status", "0");
				   response.put("message", "Record Insertion Failed");
			   }
		   }
		   catch(Exception e){
			   response.put("status", "0");
			   response.put("message", "Record Insertion Failed");
		   }
		   return response;
	   }
	   
	   @SuppressWarnings("unchecked") 
	   JSONObject createTest(){
		   JSONObject test = new JSONObject();
		   test.put("db_name", "hello");
		   test.put("tbl_name", "temp1");
		   String attributes = "Name,Roll,Marks";
		   String type = "varchar(20),varchar(20),INT";
		   test.put("attributes", attributes);
		   test.put("type", type);
		   return test;
	    }
	   
	   @SuppressWarnings("unchecked")
	   JSONObject selectTest(){
		   JSONObject test = new JSONObject();
		   test.put("db_name", "hello");
		   test.put("tbl_name", "temp1");
		   String attributes = "Name,Roll,Marks";
		   test.put("attributes", attributes);
		   
		   String conditions = "Name=\"Gaurav\"";
		   test.put("conditions", conditions);
		   return test;
	    }
	   
	   @SuppressWarnings("unchecked")
	   JSONObject insertTest(){
		   JSONObject test = new JSONObject();
		   test.put("db_name", "hello");
		   test.put("tbl_name", "temp1");
		   String attributes = "Name,Roll,Marks";
		   String values = "Gaurav,123,234";
		   test.put("attributes", attributes);
		   test.put("values", values);
		   return test;
	   }
	   
	   public static void main(String[] args) throws SQLException, JSONException {
			DataService fe = new DataService();
			JSONObject test = fe.createTest();
			JSONObject response = fe.createTable(test);
			System.out.println(response.toString());
			
			test = fe.insertTest();
			response = fe.insertRecord(test);
			System.out.println(response.toString());
			test = fe.selectTest();
			response = fe.getRecords(test);
			System.out.println(response.toString());
			   System.out.println("Goodbye!");
	   }
	   
}
