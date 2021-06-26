package com.revature.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Getter;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

/**
 * This class creates a model of a class to facilitate reading and writing
 * objects to and from a database
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/16/21
 */
public class Metamodel<T> {

	/**
	 * Dynamic representation of a class
	 */
	private Class<T> clazz;

	/**
	 * Holds information pertaining to a class's primary key in the database
	 */
	private IdField primaryKey;

	/**
	 * List of ColumnField objects that hold information pertaining to a class's
	 * attributes in the database
	 */
	private List<ColumnField> attributes;

	/**
	 * List of ForeignKeyField objects that hold information pertaining to a class's
	 * foreign keys in the database
	 */
	private List<ForeignKeyField> foreignKeys;

	/**
	 * This method acts as a constructor in the Configuration class, it is used to
	 * create a Metamodel of a class to persist in the database.
	 * 
	 * @param clazz the dynamic representation of the class used to create objects
	 *              to persist in the database
	 * @return a meta-model for the class
	 */
	public static <T> Metamodel<T> of(Class<T> clazz) {
		if (clazz.getAnnotation(Entity.class) == null) {
			throw new IllegalStateException(clazz.getName() + "is not annotated with @Entity");
		}

		return new Metamodel<>(clazz);
	}

	/**
	 * Constructor for the Metamodel class
	 * 
	 * @param clazz the dynamic representation of the class used to create objects
	 *              to persist in the database
	 */
	public Metamodel(Class<T> clazz) {
		this.clazz = clazz;
		this.attributes = new ArrayList<>();
		this.foreignKeys = new ArrayList<>();
	}

	/**
	 * Getter method for clazz
	 * 
	 * @return the dynamic representation of the class used to create objects to
	 *         persist in the database
	 */
	public Class<T> getClazz() {
		return this.clazz;
	}

	/**
	 * Getter method for the class name
	 * 
	 * @return the name of the class used to create objects to persist in the
	 *         database
	 */
	public String getClassName() {
		return this.clazz.getName();
	}

	/**
	 * Getter method for the simple class name
	 * 
	 * @return the simple name of the class used to create objects to persist in the
	 *         database
	 */
	public String getSimpleClass() {
		return this.clazz.getSimpleName();
	}

	/**
	 * Getter method for the name of the table used to persist clazz objects in the
	 * database
	 * 
	 * @return the table name
	 */
	public String getTableName() {
		try {
			return this.clazz.getAnnotation(Entity.class).tableName();
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Getter method for the primary key of the table used to persist clazz objects
	 * in the database
	 * 
	 * @return the dynamic representation of the field that is the primary key of
	 *         the table of clazz objects in the database
	 */
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
			if (this.primaryKey == null)
				throw new RuntimeException("No field annotated with @Id in " + this.clazz.getName());
		}

		return this.primaryKey;
	}

	/**
	 * Getter method for the attributes of the table used to persist clazz objects
	 * in the database
	 * 
	 * @return the dynamic representation of the fields that are the attributes of
	 *         the table of clazz objects in the database
	 */
	public List<ColumnField> getAttributes() {

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// check that the field is a ColumnField
			Column column = field.getAnnotation(Column.class);
			if (column != null && !this.attributes.contains(new ColumnField(field))) {
				this.attributes.add(new ColumnField(field));
			}
		}

		return this.attributes;
	}

	/**
	 * Getter method for the foreign key(s) of the table used to persist clazz
	 * objects in the database
	 * 
	 * @return the dynamic representation of the fields that are the foreign key(s)
	 *         of the table of clazz objects in the database
	 */
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

	/**
	 * Getter method for the getter methods belonging to clazz
	 * 
	 * @return the getter methods of clazz
	 */
	public Map<String, Method> getGetters() {
		HashMap<String, Method> getterMap = new HashMap<>();

		Method[] allMethods = this.clazz.getDeclaredMethods();
		for (Method method : allMethods) {
			if (method.getDeclaredAnnotation(Getter.class) != null) {
				getterMap.put(method.getDeclaredAnnotation(Getter.class).name(), method);
			}
		}

		return getterMap;
	}

}
