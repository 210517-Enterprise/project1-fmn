package com.revature.utiltests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.revature.models.User;
import com.revature.util.ColumnField;
import com.revature.util.Metamodel;

public class MetamodelTests {

	@Before
	public void setup() {

	}

	@Test
	public void testCreateMetamodel_withAnnotatedClass() {
		Metamodel<User> mm = Metamodel.of(User.class);

		assertEquals(mm.getClazz(), User.class);

	}

	@Test(expected = IllegalStateException.class)
	public void testCreateMetamodel_withUnannotatedClass() {
		Metamodel mm = Metamodel.of(String.class);
	}

	@Test
	public void testGetTableName() {
		Metamodel<User> mm = Metamodel.of(User.class);

		assertEquals(mm.getTableName(), "users");
	}

	@Test
	public void testGetPrimaryKey() {

		Metamodel<User> mm = Metamodel.of(User.class);

		assertEquals(mm.getPrimaryKey().getColumnName(), "id");
	}



}
