package pragma.embd.android_based_bus_tracking;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AdminMainScreenActivity extends Activity{
	
	ImageView img_add_user;
	ImageView img_add_driver;
	ImageView img_add_routes_for_buses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminmain_screen);
		
		img_add_user = (ImageView)findViewById(R.id.img_add_user);
		img_add_driver = (ImageView)findViewById(R.id.img_add_driver);
		img_add_routes_for_buses = (ImageView)findViewById(R.id.img_add_routes_for_buses);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#1E90FF")));
		
		img_add_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent addUserDriverScreen = new Intent(getApplicationContext(), AddUserDriverScreenActivity.class);
				addUserDriverScreen.putExtra("TYPE", "USER");
				startActivity(addUserDriverScreen);
			}
		});
		
		img_add_driver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent addUserDriverScreen = new Intent(getApplicationContext(), AddUserDriverScreenActivity.class);
				addUserDriverScreen.putExtra("TYPE", "DRIVER");
				startActivity(addUserDriverScreen);
			}
		});
		
		img_add_routes_for_buses.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent addRoutesScreen = new Intent(getApplicationContext(), AddBusRouteScreenActivity.class);
				startActivity(addRoutesScreen);
				
			}
		});
	}

}
