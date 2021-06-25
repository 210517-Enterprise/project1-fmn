package com.revature.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;


public class Configuration {
	
	private static Logger log = Logger.getLogger(Configuration.class);
	
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	private List<Metamodel<Class<?>>> metamodelList;
	
	public Configuration(String url, String username, String password){
		this.dbUrl = url;
		this.dbUsername = username;
		this.dbPassword = password;
		
		try {
			writeConfigToConnectionPropertiesFile();
		} catch (IOException e) {
			log.error("Failure to configure connection to database" + e.getMessage());
		}
	}
	
	private void writeConfigToConnectionPropertiesFile() throws IOException {
		Properties props = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		OutputStream out = new FileOutputStream("connection.properties");
		
		props.load((loader.getResourceAsStream("connection.properties")));
		props.setProperty("url", this.dbUrl);
		props.setProperty("username", this.dbUsername);
		props.setProperty("password", this.dbPassword);
		
		props.store(out, null);
		out.close();
		System.out.println("Loaded and stored: " + props);
	}
	
	public Configuration addAnnotatedClass(Class annotatedClass) {
		if(this.metamodelList == null)
			this.metamodelList = new ArrayList<>();
		
		//The of() method transforms a class into an appropriate data model
		//to be transposed into a relation db object
		this.metamodelList.add(Metamodel.of(annotatedClass));
		
		return this;
	}
	
	//need a method to configure db info (url/user/pass)--> write to application.properties
	
	public List<Metamodel<Class<?>>> getMetamodels(){
		return (this.metamodelList == null) ? Collections.emptyList() : this.metamodelList;
	}

}
