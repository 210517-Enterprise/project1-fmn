package com.revature.util;

import com.revature.models.Constraint;

public interface CustomField {
	
	public String getName();
	
	public Class<?> getType();
	
	public String getSQLDataType();
	
	public String getColumnName();
	
	public Constraint[] getConstraints();

}
