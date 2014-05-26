package Network;

public class OData {
	//Class to hold information loaded at startup.
	//Just a hack
	private String jsonResponse;
	private int Id;
	private String FirstName;
	private String LastName;
	private int Age;
	public String GetJson() {
		return jsonResponse;
	}
	
    public void setId(int Id) {
        this.Id = Id;
    }
 
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }
    
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }
    
    public void setAge(int Age) {
    	this.Age = Age;
    }
 
    public String getFirstName() {
        return FirstName;
    }
 
    public String getLastName() {
        return LastName;
    }
    
    public int getAge() {
        return Age;
    }
    
    public int getId() {
    	return Id;
    }
	public void SetJson(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
	
	
}
