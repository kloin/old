package Submission;

import javax.servlet.http.HttpServletRequest;
import Database.DBMaster;

public class SubmissionManager {

	DBMaster dbm;
	String jsonResponse;
	
	public SubmissionManager() {
		// Create the appropriate manager based on the given string
		dbm = DBMaster.GetAnInstanceOfDBMaster();
		jsonResponse = "";
	}

	public String SaveData(HttpServletRequest request) 
	{
		return null;
	}
	

	public String SpeakUp() 
	{
		//It's the big dawg!!
		return "I am the SubmissionManager class";
	}

}
