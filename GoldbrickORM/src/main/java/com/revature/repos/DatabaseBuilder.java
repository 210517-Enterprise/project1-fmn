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
import com.revature.util.IdField;
import com.revature.util.Metamodel;

public class DatabaseBuilder {

	private Configuration config;

	public DatabaseBuilder(Configuration cfg) {
		this.config = cfg;
	}

	public boolean createTables(Connection conn) throws NoSuchFieldException {
		// check if tables exist in db
		for (Metamodel<Class<?>> mm : this.config.getMetamodels()) {

			IdField pk = mm.getPrimaryKey();
			if (pk == null)
				throw new NoSuchFieldException("Class " + mm.getClassName() + " has no primary key");

			StringBuilder str = new StringBuilder("CREATE TABLE " + mm.getTableName(mm.getClazz()) + "(");
			// append PK+constraints
			str.append(getPrimaryKey(mm) + ", ");

			// append any FKs
			for (String fk : getForeignKeys(mm)) {
				str.append(fk + ", ");
			}
			// appends all other columns
			for(String col : getAttributes(mm)) {
				str.append(col + ", ");
			}
			str.deleteCharAt(str.length()-1); //get rid of extra " "
			str.deleteCharAt(str.length()-1); //get rid of extra ","
			str.append(");");
			
			System.out.println(str);
			
			try {
				PreparedStatement ps = conn.prepareStatement(str.toString());
				
				ps.executeQuery();
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		

		return false;
	}


	private String getPrimaryKey(Metamodel<Class<?>> model) {
		// need to figure out how to add constraints
		String nameAndConstraints = model.getPrimaryKey().getColumnName();
		nameAndConstraints += " " + model.getPrimaryKey().getSQLDataType();

		for (Constraint cons : model.getPrimaryKey().getConstraints()) {
			nameAndConstraints += " " + Constraint.stringRepresentation(cons);
		}

		return nameAndConstraints;

	}

	private String[] getForeignKeys(Metamodel<Class<?>> model) {

		List<ForeignKeyField> fks = model.getForeignKeys();
		String[] namesAndConstraints = new String[fks.size()];

		for (int i = 0; i < fks.size(); i++) {
			// need to figure out how to add constraints
			namesAndConstraints[i] = fks.get(i).getColumnName();
			namesAndConstraints[i] += " " + fks.get(i).getSQLDataType();
			for (Constraint cons : fks.get(i).getConstraints()) {
				namesAndConstraints[i] += " " + Constraint.stringRepresentation(cons);
			}
			namesAndConstraints[i] += " " + fks.get(i).getReference();
		}

		return namesAndConstraints;
	}

	private String[] getAttributes(Metamodel<Class<?>> model) {

		List<ColumnField> atts = model.getAttributes();
		String[] columns = new String[atts.size()];

		for (int i = 0; i < atts.size(); i++) {
			// need to figure out how to add constraints
			columns[i] = atts.get(i).getColumnName();
			columns[i] += " " + atts.get(i).getSQLDataType();
			for (Constraint cons : atts.get(i).getConstraints()) {
				columns[i] += " " + Constraint.stringRepresentation(cons);
			}
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
			
			
			DatabaseBuilder dbb = new DatabaseBuilder(cfg);
			dbb.createTables(connObj);
			
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
