package com.revature.repos;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.revature.annotations.Entity;
import com.revature.models.Category;
import com.revature.models.Constraint;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.util.ColumnField;
import com.revature.util.Configuration;
import com.revature.util.ConnectionUtil;
import com.revature.util.ForeignKeyField;
import com.revature.util.Metamodel;

public class InsertDB<T> {
	
	private Class<T> clazz;
	
	
	
	private Configuration config;
	
	public InsertDB(Class<T> clazz) {
		this.clazz = clazz;
		
	}
	public InsertDB(Configuration cfg) { 
		this.config = cfg;
	}
	
	
	
	public String getInsert(Connection conn) throws IllegalArgumentException, IllegalAccessException {
		
		StringBuilder insertBuilder = new StringBuilder("INSERT INTO " + getTableName(clazz) + "(" );
		StringBuilder valuesBuilder = new StringBuilder(" VALUES (");
		
		try {
			Field field = clazz.getField("column");
			ColumnField[] columns = (ColumnField[]) field.get(User.class);
			
			for (ColumnField column : columns) {
				insertBuilder.append(column.getColumnName() + ", ");
				valuesBuilder.append("?, ");
				System.out.println(insertBuilder);
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder finalString = insertBuilder.append(valuesBuilder);
		
		
		
		return finalString.toString();
	}
	
	public void saveToTable(T obj, Connection conn) throws IllegalArgumentException, IllegalAccessException {
		
		String sql = getInsert(conn);
		
		try {
			Field field = clazz.getField("column");
			
			ColumnField[] attributes = (ColumnField[]) field.get(null);
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			int counter = 1;
			
			for (ColumnField att : attributes) {
				String attName = att.getColumnName();
				
				Field newField = obj.getClass().getDeclaredField(attName);
				
				pstmt.setObject(counter, newField.get(obj));
				counter++;
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
//	public boolean getInsertString(Connection conn) {
//		
//		for(Metamodel<Class<?>> mm : this.config.getMetamodels()) {
//			
//			StringBuilder insertBuilder = new StringBuilder("INSERT INTO " + getTableName(mm.getClazz()) + "(" );
//			StringBuilder valuesBuilder = new StringBuilder(" VALUES (");
//			
//			for(String col : getAttributes(mm)) {
//				insertBuilder.append(col + ", ");
//				valuesBuilder.append("?, ");
//			}
//			
//			
//			insertBuilder.deleteCharAt(insertBuilder.length() - 1);
//			insertBuilder.deleteCharAt(insertBuilder.length() - 1);
//			valuesBuilder.deleteCharAt(valuesBuilder.length() - 1);
//			valuesBuilder.deleteCharAt(valuesBuilder.length() - 1);
//			insertBuilder.append(")");
//			System.out.println(insertBuilder);
//			valuesBuilder.append(");");
//			System.out.println(valuesBuilder);
//			
//			StringBuilder finalString = insertBuilder.append(valuesBuilder);
//			
//			try {
//				PreparedStatement ps = conn.prepareStatement(finalString.toString());
//				
//				ps.execute();
//				
//				int count = 1;
//				
//				for(String col : getAttributes(mm)) {
//		//			ps.setObject(count, );
//					count++;
//				}
//				
//				
//				
//				
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return false;
//	}
	
	private String getTableName(Class<?> clazz) {
		try {
			return clazz.getAnnotation(Entity.class).tableName();
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	private String[] getAttributes(Metamodel<Class<?>> model) {

		List<ColumnField> atts = model.getAttributes();
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
			
			Configuration cfg = new Configuration();
		//	cfg.addAnnotatedClass(User.class)
//			.addAnnotatedClass(Category.class)
//			.addAnnotatedClass(Product.class)
//			.addAnnotatedClass(Order.class);
//			
			
			InsertDB dbb = new InsertDB(cfg);
			dbb.getInsert(connObj);
			
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}
