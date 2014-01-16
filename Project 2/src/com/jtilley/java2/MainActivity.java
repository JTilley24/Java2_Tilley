package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 1


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context mContext;
	ListView list;
	JSONstorage storage;
	public ArrayList<HashMap<String, Object>> makeList = new ArrayList<HashMap<String, Object>>();
	public String urlString = "https://api.edmunds.com/api/vehicle/v2/makes?state=new&year=2014&view=full&fmt=json&api_key=saw2xy7wdxjqfueuxkv5hm8w";
	public SimpleAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		final TextView searchField = (TextView) this.findViewById(R.id.search);
		final Button filterButton = (Button) this.findViewById(R.id.filter);
		final Button queryButton = (Button) this.findViewById(R.id.query);
		
		
		//Create ListView Headers
		list = (ListView) this.findViewById(R.id.list);
		View listHeader = this.getLayoutInflater().inflate(R.layout.list_header, null);
		list.addHeaderView(listHeader);
		list.setTextFilterEnabled(true);
		
		
		final Handler carsHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Object returnedObject = msg.obj;
				
				//Access JSON Data from Internal Storage
				storage = JSONstorage.getInstance();
				String JSONString = storage.readStringFile(mContext, "cars_json");
				
				if(msg.arg1 == RESULT_OK && returnedObject != null){
					//Display Data in ListView
					displayCars(JSONString);	
				}
				
				super.handleMessage(msg);
			}
		};
		
		//Check for Network Connection
		if(checkConnection(mContext)){
			//Start Service
			Messenger serviceMessenger = new Messenger(carsHandler);
			
			Intent carServiceIntent = new Intent(this, CarService.class);
			carServiceIntent.putExtra("messenger", serviceMessenger);
			carServiceIntent.putExtra(CarService.REQUEST_KEY, urlString);
			startService(carServiceIntent);
			
			Toast.makeText(mContext, "Gathering Data", Toast.LENGTH_LONG).show();
		}else{
			storage = JSONstorage.getInstance();
			String JSONString = storage.readStringFile(mContext, "cars_json");
			if(JSONString.length() > 0){
				//Not Connected to Network and JSON Saved to Device
				Toast.makeText(mContext, "Not Connected to a Network. Displaying previous data!", Toast.LENGTH_LONG).show();
				displayCars(JSONString);
			}else{
				//No Connection or Data
				Toast.makeText(mContext, "No Data to display! Please Connect to Network and Try Again.", Toast.LENGTH_LONG).show();
			}
		}
		
		filterButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchInput = searchField.getText().toString();
				//Log.i("FILTER", searchInput);
				list.setFilterText(searchInput);
			}
		});
		
		queryButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list.clearTextFilter();
				storage = JSONstorage.getInstance();
				String JSONString = storage.readStringFile(mContext, "cars_json");
				displayCars(JSONString);
			}
		})
		
;	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void displayCars(String JSONString)
	{
			//ArrayList<HashMap<String, Object>> makeList = new ArrayList<HashMap<String, Object>>();
			JSONObject jObject = null;
			//Parse JSON Data
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
					ArrayList<Object> modelsList = new ArrayList<Object>();
					if(models != null){
						for(int n = 0;n < modelsNumber; n++){
							modelsList.add(models.get(n).toString());
						}
					}
					HashMap<String, Object> makeMap = new HashMap<String, Object>();
					Log.i("MAKE", makeName);
					makeMap.put("name", makeName);
					makeMap.put("models", modelsList);
					makeMap.put("count", modelsAmount);
					
					makeList.add(makeMap);
				}
				

				//Add JSON Data to ListView
				SimpleAdapter listAdapter = new SimpleAdapter(this, makeList, R.layout.list_row,
						new String[] {"name", "count"}, new int[] {R.id.makes, R.id.models});
				list.setAdapter(listAdapter);
				
				list.setOnItemClickListener(new OnItemClickListener() {
					@SuppressWarnings("unchecked")
					public void onItemClick(AdapterView<?> makeListItem, View view, int position, long row){
						HashMap<String, Object> makeList = (HashMap<String, Object>)makeListItem.getItemAtPosition(position);
						String modelsItem = makeList.get("models").toString();
						String makeItem = makeList.get("name").toString();
						
						Intent secondActivity = new Intent(mContext, SecondActivity.class);
						secondActivity.putExtra("MAKE_KEY", makeItem);
						secondActivity.putExtra("MODELS_KEY", modelsItem);
						startActivity(secondActivity);
						
					}
				});
				
			}catch (JSONException e){
				e.printStackTrace();
			}
	}
	
	
	
	public Boolean checkConnection(Context context){
		Boolean connect = false;
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cManager.getActiveNetworkInfo();
		if(network != null){
			if(network.isConnected()){
				Log.i("NETWORK", network.getTypeName());
				connect = true;
			}
		}
		return connect;
	}
}
