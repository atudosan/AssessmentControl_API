package com.api.ask.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;  

public class DB_Operations {
	
		//this method will get the reponse from DB in stored into a map variable
		public HashMap<String, String> getSqlResultInMap(String sql) {  
            HashMap<String, String> data_map = new HashMap<String, String>();
            System.out.println("Calling getSqlResultInMap method");

			try{  
				Class.forName("com.mysql.cj.jdbc.Driver");  
				Connection con=DriverManager.getConnection(  
					"jdbc:mysql://24.4.202.10:3307/application?serverTimezone=UTC","testuser","password"); 
						
				Statement stmt=con.createStatement();  
				ResultSet rs=stmt.executeQuery(sql);  
	            ResultSetMetaData md = rs.getMetaData();

	            while (rs.next()) {            
	                for (int i = 1; i <= md.getColumnCount(); i++) {
	                    data_map.put(md.getColumnName(i), rs.getString(i));
	                }
	            }
	           System.out.println("+++"+data_map);
				con.close();  
			}catch(Exception e){ System.out.println(e);}
			return data_map;  
		}  

}