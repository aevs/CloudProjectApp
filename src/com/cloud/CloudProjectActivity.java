package com.cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CloudProjectActivity extends ListActivity {
    /** Called when the activity is first created. */
	 private static final String TAG = "CloudProjectActivity";
	 public static final int AUTHENTICATE_ACTIVITY_REQUEST_CODE=12111;
	 private static final String API_URL = "https://api.foursquare.com/v2";
	 private static Location location;
	 
	 public static final String FULL_NAME="FULL_NAME";
	 
	 ArrayAdapter<String> namesAdapter;
	   


	    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
	    	String name=(String)l.getItemAtPosition(position);
	    	Log.e("Clicked: ",name);
	    	
	    	Intent intent= new Intent(this,PingHomePage.class);
	    	intent.putExtra(FULL_NAME, name);
	    	startActivity(intent);
	    	
	    	super.onListItemClick(l, v, position, id);
	}




		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
//	        setContentView(R.layout.main);
	        namesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.namesadapter);
	        ListView namesListView= (ListView)findViewById(R.id.namesList);
//	        namesListView.setAdapter(namesAdapter);
	        setListAdapter(namesAdapter);
	        
	        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	        
	       Intent intent= new Intent(this,AuthenticateWithFourSquare.class);
	       startActivityForResult(intent, AUTHENTICATE_ACTIVITY_REQUEST_CODE);
	    }
		public static final String CLIENT_ID = "X4KFIR5JOLZPYRU0KUHTUNM5K4E4HPHLQISYT1NE0IUNRWWM";

	    String url =
            "https://foursquare.com/oauth2/authenticate" + 
                "?client_id=" + CLIENT_ID + 
                "&response_type=token"+
                "&redirect_uri=" + "android://only";
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			Log.e(TAG,"before Web view ideally loaded");

			String accessToken=(String)data.getExtras().get(AuthenticateWithFourSquare.ACCESS_TOKEN);
//			WebView webView=(WebView)findViewById(R.id.main);
//			webView.getSettings().setJavaScriptEnabled(true);
//			webView.setWebViewClient(new WebViewClient());
//			
//			String checkInURL="https://api.foursquare.com/v2/checkins/add";
//			String selfUrl = "https://api.foursquare.com/v2/users/self?oauth_token=" + accessToken;
//			webView.loadUrl(checkInURL);
			Log.e(TAG,"after Web view ideally loaded");
			
			getUserName(accessToken);
			
			
			try {
				getNearby(accessToken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		LocationListener locationListener= new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				CloudProjectActivity.location=location;
				
			}
		};
		
		public ArrayList<String> getNearby(String accessToken) throws Exception {
			ArrayList<String> venueList = new ArrayList<String>();

			try {
				
				String latitude=String.valueOf(location.getLatitude());
				String longitude=String.valueOf(location.getLongitude());
				
				
				String ll 	= String.valueOf(latitude) + "," + String.valueOf(longitude);
				URL url 	= new URL(API_URL + "/venues/search?ll=" +ll + "&oauth_token=" + accessToken);

				Log.d(TAG, "Opening URL " + url.toString());

				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);

				urlConnection.connect();

				String response		= streamToString(urlConnection.getInputStream());
				JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();

				JSONArray groups	= (JSONArray) jsonObj.getJSONObject("response").getJSONArray("groups");

				int length			= groups.length();

				if (length > 0) {
					for (int i = 0; i < length; i++) {
						JSONObject group 	= (JSONObject) groups.get(i);
						JSONArray items 	= (JSONArray) group.getJSONArray("items");

						int ilength 		= items.length();

						for (int j = 0; j < ilength; j++) {
							JSONObject item = (JSONObject) items.get(j);

//							FsqVenue venue 	= new FsqVenue();
//
//							venue.id 		= item.getString("id");
//							venue.name		= item.getString("name");
//
							checkVenue(item.getString("id"),accessToken);
							
							JSONObject venueLocation = (JSONObject) item.getJSONObject("location");
//
//							Location loc 	= new Location(LocationManager.GPS_PROVIDER);
//
//							loc.setLatitude(Double.valueOf(location.getString("lat")));
//							loc.setLongitude(Double.valueOf(location.getString("lng")));
//
//							venue.location	= loc;
//							venue.address	= location.getString("address");
//							venue.distance	= location.getInt("distance");
//							venue.herenow	= item.getJSONObject("hereNow").getInt("count");
//							venue.type		= group.getString("type");
//
//							venueList.add(venue);
							
							Log.e(TAG,"venue name: "+item.getString("name"));
							Log.e(TAG,"address: "+venueLocation.getString("address"));
							Log.e(TAG,"user count: "+item.getJSONObject("hereNow").getInt("count")+"");

						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
			}

			return venueList;
		}
		
		
		public void checkVenue(String id,String accessToken) throws MalformedURLException{
			
			try {
				URL url 	= new URL("https://api.foursquare.com/v2/venues/"+id+ "/herenow?oauth_token=" + accessToken+"&v=20111215");

				Log.e(TAG, "Opening URL " + url.toString());

				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);

				urlConnection.connect();

				String response		= streamToString(urlConnection.getInputStream());
				JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();

				 Log.e("Test Count",jsonObj.getJSONObject("response").getJSONObject("hereNow").getInt("count")+"");

				JSONArray items	= (JSONArray) jsonObj.getJSONObject("response").getJSONObject("hereNow").getJSONArray("items");
				
//				int length			= groups.length();
//				
//				if (length > 0) {
//					for (int i = 0; i < length; i++) {
//						JSONObject group 	= (JSONObject) groups.get(i);
//						JSONArray items 	= (JSONArray) group.getJSONArray("items");
						
						int ilength 		= items.length();

						for (int j = 0; j < ilength; j++) {
							JSONObject item = (JSONObject) items.get(0);
							Log.e("VenuehereNow Id :",item.getString("id"));
							String fullName=item.getJSONObject("user").getString("firstName")+" "+
							 item.getJSONObject("user").getString("lastName");
							Log.e("VenueHereNow Name : ",fullName);
							
							namesAdapter.add(fullName);
							Toast.makeText(getApplicationContext(), fullName, Toast.LENGTH_SHORT).show();

						}
//					}
//				}
				
				
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			
		}
		
		
		private void getUserName(String accessToken){
			try {
				URL url = new URL(API_URL + "/users/self?oauth_token=" + accessToken);

				Log.d(TAG, "Opening URL " + url.toString());

				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);

				urlConnection.connect();

				String response		= streamToString(urlConnection.getInputStream());
				JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();

				JSONObject resp		= (JSONObject) jsonObj.get("response");
				JSONObject user		= (JSONObject) resp.get("user");

				String firstName 	= user.getString("firstName");
				String lastName		= user.getString("lastName");

				Log.e(TAG, "Got user name: " + firstName + " " + lastName);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		}
		private String streamToString(InputStream is) throws IOException {
			String str  = "";

			if (is != null) {
				StringBuilder sb = new StringBuilder();
				String line;

				try {
					BufferedReader reader 	= new BufferedReader(new InputStreamReader(is));

					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}

					reader.close();
				} finally {
					is.close();
				}

				str = sb.toString();
			}

			return str;
		}

	    
		
	    
}