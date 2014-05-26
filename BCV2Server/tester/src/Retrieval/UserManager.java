package Retrieval;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.graphdb.Node;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;

import Database.DBMaster;

public class UserManager extends IDataManager {

	private HttpServletRequest request;
	private DBMaster dbMaster;
	
	private JSONObject jsonResponse;
	
	public UserManager(HttpServletRequest request) {
		this.request = request;
		dbMaster = DBMaster.GetAnInstanceOfDBMaster();
	}
	
	@Override
	public JSONObject GetData() {
		String requestString = request.getParameter("requestString");
		
		if (requestString.charAt(2) == '1') {
			getUser();
		}

		return jsonResponse;
	}
	
	private void getUser() {
		System.out.println("Constructing JSON");
		Node localUserNode =  dbMaster.RetrieveNode(Integer.parseInt(request.getParameter("userId")));
		Integer userId = (int) localUserNode.getId();
		String Age = localUserNode.getProperty("Age").toString();
		String FName = localUserNode.getProperty("FirstName").toString();
		String LName = localUserNode.getProperty("LastName").toString();
		jsonResponse = new JSONObject();
		
		try {
			jsonResponse.put("userId", userId);
			jsonResponse.put("FirstName", FName);
			jsonResponse.put("LastName", LName);
			jsonResponse.put("Age", Age);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	

}
