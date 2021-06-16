package com.revature.repos;

import java.util.List;

import com.revature.util.ColumnField;
import com.revature.util.Configuration;
import com.revature.util.ForeignKeyField;
import com.revature.util.IdField;
import com.revature.util.Metamodel;

public class DatabaseBuilder {
	
	private Configuration config;
	
	public DatabaseBuilder(Configuration cfg) {
		this.config = cfg;
	}
	
	public boolean createTables() throws NoSuchFieldException {
		//check if tables exist in db
		for(Metamodel<Class<?>> mm : this.config.getMetamodels()) {
			
			IdField pk = mm.getPrimaryKey();
			if(pk == null)
				throw new NoSuchFieldException("Class " + mm.getClassName() + " has no primary key");
			
			List<ColumnField> attributes = mm.getAttributes();
			List<ForeignKeyField> foreignKeys = mm.getForeignKeys();
			
			StringBuilder str = new StringBuilder("CREATE TABLE " + getTableName(mm.getClass()) + "(");
			//append PK
			//append any FKs
			//appends all other columns
			str.append(");");
		}
		
		return false;
	}
	
	private String getTableName(Class<?> clazz) {
		try {
			return clazz.getField("tableName").getName();
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getPrimaryKey(Metamodel<Class<?>> model) {
		//need to figure out how to add constraints
		return model.getPrimaryKey().getColumnName();
	}
	
	private String [] getForeignKeys(Metamodel<Class<?>> model) {
		
		List<ForeignKeyField> fks = model.getForeignKeys();
		String [] foreignKeyNames = new String[fks.size()];
		
		for(int i = 0; i < fks.size(); i++) {
			//need to figure out how to add constraints
			foreignKeyNames[i] = fks.get(i).getColumnName();
		}
		
		return foreignKeyNames;
	}
	
private String [] getAttributes(Metamodel<Class<?>> model) {
		
		List<ColumnField> atts = model.getAttributes();
		String [] columns = new String[atts.size()];
		
		for(int i = 0; i < atts.size(); i++) {
			//need to figure out how to add constraints
			columns[i] = atts.get(i).getColumnName();
		}
		
		return columns;
	}
	
	

}
