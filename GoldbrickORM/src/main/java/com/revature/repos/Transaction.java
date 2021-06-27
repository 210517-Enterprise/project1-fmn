package com.revature.repos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;

import org.apache.log4j.Logger;


/**
 * Class that defines SQL transaction management (commit, savepoint, rollback)
 * @author Frank Aurori, Mollie Morrow, Nick Gianino
 *
 */
public class Transaction {
	
	private static Transaction transaction = new Transaction();
	
	private HashMap<String, Savepoint> savepoints;
	
	private static Logger log = Logger.getLogger(Transaction.class);
	
	public Transaction() {
		savepoints = new HashMap();
	}
	
	public static Transaction getTransaction() {
		return transaction;
	}
	
	/**
	 * Turns on auto commit for specified connection
	 * @param conn
	 */
	public void enableAutoCommit(Connection conn) {
		
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Could not enable auto commit!");
		}
		
	}
	
	/**
	 * Commit all uncommitted transactions for specified connection
	 * @param conn
	 */
	public void commitTrans(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Unable to commit transaction!");
		}
	}
	
	/**
	 * Rollback all uncommitted transactions for specified conenction
	 * @param conn
	 */
	public void rollbackTrans(Connection conn) {
		
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Unable to rollback transaction!");
		}
	}
	
	/**
	 * Rollback to a prevous transaction, given the name of 
	 * the savepoint and specified connection
	 * @param name 
	 * @param conn
	 */
	public void rollbackTrans(String name, Connection conn) {
		
		try {
			if (savepoints.containsKey(name)) {
				conn.rollback(savepoints.get(name));
			} else {
				log.warn("Cannot rollback, no savepoint found under name: " + name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Cannot rollback to savepoint!");
		}
	}
	
	
	/**
	 * Method that sets the saved savepoint 
	 * @param name
	 * @param conn
	 */
	public void setSavepoint(String name, Connection conn) {
		
		try {
			Savepoint save = conn.setSavepoint(name);
			savepoints.put(name, save);
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Could not create savepoint " + name);
		}
	}
	
	/**
	 * Method that releases a previously saved savedpoint
	 * @param name
	 * @param conn
	 */
	public void releaseSavepoint(String name, Connection conn) {
		
		try {
			if (savepoints.containsKey(name)) {
				conn.releaseSavepoint(savepoints.get(name));
			} else {
				log.warn("Unable to release savepoint, no savepoint found under " + name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Cannot release savepoint " + name);
		}
	}
	
	/**
	 * Method that sets a transaction
	 * @param conn
	 */
	public void setTransaction(Connection conn) {
		
		try {
			conn.setTransactionIsolation(conn.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Cannot set transaction!");
		}
	}

}
