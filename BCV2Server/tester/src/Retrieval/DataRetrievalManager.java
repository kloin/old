package Retrieval;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.shell.util.json.JSONObject;

public class DataRetrievalManager {
	
	private JSONObject jsonResponse;
	private IDataManager getManager;
	private HttpServletRequest request;
	
	public JSONObject GetData(HttpServletRequest request) {
		System.out.println("About to get some data.");
		this.request = request;
		
		//Create our appropriate manager
		managerFactory();
		jsonResponse = getManager.GetData();
		System.out.println("GET successfully retrieved this Data: " + jsonResponse);
		return jsonResponse;
	}
	
	//This creates the appropriate interface using all the local variables.
	private void managerFactory() {
		String userRequestCode = request.getParameter("requestString");
		if (userRequestCode.startsWith("6")) {
			//We want a user / user data
			System.out.println("Created a user Manager");
			getManager = new UserManager(request);
		}
		
		else if(userRequestCode.startsWith("7")) {
			//We want a trail
			System.out.println("Created a trailManager");
			getManager =new TrailManager(request);
		}
			
		else if(userRequestCode.startsWith("8")) {
			//We want a locality
			System.out.println("Created a localityManager");
			getManager = new LocalityManager(request);
		}
	}
	
}
