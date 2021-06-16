package com.revature.util;

import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class ConnectionUtil {
	
	/**
	 * JDBC Driver name and Database Credentials
	 */
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String JDBC_URL = System.getenv("jdbc:postgresql://nick-rdbms.cvpo3qhbh5qs.us-east-2.rds.amazonaws.com:5432/postgres?currentSchema=public");
	static final String JDBC_USERNAME = System.getenv("DB_USERNAME");
	static final String JDBC_PASSWORD = System.getenv("DB_PASSWORD");
	
	//no we will create the pool for connecitons
	private static GenericObjectPool gPool = null;
	
	/**
	 * Method aims to setup the connection pool which will have at most 10 active connections
	 * @return
	 * @throws Exception
	 */
	public DataSource setUpPool() throws Exception {
		//create an instance of GenericObjectPool
		Class.forName(JDBC_DRIVER);
		gPool = new GenericObjectPool();
		gPool.setMaxActive(10);
		//create a connectionfactory obejct which will allow the pool to connect to DB
		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD); 
		//Create a PoolableConnectionFactory that will wrap around the Connection Object created by the above connectionFactor
		//in order to add pooling functionality to the program.
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		return new PoolingDataSource(gPool);
	}

	/**
	 * Method will return the GOP to called method, allowing for CRUD operations to be preformed
	 * @return
	 */
	public GenericObjectPool getConnectionPool() {
			System.out.println("Connection to Pool Established");
			return gPool;
	}
	
	/**
	 * Method aims to print the current status of the Database(isConnected/!isConnected)
	 */
	public void printDBStatus() {
		System.out.println("Max: " + getConnectionPool().getMaxActive() + "; active: " + getConnectionPool().getNumActive() 
				+ "; Idle: " + getConnectionPool().getNumIdle());
	}

	public static void main(String[] args) {
		ConnectionUtil conn = new ConnectionUtil();
		conn.getConnectionPool();
 }
}
