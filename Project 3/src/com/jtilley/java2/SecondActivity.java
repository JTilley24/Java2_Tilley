package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 2

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class SecondActivity extends Activity implements SecondActivityFragment.onModelSelected {
	public String make;
	public String model;
	ListView modelView;
	ArrayList<String> modelList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_fragment);
		
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			finish();
		}
		
			//Retrieve Data sent from Main Activity
			Intent intent = this.getIntent();
			make = intent.getStringExtra("MAKE_KEY");
			String models = intent.getStringExtra("MODELS_KEY").toString();
			
			getModels(models);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

	public void getModels(String modelsJSON){
		
		try {
			//Parse Selected Object
			JSONArray jsonArray = new JSONArray(modelsJSON);
			for(int i=0; i< jsonArray.length(); i++){
				JSONObject modelsObj = jsonArray.getJSONObject(i);
				String modelName = modelsObj.getString("name");
				
				String tempString = new String(make + " " + modelName);
				
				modelList.add(tempString);
			}
			
			SecondActivityFragment fragment2 = (SecondActivityFragment)getFragmentManager().findFragmentById(R.id.second_fragment);
			fragment2.displayModels(modelList);
			
			/*ArrayAdapter<String> modelsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, modelList);
			
			modelView.setAdapter(modelsListAdapter);
			
			modelView.setOnItemClickListener(new OnItemClickListener() {
				//Open Google Search for Selected Item
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					model = modelList.get(position).toString();
					String modelURL = model.replaceAll(" ", "+");
					Log.i("SELECTED", modelURL);
					Intent google = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/#q=" + modelURL));
					startActivity(google);
					
				}
				
				
			});*/
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i("MODELS_RESULTS", e.getMessage().toString());
			e.printStackTrace();
		}
	}
	
	public void googleSearch(String modelURL){
		Intent google = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/#q=" + modelURL));
		startActivity(google);
	}
	
	//Sent Previous Selection back to Main Activity
	/*@Override
	public void finish(){
		Intent data = new Intent();
		data.putExtra("model", model);
		setResult(RESULT_OK, data);
		super.finish();
	}*/
}
