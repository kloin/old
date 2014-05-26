package Tests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.neo4j.*;

import Database.DBMaster;
import Main.Reciever;
import Submission.AddManager;
import Submission.SubmissionController;
import Submission.SubmissionManager;

public class SubmissionManagerTest {

	DBMaster dbm;
	
	@Before
	public void setUp() throws Exception {
		dbm = DBMaster.GetAnInstanceOfDBMaster();
	}

	@After
	public void tearDown() throws Exception {
		dbm.Stop();
	}
	
	//You have to create a new DB before every test
		@Before
		public void prepareTestDatabase()
		{
			dbm = DBMaster.GetAnInstanceOfDBMaster();
		}
		//You also have to tear down the db after each test 
		@After
		public void TearDownDB() {
			dbm.Stop();
		}
}
	
	
