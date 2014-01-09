package com.jtilley.java2;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		final Handler carsHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Object returnedObject = msg.obj;
				
				if(msg.arg1 == RESULT_OK && returnedObject != null){
					
				}
				
				super.handleMessage(msg);
			}
		};
		
		Messenger serviceMessenger = new Messenger(carsHandler);
		
		Intent carServiceIntent = new Intent(this, CarService.class);
		carServiceIntent.putExtra("messenger", serviceMessenger);
		startService(carServiceIntent);	
		
;	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
