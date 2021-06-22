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

public class DatabaseDeleter {

	private static Logger log = Logger.getLogger(DatabaseDeleter.class);
	
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
