package com.jtilley.java2;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	ListView list;
	JSONstorage storage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = (ListView) this.findViewById(R.id.list);
		View listHeader = this.getLayoutInflater().inflate(R.layout.list_header, null);
		list.addHeaderView(listHeader);
		
		
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
		
		displayCars();
		
		
		
;	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void displayCars()
	{
			storage = JSONstorage.getInstance();
			String JSONString = storage.readStringFile(this, "cars_json");
			String jsonAmount = Integer.toString(JSONString.length());
			Log.i("JSON", jsonAmount);
			ArrayList<HashMap<String, String>> makeList = new ArrayList<HashMap<String, String>>();
			JSONObject jObject = null;
			
			try{
				jObject = new JSONObject(JSONString);
				JSONArray makes = jObject.getJSONArray("makes");
				int makesSize = makes.length();
				
				for(int i = 0; i < makesSize; i++)
				{
					JSONObject makeObject = makes.getJSONObject(i);
					
					String makeName = makeObject.getString("name");
					JSONArray models = makeObject.getJSONArray("models");
					int modelsNumber = models.length();
					String modelsAmount = Integer.toString(modelsNumber);
					Log.i("MAKE NAME", makeName);
					Log.i("Models", modelsAmount);
					
					HashMap<String, String> makeMap = new HashMap<String, String>();
					makeMap.put("name", makeName);
					makeMap.put("count", modelsAmount);
					
					makeList.add(makeMap);
					
				}
				
				SimpleAdapter adapter = new SimpleAdapter(this, makeList, R.layout.list_row,
						new String[] {"name", "count"}, new int[] {R.id.makes, R.id.models});
				
				list.setAdapter(adapter);
				
			}catch (JSONException e){
				e.printStackTrace();
			}
	}
}
