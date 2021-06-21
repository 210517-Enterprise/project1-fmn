package com.revature.repos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ColumnField;
import com.revature.util.ConnectionUtil;
import com.revature.util.IdField;
import com.revature.util.Metamodel;
/**
 * This class handles Database update requests for the GoldbrickORM framework
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/21/21
 *
 */
public class DatabaseUpdater {

	/**
	 * This method updates the values of an existing Category object in the database
	 * 
	 * @param mm   The meta-model of the Category class
	 * @param cat  The Category object that needs to be updated in the database
	 * @param conn A connection from the connection pool
	 */
	public static void updateCategory(Metamodel<Category> mm, Category cat, Connection conn) {
		IdField pk = mm.getPrimaryKey();
		ColumnField name = mm.getAttributes().get(0); // category only has 1 attribute

		String sql = "UPDATE " + mm.getTableName() + " SET " + name.getColumnName() + " = \'" + cat.getCategoryName()
				+ "\' WHERE " + pk.getColumnName() + " = " + cat.getId();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method updates the values of an existing Order object in the database
	 * 
	 * @param mm    The meta-model of the Order class
	 * @param order The Order object that needs to be updated in the database
	 * @param conn  A connection from the connection pool
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void updateOrder(Metamodel<Order> mm, Order order, Connection conn)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = mm.getPrimaryKey();
		Map<String, Method> getters = mm.getGetters();

		String sql = "UPDATE " + mm.getTableName() + " SET ";

		// loop through column name, getter method pairs from meta-model
		for (String columnName : getters.keySet()) {
			// update each column's values (by invoking the getter method) except for the primary key
			if (!columnName.equals(pk.getColumnName()))
				sql += columnName + " = " + getters.get(columnName).invoke(order, null) + ", ";
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		//change the row that matches the object's id
		sql += " WHERE " + pk.getColumnName() + " = " + getters.get(pk.getColumnName()).invoke(order, null);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method updates the values of an existing Product object in the database
	 * 
	 * @param mm    The meta-model of the Product class
	 * @param order The Product object that needs to be updated in the database
	 * @param conn  A connection from the connection pool
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void updateProduct(Metamodel<Product> mm, Product product, Connection conn)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = mm.getPrimaryKey();
		Map<String, Method> getters = mm.getGetters();

		String sql = "UPDATE " + mm.getTableName() + " SET ";

		// loop through column name, getter method pairs from meta-model
		for (String columnName : getters.keySet()) {
			// update each column's values (by invoking the getter method) except for the primary key
			if (!columnName.equals(pk.getColumnName())) {
				sql += columnName + " = ";
				
				//format string data for sql request
				if (getters.get(columnName).getReturnType() == String.class)
					sql += "\'" + getters.get(columnName).invoke(product, null) + "\', ";
				else
					sql += getters.get(columnName).invoke(product, null) + ", ";
			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		//change the row that matches the object's id
		sql += " WHERE " + pk.getColumnName() + " = " + getters.get(pk.getColumnName()).invoke(product, null);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method updates the values of an existing User object in the database
	 * 
	 * @param mm    The meta-model of the User class
	 * @param order The User object that needs to be updated in the database
	 * @param conn  A connection from the connection pool
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void updateUser(Metamodel<User> mm, User user, Connection conn)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = mm.getPrimaryKey();
		Map<String, Method> getters = mm.getGetters();

		String sql = "UPDATE " + mm.getTableName() + " SET ";

		// loop through column name, getter method pairs from meta-model
		for (String columnName : getters.keySet()) {
			// update each column's values (by invoking the getter method) except for the primary key
			if (!columnName.equals(pk.getColumnName())) {
				sql += columnName + " = ";
				
				//format string data for sql request
				if (getters.get(columnName).getReturnType() == String.class)
					sql += "\'" + getters.get(columnName).invoke(user, null) + "\', ";
				//format role data for sql request
				else if (getters.get(columnName).getReturnType() == Role.class)
					sql += "\'" + getters.get(columnName).invoke(user, null).toString() + "\', ";
				else
					sql += getters.get(columnName).invoke(user, null) + ", ";
			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		//change the row that matches the object's id
		sql += " WHERE " + pk.getColumnName() + " = " + getters.get(pk.getColumnName()).invoke(user, null);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ConnectionUtil jdbcObj = new ConnectionUtil();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connObj = null;

		try {
			DataSource datasource = jdbcObj.setUpPool();
			jdbcObj.printDBStatus();
			connObj = datasource.getConnection();

			Category cat = new Category(1, "testupdate");
			Metamodel<Category> mm = new Metamodel<>(Category.class);

			updateCategory(mm, cat, connObj);

			Order order = new Order(2, 1, 1, null, 0, false, 0);
			Metamodel<Order> mmO = new Metamodel<>(Order.class);

			updateOrder(mmO, order, connObj);

			Product prod = new Product(1, 1, "test2", "test3", 0, 0, false);
			Metamodel<Product> mmP = new Metamodel<>(Product.class);
			updateProduct(mmP, prod, connObj);

			User user = new User(2, "test", "test", "test", "test", Role.CUSTOMER);
			Metamodel<User> mmU = new Metamodel<>(User.class);
			updateUser(mmU, user, connObj);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
