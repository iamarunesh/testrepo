package com;

import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Build_map {
    
	
	public String pre_auto() throws IOException {
    	 String sourceLine;
         String content = "";
      
         Build_Automation wep=new Build_Automation();
         
		 Properties prop = new Properties();
         InputStream inStream = wep.loadPropertiesFile();
         prop.load(inStream);
        
         // The URL address of the page to open.
         String pp=prop.getProperty("hudson_url"); 
         URL address = new URL(pp);
                                                     
         // Open the address and create a BufferedReader with the source code.
         InputStreamReader pageInput = new InputStreamReader(address.openStream());
         BufferedReader source = new BufferedReader(pageInput);        
                
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
         String bn;
         bn=content.substring(content.indexOf("#")+1, content.indexOf("#")+4);
         bn=bn.trim();
        
         pageInput.close();
         source.close();
         return bn;
     }
	
	
}
