package com.revature.repotests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.InsertDB;
import com.revature.util.ConnectionUtil;
import com.revature.util.Metamodel;



public class InsertDBTests {
	
	private InsertDB insertDB;
	
	@Before
	public void startup() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	@Test
	public void InsertUserTest() {
		
	}
	@Test
	public void insertUserTest() throws Exception {
		ConnectionUtil jdbcObj = new ConnectionUtil();
		
		Connection connObj = null;
		DataSource datasource = jdbcObj.setUpPool();
		connObj = datasource.getConnection();
		
		User user = new User(1, "Frank", "Aurori", "test123@gmail.com", "pass", Role.ADMIN);
		Metamodel<User> um = new Metamodel<>(User.class);
		
		InsertDB.insertUser(um, user, connObj);
		
		//assertThat(InsertDB.insertUser(um, user, connObj), is(notNullValue()));
		
	}
	
	@Test
	public void insertProduct() {
		
	}
	@Test
	public void insertCategory() {
		
	}
	@Test
	public void insertOrder() {
		
	}
	

}
