package com.revature.repos;

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

public class InsertDB {
	
	private Configuration config;
	
	public InsertDB(Configuration cfg) { 
		this.config = cfg;
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
			cfg.addAnnotatedClass(User.class)
			.addAnnotatedClass(Category.class)
			.addAnnotatedClass(Product.class)
			.addAnnotatedClass(Order.class);
			
			
			InsertDB dbb = new InsertDB(cfg);
			dbb.getInsertString(connObj);
			
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}
