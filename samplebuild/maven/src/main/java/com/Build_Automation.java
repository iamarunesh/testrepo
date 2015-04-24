package com;

import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Statement;
import java.util.Properties;


public class Build_Automation {
        
	
	public  void auto()throws IOException, SQLException, ParseException {
		// System.out.println("Array map");
		 Map<String, String> m=new HashMap<String, String>();
         m.put("Jan", "01");
         m.put("Feb", "02");
         m.put("Mar", "03");
         m.put("Apr", "04");
         m.put("May", "05");
         m.put("Jun", "06");
         m.put("Jul", "07");
         m.put("Aug", "08");
         m.put("Sep", "09");
         m.put("Oct", "10");
         m.put("Nov", "11");
         m.put("Dec", "12");
         // The URL address of the page to open.
         
         TestJDBC db1=new TestJDBC();
         Connection conn = db1.establishConnection();
         //System.out.println("DB connection");
        
         // System.out.println("created successfully");
         int count = 0;
         int counting=0;
         int bno=0;
         Build_map mapobj=new Build_map();
         String bn = mapobj.pre_auto();
         int bnew=Integer.parseInt(bn);
         Build_Automation wep=new Build_Automation();
         
 		 Properties prop = new Properties();
         InputStream inStream = wep.loadPropertiesFile();
        
         prop.load(inStream);
            
         String db_name = prop.getProperty("database_name");
         String tb_name = prop.getProperty("table_name");
         String table_query = "show tables from " + db_name + "";
         PreparedStatement stmt=conn.prepareStatement(table_query);
      	 
           ResultSet rs1 = stmt.executeQuery();
           while (rs1.next()) {
           //System.out.println(rs1.getString(1));
               if ((rs1.getString(1)).equals(tb_name)) {
                   count++;
               }
           }
           if (count == 0)
           {
        	   bno=0;
        
           }
           else
           {
        	   PreparedStatement statement1 = conn.prepareStatement("select max(build_no) from "+ db_name+ "."+tb_name+"");
        	   ResultSet rs = statement1.executeQuery();
        
        	   while(rs.next())
        	   	{
        		   bno=rs.getInt(1);
        	   	}

           }
        
            
       
           String create_query = null;
         
        
           if (count == 0)
           {
              create_query = "create table "+ db_name+ "."+ tb_name+ " (build_no int(20),build_date date,build_duration varchar(20),build_status varchar(20))";
              stmt.executeUpdate(create_query);
              System.out.println("New Table named " +tb_name+ " is Created inside the database named "+db_name+"");
           }
           else
           {
        	   System.out.println("Table named " +tb_name+ "  in database "+db_name+" already exist");
           }
           for(int b=bno+1;b<=bnew;b++)
           {
          
            	String sourceLine;
                String content = "";
               
                String sourceLine1;
                String content1 = "";
                
                // Open the address and create a BufferedReader with the source code.
                String aa=prop.getProperty("hudson_url")+b+"/console"; 
                URL address = new URL(aa);
                String bb=prop.getProperty("hudson_url")+b; 
                URL address1 = new URL(bb);
              
                InputStreamReader pageInput = new InputStreamReader(address.openStream());
                InputStreamReader pageInput1 = new InputStreamReader(address1.openStream());
                
                BufferedReader source = new BufferedReader(pageInput);
                BufferedReader source1 = new BufferedReader(pageInput1);
                       
                // Append each new HTML line into one string. Add a tab character.
            
                while ((sourceLine = source.readLine()) != null)
                	content += sourceLine + "\t";
                
                    // Remove script tags & inclusive content
                	Pattern script = Pattern.compile("<script.*?>.*?</script>");
                	Matcher mscript = script.matcher(content);
                	while (mscript.find()) content = mscript.replaceAll("");

                	// Remove primary HTML tags
                	Pattern tag = Pattern.compile("<.*?>");
                	Matcher mtag = tag.matcher(content);
                	while (mtag.find()) content = mtag.replaceAll("");

                	// Remove comment tags & inclusive content
                	Pattern comment = Pattern.compile("<!--.*?-->");
                	Matcher mcomment = comment.matcher(content);
                	while (mcomment.find()) content = mcomment.replaceAll("");

                	// Remove special characters, such as &nbsp;
                	Pattern sChar = Pattern.compile("&.*?;");
                	Matcher msChar = sChar.matcher(content);
                	while (msChar.find()) content = msChar.replaceAll("");

                	// Remove the tab characters. Replace with new line characters.
                	Pattern nLineChar = Pattern.compile("\t+");
                	Matcher mnLine = nLineChar.matcher(content);
                	while (mnLine.find()) content = mnLine.replaceAll("\n");

                	// Print the clean content & close the Readers                 
                	int new_build=0;
                	String min_val;
                	String min_val1="";
                	int min_val2=0;
                           
                	String build_duration=content.substring(content.indexOf("Total time")+12, content.indexOf("Total time")+21);
                	{
                		if ( build_duration.contains("minute"))
                		{
            	
                			new_build=build_duration.indexOf("minute");
                			min_val=build_duration.substring(0, new_build);
                	       	min_val=min_val.trim();
                			min_val2=Integer.parseInt(min_val);
                			min_val2=min_val2*60;
                		
            	
                		}	
            
                		else if ( build_duration.contains("second"))
                		{
            	
                			new_build=build_duration.indexOf("second");
                			min_val1=build_duration.substring(0, new_build);
                			min_val1=min_val1.trim();
                			min_val2=Integer.parseInt(min_val1);                		       
                		}
                	}
                	while ((sourceLine1 = source1.readLine()) != null)
                    	content1 += sourceLine1 + "\t";
         
                    // Remove script tags & inclusive content
                	Pattern script1 = Pattern.compile("<script.*?>.*?</script>");
                	Matcher mscript1 = script1.matcher(content1);
                	while (mscript1.find()) content1 = mscript1.replaceAll("");

                	// Remove primary HTML tags
                	Pattern tag1 = Pattern.compile("<.*?>");
                	Matcher mtag1 = tag1.matcher(content1);
                	while (mtag1.find()) content1 = mtag1.replaceAll("");

                	// Remove comment tags & inclusive content
                	Pattern comment1 = Pattern.compile("<!--.*?-->");
                	Matcher mcomment1 = comment1.matcher(content1);
                	while (mcomment1.find()) content1 = mcomment1.replaceAll("");

                	// Remove special characters, such as &nbsp;
                	Pattern sChar1 = Pattern.compile("&.*?;");
                	Matcher msChar1 = sChar1.matcher(content1);
                	while (msChar1.find()) content1 = msChar1.replaceAll("");

                	// Remove the tab characters. Replace with new line characters.
                	Pattern nLineChar1 = Pattern.compile("\t+");
                	Matcher mnLine1 = nLineChar1.matcher(content1);
                	
                	while (mnLine1.find()) content1 = mnLine1.replaceAll("\n");
                	 String date;
                	 {
                		 if((b>9)&&(b<1000))  
                     
                		 {
                			 String build_mon=content1.substring(content1.indexOf("Build #")+18, content1.indexOf("Build #")+23);
                			 build_mon=build_mon.replace('(' , ' ');
                			 build_mon=build_mon.trim();
                     
                			 String build_date=content1.substring(content1.indexOf("Build #")+23, content1.indexOf("Build #")+26);
                			 build_date=build_date.replace(',' , ' ');
                			 build_date=build_date.trim();
                			 
                			 String build_year=content1.substring(content1.indexOf("Build #")+24, content1.indexOf("Build #")+32);
                			 build_year=build_year.replace(',' , ' ');
                			 String newbuild_year=build_year.substring(2, 8);
                			 newbuild_year=newbuild_year.trim();
                			 String new_build_year=newbuild_year.substring(0, 4);
                			 new_build_year=new_build_year.trim();
                			 
                			 date=new_build_year+"-"+m.get(build_mon)+"-"+build_date;
                    
                		 }
                		 else if(b<=9)
                		 {
                			 String build_mon=content1.substring(content1.indexOf("Build #")+18, content1.indexOf("Build #")+21);
                			 build_mon=build_mon.replace('(' , ' ');
                			 build_mon=build_mon.trim();
                       
                			 String build_date=content1.substring(content1.indexOf("Build #")+22, content1.indexOf("Build #")+26);
                			 build_date=build_date.replace(',' , ' ');
                			 String newbuild_date=build_date.substring(0, 3);
                			 newbuild_date=newbuild_date.trim();
                			 
                			 String build_year=content1.substring(content1.indexOf("Build #")+25, content1.indexOf("Build #")+33);
                			 build_year=build_year.replace(',' , ' ');
                			 String newbuild_year=build_year.substring(0, 5);
                			 newbuild_year=newbuild_year.trim();
                			
                			 date=newbuild_year+"-"+m.get(build_mon)+"-"+newbuild_date;
                		 }
                		 else
                		 {
                			 String build_mon=content1.substring(content1.indexOf("Build #")+19, content1.indexOf("Build #")+24);
                			 build_mon=build_mon.replace('(' , ' ');
                			 build_mon=build_mon.trim();
                       
                			 String build_date=content1.substring(content1.indexOf("Build #")+24, content1.indexOf("Build #")+27);
                			 build_date=build_date.replace(',' , ' ');
                			 build_date=build_date.trim();
                			
                			 String build_year=content1.substring(content1.indexOf("Build #")+26, content1.indexOf("Build #")+34);
                			 build_year=build_year.replace(',' , ' ');
                			 String newbuild_year=build_year.substring(1, 7);
                			 newbuild_year=newbuild_year.trim();
                			 
                			 date=newbuild_year+"-"+m.get(build_mon)+"-"+build_date;
                		 }

                	
            
                		 String build_status=content.substring(content.indexOf("Finished:")+10, content.indexOf("Finished:")+21);
                		 build_status=build_status.trim();
                   
                		 pageInput.close();
                		 source.close();
                		 pageInput1.close();
                		 source1.close();
           
                		 if(bno<b)
                		 {
                		
                	
                			PreparedStatement statement2 = conn.prepareStatement("INSERT INTO "+ db_name+ "."+ tb_name+ " values('"+b+"','"+date+"', '" +min_val2+ "','" +build_status+ "')");
                			statement2.executeUpdate();
                		

                		 }
                		 else
                		 {
                		 }
                		 counting++;
                   	 }
           }
                	 

                	 if(counting==0)
                	 {
                		 System.out.println("No records inserted. Since all the builds in the "+tb_name+" table are upto Date");
                	 }
                	 else if (count==0)
                	 {
                		 System.out.println("inserted "+counting+" number of builds into the "+tb_name+" table");
                	 }
                	 else
                	 {
                		 System.out.println("updated "+counting+" number of builds into the "+tb_name+"");
                	 }
                	 }
           
		

    public InputStream loadPropertiesFile() {
    	
        InputStream in = null;
        try {
        	
            in = new FileInputStream("resources/config.properties");
       
        } catch (Exception e) {
        	
            System.err.println(e.getMessage());
            e.printStackTrace();
            
        }
        return in;
    }

        public static void main (String[] args) throws IOException, SQLException, ParseException {
        	
        	
        	    Build_Automation wep=new Build_Automation();
        	        wep.auto();
         }
	}
