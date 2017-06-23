package com.oxchains.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBHelper {

	public static String url;
	public static String clazz;
	public static String user;
	public static String password;

	private static Connection conn = null;

	static{
		try {
			InputStream fis=DBHelper.class.getResourceAsStream("/db.properties");  //加载数据库配置文件到内存中  
	        Properties p=new Properties();  
	        p.load(fis);  
	          
	        clazz=p.getProperty("driver_class");      //获取数据库配置文件  
	        url=p.getProperty("driver_url");  
	        user=p.getProperty("database_user");  
	        password=p.getProperty("database_password");  
			Class.forName(clazz);
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
	}
	
	public DBHelper() {
	}
	
	public static Connection openCon(){
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return conn;
	}

	public static void close() {
		try {
			if(conn!=null){
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
}
