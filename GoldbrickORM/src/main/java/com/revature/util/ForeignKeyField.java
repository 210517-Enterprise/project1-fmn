package com.revature.util;

import java.lang.reflect.Field;

import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.models.Constraint;

public class ForeignKeyField implements CustomField{

	private Field field;

	public ForeignKeyField(Field field) {
		if (field.getAnnotation(JoinColumn.class) == null) {
			throw new IllegalStateException(field.getName() + " is not annotated with @JoinColumn");
		}
		
		this.field = field;
	}

	@Override
	public String getName() {
		return this.field.getName();
	}
	
	@Override
	public Class<?> getType(){
		return this.field.getClass();
	}
	
	@Override
	public String getSQLDataType() {
		return field.getAnnotation(JoinColumn.class).dataType();
	}
	
	@Override
	public String getColumnName() {
		return this.field.getAnnotation(JoinColumn.class).columnName();
	}
	
	@Override
	public Constraint[] getConstraints() {
		return field.getAnnotation(JoinColumn.class).constraints();
	}
	
	public String getReference() {
		return field.getAnnotation(JoinColumn.class).reference();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
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
		ForeignKeyField other = (ForeignKeyField) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		return true;
	}
	
	
}
