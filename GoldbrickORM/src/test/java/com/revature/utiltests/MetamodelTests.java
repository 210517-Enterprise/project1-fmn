package com.revature.utiltests;

import org.junit.Before;
import org.junit.Test;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.Metamodel;

public class MetamodelTests {

	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testCreateMetamodel_withAnnotatedClass() {
		User testUser = new User(1, "David", "Rose", "drose@roseapothocary.com", "eatglass", Role.CUSTOMER);
		Metamodel.of(User.class);
		
	}
	
	@Test
	public void testCreateMetamodel_withUnannotatedClass() {
		
	}
	
	@Test
	public void testGetTableName() {
		
	}
	
	@Test
	public void testGetPrimaryKey() {
		
	}
	
	@Test
	public void testGetAttributes() {
		
	}
	
	@Test
	public void testGetForeignKeys() {
		
	}
	
	@Test
	public void testGetGetters() {
		
	}
	
}
