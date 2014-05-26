package Submission;

import javax.servlet.http.HttpServletRequest;

public class RemoveManager extends SubmissionManager{

	public String SpeakUp()
	{
		return "RemoveManager";
	}
	
	public String SaveData(HttpServletRequest request) {
		return jsonResponse;
	}
}
