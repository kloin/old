package Retrieval;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;

import Database.DBMaster;
import Tests.CrumbTests.myLabels;
import Trail.Trail;

public class TrailManager extends IDataManager {

	private Trail trail;
	private DBMaster dbMaster;
	private JSONObject jsonResponse;
	HttpServletRequest request;
	
	public TrailManager(HttpServletRequest request) {
		
		this.request = request;
		dbMaster = DBMaster.GetAnInstanceOfDBMaster();
		jsonResponse = new JSONObject();
	}
	/* requestString: 
	 * 	- 'x1x' : get crumb
	 * ------------------------
	 * ------------------------
	 *  - 'x0x' : get trial
	 * 
	 * @see Retrieval.IDataManager#GetData()
	 */
	public JSONObject GetData() {
		String requestString = request.getParameter("requestString");
		System.out.println("Fetching data in TrailManager()");
		//1 is a reqest for a crumb (e.g 710).. Sorry about the nested I was high
		if (requestString.charAt(1) == '1') {
			if(requestString.charAt(2) == '0') {
				System.out.println("Crumb requested");
				GetCrumb();
			}
		}
		
		else if(requestString.charAt(1) == '9') {
			//get all trails for a user.
			if (requestString.charAt(2) == '1') {
				getAllTrailsForUser();
			}
			else if (requestString.charAt(2) == '2') {
				GetAllCrumbsForUser();
			}
		}
		
		//0 Is a request for a trail
		else if (requestString.charAt(1) == '0') {
			System.out.println("Trail requested");
			GetTrail();
		}
		System.out.println("returning this json data :" + jsonResponse);
		return jsonResponse;
	}
	/*
	 * Retrieves a node using the given Id, and with the nodes data it
	 * creates a Trail.
	 * 
	 * PI: How big can a trail be? For massive trails, we may have to grab only 
	 * a few crumbs, e.g the top 50.
	 */

	public void GetTrail() {
		
		//Debug log
		System.out.println("Constructing JSON for trail");
		
		//Fetch our crumb node
		Node trail = dbMaster.RetrieveNode(Integer.parseInt(request.getParameter("trailId")));
		System.out.println("Fetched trail...");
		
		String Title = trail.getProperty("Title").toString();
		System.out.println("Fetched trail Title: "+ Title);
		String Description = trail.getProperty("Description").toString();
		String TrailName = trail.getProperty("trailName").toString();
		System.out.println("Fetched Description: "+ Description);
		
		//Now start constructing the JSON
		try {
			jsonResponse.put("userId", 0);
			jsonResponse.put("trailId", request.getParameter("trailId"));
			jsonResponse.put("Title", Title);
			jsonResponse.put("TrailName", TrailName);
			jsonResponse.put("Description", Description);
		} catch (JSONException e) {
			// Debug info
			System.out.println("Generating the JSON string (TrailManager.GetTrail() failed: " + e);
		}
		
	}
	
	/*
	 * This function retrieves a node via its Id, and using the data from
	 * that node it constructs a Crumb.
	 */
	private void GetCrumb() {
			
			//Debug log
			System.out.println("Constructing JSON for crumb");
			
			//Fetch our crumb node
			Node crumb = dbMaster.RetrieveNode(Integer.parseInt(request.getParameter("crumbId")));
			System.out.println("Fetched Crumb...");
			
			convertSingleNodeToJSON(crumb);
	}
	
	/*
	 * Return all the trails for a certain user.
	 * 
	 * PI: This could cause issues with users with a lots of related trails.
	 * It should return an appropriate amount of trails at one time (think continuous 
	 * scrolling). The reason for this is that trails will be pretty large, so we don't 
	 * want to be fetching 30 trails, all ~5 MB. The user will be waiting for ever.
	 */
	private void getAllTrailsForUser() {
		ResourceIterable<Node> node = dbMaster.GetDatabaseInstance().findNodesByLabelAndProperty(myLabels.Trail, "userId", 0);
		Iterator nodeSearcher = node.iterator();
		System.out.println("Getting all Trails for a user");
		try {
			int numberOfNodes = 0;
			
			while (nodeSearcher.hasNext()) {
				//Get the node once, as each time we use "Next()" we move forward on the list
				Node trail = (Node) nodeSearcher.next();
				//Temp node to store objects then add it to the string
				JSONObject temporaryNode = new JSONObject();
				System.out.println("adding data:");
				//Now get the deets
				String TrailName = trail.getProperty("TrailName").toString();	
				System.out.println("added trailname");
				String Description = trail.getProperty("Description").toString();
				System.out.println("added description");
				String userId = trail.getProperty("userId").toString();
				System.out.println("Added userId");
				
				//the actual neo4j id of the node
				int node_index_id = (int) trail.getId();
				
				System.out.println("Adding crumb Count");
				//Get all the crumbs attatched to this and add them up.
				
				//MATCH (m:Movie {title:"The Matrix"})<-[:ACTS_IN]-(actor)
				//RETURN count(*);
				ResourceIterable<Node> crumbs = dbMaster.GetDatabaseInstance().findNodesByLabelAndProperty(myLabels.Crumb, "trail", request.getParameter("userId"));
				Iterator crumbsIterator = crumbs.iterator();
				int numberOfCrumbs = 0;
				while(crumbsIterator.hasNext()) {
					crumbsIterator.next();
					numberOfCrumbs += 1;
				}
					
				//Debug
				System.out.println("Fetched Description: "+ Description + " Title: " + TrailName);
	
				//now add the deets
				temporaryNode.put("TrailName", TrailName);
				temporaryNode.put("Description", Description);
				temporaryNode.put("NumberOfCrumbs", numberOfCrumbs);
				temporaryNode.put("UserId", userId);
				temporaryNode.put("trailId", node_index_id);
				//Now add the json object into our main json - we need this because i dont know how to iterate a json String
				jsonResponse.put("Node"+numberOfNodes, temporaryNode);
				System.out.println("Added test successfully");
				//System.out.println("Trails Description: " + ((PropertyContainer) nodeSearcher.next()).getProperty("Description"));
				JSONObject object2 = (JSONObject) jsonResponse.get("Node"+numberOfNodes);
				numberOfNodes += 1;
				} 
				
			} catch(JSONException ex) {
				System.out.println(ex);
		}
	}
	
	public void GetAllCrumbsForUser() {

		System.out.println(myLabels.Crumb);
		System.out.println(request.getAttribute("trailId"));
		
		ResourceIterable<Node> node = dbMaster.GetDatabaseInstance().findNodesByLabelAndProperty(myLabels.Crumb, "trailId", request.getParameter("trailId"));
		Iterator nodeSearcher = node.iterator();
		System.out.println("Getting all crumbs for an entity: " + node.toString());
		try {
			int numberOfNodes = 0;
			
			while (nodeSearcher.hasNext()) {
				//Get the node once, as each time we use "Next()" we move forward on the list
				System.out.println(numberOfNodes);
				Node trail = (Node) nodeSearcher.next();
				//Temp node to store objects then add it to the string
				JSONObject temporaryNode = new JSONObject();
				
				//Now get the deets
				String Title = trail.getProperty("Title").toString();				
				String Description = trail.getProperty("Description").toString();
				int crumbId = (int) trail.getId();
				String Latitude = trail.getProperty("Latitude").toString();
				String Longitude = trail.getProperty("Longitude").toString();
				//Debug
				System.out.println("Fetched Description: "+ Description + " Title: " + Title + "id" + crumbId);
	
				//now add the deets
				temporaryNode.put("Title", Title);
				temporaryNode.put("Description", Description);
				temporaryNode.put("crumbId", crumbId);
				temporaryNode.put("Latitude", Latitude);
				temporaryNode.put("Longitude", Longitude);
				
				//Now add the json object into our main json:
				jsonResponse.put("Node"+numberOfNodes, temporaryNode);
				System.out.println("Added test successfully");
				//System.out.println("Trails Description: " + ((PropertyContainer) nodeSearcher.next()).getProperty("Description"));
				JSONObject object2 = (JSONObject) jsonResponse.get("Node"+numberOfNodes);
				numberOfNodes += 1;
				} 
				
			} catch(JSONException ex) {
				System.out.println(ex);
		}

	}

	/*
	 * Convert the crumb we've got to a json String
	 */
	private void convertSingleNodeToJSON(Node crumb) {
		String Title = crumb.getProperty("Title").toString();
		System.out.println("Fetched Crumb Title: "+ Title);
		String Description = crumb.getProperty("Description").toString();
		
		System.out.println("Fetched Description: "+ Description);
		
		//Now start constructing the JSON
		jsonResponse = new JSONObject();
		try {
			jsonResponse.put("userId", 0);
			jsonResponse.put("crumbId", request.getParameter("crumbId"));
			jsonResponse.put("Title", Title);
			jsonResponse.put("Description", Description);
		} catch (JSONException e) {
			// Debug info
			System.out.println("Generating the JSON string (TrailManager.getCrumb() failed: " + e);
		}
	}
	
	
}
