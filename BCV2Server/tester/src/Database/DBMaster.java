package Database;

import org.neo4j.*;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import sun.rmi.runtime.Log;
/*
 * This class controls the database.
 * 
 * It has a constructor, a get method that returns an instance of
 * the database, and a shutdown method (private)
 * 
 * Written by Josiah Kendall, 1 February 2014
 */
public class DBMaster {
	
	//Our database instance
	private static GraphDatabaseService _db;
	private static DBMaster singletonDBMaster;
	
	/*
	 * Straight up constructing. This needs to be a singleton because the db can only be shared,
	 * because constructing it is very expensive.
	 */
	private DBMaster() {
		//The current address is localhost - needs to be changed at a later date
		_db = new GraphDatabaseFactory().newEmbeddedDatabase("C:\\Users\\Josiah\\Documents\\Neo4j\\default.graphdb");
		registerShutdownHook();
	}
	
	public static DBMaster GetAnInstanceOfDBMaster() {
		//If an instance exists, return it, else create a new one.
		//NOTE: I'm not sure what happens when multiple threads access this one instance
		//This could cause bugs
		if (singletonDBMaster == null) {
			singletonDBMaster = new DBMaster();
		}
		
		return singletonDBMaster;
	}
	
	
	
	public enum myRelationships implements RelationshipType
	 {
		Controls,
		Friends_With,
		Part_Of
	 }

	public enum myLabels implements Label
	{
		Person,
		User,
		Trail,
		Crumb
	}
	//Return an a reference to the instance of the database
	public static GraphDatabaseService GetDatabaseInstance() {
		return  _db;
	}
	
	public void Stop() {
		_db.shutdown();
	}
	
	//Shut down the database on exit - Should actually never happen once the db is running
	private static void registerShutdownHook() {
		// Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            _db.shutdown();
	        }
	    } );
		
	}

	public Node RetrieveNode(long id) {
		
		Node node = _db.getNodeById(id);
		return node;
	}

	public int AddUser(String firstName, String lastName, int age) {
		int id = 0;
		Transaction tx = _db.beginTx();
		try {
			Node node = _db.createNode();
			node.addLabel(myLabels.Person);
			id = (int) node.getId();
			//Set first name, lastname, any other name.. (to be implemented)
			
			node.setProperty("FirstName", firstName);
			node.setProperty("LastName", lastName);
			node.setProperty("Age", age);
			tx.success();			
		}
		catch (Exception ex){
			tx.failure();
		}
		finally {
			tx.finish();
		}
		
		
		//The successful case;
		return id;
		
	}

	//Add a trail related to a certain user.
	public int AddTrail(String trailName, int userId, String description) {
		int id = 0;
		Transaction tx = _db.beginTx();
		
		try {
			
			//Create trail node
			Label label = myLabels.Trail;
			Node trailNode = _db.createNode();
			
			//Attatch a label to our node
			trailNode.addLabel(label);
			id = (int) trailNode.getId();
			
			//Set properties
			trailNode.setProperty("TrailName", trailName);
			trailNode.setProperty("userId", userId);
			trailNode.setProperty("Description", description);
			
			//Get the user node (it must exist as this function can only occur as
			//a result of a user request.
			Node userNode = _db.getNodeById(userId);
			userNode.createRelationshipTo(trailNode, myRelationships.Controls);
			
			System.out.println("Added trail with id: " + id);
			tx.success();			
		}
		catch (Exception ex){
			tx.failure();
		}
		finally {
			tx.finish();
		}
		
		return id;
	}

	public int AddCrumb(String crumbTitle, String description, int userId, String trailId,
			String crumbMedia, String Latitude, String Longitude) {
		
		int id = 0;
		
		Transaction tx = _db.beginTx();
				
			try {
				//Create trail node
				Label label = myLabels.Crumb;
				Node crumb = _db.createNode();
				
				//Attatch a label to our node
				crumb.addLabel(label);
				id = (int) crumb.getId();
				
				//Set properties
				crumb.setProperty("Title", crumbTitle);
				crumb.setProperty("user", userId);
				crumb.setProperty("trailId", trailId);
				crumb.setProperty("Description", description);
				crumb.setProperty("mediaLocation", crumbMedia);
				crumb.setProperty("Longitude", Longitude);
				crumb.setProperty("Latitude", Latitude);
				
				//Set the trail that the crumb belongs to
				Node trail = _db.getNodeById(Integer.parseInt(trailId));
				trail.createRelationshipTo(crumb, myRelationships.Part_Of);
				
				tx.success();			
			}
			catch (Exception ex){
				System.err.println("The database has crashed during saving");
				ex.printStackTrace();
				
				//Let the database know its know happening
				tx.failure();
			}
			finally {
				//Shut the fucking door
				tx.finish();
			}
		
		
		return id;
	}


	
}
