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
import com.revature.util.ColumnField;
import com.revature.util.ConnectionUtil;
import com.revature.util.ForeignKeyField;
import com.revature.util.IdField;
import com.revature.util.Metamodel;

public class DatabaseUpdater {

	public static void updateCategory(Metamodel<Category> mm, Category cat, Connection conn) {
		IdField pk = mm.getPrimaryKey();
		ColumnField name = mm.getAttributes().get(0); //category only has 1 attribute
		
		String sql = "UPDATE " + mm.getTableName() + " SET " + name.getColumnName() + " = \'" 
				+ cat.getCategoryName() +  "\' WHERE " + pk.getColumnName() + " = " 
				+ cat.getId();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void updateOrder(Metamodel<Order> mm, Order order, Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IdField pk = mm.getPrimaryKey();
		Map<String, Method> getters = mm.getGetters();
		
		String sql = "UPDATE " + mm.getTableName() + " SET ";
		
		for(String columnName : getters.keySet()) {
			if(!columnName.equals(pk.getColumnName()))
				sql += columnName + " = " + getters.get(columnName).invoke(order, null) + ", ";
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += " WHERE " + pk.getColumnName() + " = " + getters.get(pk.getColumnName()).invoke(order, null);
		System.out.println(sql);
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateProduct() {

	}

	public void updateUser() {

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
			
			//updateCategory(mm, cat, connObj);
			
			Order order = new Order(2, 1, 1, null, 0, false, 0);
			Metamodel<Order> mmO = new Metamodel<>(Order.class);
			
			updateOrder(mmO, order, connObj);
			
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
