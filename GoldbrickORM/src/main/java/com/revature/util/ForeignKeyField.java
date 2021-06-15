package com.revature.util;

import java.lang.reflect.Field;

import com.revature.annotations.JoinColumn;

public class ForeignKeyField {

	private Field field;

	public ForeignKeyField(Field field) {
		if (field.getAnnotation(JoinColumn.class) == null) {
			throw new IllegalStateException(field.getName() + " is not annotated with @JoinColumn");
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
		return this.field.getAnnotation(JoinColumn.class).columnName();
	}
}
