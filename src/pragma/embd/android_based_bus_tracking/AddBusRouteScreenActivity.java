package pragma.embd.android_based_bus_tracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pragma.embd.webservicecall.CallingWebservice;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AddBusRouteScreenActivity extends Activity {

	EditText et_bus_no, et_bus_name;
	Spinner sp_bus_routes;
	Button btn_submit;

	String str_bus_routes;
	
	RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bus_route_screen);
		
		et_bus_no = (EditText) findViewById(R.id.et_bus_no);
		et_bus_name = (EditText) findViewById(R.id.et_bus_name);
		
		btn_submit = (Button) findViewById(R.id.btn_submit);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#1E90FF")));
		
		getBusRoutesFromDB();
		
		et_bus_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_bus_no.setHint("");

			}
		});

		et_bus_no.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_bus_no.setHint("Enter Bus No");
				}

			}
		});
		
		
		et_bus_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_bus_name.setHint("");

			}
		});

		et_bus_name.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_bus_name.setHint("Enter Bus Name");
				}

			}
		});
		
		
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (et_bus_no.getText().toString().trim().length() == 0) {
					et_bus_no.setError("Please Enter Bus No");
				}

				else if (et_bus_name.getText().toString().trim().length() == 0) {
					et_bus_name.setError("Please Enter Bus Name");
				}
				else{
					
					addBusDetailsToDB();
				}
				
			}
		});
		
		
	}
	
	
	void getBusRoutesFromDB(){
		


		final CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());
		 params = new RequestParams();

		// get Certification Credit Points
		// Put Http parameter
		params.put("database", "trackmybusdb");
		params.put("tablename", "routedetails");
		params.put("columns", "busroute");
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
										"Not able to fetch Bus Route details, connection to database failed",
										Toast.LENGTH_LONG)
										.show();

							} else if (obj.getString("0")
									.equals("noDataExists")) {
								Toast.makeText(
										getApplicationContext(),
										"Not able to fetch Bus Route details, no data exists",
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
								
								sp_bus_routes = (Spinner) findViewById(R.id.sp_bus_routes);
								ArrayAdapter<String> adapter = 
							            new ArrayAdapter<String> (getApplicationContext(), 
							                    android.R.layout.simple_spinner_item, items);
								// set dropdown view and bind the adapter
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

								
								sp_bus_routes.setAdapter(adapter);
								
					//			str_certification_date = items[0];
								
								sp_bus_routes.setOnItemSelectedListener(new OnItemSelectedListener(){

						            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
						                int item = sp_bus_routes.getSelectedItemPosition();
						  //           Toast.makeText(getBaseContext(), "You have selected the playlist: " +items[item], Toast.LENGTH_SHORT).show();
						                str_bus_routes = items[item];
						                
						        //        Toast.makeText(getBaseContext(), "You have selected the playlist: " +spinnerItems[item],  Toast.LENGTH_SHORT).show();
						          
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
	
	void addBusDetailsToDB(){
		
		CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());

		params = new RequestParams();

		params.put("database", "trackmybusdb");
		params.put("tablename", "busdetails");
		params.put("columnnames",
				"busno, busname, busroute");
		params.put("columnvalues", "'"
				+ et_bus_no.getText().toString().trim() + "','"
				+ et_bus_name.getText().toString().trim() + "','" 
				+ str_bus_routes + "'");

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
										"Bus details Added Successfully",
										Toast.LENGTH_LONG).show();
								
								Intent adminMainScreen = new Intent(getApplicationContext(), AdminMainScreenActivity.class);
								startActivity(adminMainScreen);
							
							}
							// Else display error message
							else if (obj.getString("response")
									.equalsIgnoreCase(
											"insertionFailed")) {

								Toast.makeText(
										getApplicationContext(),
										"Adding Bus details Failed: "
												+ obj.getString("error_msg"),
										Toast.LENGTH_LONG).show();
							} else if (obj.getString("response")
									.equalsIgnoreCase(
											"connectionToDBFailed")) {

								Toast.makeText(
										getApplicationContext(),
										"Adding Bus details Failed: "
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
