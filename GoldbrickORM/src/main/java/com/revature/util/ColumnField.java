package com.revature.util;

import java.lang.reflect.Field;

import com.revature.annotations.Column;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.models.Constraint;

/**
 * This class holds the information and functionality pertinent to the instance
 * variables annotated with "@ColumnField" in each of the annotated model
 * classes
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/15/21
 *
 */
public class ColumnField implements CustomField {

	private Field field;

	/**
	 * Constructor for a ColumnField
	 * @param field dynamic access to a field in a class
	 */
	public ColumnField(Field field) {
		if (field.getAnnotation(Column.class) == null) {
			throw new IllegalStateException(field.getName() + " is not annotated by @ColumnField");
		}
		this.field = field;
	}

	@Override
	public String getName() {
		return this.field.getName();
	}

	@Override
	public Class<?> getType() {
		return this.field.getClass();
	}

	@Override
	public String getSQLDataType() {
		return field.getAnnotation(Column.class).dataType();
	}

	@Override
	public String getColumnName() {
		return this.field.getAnnotation(Column.class).columnName();
	}

	@Override
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
