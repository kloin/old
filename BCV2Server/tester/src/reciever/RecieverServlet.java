package reciever;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.shell.util.json.JSONObject;

import Database.DBMaster;
import Retrieval.DataRetrievalManager;
import Retrieval.IDataManager;
import Retrieval.TrailManager;
import Submission.AddManager;
import Submission.SubmissionController;
import Submission.SubmissionManager;

/**
 * Servlet implementation class RecieverServlet
 */
public class RecieverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String jsonResponseString;
	private JSONObject jsonResponse;
	private DBMaster db; 
	private DataRetrievalManager manager;
	private SubmissionController submissionController;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecieverServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	//Initialise DB. 
    	db = DBMaster.GetAnInstanceOfDBMaster();
    	manager = new DataRetrievalManager();
    	submissionController = new SubmissionController();
    }

    /*
	 * Here is the list of messages that can be passed by the user, 
	 * and their required action(s)
	 * =====================================================================
	 * 
	 * User Events (POST)
	 * -----------------------------------------------------------
	 * (000) - Create user 
	 * (001) - Log user in
	 * (301) - Add user as friend
	 * -----------------------------------------------------------
	 * Add data (trail/crumb) events (POST)
	 * -----------------------------------------------------------
	 * (310) - Create Trail
	 * (311) - Create Crumb/Add crumb to trail
	 * (312) - Create Comment
	 * -----------------------------------------------------------
	 * Remove events (POST)
	 * -----------------------------------------------------------
	 * (411) - Remove trail
	 * (421) - Remove crumb from trail
	 * (431) - Remove Comment
	 * -----------------------------------------------------------
	 * Update events (POST)
	 * -----------------------------------------------------------
	 * (511) - Update trail
	 * (521) - Update Crumb
	 * (531) - Update Comment
	 * -----------------------------------------------------------
	 * Retrieving events (GET)
	 * -----------------------------------------------------------
	 * (600) get specific user
 	 * (610) get specific users friends
	 * ------------------------------------------------------------
	 * (700) get specific trail
	 * (710) get specific crumb
	 * (710) get trail with top 20 crumbs
	 * (711) get trail with 20 - 40 crumbs`
	 * (71X) etc....
	 * (790) Get All trails for this user
	 * --------------------------------------
	 * (900) Get Specific User
	 * (910) - 
	 * (920) - Get All crumbs for this user
	 * (911) - Get trail (with specific id)
	 * (921) - Get crumb (with specific id)
	 * 
	 * =====================================================================
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // Set response content type
	      response.setContentType("text/html");
	      //Allow CORS
	      response.addHeader("Access-Control-Allow-Origin", "*");
	      
	      //Start grabbing data
	      Transaction tx = db.GetDatabaseInstance().beginTx();
	      // Actual logic goes here.
	      try {
	    	  System.out.println("About to begin retrieval");
	    	  //Get some data
	    	  jsonResponse = manager.GetData(request);
	    	  
	      } catch (Exception ex) {
	    	  //We have crashed. Stop the DB.  
	    	  System.out.println("I've failed with this exception: " + ex);
	    	  tx.failure();
			}
			
			finally {
				tx.finish();
			}
	      System.out.println("Retrieved this data: " + jsonResponse);
	      response.getOutputStream().println(jsonResponse.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      
		response.setContentType("text/html");
	    response.addHeader("Access-Control-Allow-Origin", "*");
	      
	    //The current logging system.
		System.out.println("Recieved a request to create user: " + request.getParameter("FirstName") + request.getParameter("requestString"));

	    try {
	    	
	    	jsonResponseString = submissionController.SaveData(request);

	    }catch(Exception ex) {
	    	System.out.println("failed to save data. " + ex + " was thrown");
	    } 
	   
	   response.getOutputStream().print(jsonResponseString);
	   System.out.println("Sent: " + jsonResponseString);
	}
	

}
