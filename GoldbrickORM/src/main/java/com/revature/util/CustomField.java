package com.revature.util;

import com.revature.models.Constraint;

/**
 * This interface describes the functionality of a custom field object that uses
 * annotations to persist the necessary information of a model in order to
 * create a meta-model to build a database full of annotated objects
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/15/21
 *
 */
public interface CustomField {

	/**
	 * This getter method for the name of the field
	 * 
	 * @return the name of the field
	 */
	public String getName();

	/**
	 * This getter method for the type of class where the field is found
	 * 
	 * @return the name of the class
	 */
	public Class<?> getType();

	/**
	 * This getter method for the SQL data type of the field
	 * 
	 * @return the SQL data type
	 */
	public String getSQLDataType();

	/**
	 * This getter method for the column name declared in the annotation of the
	 * field
	 * 
	 * @return the name of the column in the database
	 */
	public String getColumnName();

	/**
	 * This getter method for the SQL constraints that apply to the field in the
	 * database
	 * 
	 * @return the SQL constraints
	 */
	public Constraint[] getConstraints();

}
