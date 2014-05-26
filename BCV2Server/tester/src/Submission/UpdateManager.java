package Submission;

import javax.servlet.http.HttpServletRequest;

public class UpdateManager extends SubmissionManager {

	private String jsonResponse;
	public String SpeakUp() 
	{
		return "UpdateManager";
		
	}
	
	public String SaveData(HttpServletRequest request) {
		return jsonResponse;
	}
}
