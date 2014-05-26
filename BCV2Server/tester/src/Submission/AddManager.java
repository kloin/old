package Submission;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;
import Main.Main;

public class AddManager extends SubmissionManager {
	private HttpServletRequest request;
	public String SpeakUp() {
		// TODO Auto-generated method stub
		return "AddManager";
	}
	
	//Add a user and notify the main class of the newly generated id.
	private int AddUser(String firstName, String lastName, int age) {
		int userId = dbm.AddUser(firstName, lastName, age);
		return userId;
	}
	
	//Add a trail for a certain user (based on ID)
	private void AddTrail() {
		System.out.println("Adding trail");
		String trailName = request.getParameter("TrailName");
		int userId = Integer.parseInt(request.getParameter("userId"));
		String description = request.getParameter("Description");
		
		//Submit data
		int trailId = dbm.AddTrail(trailName, userId, description);
	}
	
	//The use case of a user adding
	private void AddCrumb(String crumbTitle, String description, int userId, String trailId, String crumbMedia) {
		int crumbId = dbm.AddCrumb(crumbTitle, description, userId, trailId, "Test", request.getParameter("Latitude"), request.getParameter("Longitude"));
		if (crumbMedia.length() < 1) {
		//==========================================================
			//Begin constructing image
			RenderedImage image = null;
	        byte[] imageByte;
	        try {
	        	//Decode the BASE64 media string
	            BASE64Decoder decoder = new BASE64Decoder();
	            imageByte = decoder.decodeBuffer(crumbMedia);
	            
	            //Open up the input for our decoder to buffer to
	            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	            
	            //Construct the image
	            image = ImageIO.read(bis);
	            
	            //It's common courtesy to shut the door on the way out...
	            bis.close();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        //End contstruction
	        //=============================================================
	               
		
		
		
			//Save this badboy with the crumbId so we can find it again
			File outputfile = new File("C:\\Users\\Josiah\\Desktop\\BreadCrumbsMedia\\"+crumbId+".png");

	        try {
				ImageIO.write(image, "png", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void AddFriend(int userId, int friendId) {
		//dbm.AddFriend(userId, friendId);
	}
	
	@Override
	public String SaveData(HttpServletRequest request) {
		this.request = request;
		PerformRequestedFunction();
		return jsonResponse;
	}
	private void PerformRequestedFunction() {
		
		char userRequestCode = request.getParameter("requestString").charAt(1);
		
		//Here lies some filthy fucking dragons. find a more
		//elegant solution than manufactured JSON strings..
		if (userRequestCode == '0') {
			Integer id = AddUser(request.getParameter("FirstName"), 
					request.getParameter("LastName"), Integer.parseInt(request.getParameter("Age")));
			
			jsonResponse = "{'ObjectAdded': 'User', UserAdded': true, 'UserId: " + id.toString() + "' }";
		}
		
		if (userRequestCode == '1') {
			AddTrail();
		}
		
		if (userRequestCode == '2') {
			String description = request.getParameter("Description");
			
			
	        
			String title = request.getParameter("Title");
			String photo = request.getParameter("Photo");
			String trailId = request.getParameter("trailId");
			System.out.println("ADDING CRUMB:" + title);
			AddCrumb(title, description, 0, trailId, photo);
		}
		
		
			
	}

}
