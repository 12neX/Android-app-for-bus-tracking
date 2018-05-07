package pragma.embd.android_based_bus_tracking;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import pragma.embd.android_based_bus_tracking.MyService.MyLocationListener;
import pragma.embd.webservicecall.CallingWebservice;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerMainScreenActivity extends Activity{
	
	TextView tv_heading;
	TextView tv_boarding_point, tv_bus_no;
	TextView tv_bus_arrival_time;
	ImageView img_select_bus, img_bus_arrival_time;
	ImageView img_map;
	
	String str_id;
	String str_name;
	
	RequestParams params;
	
	LocationManager mlocManager;
	LocationListener mlocListener;
	
	
	double c_lat;
	double c_long;
	
	 String str_c_lat;
	 String str_c_long;
	 
	 String str_bus_lat;
	 String str_bus_long;
	 
	 static String angleFormated;
		static long distance;
	
		long time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customermain_screen);
		
		tv_heading = (TextView)findViewById(R.id.tv_heading);
		tv_bus_no = (TextView)findViewById(R.id.tv_bus_no);
		tv_boarding_point = (TextView)findViewById(R.id.tv_boarding_point);
		tv_bus_arrival_time = (TextView)findViewById(R.id.tv_bus_arrival_time);
		img_select_bus = (ImageView)findViewById(R.id.img_select_bus);
		img_bus_arrival_time = (ImageView)findViewById(R.id.img_bus_arrival_time);
		img_map = (ImageView)findViewById(R.id.img_map);
		
		str_id = getIntent().getStringExtra("ID");
		str_name = getIntent().getStringExtra("NAME");
		
		tv_heading.setText("Welcome " + str_name.toUpperCase());
		
		tv_bus_no.setVisibility(View.GONE);
		tv_boarding_point.setVisibility(View.GONE);
		tv_bus_arrival_time.setVisibility(View.GONE);
		
		get_lat_long_details();
		
		img_select_bus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDataFromDB();
			}
		});
		
		img_bus_arrival_time.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				getLatestBusLocationDetails("B");
			}
		});

		img_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				getLatestBusLocationDetails("M");
			}
		});
		
		
	}
	
	void getLatestBusLocationDetails(final String id){
		

		CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());
		params = new RequestParams();

		// Put Http parameter
		params.put("database", "trackmybusdb");
		params.put("tablename", "buslocationdetails");
		params.put("columns", "latitude, longitude");
		params.put("conditions", "ORDER BY Id DESC LIMIT 1");

		callWebservice.callWebservice(params, "select",
				new AsyncHttpResponseHandler() {

					public void onSuccess(String response) {

						try {

							
							// JSON Object
							JSONObject obj = new JSONObject(
									response);
							// When the JSON response has status
							// boolean value assigned with true
							if (obj.getString("0").equals(
									"connectionToDBFailed")) {
								Toast.makeText(
										getApplicationContext(),
										"connection to database failed",
										Toast.LENGTH_LONG)
										.show();

							} else if (obj.getString("0")
									.equals("noDataExists")) {
								Toast.makeText(
										getApplicationContext(),
										"No data exists",
										Toast.LENGTH_LONG)
										.show();

							}
							// Else display error message
							else {
							

								JSONArray result = null;
								result = (JSONArray) obj
										.get(String
												.valueOf("0"));
								
							
								str_bus_lat = result.getString(0);
								str_bus_long = result.getString(1);
								
								if(id.equalsIgnoreCase("B")){
								calculateDistance();
								}
								else if(id.equalsIgnoreCase("M")){
									
									displayMap(Double.parseDouble(str_bus_lat), Double.parseDouble(str_bus_long));
								}
									
							
							}
						} catch (Exception e) {

							Toast.makeText(
									getApplicationContext(),
									"Error Occured [Server's JSON response might be invalid]!"
											+ e.getMessage(),
									Toast.LENGTH_LONG).show();
							e.printStackTrace();

						}
					}

					// When the response returned by REST has
					// Http response code other than '200'
					public void onFailure(int statusCode,
							Throwable error, String content) {

						Toast.makeText(getApplicationContext(),
								"failed", Toast.LENGTH_LONG)
								.show();

						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(
									getApplicationContext(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code is '500'
						else if (statusCode == 500) {
							Toast.makeText(
									getApplicationContext(),
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code other than
						// 404, 500
						else {
							Toast.makeText(
									getApplicationContext(),
									"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	
	
	}
	
	
public void displayMap(double clat, double clong){
        
        try{
        	
    
        String geoCode = "geo:0,0?q=" + clat + ","
               + clong ;
       Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
               Uri.parse(geoCode));
       startActivity(sendLocationToMap);
        }
        catch(Exception e){
                
          //     Toast.makeText(getApplicationContext(),"hi" + e.getMessage(), Toast.LENGTH_SHORT).show();
                
        }


}
	
void calculateDistance(){
	
		
	    double dist = get_distance_haversine(c_lat, c_long, Double.parseDouble(str_bus_lat), Double.parseDouble(str_bus_long));
	    
	    	
	    	distance=(long) dist;

	    	
	    	DecimalFormat dist_value = new DecimalFormat("#.00"); 
	    	angleFormated = dist_value.format(dist);
	    	
	    	time = (distance/50) * 100;
	    	
	    	tv_bus_arrival_time.setVisibility(View.VISIBLE);
	    	tv_bus_arrival_time.setText("Approximate Bus Arrival Time: " + time + " Minutes");
	    
			
		
	}
	
public static double get_distance_haversine(
        double lat1, double lng1, double lat2, double lng2) {
    int r = 6371; // average radius of the earth in km
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lng2 - lng1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
       Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
      * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double d = r * c;
    return d;
}


	private void get_lat_long_details() {

		try {
			
//			Toast.makeText(this, "fetching location", Toast.LENGTH_SHORT).show();
			mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			mlocListener = new MyLocationListener();

			mlocManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
		} catch (Exception e) {

			Toast.makeText(getApplicationContext(),

			"error is: " + e.getMessage(),

			Toast.LENGTH_LONG).show();
		}
	}
	 
	 
	/* Class My Location Listener */

	public class MyLocationListener implements LocationListener

	{

		public void onLocationChanged(Location loc)

		{

			loc.getLatitude();

			loc.getLongitude();

			c_lat = loc.getLatitude();

			c_long = loc.getLongitude();

			str_c_lat = Double.toString(c_lat);

			str_c_long = Double.toString(c_long);

			Toast.makeText(getApplicationContext(),

			"latitude->" + c_lat + "\n" + "longitude->" + c_long + "\n",

			Toast.LENGTH_SHORT).show();

	
	
		
		}

		public void onProviderDisabled(String provider)

		{

			Toast.makeText(getApplicationContext(),

			"Gps Disabled",

			Toast.LENGTH_SHORT).show();

		}

		public void onProviderEnabled(String provider)

		{

			Toast.makeText(getApplicationContext(),

			"Gps Enabled",

			Toast.LENGTH_SHORT).show();

		}

		public void onStatusChanged(String provider, int status, Bundle extras)

		{

		}

	}/* End of Class MyLocationListener */
	
	
	void getDataFromDB(){
		
		CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());
		params = new RequestParams();

		// Put Http parameter
		params.put("database", "trackmybusdb");
		params.put("tablename", "driveruserdetails");
		params.put("columns", "Id, phoneno, emailid, boardingpoint, busno");
		params.put("conditions", "Id="
				+ str_id);

		callWebservice.callWebservice(params, "select",
				new AsyncHttpResponseHandler() {

					public void onSuccess(String response) {

						try {

							
							// JSON Object
							JSONObject obj = new JSONObject(
									response);
							// When the JSON response has status
							// boolean value assigned with true
							if (obj.getString("0").equals(
									"connectionToDBFailed")) {
								Toast.makeText(
										getApplicationContext(),
										"Not able to fetch details, connection to database failed",
										Toast.LENGTH_LONG)
										.show();

							} else if (obj.getString("0")
									.equals("noDataExists")) {
								Toast.makeText(
										getApplicationContext(),
										"Not able to fetch details, No data exists",
										Toast.LENGTH_LONG)
										.show();

							}
							// Else display error message
							else {
							/*	Toast.makeText(
										getApplicationContext(),
										"You are successfully Logged in",
										Toast.LENGTH_LONG)
										.show();*/

								JSONArray result = null;
								result = (JSONArray) obj
										.get(String
												.valueOf("0"));
								
							
								tv_bus_no.setVisibility(View.VISIBLE);
								tv_boarding_point.setVisibility(View.VISIBLE);
							
								
								tv_bus_no.setText("Bus No: " + result.getString(4));
								tv_boarding_point.setText("Boarding Point: " + result.getString(3));
								tv_bus_arrival_time.setText("Approximate Bus Arrival Time: ");
							
							}
						} catch (Exception e) {

							Toast.makeText(
									getApplicationContext(),
									"Error Occured [Server's JSON response might be invalid]!"
											+ e.getMessage(),
									Toast.LENGTH_LONG).show();
							e.printStackTrace();

						}
					}

					// When the response returned by REST has
					// Http response code other than '200'
					public void onFailure(int statusCode,
							Throwable error, String content) {

						Toast.makeText(getApplicationContext(),
								"failed", Toast.LENGTH_LONG)
								.show();

						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(
									getApplicationContext(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code is '500'
						else if (statusCode == 500) {
							Toast.makeText(
									getApplicationContext(),
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code other than
						// 404, 500
						else {
							Toast.makeText(
									getApplicationContext(),
									"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	
	
	}

}
