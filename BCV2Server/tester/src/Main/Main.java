package Main;

public class Main {
	//Default userid
	private static int _id = 0;
	
	//Static setter for the users id (to be sent back to the device)
	public static void NotifyIdSet(int id) {
		_id = id;
	}
	
}
