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

import com.revature.annotations.Entity;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ColumnField;
import com.revature.util.Configuration;
import com.revature.util.ConnectionUtil;
import com.revature.util.ForeignKeyField;
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
		List<ColumnField> attributes = um.getAttributes();
		// System.out.println(attributes);

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
				sql += getters.get(columnName).invoke(user, null) + ",";
			}
		}

		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ")";
		// for(String columnName : a) {
		// sql += columnName;
		// }
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(sql);
	}
	
	public boolean getInsertString(Connection conn) {
		
		for(Metamodel<Class<?>> mm : this.config.getMetamodels()) {
			
			StringBuilder insertBuilder = new StringBuilder("INSERT INTO " + getTableName(mm.getClazz()) + "(" );
			StringBuilder valuesBuilder = new StringBuilder(" VALUES (");
			
			for(String col : getAttributes(mm)) {
				insertBuilder.append(col + ", ");
				valuesBuilder.append("?, ");
			}
			
			
			insertBuilder.deleteCharAt(insertBuilder.length() - 1);
			insertBuilder.deleteCharAt(insertBuilder.length() - 1);
			valuesBuilder.deleteCharAt(valuesBuilder.length() - 1);
			valuesBuilder.deleteCharAt(valuesBuilder.length() - 1);
			insertBuilder.append(")");
			System.out.println(insertBuilder);
			valuesBuilder.append(");");
			System.out.println(valuesBuilder);
			
			StringBuilder finalString = insertBuilder.append(valuesBuilder);
			
			try {
				PreparedStatement ps = conn.prepareStatement(finalString.toString());
				
				ps.execute();
				
				int count = 1;
				
				for(String col : getAttributes(mm)) {
		//			ps.setObject(count, );
					count++;
				}
				
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private String getTableName(Class<?> clazz) {
		try {
			return clazz.getAnnotation(Entity.class).tableName();
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	private String[] getAttributes(Metamodel<Class<?>> model) {
		Metamodel<User> um = new Metamodel<>(User.class);
		List<ColumnField> atts = um.getAttributes();
		String[] columns = new String[atts.size()];

		for (int i = 0; i < atts.size(); i++) {
			
			columns[i] = atts.get(i).getColumnName();
		
		}

		return columns;
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
			
			User user = new User(0, "Frank", "Aurori", "faurori@gmail.com", "pass", null);
			Metamodel<User> um = new Metamodel<>(User.class);
			
			insertUser(um, user, connObj);
			
					
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}