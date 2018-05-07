package pragma.embd.android_based_bus_tracking;

import org.json.JSONArray;
import org.json.JSONObject;

import pragma.embd.constants.Constants;
import pragma.embd.webservicecall.CallingWebservice;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class LoginScreenActivity extends Activity {

	EditText et_username, et_pwd;
	RadioGroup rg_user_type;
	RadioButton rb_customer;
	RadioButton rb_driver;
	Button btn_login, btn_clear;

	RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		rg_user_type = (RadioGroup) findViewById(R.id.rg_user_type);
		rb_customer = (RadioButton) findViewById(R.id.rb_customer);
		rb_driver = (RadioButton) findViewById(R.id.rb_driver);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#1E90FF")));
		
		

		et_username.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_username.setHint("");

			}
		});

		et_username.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					et_username.setHint("Enter UserName");
				}

			}
		});

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

		
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int selectedId = rg_user_type.getCheckedRadioButtonId();
				RadioButton	rb_user_type = (RadioButton) findViewById(selectedId);

				
				if (et_username.getText().toString().trim().length() == 0) {
					et_username.setError("Please Enter UserName");
				}

				else if (et_pwd.getText().toString().trim().length() == 0) {
					et_pwd.setError("Please Enter Password");
				}
				
				else if (et_username.getText().toString().trim()
						.equalsIgnoreCase("a")
						&& et_pwd.getText().toString().trim()
								.equalsIgnoreCase("a")) {

					Toast.makeText(getApplicationContext(), "Login Success",
							Toast.LENGTH_LONG).show();

					Intent adminMainScreen = new Intent(getApplicationContext(),
							AdminMainScreenActivity.class);
					startActivity(adminMainScreen);

				}
				else{
					
					validateUser(rb_user_type.getText().toString().trim());
				}

			

			}
		});

		
		btn_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				et_username.setHint("Enter UserName");
				et_pwd.setHint("Enter Password");
			}
		});

	}
	
	void validateUser(final String type){
		


		CallingWebservice callWebservice = new CallingWebservice(
				getApplicationContext());
		params = new RequestParams();

		// Put Http parameter
		params.put("database", "trackmybusdb");
		params.put("tablename", "driveruserdetails");
		params.put("columns", "Id, username, phoneno, emailid, address, boardingpoint");
		params.put("conditions", "username='"
				+ et_username.getText().toString().trim()
				+ "' " + "AND password='"
				+ et_pwd.getText().toString().trim() + "' AND  type = '" + type + "'");

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
										"Login unsuccessful, connection to database failed",
										Toast.LENGTH_LONG)
										.show();

							} else if (obj.getString("0")
									.equals("noDataExists")) {
								Toast.makeText(
										getApplicationContext(),
										"Login unsuccessful, Invalid credentials",
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
								
							
								if(type.equalsIgnoreCase("USER")){
									
									Intent userMainScreen = new Intent(
											getApplicationContext(),
											CustomerMainScreenActivity.class);
									userMainScreen
											.putExtra("ID", result
													.getString(0));
									userMainScreen
									.putExtra("NAME", result
											.getString(1));
									startActivity(userMainScreen);
								}
								else if(type.equalsIgnoreCase("DRIVER")){
									
									Intent driverMainScreen = new Intent(
											getApplicationContext(),
											DriverMainScreenActivity.class);
									driverMainScreen
											.putExtra("ID", result
													.getString(0));
									driverMainScreen
									.putExtra("NAME", result
											.getString(1));
									startActivity(driverMainScreen);
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
	}

}
