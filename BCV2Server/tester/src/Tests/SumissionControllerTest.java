package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Submission.AddManager;
import Submission.RemoveManager;
import Submission.SubmissionController;
import Submission.UpdateManager;

public class SumissionControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void CheckControllerCanCreateUpdateManager() 
	{
		//Ensure that the class is an instance of UpdateManager
		SubmissionController sc = new SubmissionController();
		sc = new SubmissionController();
		sc.CreateAppropriateInterface("5000", new Object());
		UpdateManager um = (UpdateManager) sc.GetClassReference();
		Assert.assertEquals("UpdateManager", um.SpeakUp());		
	}
	
	@Test
	public void CheckControllerCanCreateAddManager(){
		//Create add Manager
		SubmissionController sc = new SubmissionController();
		sc.CreateAppropriateInterface("3000", new Object());
		AddManager am = (AddManager) sc.GetClassReference();
		Assert.assertEquals("AddManager", am.SpeakUp());	
	}
	
	@Test
	public void CheckControllerCanCreateRemoveManager(){
		SubmissionController sc = new SubmissionController();
		sc.CreateAppropriateInterface("4000", new Object());
		RemoveManager rm = (RemoveManager) sc.GetClassReference();
		Assert.assertEquals("RemoveManager", rm.SpeakUp());		
	}

}
