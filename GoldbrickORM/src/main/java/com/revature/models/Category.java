package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Getter;
import com.revature.annotations.Id;

/**
 * This class defines a Category class that can be mapped to a database table called categories.
 * @author Frank Aurori, Mollie Morrow, Nick Gianino
 *
 */
@Entity(tableName = "categories")
public class Category implements AnnotatedClass {

	/**
	 * @Id indicates the private field "id" is the primary key of table category
	 */
	@Id(columnName = "id", constraints = { Constraint.PRIMARY_KEY }, dataType = "SERIAL")
	private int id;

	/*
	 * @column indicates the database column name "category_name" in categories table with not null constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "category_name", constraints = { Constraint.NOT_NULL }, dataType = "VARCHAR(50)")
	private String categoryName;

	/**
	 * Constructor for Category object
	 * @param categoryName
	 */
	public Category(String categoryName) {
		super();
		this.id = -1;
		this.categoryName = categoryName;
	}
	/**
	 * Constructor for category object
	 * @param id
	 * @param categoryName
	 */

	public Category(int id, String categoryName) {
		super();
		this.id = id;
		this.categoryName = categoryName;
	}
	/**
	 * default constructor for category object
	 */

	public Category() {
	}
	
	/**
	 * Method that returns id (primary key)
	 */
	@Getter(name = "id")
	public int getId() {
		return id;
	}
	/**
	 * Method that sets id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Method that returns category name
	 * @return 
	 */
	@Getter(name = "category_name")
	public String getCategoryName() {
		return categoryName;
	}
	
	/**
	 * method that sets the category name
	 * @param categoryName
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "----------------------------------------------------------------------------------------------\n"
				+  id + ": " + categoryName
				+ "\n----------------------------------------------------------------------------------------------";
	}

}