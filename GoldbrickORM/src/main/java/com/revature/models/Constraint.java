package com.revature.models;

/**
 * This enum class defines constraints for each attribute in the database
 * @author Frank Aurori, Mollie Morrow, Nick Gianino
 *
 */
public enum Constraint {
	PRIMARY_KEY, FOREIGN_KEY, SERIAL, NOT_NULL, DEFAULT, UNIQUE, CHECK;
	
	public static String stringRepresentation(Constraint cons) {
		switch(cons) {
		case PRIMARY_KEY:
			return "PRIMARY KEY";
		case FOREIGN_KEY:
			return "";
		case SERIAL:
			return "SERIAL";
		case NOT_NULL:
			return "NOT NULL";
		case DEFAULT:
			return "DEFAULT";
		case UNIQUE:
			return "UNIQUE";
		case CHECK:
			return "CHECK";
		}
		
		return null;
	}
}
