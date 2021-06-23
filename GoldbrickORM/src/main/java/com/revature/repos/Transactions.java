package com.revature.repos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;

import org.apache.log4j.Logger;

/*
 * Transaction Management :  (begin, commit, savepoint, rollback)
 */

public class Transactions {
	
	private static Transactions transaction = new Transactions();
	
	private HashMap<String, Savepoint> savepoints;
	
	private static Logger log = Logger.getLogger(Transactions.class);
	
	public Transactions() {
		savepoints = new HashMap();
	}
	
	public static Transactions getTransaction() {
		return transaction;
	}
	
	public void enableAutoCommit(Connection conn) {
		
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Could not enable auto commit!");
		}
		
	}
	
	public void commitTrans(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Unable to commit transaction!");
		}
	}
	
	public void rollbackTrans(Connection conn) {
		
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Unable to rollback transaction!");
		}
	}
	
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
	
	public void setSavepoint(String name, Connection conn) {
		
		try {
			Savepoint save = conn.setSavepoint(name);
			savepoints.put(name, save);
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Could not create savepoint " + name);
		}
	}
	
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
	
	public void setTransaction(Connection conn) {
		
		try {
			conn.setTransactionIsolation(conn.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("Cannot set transaction!");
		}
	}
	

}
