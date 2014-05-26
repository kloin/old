package Network;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.OutputStreamWriter;
import java.net.Socket;

import java.io.PrintWriter;

import android.os.AsyncTask;
import android.util.Log;
/**
 * This is a (currently) unused class which asynchronously sends bits over a socket to the
 * network. This was created to send photos and videos (and in the future may still be used
 * that way) but right now I am using base64 because it is easy. It also means very little 
 * changes on the server side. The down side is that it is ~33% more data hungry. Sorry users
 * 
 * @since 8/5/2014
 * @deprecated
 * @author Josiah
 */
public class ClientSideImageService extends AsyncTask<Object, Void, Object>{

	    private static Socket socket;
	    private static PrintWriter printWriter;

			@Override
			protected Object doInBackground(Object... arg0) {
				try {
		    	Log.d("CLIENT", "Creating client");
	            socket = new Socket("192.168.1.16",9999);
	            PrintWriter out;
	            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

//	            printWriter = new PrintWriter(socket.getOutputStream(),true);
	           
	            out.println("yay bro");
	            System.out.println("Sent data");
	        }

	        catch(Exception e)

	        {

	            System.out.println("An error occured: " +e);

	        }
				return arg0;
			}
			
			public void onPostExecute() {
				Log.d("CLIENT", "Done Sending");
			}
}