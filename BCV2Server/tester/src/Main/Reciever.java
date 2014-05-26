package Main;

import Main.Message;
import Submission.SubmissionController;
import org.neo4j.*;

public class Reciever {

	private String _message;
	private SubmissionController _submissionController;
	private static boolean _submissionResult; //True is success
	private static int _id;
	
	/*
	 * Here is the list of messages that can be passed by the user, 
	 * and their required action(s)
	 * =====================================================================
	 * 
	 * User Events
	 * -----------------------------------------------------------
	 * (000) - Create user
	 * (001) - Log user in
	 * (301) - Add user as friend
	 * -----------------------------------------------------------
	 * Add data (trail/crumb) events
	 * -----------------------------------------------------------
	 * (311) - Create Trail
	 * (321) - Create Crumb/Add crumb to trail
	 * (331) - Create Comment
	 * -----------------------------------------------------------
	 * Remove events 
	 * -----------------------------------------------------------
	 * (411) - Remove trail
	 * (421) - Remove crumb from trail
	 * (431) - Remove Comment
	 * -----------------------------------------------------------
	 * Update events
	 * -----------------------------------------------------------
	 * (511) - Update trail
	 * (521) - Update Crumb
	 * (531) - Update Comment
	 * 
	 * ======================================================================
	 *  
	 */
	
	//The message processing class
	public void RecieveMessage(String message)
	{
		//Begin processing a message
		_message = message;
		NotifyController();
		
	}
	//Decide what is being requested, and create the appropriate manager
	private void NotifyController() {
		
		_submissionController = new SubmissionController();
		_submissionController.CreateAppropriateInterface(_message, new Object());
	
	}
	
	//Get the result of the data submission
	public static boolean GetSubmissionResult() {
		return _submissionResult;
	}
	
	//A static function so that the manager (e.g AddManager) can notify the reciever of the result.
	//TODO move this to the submissionController
	public static void SetSubmissionResult(boolean _submissionResult) {
		Reciever._submissionResult = _submissionResult;
	}

}
