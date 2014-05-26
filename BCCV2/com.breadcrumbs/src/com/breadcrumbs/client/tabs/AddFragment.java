package com.breadcrumbs.client.tabs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.breadcrumbs.client.MainPage;
import com.breadcrumbs.client.R;

import Network.ClientSideImageService;
import ServiceProxy.MasterProxy;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddFragment extends Fragment {
	private View rootView;
	private MasterProxy clientProxyService;
	private Camera mCamera;
	private Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.add_activity, container, false);
		clientProxyService = MasterProxy.GetProxyInstance();
		mCamera = getCameraInstance();
		context = rootView.getContext();
		if (mCamera != null) {
			//Just clutching really
		TakePhotoWithCamera();
		//Open up server
		Log.d("CLIENT", "About to call client");
        // Create our Preview view and set it as the content of our activity.
        CameraController camController = new CameraController(this.getActivity(), mCamera);
        FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
        preview.addView(camController);
		}
        
		//Create our camera in a seperate threa
		
		
		//Button handlers
	//	addClickListeners();
		return rootView;
	}
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    	System.out.println("Fuck the camera is not letting us use it");
 	    }
	    return c; // returns null if camera is unavailable
	}
	
	
	/*private void addClickListeners() {
		//Add button (+)
		ImageButton addCTButton = (ImageButton) rootView.findViewById(R.id.AddTrailOrCrumb);
		final TextView trailName = (TextView) rootView.findViewById(R.id.trailName);
		final TextView trailDescription = (TextView) rootView.findViewById(R.id.trailDescription);
		addCTButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	System.out.println("Adding crumb\n\n adding crumb");
		    	clientProxyService.AddCrumb(trailName.getText().toString(), trailDescription.getText().toString(), "0");
		    }
		});
		
		
		
	}*/
	
	
	private class CameraController extends SurfaceView {
		private SurfaceHolder mHolder;

	    public CameraController(Context context, Camera camera) {
	        super(context);
	        setUpCamera(mCamera);
	    }
	    
	    private void setUpCamera(Camera camera) {
	    	mCamera = camera;
	    	Camera.Parameters parameters = mCamera.getParameters();
	    	if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
	              parameters.set("orientation", "portrait");
	              camera.setDisplayOrientation(0);
	              // Uncomment for Android 2.0 and above
	              parameters.setRotation(0);
	           } 
	    	//parameters.set("camera-id", 2);
	    	mCamera.setParameters(parameters);
	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        try {
	        	//Set up our preview
				mCamera.setPreviewDisplay(mHolder);
	            mCamera.setDisplayOrientation(0);
	            
				//Start the preview
	            mCamera.stopPreview(); //Safety
				mCamera.startPreview();
				
				//Attatch listeners/Callbacks
				mHolder.addCallback(new SurfaceHolder.Callback() {
				
				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					//Free the camera
					mCamera.stopPreview();
				}
				
				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					 // The Surface has been created, now tell the camera where to draw the preview.
			        try {
			            mCamera.setPreviewDisplay(holder);
			            mCamera.startPreview();
			        } catch (IOException e) {
			            Log.d("Error setting camera preview: " + e.getMessage(), "ERROR");
			        }
					
				}
				
				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width,
						int height) {
					// If your preview can change or rotate, take care of those events here.
			        // Make sure to stop the preview before resizing or reformatting it.

			        if (mHolder.getSurface() == null){
			          // preview surface does not exist
			          return;
			        }

			        // stop preview before making changes
			        try {
			            mCamera.stopPreview();
			        } catch (Exception e){
			          // ignore: tried to stop a non-existent preview
			        }

			        // set preview size and make any resize, rotate or
			        // reformatting changes here

			        // start preview with new settings
			        try {
			            mCamera.setPreviewDisplay(mHolder);
			            //mCamera.setDisplayOrientation(90);
			            mCamera.startPreview();

			        } catch (Exception e){
			            Log.d("Error starting camera preview: " + e.getMessage(), "ERROR");
			        }
					
				}
			});
	        } catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
	        // deprecated setting, but required on Android versions prior to 3.0. Not sure if i will support this low
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }
	    
	    public void surfaceCreated(SurfaceHolder holder) {
	        // The Surface has been created, now tell the camera where to draw the preview.
	        try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	        } catch (IOException e) {
	            Log.d("Error setting camera preview: " + e.getMessage(), "ERROR");
	        }
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // empty. Take care of releasing the Camera preview in your activity.
	    	mCamera.stopPreview();
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        // If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.

	        if (mHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        try {
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();

	        } catch (Exception e){
	            Log.d("Error starting camera preview: " + e.getMessage(), "ERROR");
	        }
	    }

		
	}

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			System.out.println( "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			BitmapFactory.Options options=new BitmapFactory.Options();
			//dunno
			options.inSampleSize = 0;

			Bitmap m=BitmapFactory.decodeByteArray(data,0,data.length,options);
			System.out.println( "onPictureTaken - raw" + m.toString());
			
			/*itmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			byte[] b = baos.toByteArray(); */
			String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
			SettingsFragment.SetMedia(m);
			MainPage.setViewPager(2);
			//clientProxyService.AddCrumb("haha", "talk about it niggaaa", "0", encodedImage);
			System.out.println(encodedImage);
			MediaStore.Images.Media.insertImage(context.getContentResolver(), m, "titties" , "D");
			
		}
	};
	

	
	//The single click to take a photo. Video not currently supported
	public void TakePhotoWithCamera() {
		//Our button via tag
		Button photoButton = (Button) rootView.findViewById(R.id.button_capture);
		
		//Listener for click
		photoButton.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
		        //TEST
				System.out.println("Taking photo");
				mCamera.takePicture(null, rawCallback, jpegCallback);
		    }
		});
		
		
	}
	
	String mCurrentPhotoPath;

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    
	    return image;
	}

}
