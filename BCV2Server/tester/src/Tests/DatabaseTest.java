package Tests;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import Database.DBMaster;
import Database.DBMaster.myRelationships;

public class DatabaseTest {
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
	
	@Test
	public void CheckDatabaseInstanceIsObtainable() {
		
		Assert.assertNotNull(dbm.GetDatabaseInstance());
	}
	
	//A brief check to see if we can get data out of the DB
	@Test
	public void CheckIfNodesCanBeRetrieved() {
		
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
		 
		Node node = null;
		try
		{
			node = dbm.RetrieveNode(0);
			Assert.assertEquals("Josiah", node.getProperty("name"));
			tx.success();
		}
		catch (Exception ex)
		{
			tx.failure();
		}
		
		finally {
			tx.finish();
		}
	}
	
	@Test
	public void CheckIfUserCanAddTrail() {
		
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
		
		//Add the trail	 
		Node user = null;
		try//
		{
			int id = dbm.AddTrail("This is a new Trail", 0);
			user = dbm.RetrieveNode(0);
			Assert.assertEquals("Controls", user.getRelationships().iterator().next().getType().name());
			
			user.getSingleRelationship( myRelationships.Controls, Direction.OUTGOING ).delete();
			//user.delete();	//This deletes the base node in the DB
			
			Node trail = dbm.RetrieveNode(id);
			trail.delete();
			
			tx.success();
		}
		catch (Exception ex)
		{
			tx.failure();
		}
		
		finally {
			tx.finish();
		}
	}
	
	@Test
	public void CheckIfUserCanAddCrumbToTrail() {
		
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
			 
		Node crumb = null;
		Node trail = null;
		try
		{
			//Add a trail to add crumbs to.
			int trailId = dbm.AddTrail("TrailA", 1);
			int crumbId = dbm.AddCrumb("This is a crumb", 1, trailId, "MyVideo");
			crumb = dbm.RetrieveNode(crumbId);
			trail = dbm.RetrieveNode(trailId);
			
			//Check the correct relationship is there
			Assert.assertEquals("Part_Of", crumb.getRelationships().iterator().next().getType().name());
			
			//Delete the relationship and node
			crumb.delete();
			trail.getSingleRelationship( myRelationships.Controls, Direction.INCOMING ).delete();
			trail.delete();

			tx.success();
		}
		catch (Exception ex)
		{
			tx.failure();
		}
		
		finally {
			
			//If no exceptions thrown, wrap this shit up. Else, roll back all changes.
			tx.finish();
		}
		
	}


}
