package com.example.wallet2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class DisplayMapActivity extends FragmentActivity {

	private GoogleMap mMap;
	private LocationManager locMan;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_map);
		// Show the Up button in the action bar.
		setupActionBar();

		if(mMap == null)
		{
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

			if(mMap!=null)
			{
				mMap.setMyLocationEnabled(true);
				updatePlaces();
			}
		}
	}

	private void updatePlaces()
	{
		//get location manager
		locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//get last location
		Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double lat = lastLoc.getLatitude();
		double lng = lastLoc.getLongitude();
		//create LatLng
		LatLng lastLatLng = new LatLng(lat, lng);

		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(lastLatLng)              // Sets the center of the map to current user location
		.zoom(15)                   // Sets the zoom
		.bearing(90)                // Sets the orientation of the camera to east
		.tilt(30)                   // Sets the tilt of the camera to 30 degrees
		.build();                   // Creates a CameraPosition from the builder

		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
				"json?location="+lat+","+lng+
				"&radius=1000&sensor=true" +
				"&types=food|bar|store"+
				"&key=AIzaSyCQ85NGt-6127aktMVjxg3hymGswkh0h_Y";
	}


	private class GetPlaces extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			StringBuilder placesBuilder = new StringBuilder();

			for (String placeSearchURL : params)
			{
				HttpClient placesClient = new DefaultHttpClient();
				try 
				{
					//try to fetch the data
					HttpGet placesGet = new HttpGet(placeSearchURL);
							HttpResponse placesResponse = placesClient.execute(placesGet);
							StatusLine placeSearchStatus = placesResponse.getStatusLine();
							if (placeSearchStatus.getStatusCode() == 200) 
							{
								//we have an OK response
								HttpEntity placesEntity = placesResponse.getEntity();
								InputStream placesContent = placesEntity.getContent();
								InputStreamReader placesInput = new InputStreamReader(placesContent);
								BufferedReader placesReader = new BufferedReader(placesInput);

								String lineIn;
								while ((lineIn = placesReader.readLine()) != null) 
								{
									placesBuilder.append(lineIn);
								}
							}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			return placesBuilder.toString();
		}

	}
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}