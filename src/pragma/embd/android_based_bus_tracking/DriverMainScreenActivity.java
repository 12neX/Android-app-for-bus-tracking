package pragma.embd.android_based_bus_tracking;

import org.json.JSONArray;
import org.json.JSONObject;

import pragma.embd.constants.Constants;
import pragma.embd.webservicecall.CallingWebservice;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DriverMainScreenActivity extends Activity{
	
	TextView tv_heading;
	Spinner sp_bus_no;
	TextView tv_my_bus_name;
	TextView tv_my_bus_route;
	TextView tv_start_stop_travel;
	ImageView img_select_bus;
	ImageView img_start_stop_travel;
	
	String str_id;
	String str_name;
	
	String str_bus_no;
	
	RequestParams params;
	
	public static AlarmManager alarmManager;
	public static PendingIntent piHeartBeatService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drivermain_screen);
		
		tv_heading = (TextView)findViewById(R.id.tv_heading);
		sp_bus_no = (Spinner) findViewById(R.id.sp_bus_no);
		tv_my_bus_name = (TextView)findViewById(R.id.tv_my_bus_name);
		tv_my_bus_route = (TextView)findViewById(R.id.tv_my_bus_route);
		tv_start_stop_travel = (TextView)findViewById(R.id.tv_start_stop_travel);
		img_select_bus = (ImageView)findViewById(R.id.img_select_bus);
		img_start_stop_travel = (ImageView)findViewById(R.id.img_start_stop_travel);
		
		str_id = getIntent().getStringExtra("ID");
		str_name = getIntent().getStringExtra("NAME");
		
		tv_heading.setText("Welcome " + str_name.toUpperCase());
		
		img_select_bus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sp_bus_no.setVisibility(View.VISIBLE);
				getBusNoFromDB();
			}
		});
		
		
		if(isMyServiceRunning(MyService.class)){
			
		
			img_start_stop_travel.setImageResource(R.drawable.stop);
			tv_start_stop_travel.setText("STOP TRAVEL");
			
			
		}
		else{
			
			img_start_stop_travel.setImageResource(R.drawable.start);
			tv_start_stop_travel.setText("START TRAVEL");
			
		}
		
		img_start_stop_travel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isMyServiceRunning(MyService.class)){
					
			//		Toast.makeText(getApplicationContext(), "Service already Running", Toast.LENGTH_SHORT).show();
					img_start_stop_travel.setImageResource(R.drawable.start);
					tv_start_stop_travel.setText("START TRAVEL");
					
					alarmManager.cancel(piHeartBeatService);
			
					Intent iHeartBeatService = new Intent(getApplicationContext(),
							MyService.class);
					piHeartBeatService = PendingIntent.getService(getApplicationContext(),
							0, iHeartBeatService, PendingIntent.FLAG_CANCEL_CURRENT);
					alarmManager = (AlarmManager) getApplicationContext().getSystemService(
							Context.ALARM_SERVICE);
					   alarmManager.cancel(piHeartBeatService);
					
				
				}
				else{
			//		Toast.makeText(getApplicationContext(), "Service not running, calling now", Toast.LENGTH_SHORT).show();
					img_start_stop_travel.setImageResource(R.drawable.stop);
					tv_start_stop_travel.setText("STOP TRAVEL");
					callServiceToRunInBackground();
				}
				
		
			}
		});

	}
	
	
void getBusNoFromDB(){
		

		final CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());
		 params = new RequestParams();

		// get Certification Credit Points
		// Put Http parameter
		params.put("database", "trackmybusdb");
		params.put("tablename", "busdetails");
		params.put("columns", "busno");
		params.put("conditions", "no");

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
										"Not able to fetch Bus No , connection to database failed",
										Toast.LENGTH_LONG)
										.show();

							} else if (obj.getString("0")
									.equals("noDataExists")) {
								Toast.makeText(
										getApplicationContext(),
										"Not able to fetch Bus No, no data exists",
										Toast.LENGTH_LONG)
										.show();

							}
							// Else display error message
							else {
								
							

								final String[]  items  = new String[obj.length()];
								
								for (int js = 0; js <= obj.length() - 1; js++) {
									JSONArray result = (JSONArray) obj
											.get(String.valueOf(js));
									
									
								
									for (int m = 0; m < result.length(); m += 1) {
										
										
										items[js] = result.getString(m);
										
								
										
									}
									
									
								

									
								} 
								
								
								ArrayAdapter<String> adapter = 
							            new ArrayAdapter<String> (getApplicationContext(), 
							                    android.R.layout.simple_spinner_item, items);
								// set dropdown view and bind the adapter
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

								
								sp_bus_no.setAdapter(adapter);
								
					//			str_certification_date = items[0];
								
								sp_bus_no.setOnItemSelectedListener(new OnItemSelectedListener(){

						            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
						                int item = sp_bus_no.getSelectedItemPosition();
						  //           Toast.makeText(getBaseContext(), "You have selected the playlist: " +items[item], Toast.LENGTH_SHORT).show();
						                str_bus_no = items[item];
						                
						           //     Toast.makeText(getBaseContext(), "You have selected : " + str_bus_no,  Toast.LENGTH_SHORT).show();
						          
						                Constants.str_bus_no = str_bus_no;
						                getBusDetailsFromDB();
						            }

						            public void onNothingSelected(AdapterView<?> arg0){
						            }
						        });

							
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


void getBusDetailsFromDB(){
	

	


	CallingWebservice callWebservice = new CallingWebservice(
			getApplicationContext());
	params = new RequestParams();

	// Put Http parameter
	params.put("database", "trackmybusdb");
	params.put("tablename", "busdetails");
	params.put("columns", "Id, busname, busroute");
	params.put("conditions", "busno='"
			+ str_bus_no + "'");

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
									"Not able to fetch bus details, connection to database failed",
									Toast.LENGTH_LONG)
									.show();

						} else if (obj.getString("0")
								.equals("noDataExists")) {
							Toast.makeText(
									getApplicationContext(),
									"Not able to fetch bus details, Invalid credentials",
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
							
							tv_my_bus_name.setVisibility(View.VISIBLE);
							tv_my_bus_route.setVisibility(View.VISIBLE);
							
							tv_my_bus_name.setText("Bus Name: " + result.getString(1));
							tv_my_bus_route.setText("Bus Route: " + result.getString(2));
							
						
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

private boolean isMyServiceRunning(Class<MyService> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
            return true;
        }
    }
    return false;
}

void callServiceToRunInBackground() {

	Intent iHeartBeatService = new Intent(getApplicationContext(),
			MyService.class);
	piHeartBeatService = PendingIntent.getService(getApplicationContext(),
			0, iHeartBeatService, PendingIntent.FLAG_UPDATE_CURRENT);
	alarmManager = (AlarmManager) getApplicationContext().getSystemService(
			Context.ALARM_SERVICE);
	alarmManager.cancel(piHeartBeatService);
	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
			System.currentTimeMillis(), 60000, piHeartBeatService);
}


@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	finish();
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	
	super.onBackPressed();
	finish();
	/*if(isMyServiceRunning(MyService.class)){
		
		Toast.makeText(getApplicationContext(), "Service already Running, cant go out of the screen", Toast.LENGTH_SHORT).show();
		
	
	}
	else{
		super.onBackPressed();
	finish();
	}*/
}

}
