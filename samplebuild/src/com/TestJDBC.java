package com;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class TestJDBC {

	public Connection establishConnection() throws IOException {
		// TODO Auto-generated method stub
		Connection conn = null;
		Build_Automation wep=new Build_Automation();
        
		 Properties prop = new Properties();
        InputStream inStream = wep.loadPropertiesFile();
        prop.load(inStream);
       // System.out.println("properties ");
		try
		{
			// Step 1: Load the JDBC driver.
			Class.forName("com.mysql.jdbc.Driver") ;
			//System.out.println("Driver loaded success");
			
			   String u_name = prop.getProperty("user_name");
			   String pwd = prop.getProperty("password");


			// Step 2: Establish the connection to the database.
			String url = prop.getProperty("database_url");
			conn = DriverManager.getConnection(url,u_name,pwd );
			//System.out.println("test connection success");
			
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}
}
