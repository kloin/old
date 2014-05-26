package IntegrationTests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import Database.DBMaster;
import Database.DBMaster.myRelationships;
import Retrieval.DataRetrievalManager;
import Retrieval.IDataManager;
import Retrieval.TrailManager;

public class GetAndSetTest {
	private DBMaster dbm;
	private DataRetrievalManager retriever;
	private TrailManager tm;
	
	@Before
	public void setUp() throws Exception {
		dbm = DBMaster.GetAnInstanceOfDBMaster();
		retriever = new DataRetrievalManager();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void TestThatWeCanGetAfterSet() {
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
		tm = (TrailManager) retriever.CreateAppropriateInterface("7", new Object());
		Node crumb = null;
		Node trail = null;
		try
		{
			//Add a trail to add crumbs to.
			int trailId = dbm.AddTrail("TrailA", 1);
			int crumbId = dbm.AddCrumb("This is a crumb", 1, trailId, "MyVideo");
			crumb = tm.GetCrumb(crumbId);
			trail = tm.GetTrail(trailId);
			
			Assert.assertNotNull(crumb);
			Assert.assertNotNull(trail);

			//Check the correct relationship is there
			Assert.assertEquals("Part_Of", crumb.getRelationships().iterator().next().getType().name());
			
			
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
