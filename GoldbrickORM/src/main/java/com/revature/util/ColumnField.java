package com.revature.util;

import java.lang.reflect.Field;

import com.revature.annotations.Column;
import com.revature.annotations.Id;
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
	
	public String getSQLDataType() {
		return field.getAnnotation(Column.class).dataType();
	}
	
	public String getColumnName() {
		return this.field.getAnnotation(Column.class).columnName();
	}
	
	public Constraint[] getConstraints() {
		return field.getAnnotation(Column.class).constraints();
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
		ColumnField other = (ColumnField) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		return true;
	}
	
	

}
