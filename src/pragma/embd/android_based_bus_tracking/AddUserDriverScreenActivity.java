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
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AddUserDriverScreenActivity extends Activity {

	TextView tv_heading, tv_select_bus;
	EditText et_username, et_pwd, et_age, et_boarding_point, et_address, et_phoneno, et_email_id;
	Spinner sp_bus_no;
	RadioGroup rg_gender;
	RadioButton rb_male;
	RadioButton rb_female;
	Button btn_submit, btn_clear;

	String str_type;
	String str_bus_no;
	RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_user_driver_screen);
		
		tv_select_bus = (TextView) findViewById(R.id.tv_select_bus);
		tv_heading = (TextView) findViewById(R.id.tv_heading);
		et_username = (EditText) findViewById(R.id.et_username);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_age = (EditText) findViewById(R.id.et_age);
		et_boarding_point = (EditText) findViewById(R.id.et_boarding_point);
		et_address = (EditText) findViewById(R.id.et_address);
		et_phoneno = (EditText) findViewById(R.id.et_phoneno);
		et_email_id = (EditText) findViewById(R.id.et_email_id);
		
		rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
		rb_male = (RadioButton) findViewById(R.id.rb_male);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		
		
		str_type = getIntent().getStringExtra("TYPE");
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#1E90FF")));

		if(str_type.equalsIgnoreCase("USER")){
			
			tv_heading.setText("Add Customer Screen");
			et_boarding_point.setVisibility(View.VISIBLE);
			
			getBusNoFromDB();
		}
		
		else if(str_type.equalsIgnoreCase("DRIVER")){
						
			tv_heading.setText("Add Driver Screen");
			et_boarding_point.setVisibility(View.GONE);
			tv_select_bus.setVisibility(View.GONE);
									
		}
		
		


		et_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_pwd.setHint("");

			}
		});

		et_pwd.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_pwd.setHint("Enter Password");
				}

			}
		});
		
		
		et_age.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_age.setHint("");

			}
		});

		et_age.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_age.setHint("Enter Age");
				}

			}
		});
		
		
		et_boarding_point.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_boarding_point.setHint("");

			}
		});

		et_boarding_point.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_boarding_point.setHint("Enter Boarding Point");
				}

			}
		});
		
		
		et_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_address.setHint("");

			}
		});

		et_address.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_address.setHint("Enter Address");
				}

			}
		});
		
		
		et_phoneno.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_phoneno.setHint("");

			}
		});

		et_phoneno.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_phoneno.setHint("Enter Phone No");
				}

			}
		});
		
		et_email_id.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_email_id.setHint("");

			}
		});

		et_email_id.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_email_id.setHint("Enter Email ID");
				}

			}
		});
		
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (et_username.getText().toString().trim().length() == 0) {
					et_username.setError("Please Enter UserName");
				}

				else if (et_pwd.getText().toString().trim().length() == 0) {
					et_pwd.setError("Please Enter Password");
				}
				
				else if (et_age.getText().toString().trim().length() == 0) {
					et_age.setError("Please Enter Age");
				}
					
				else if (et_boarding_point.getText().toString().trim().length() == 0 && str_type.equalsIgnoreCase("USER")){
					et_boarding_point.setError("Please Enter Boarding Point");
						
				}
				
					
				
				else if (et_phoneno.getText().toString().trim().length() == 0) {
					et_phoneno.setError("Please Enter Phone No");
				}
				

				else if (et_email_id.getText().toString().trim().length() == 0) {
					et_email_id.setError("Please Enter Email ID");
				}
				
				else if (et_address.getText().toString().trim().length() == 0) {
					et_address.setError("Please Enter Address");
				}
				
			
				else{
					
					addUserDriverDetailstoDatabase();
				}
			}
		});
		
		
		btn_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				et_username.setText("");
				et_pwd.setText("");
				et_age.setText("");
				et_boarding_point.setText("");
				et_email_id.setText("");
				et_phoneno.setText("");
				et_address.setText("");
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
								
								sp_bus_no = (Spinner) findViewById(R.id.sp_bus_no);
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
	
	void addUserDriverDetailstoDatabase(){
		
		int selectedId = rg_gender.getCheckedRadioButtonId();

		// find the radiobutton by returned id
		RadioButton	rb_gender = (RadioButton) findViewById(selectedId);
		
		CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());

		params = new RequestParams();

		params.put("database", "trackmybusdb");
		params.put("tablename", "driveruserdetails");
		params.put("columnnames",
				"username, password, age, gender, address, phoneno, emailid, boardingpoint, type, busno");
		params.put("columnvalues", "'"
				+ et_username.getText().toString().trim() + "','"
				+ et_pwd.getText().toString().trim() + "','" 
				+ et_age.getText().toString().trim() + "','"
				+ rb_gender.getText().toString().trim() + "','"
				+ et_address.getText().toString().trim() + "','"
				+ et_phoneno.getText().toString().trim() + "','"
				+ et_email_id.getText().toString().trim() + "','"
				+ et_boarding_point.getText().toString().trim() + "','"
				+ str_type + "','" +
				str_bus_no + "'");

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
										"Details Added Successfully",
										Toast.LENGTH_LONG).show();
							if(str_type.equalsIgnoreCase("USER")){
								
								sendSMS("Welcome to Track My Bus." +  System.getProperty("line.separator")
										+ "Username: " + et_username.getText().toString().trim() + " and Pwd: " +
										et_pwd.getText().toString().trim() + " and Bus No: " + str_bus_no, "+91" + et_phoneno.getText().toString().trim());
							}
							
							else if(str_type.equalsIgnoreCase("DRIVER")){
								
								sendSMS("Welcome to Track My Bus." +  System.getProperty("line.separator")
										+ "Username: " + et_username.getText().toString().trim() + " and Pwd: " +
										et_pwd.getText().toString().trim(), "+91" + et_phoneno.getText().toString().trim());
							}
							
							}
							// Else display error message
							else if (obj.getString("response")
									.equalsIgnoreCase(
											"insertionFailed")) {

								Toast.makeText(
										getApplicationContext(),
										"Adding details Failed: "
												+ obj.getString("error_msg"),
										Toast.LENGTH_LONG).show();
							} else if (obj.getString("response")
									.equalsIgnoreCase(
											"connectionToDBFailed")) {

								Toast.makeText(
										getApplicationContext(),
										"Adding details Failed: "
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
	
	
	private void  sendSMS(String msg, String number){
		
		 try {
			 
			
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(number, null, msg , null, null);
				
				Toast.makeText(getApplicationContext(),
						"Credentials Sent to User Successfully",
						Toast.LENGTH_LONG).show();
				
				Intent adminMainScreen = new Intent(getApplicationContext(), AdminMainScreenActivity.class);
				startActivity(adminMainScreen);
				
			  } catch (Exception e) {
				Toast.makeText(getApplicationContext(),
					"SMS faild, please try again later!",
					Toast.LENGTH_LONG).show();
			//	e.printStackTrace();
			  }
	   	
	   }
	
}
