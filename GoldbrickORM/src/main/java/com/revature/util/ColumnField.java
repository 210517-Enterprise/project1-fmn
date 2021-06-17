package com.revature.util;

import java.lang.reflect.Field;

import com.revature.annotations.Column;
import com.revature.models.Constraint;

public class ColumnField {

	private Field field;

	public ColumnField(Field field) {
		if (field.getAnnotation(Column.class) == null) {
			throw new IllegalStateException(field.getName() + " is not annotated by @ColumnField");
		}
		this.field = field;
	}
	
	public String getName() {
		return this.field.getName();
	}
	
	public Class<?> getType(){
		return this.field.getClass();
	}
	
	public String getColumnName() {
		return this.field.getAnnotation(Column.class).columnName();
	}
	
	public Constraint[] getConstraints() {
		return field.getAnnotation(Column.class).constraints();
	}

}
