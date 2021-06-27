package com.revature.util;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;


public class ConnectionUtil {
	
	private static Logger log = Logger.getLogger(ConnectionUtil.class);
	
	/**
	 * Holder for JDBC Credentials
	 */

	static String JDBC_DRIVER = "org.postgresql.Driver";
	static String JDBC_URL = "";
	static String JDBC_USERNAME = "";
	static String JDBC_PASSWORD = "";
	
	static Properties props = new Properties();
	static ClassLoader loader = Thread.currentThread().getContextClassLoader();

	

	//now we will create the pool for connections
	private static GenericObjectPool gPool = null;
	
	/**
	 * Method aims to setup the connection pool which will have at most 10 active connections
	 * @return
	 * @throws Exception
	 */
	public static DataSource setUpPool() throws Exception {
		
		try { //pull data from connection.properties file
			Class.forName(JDBC_DRIVER);
			props.load((loader.getResourceAsStream("connection.properties")));
		    JDBC_URL = props.getProperty("url");
 			JDBC_USERNAME = props.getProperty("username");
			JDBC_PASSWORD = props.getProperty("password");
		} catch(IOException e) {
			log.warn("Could not locate connection.properties file");
		}
		
		//create an instance of GenericObjectPool	
		gPool = new GenericObjectPool();
		gPool.setMaxActive(10);
		
		//create connection factory to abstract the initalization of connections
		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
		
		//Create a PoolableConnectionFactory that will wrap around the Connection Object created by the above connectionFactor
		//in order to add pooling functionality to the program.
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		
		try {
			if(gPool != null) { //if we get connnection, return it
				System.out.println("Pool Setup Sucessful!");
				return new PoolingDataSource(gPool);
			}
			if(gPool == null) {
				log.error("gPool was null.. no connection established");
				return null;
			}
			} catch(Exception e) { //catch error and log it
				log.error("Process has Failed to establish connetion to databse...", e);
				return null; //return null to disconnect 
			}
		
		
		return new PoolingDataSource(gPool);
	}

	/**
	 * Method will return the GOP to called method, allowing for CRUD operations to be performed
	 * @return
	 */
	public static GenericObjectPool getConnectionPool() {
		
			//System.out.println("getConnectionPool() called");
			return gPool;
	}
	
	/**
	 * Method returns the current amount of threads that are occupied
	 * @return
	 */
	public String getDBStatus() {
		
		return "Max: " + getConnectionPool().getMaxActive() + "; active: " + getConnectionPool().getNumActive() 
				+ "; Idle: " + getConnectionPool().getNumIdle();
	}

	
}
