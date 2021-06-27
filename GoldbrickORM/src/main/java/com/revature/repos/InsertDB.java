package com.revature.repos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ColumnField;
import com.revature.util.Configuration;
import com.revature.util.ConnectionUtil;
import com.revature.util.IdField;
import com.revature.util.Metamodel;

public class InsertDB {
	
	private static Logger log = Logger.getLogger(InsertDB.class);
	
	private Configuration config;
	
	public InsertDB(Configuration cfg) { 
		this.config = cfg;
	}
	
	/**
	 * This method inserts the values of a User object in the database	
	 * @param um    The meta-model of the User class
	 * @param user  The User object that needs to be inserted in the database
	 * @param conn  A connection from the connection pool
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void insertUser(Metamodel<User> um, User user, Connection conn)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = um.getPrimaryKey();

		Map<String, Method> getters = um.getGetters();

		String sql = "INSERT INTO " + um.getTableName() + "(";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				sql += columnName + ", ";

			}
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ") VALUES (";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				if (getters.get(columnName).getReturnType() == String.class)
					sql += "\'" + getters.get(columnName).invoke(user, null) + "\',";
				else if (getters.get(columnName).getReturnType() == Role.class)
					sql += "\'" + getters.get(columnName).invoke(user, null).toString() + "\',";
				else
					sql += getters.get(columnName).invoke(user, null) + ", ";

			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ")";
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			conn.close();
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * This method inserts the values of a Category object in the database
	 * @param cm       The meta-model of the Category class
	 * @param category The Category object that needs to be inserted in the database
	 * @param conn     A connection from the connection pool
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static int insertCategory(Metamodel<Category> cm, Category category, Connection conn)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = cm.getPrimaryKey();

		Map<String, Method> getters = cm.getGetters();

		String sql = "INSERT INTO " + cm.getTableName() + "(";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				sql += columnName + ", ";

			}
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ") VALUES (";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				if (getters.get(columnName).getReturnType() == String.class)
					sql += "\'" + getters.get(columnName).invoke(category, null) + "\',";
				else
					sql += getters.get(columnName).invoke(category, null) + ", ";

			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ") RETURNING " + cm.getTableName() + "." + cm.getPrimaryKey().getColumnName();
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs;
			if ((rs = ps.executeQuery()) != null) {
				rs.next();
				int newID = rs.getInt(cm.getPrimaryKey().getColumnName());
				conn.close();
				return newID;
			}	
			
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
		
		return -1;
		
	}
	
	/**
	 * This method inserts the values of a Order object in the database
	 * 
	 * @param om    The meta-model of the Order class
	 * @param order The Order object that needs to be inserted in the database
	 * @param conn  A connection from the connection pool
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void insertOrder(Metamodel<Order> om, Order order, Connection conn)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = om.getPrimaryKey();

		Map<String, Method> getters = om.getGetters();

		String sql = "INSERT INTO " + om.getTableName() + "(";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				sql += columnName + ", ";

			}
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ") VALUES (";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				if (getters.get(columnName).getReturnType() == String.class)
					sql += "\'" + getters.get(columnName).invoke(order, null) + "\', ";
				else if (getters.get(columnName).getReturnType() == Date.class)
					sql += "DATE \'" + getters.get(columnName).invoke(order, null) + "\', ";
				else
					sql += getters.get(columnName).invoke(order, null) + ", ";

			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ")";
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			conn.close();
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method inserts the values of a product object in the database
	 * 
	 * @param pm      The meta-model of the Product class
	 * @param product The Product object that needs to be inserted in the database
	 * @param conn    A connection from the connection pool
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void insertProduct(Metamodel<Product> pm, Product product, Connection conn)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = pm.getPrimaryKey();

		Map<String, Method> getters = pm.getGetters();

		String sql = "INSERT INTO " + pm.getTableName() + "(";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				sql += columnName + ", ";

			}
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ") VALUES (";

		for (String columnName : getters.keySet()) {
			if (!columnName.equals(pk.getColumnName())) {
				if (getters.get(columnName).getReturnType() == String.class)
					sql += "\'" + getters.get(columnName).invoke(product, null) + "\',";
				else
					sql += getters.get(columnName).invoke(product, null) + ", ";

			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ")";
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			conn.close();
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}