package Submission;

import javax.servlet.http.HttpServletRequest;

import Main.Reciever;

public class SubmissionController {
/*
 * This is a class that creates the appropriate interface for the submitted data. 
 * This class controlls the submission of data from the user, namely adding, updating,
 * and deleting data from the database
 */
	private boolean _submissionResult = false;
	private SubmissionManager _submissionManager;
	private String jsonResponse;
	
	public SubmissionController()
	{
		jsonResponse = "";
	}

	public String SaveData(HttpServletRequest request) {
		System.out.println("About to save data.");
		//Create our appropriate manager
		managerFactory(request);
		System.out.println("created manager of type: " + _submissionManager.SpeakUp());
		jsonResponse = _submissionManager.SaveData(request);
		System.out.println("Saved Data and returned JSON STRING: " + jsonResponse);
		return jsonResponse;
		
	}
	
	//Create the class we
	private void managerFactory(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String userRequestCode = request.getParameter("requestString");
		System.out.println("Trying to create a manager with: "+ userRequestCode);
		if (userRequestCode.startsWith("3"))
		{
			System.out.println("Trying to create an ADDMANAGER");
			_submissionManager = new AddManager();
			System.out.println("Created: " + _submissionManager.SpeakUp());
			_submissionResult = true;
		}
		
		else if(userRequestCode.startsWith("4")) 
		{
			_submissionManager = new RemoveManager();
			_submissionResult = true;
		}
		
		else if(userRequestCode.startsWith("5")) 
		{
			_submissionManager = new UpdateManager();
			_submissionResult = true;
		}
		else
		{
			System.out.println("Trying to create an ADDMANAGER");
			//This should not happen. Fuck it
			_submissionResult = false;
			
		}
		
	}
	
	public SubmissionManager GetClassReference() 
	{
		return _submissionManager;
	}
	
	
}
