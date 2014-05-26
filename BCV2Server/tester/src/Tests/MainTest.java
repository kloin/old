package Tests;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Main.Reciever;

public class MainTest {
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	//This class tests that the reciever can create a 
	@Test
	public void TestReciever() {
		Reciever reciever = new Reciever();
		reciever.RecieveMessage("3000");
		Assert.assertEquals(true, reciever.GetSubmissionResult());
	}
	

}
