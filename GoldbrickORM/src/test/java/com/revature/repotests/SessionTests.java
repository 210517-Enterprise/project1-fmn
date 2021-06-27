package com.revature.repotests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import com.revature.repos.DatabaseBuilder;
import com.revature.repos.DatabaseDeleter;
import com.revature.repos.DatabaseUpdater;
import com.revature.repos.GetDB;
import com.revature.repos.InsertDB;
import com.revature.repos.Session;
import com.revature.util.Configuration;

public class SessionTests {
	
	private DatabaseBuilder builder;
	private DatabaseDeleter deleter;
	private DatabaseUpdater updater;
	private InsertDB inserter;
	private GetDB getter;
	private Connection conn;
	private Configuration cfg;
	
	@Before
	public void startup() {
		this.builder = mock(DatabaseBuilder.class);
		this.deleter = mock(DatabaseDeleter.class);
		this.updater = mock(DatabaseUpdater.class);
		this.inserter = mock(InsertDB.class);
		this.getter = mock(GetDB.class);
		this.conn = mock(Connection.class);
		this.cfg = mock(Configuration.class);
		
	}
	
	@Test
	public void testCreateTables_successful() {
		Session ses = new Session();
		
		//when(ses.createTables(cfg, conn)).thenReturn(true);
		
	}

}
