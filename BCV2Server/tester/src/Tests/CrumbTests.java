package Tests;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;

import Database.DBMaster;
import Retrieval.DataRetrievalManager;
import Submission.AddManager;

public class CrumbTests {

	private static final String USER_AGENT = null;
	DBMaster dbm;
	@Before
	public void setUp() throws Exception {
		dbm = DBMaster.GetAnInstanceOfDBMaster();
	}

	@After
	public void tearDown() throws Exception {
		dbm.Stop();
	}
	/*
	@Test
	public void TestCrumbCanBeRetrieved() {
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
		
		JSONObject jsonResponse = new JSONObject();
		Node node = null;
		try
		{
			//Create variable
		    HttpServletRequest req = createMock(HttpServletRequest.class);

			//set variable properties - userid, crumbid, title, description
		    expect(req.getParameter("requestString")).andReturn("710").times(1, 2);
		    expect(req.getParameter("userId")).andReturn("0").times(1);
		    expect(req.getParameter("crumbId")).andReturn("13477").times(1,2);
		    expect(req.getParameter("Title")).andReturn("Titties");
		    expect(req.getParameter("Description")).andReturn("YuM!!!");


		      // switch the mock to replay state
		    replay(req);
			//call method
		    DataRetrievalManager drm = new DataRetrievalManager();
		    jsonResponse = drm.GetData(req);
		    
			//test result matches submission
		    Assert.assertEquals(jsonResponse.getString("userId"), "0");
		    Assert.assertEquals(jsonResponse.getString("crumbId"), "13477");
		    Assert.assertEquals(jsonResponse.getString("Title"), "Title");
		    Assert.assertEquals(jsonResponse.getString("Description"), "Thesese");
			
			tx.success();
		}
		catch (Exception ex)
		{
			System.out.println("Exception in request: "+ ex);
			fail();
			tx.failure();
		}
		
		finally {
			tx.finish();
		}
	}*/
	
	@Test
	public void TestAllCrumbsCanBeRetrieved() throws IOException {
		//Get every crumb in the db
		
		Transaction tx = dbm.GetDatabaseInstance().beginTx();
		try {
		
		ResourceIterable<Node> node = dbm.GetDatabaseInstance().findNodesByLabelAndProperty(myLabels.Crumb, "this", "this");
		Iterator nodeSearcher = node.iterator();
		
		while (nodeSearcher.hasNext()) {
			System.out.println(node.iterator().next().getProperty("Description"));
		}
		
		Assert.assertEquals(node, null);
		tx.success();
		}
		catch(Exception ex) {
			tx.failure();
		}
		finally {
			tx.finish();
		}
	}
	
	public enum myLabels implements Label
	{
		Person,
		User,
		Trail,
		Crumb
	}
}
