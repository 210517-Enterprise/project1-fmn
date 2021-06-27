package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Getter;
import com.revature.annotations.Id;

/**
 * This class defines a User class that can be mapped to a database table called users.
 * @author Frank Aurori, Mollie Morrow, Nick Gianino
 *
 */
@Entity(tableName = "users")
public class User implements AnnotatedClass {
	
	/**
	 * @Id indicates the private field "id" is the primary key of table User
	 */
	@Id(columnName = "id", constraints = { Constraint.PRIMARY_KEY }, dataType = "SERIAL")
	private int id;
	
	/**
	 * @column indicates the database column name "first_name" in user table with not null constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "first_name", constraints = { Constraint.NOT_NULL }, dataType = "VARCHAR(50)")
	private String firstName;
	
	/**
	 * @column indicates the database column name "last_name" in user table with not null constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "last_name", constraints = { Constraint.NOT_NULL }, dataType = "VARCHAR(50)")
	private String lastName;

	/**
	 * @column indicates the database column name "email" in user table with not null and unique constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "email", constraints = { Constraint.NOT_NULL, Constraint.UNIQUE }, dataType = "VARCHAR(50)")
	private String email;
	
	/**
	 * @column indicates the database column name "pwd (password)" in user table with not null constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "pwd", constraints = { Constraint.NOT_NULL }, dataType = "VARCHAR(50)")
	private String password; // encrypt this
	
	/**
	 * @column indicates the database column name "user_role" in user table with not null constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "user_role", constraints = { Constraint.NOT_NULL }, dataType = "VARCHAR(50)")
	private Role role;
	
	/**
	 * Constructor for user object
	 * @param email
	 * @param password
	 */
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	/**
	 * Constructor for user object
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @param role
	 */
	public User(int id, String firstName, String lastName, String email, String password, Role role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	/**
	 * Method that returns the user id (primary key)
	 */
	@Getter(name = "id")
	public int getId() {
		return id;
	}
	
	/**
	 * Method that sets the user id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method that returns the users first_name
	 * @return
	 */
	@Getter(name = "first_name")
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Method that sets the users first_name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Method that returns the users last_name
	 * @return
	 */
	@Getter(name = "last_name")
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Method that sets the users last_name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Method that returns the users email 
	 * @return
	 */
	@Getter(name = "email")
	public String getEmail() {
		return email;
	}
	
	/**
	 * method that sets the users email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Method that returns the users password (pwd)
	 * @return
	 */
	@Getter(name = "pwd")
	public String getPassword() {
		return password;
	}
	
	/**
	 * Method that sets the users password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Method returns the user_role (enum of type Role)
	 * @return
	 */
	@Getter(name = "user_role")
	public Role getRole() {
		return role;
	}
	
	/**
	 * Method that sets the user role (either admin or customer)
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "----------------------------------------------------------------------------------------------\n"
				+  id + ": " + firstName + " " + lastName + ", " + email + ", " + password + ", " + role
				+ "\n----------------------------------------------------------------------------------------------";
	}

}