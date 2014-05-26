package Main;

public class Message {

	//The code that reflects what is being done (update, add, remove etc..)
	private int _identifier;
	
	//User ID
	private int _userId;
	
	//Just an object at the moment because I don't know what I want to 
	//do with it.
	private Object _media;
	
	public Message(Object media, int userId, int identifier) 
	{
		_media = media;
		_userId = userId;
		_identifier = identifier;
				
	}
	
	public Object GetMedia()
	{
		return _media;
	}
	
	
	public int GetUser()
	{
		return _userId;
	}
	
	public int GetIdentifier()
	{
		return _identifier;
	}
}
