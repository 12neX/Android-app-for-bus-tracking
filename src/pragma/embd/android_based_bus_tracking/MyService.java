package pragma.embd.android_based_bus_tracking;

import org.json.JSONException;
import org.json.JSONObject;

import pragma.embd.constants.Constants;
import pragma.embd.webservicecall.CallingWebservice;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyService extends Service {
	
	
	Notification notification;
	static final int MY_NOTIFICATION_ID= 1234;
	 
	private byte[] img_byte=null;
	
	LocationManager mlocManager;
	LocationListener mlocListener;
	
	static NotificationManager nm=null;
	 
	
	double c_lat;
	double c_long;
	
	 String str_c_lat;
	 String str_c_long;
	 String str_date;
	 String str_time;
	 String str_time1;
	 
	 double dob_str_time1;
	 double dob_thisLine1;
	
	 String text;
	 Context ctx;
	 
	 String addressText;
	static String str_address = "";
		
		static String Address1 = "";
		static String Address2 = "";
		static String Street = "";
		static String Route = "";
		static String Area = "";
		static String County = "";
		static String Country = "";
		static String State = "";
		static String PIN = "";
		static String City = "";
		
	
		String id = null;
		String companyName = null;
		String contactNo = null;
		String location = null;
		String latitude = null;
		String longitude = null;
		String image = null;
		
		RequestParams params;
		
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
	
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_SHORT).show();
	//	Log.d(TAG, "onDestroy");
		//player.stop();
	}
	
	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		
	
		get_lat_long_details();
	
	
		 return Service.START_NOT_STICKY;
		

		
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

	
		insertDataToDB();
		
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
	
	
	void insertDataToDB(){
		

		
		 Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();

			int int_month = today.month + 1;

		String	str_date = "" + today.monthDay + "/" + int_month + "/" + today.year;
		String	str_time = today.format("%k:%M:%S");
		
		CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());

		params = new RequestParams();

		params.put("database", "trackmybusdb");
		params.put("tablename", "buslocationdetails");
		params.put("columnnames",
				"busno, latitude, longitude, dates, times");
		params.put("columnvalues", "'"
				+ Constants.str_bus_no + "','"
				+ str_c_lat.trim() + "','"
				+ str_c_long.trim() + "','"
				+ str_date + "','"
				+ str_time + "'");

				callWebservice.callWebservice(params, "insert",
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {

						try {
							// JSON Object
							JSONObject obj = new JSONObject(
									response);

							if (obj.getString("response")
									.equalsIgnoreCase(
											"insertionSuccess")) {

								Toast.makeText(
										getApplicationContext(),
										"Bus location added successfully",
										Toast.LENGTH_LONG).show();
							
								
							}
							// Else display error message
							else if (obj.getString("response")
									.equalsIgnoreCase(
											"insertionFailed")) {

								Toast.makeText(
										getApplicationContext(),
										"Adding bus location Failed: "
												+ obj.getString("error_msg"),
										Toast.LENGTH_LONG).show();
							} else if (obj.getString("response")
									.equalsIgnoreCase(
											"connectionToDBFailed")) {

								Toast.makeText(
										getApplicationContext(),
										"Adding bus location Failed: "
												+ obj.getString("error_msg"),
										Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {

							Toast.makeText(
									getApplicationContext(),
									"Error Occured [Server's JSON response might be invalid]!",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();

						}
					}

					// When the response returned by REST has Http
					// response code other than '200'
					public void onFailure(int statusCode,
							Throwable error, String content) {

						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
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
						// When Http response code other than 404,
						// 500
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
