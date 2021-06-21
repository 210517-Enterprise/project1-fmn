package com.revature.util;

import java.lang.reflect.Field;

import com.revature.annotations.Id;
import com.revature.models.Constraint;


public class IdField implements CustomField{
	
	private Field field;

	public IdField(Field field) {
		//check that given field is an id field
		if(field.getAnnotation(Id.class) == null) {
			throw new IllegalStateException(field.getName() + " is not annotated with @Id");
		}
		this.field = field;
	}
	

	@Override
	public String getName() {
		return field.getName();
	}
	
	@Override
	public Class<?> getType(){
		return field.getType();
	}
	
	@Override
	public String getSQLDataType() {
		return field.getAnnotation(Id.class).dataType();
	}
	
	@Override
	public String getColumnName() {
		return field.getAnnotation(Id.class).columnName();
	}
	
	@Override
	public Constraint[] getConstraints() {
		return field.getAnnotation(Id.class).constraints();
	}

}
