package Tests;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.shell.util.json.JSONObject;

import Database.DBMaster;
import Retrieval.DataRetrievalManager;
import Submission.SubmissionController;

public class TrailTests {

	DBMaster dbm;
	@Before
	public void setUp() throws Exception {
		dbm = DBMaster.GetAnInstanceOfDBMaster();
	}

	@After
	public void tearDown() throws Exception {
		dbm.Stop();
	}
	
	@Test 
	public void TestTrailCanBeSaved() {
		//create trail data - json
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
		
		JSONObject jsonResponse = new JSONObject();
		Node node = null;
		try
		{
			//Create variable
		    HttpServletRequest req = createMock(HttpServletRequest.class);

			//set variable properties - userid, crumbid, title, description
		    expect(req.getParameter("requestString")).andReturn("310").times(1, 2);
		    expect(req.getParameter("UserId")).andReturn("0");
		    expect(req.getParameter("TrailName")).andReturn("trailName");
		    expect(req.getParameter("TrailId")).andReturn("1233333333");
		    expect(req.getParameter("Title")).andReturn("title");
		    expect(req.getParameter("Description")).andReturn("description");

		    // switch the mock to replay state
		    replay(req);
			//call method
		    SubmissionController sc = new SubmissionController();
		    sc.SaveData(req);
			
			tx.success();
		}
		catch (Exception ex)
		{
			System.out.println("Exception in request: "+ ex);
			fail();
			dbm.Stop();
			tx.failure();
		}
		
		finally {
			tx.finish();
		}
	}
	
	//Retrieve an already existing trail
	@Test
	public void TestTrailCanBeRetrieved() {
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
		
		JSONObject jsonResponse = new JSONObject();
		Node node = null;
		try
		{
			//Create variable
		    HttpServletRequest req = createMock(HttpServletRequest.class);

			//set variable properties - userid, crumbid, title, description
		    expect(req.getParameter("requestString")).andReturn("700").times(1, 2);
		    expect(req.getParameter("userId")).andReturn("0").times(1);
		    expect(req.getParameter("trailId")).andReturn("14599").times(1,2);
		    expect(req.getParameter("Title")).andReturn("title");
		    expect(req.getParameter("Description")).andReturn("description");


		    // switch the mock to replay state
		    replay(req);
			//call method
		    DataRetrievalManager drm = new DataRetrievalManager();
		    jsonResponse = drm.GetData(req);
		    
			//test result matches submission
		    Assert.assertEquals(jsonResponse.getString("userId"), "0");
		    Assert.assertEquals(jsonResponse.getString("TrailName"), "trailName");
		    Assert.assertEquals(jsonResponse.getString("Title"), "title");
		    Assert.assertEquals(jsonResponse.getString("Description"), "description");
			
			tx.success();
		}
		catch (Exception ex)
		{
			System.out.println("Exception in request: "+ ex);
			fail();
			dbm.Stop();
			
			tx.failure();
		}
		
		finally {
			tx.finish();
		}
	}


}
