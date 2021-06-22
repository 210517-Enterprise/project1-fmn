package com.revature.repos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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
	
	private Configuration config;
	
	public InsertDB(Configuration cfg) { 
		this.config = cfg;
	}
	
		
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

	public static void insertCategory(Metamodel<Category> cm, Category category, Connection conn)
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
		sql += ")";
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
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
					sql += "\'" + getters.get(columnName).invoke(order, null) + "\',";
				else
					sql += getters.get(columnName).invoke(order, null) + ", ";

			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ")";
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
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
			
			System.out.println("weeeeeee");
			connObj = datasource.getConnection();
			
			User user = new User(0, "Frank", "Aurori", "test123@test.com", "pass", Role.ADMIN);
			Metamodel<User> um = new Metamodel<>(User.class);
			//insertUser(um, user, connObj);
			
			Category category = new Category(0, "clothing");
			Metamodel<Category> cm = new Metamodel<>(Category.class);
			//insertCategory(cm, category, connObj);
			
			Order order = new Order(3, 1, 1, null, 0, false, 0);	
			Metamodel<Order> om = new Metamodel<>(Order.class);
			//insertOrder(om, order, connObj);
			
			Product prod = new Product(2, 2, "test123", "test123", 0, 0, false);
			Metamodel<Product> pm = new Metamodel<>(Product.class);
			insertProduct(pm, prod, connObj);
			
			
					
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}