package com.revature.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.util.Metamodel;

/**
 * This class handles Database delete requests for the GoldbrickORM framework
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/21/21
 *
 */
public class DatabaseDeleter {

	private static Logger log = Logger.getLogger(DatabaseDeleter.class);
	
	/**
	 * This method deletes a product from the products table by its primary key, ID
	 * @param mm The meta-model of the Product class
	 * @param id The primary key of the product being deleted
	 * @param conn A connection from the connection pool
	 * @return
	 */
	public static boolean deleteProductById(Metamodel<Product> mm, int id, Connection conn) {
		String sql = "DELETE FROM " + mm.getTableName() + " WHERE " + mm.getPrimaryKey().getColumnName() + " = " + id;
		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			if(stmt.execute(sql))
				return true;
			else {
				log.warn("Failure to delete Product #" + id);
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method deletes an order from the orders table by its primary key, ID
	 * @param mm The meta-model of the Order class
	 * @param id The primary key of the order being deleted
	 * @param conn A connection from the connection pool
	 * @return
	 */
	public static boolean deleteOrderById(Metamodel<Order> mm, int id, Connection conn) {
		String sql = "DELETE FROM " + mm.getTableName() + " WHERE " + mm.getPrimaryKey().getColumnName() + " = " + id;
		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			if(stmt.execute(sql))
				return true;
			else {
				log.warn("Failure to delete Product #" + id);
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method deletes a category from the categories table by its primary key, ID
	 * @param mm The meta-model of the Category class
	 * @param id The primary key of the category being deleted
	 * @param conn A connection from the connection pool
	 * @return
	 */
	public static boolean deleteCategoryById(Metamodel<Category> mm, int id, Connection conn) {
		String sql = "DELETE FROM " + mm.getTableName() + " WHERE " + mm.getPrimaryKey().getColumnName() + " = " + id;
		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			if(stmt.execute(sql))
				return true;
			else {
				log.warn("Failure to delete Product #" + id);
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method deletes a user from the users table by its primary key, ID
	 * @param mm The meta-model of the User class
	 * @param id The primary key of the user being deleted
	 * @param conn A connection from the connection pool
	 * @return
	 */
	public static boolean deleteUserById(Metamodel<User> mm, int id, Connection conn) {
		String sql = "DELETE FROM " + mm.getTableName() + " WHERE " + mm.getPrimaryKey().getColumnName() + " = " + id;
		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			if(stmt.execute(sql))
				return true;
			else {
				log.warn("Failure to delete Product #" + id);
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
