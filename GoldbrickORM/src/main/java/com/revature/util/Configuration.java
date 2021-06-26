package com.revature.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This file provides the configuration for the database
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/16/21
 *
 */
public class Configuration {

	private static Logger log = Logger.getLogger(Configuration.class);

	/**
	 * The database URL
	 */
	private String dbUrl;

	/**
	 * The database username
	 */
	private String dbUsername;

	/**
	 * The database password
	 */
	private String dbPassword;

	/**
	 * The list of meta-models used to create the tables in the database
	 */
	private List<Metamodel<Class<?>>> metamodelList;

	/**
	 * The constructor for the Configuration object
	 * 
	 * @param url      the URL for the database
	 * @param username the username to access the database
	 * @param password the password to access the database
	 */
	public Configuration(String url, String username, String password) {
		this.dbUrl = url;
		this.dbUsername = username;
		this.dbPassword = password;

		try {
			writeConfigToConnectionPropertiesFile();
		} catch (IOException e) {
			log.error("Failure to configure connection to database" + e.getMessage());
		}
	}

	/**
	 * This method writes the database URL, username, and password to the
	 * application's connection.properties file to facilitate access to the database
	 * 
	 * @throws IOException
	 */
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

	/**
	 * This method creates and adds a meta-model of a class to the list of
	 * meta-models to create a table in the database to store annotatedClass type
	 * objects
	 * 
	 * @param annotatedClass class to create a table in the database to store
	 *                       annotatedClass type objects
	 */
	public void addAnnotatedClass(Class annotatedClass) {
		if (this.metamodelList == null)
			this.metamodelList = new ArrayList<>();

		// The of() method transforms a class into an appropriate data model
		// to be transposed into a relation db object
		this.metamodelList.add(Metamodel.of(annotatedClass));

	}

	/**
	 * Getter method for the list of meta-models
	 * @return the list of meta-models
	 */
	public List<Metamodel<Class<?>>> getMetamodels() {
		return (this.metamodelList == null) ? Collections.emptyList() : this.metamodelList;
	}

}
