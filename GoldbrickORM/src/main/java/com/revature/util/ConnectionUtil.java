package com.revature.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	Properties props = new Properties();
	ClassLoader loader = Thread.currentThread().getContextClassLoader();

	

	//now we will create the pool for connecitons
	private static GenericObjectPool gPool = null;
	
	/**
	 * Method aims to setup the connection pool which will have at most 10 active connections
	 * @return
	 * @throws Exception
	 */
	public DataSource setUpPool() throws Exception {
		
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
	 * Method will return the GOP to called method, allowing for CRUD operations to be preformed
	 * @return
	 */
	public static GenericObjectPool getConnectionPool() {
		
			//System.out.println("getConnectionPool() called");
			return gPool;
	}
	
	/**
	 * Method aims to print the current status of the Database(isConnected/!isConnected)
	 */
	public void printDBStatus() {
		
		System.out.println("Max: " + getConnectionPool().getMaxActive() + "; active: " + getConnectionPool().getNumActive() 
				+ "; Idle: " + getConnectionPool().getNumIdle());
	}

	
	public static void main(String[] args) throws Exception {
		ConnectionUtil jdbcObj = new ConnectionUtil();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connObj = null;
		
		try {
			DataSource datasource = jdbcObj.setUpPool();
			jdbcObj.printDBStatus();
			
			System.out.println("weeeeeee");
			connObj = datasource.getConnection();
			
			ps = connObj.prepareStatement("SELECT * FROM heroes");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				System.out.println("Hero name is: " + rs.getString("hero_name"));
			}
			
			
		} catch(SQLException e) {
			//log.error("Could not retrieve");
			e.printStackTrace();
		}
		
	
 }
}
