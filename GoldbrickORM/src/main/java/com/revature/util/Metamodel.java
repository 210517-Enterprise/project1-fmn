package com.revature.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.models.Product;

public class Metamodel<T> {

	private Class<T> clazz;
	private IdField primaryKey;
	private List<ColumnField> attributes;
	private List<ForeignKeyField> foreignKeys;

	/*
	 * This is our of() method which is being called in our configuration. This is
	 * the class that examines all the classes that we want to persist that our
	 * MetaModel builds that list of all the classes to be sent to the database and
	 * builds the tables
	 */
	public static <T> Metamodel<T> of(Class<T> clazz) {
		if (clazz.getAnnotation(Entity.class) == null) {
			throw new IllegalStateException(clazz.getName() + "is not annotated with @Entity");
		}

		return new Metamodel<>(clazz);
	}

	public Metamodel(Class<T> clazz) {
		this.clazz = clazz;
		this.attributes = new ArrayList<>();
		this.foreignKeys = new ArrayList<>();
	}
	
	public Class<T> getClazz(){
		return this.clazz;
	}

	public String getClassName() {
		return this.clazz.getName();
	}

	public String getSimpleClass() {
		return this.clazz.getSimpleName();
	}
	
	public String getTableName(Class<?> clazz) {
		try {
			return clazz.getAnnotation(Entity.class).tableName();
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	public IdField getPrimaryKey() {
		if (this.primaryKey == null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				// check that the field is the IdField
				Id pk = field.getAnnotation(Id.class);
				if (pk != null) {
					this.primaryKey = new IdField(field);

				}
			}
			if(this.primaryKey == null)
				throw new RuntimeException("No field annotated with @Id in " + this.clazz.getName());
		}

		return this.primaryKey;
	}

	public List<ColumnField> getAttributes() {

		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			// check that the field is a ColumnField
			Column column = field.getAnnotation(Column.class);
			if (column != null && !this.attributes.contains(new ColumnField(field))) {
				this.attributes.add(new ColumnField(field));
			}
		}

//		if (this.attributes.isEmpty())
//			throw new RuntimeException("No attributes found in " + this.clazz.getName());

		return this.attributes;
	}

	public List<ForeignKeyField> getForeignKeys() {
		Field[] fields = this.clazz.getDeclaredFields();

		for (Field field : fields) {
			JoinColumn fk = field.getAnnotation(JoinColumn.class);

			if (fk != null && !this.foreignKeys.contains(new ForeignKeyField(field))) {
				this.foreignKeys.add(new ForeignKeyField(field));
			}
		}

		return this.foreignKeys;
	}
	

}
