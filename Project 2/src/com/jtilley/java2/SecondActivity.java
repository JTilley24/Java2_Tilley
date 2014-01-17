package com.jtilley.java2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SecondActivity extends Activity {
	public String make;
	public String model;
	ListView modelView;
	ArrayList<String> modelList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		
		Intent intent = this.getIntent();
		make = intent.getStringExtra("MAKE_KEY");
		String models = intent.getStringExtra("MODELS_KEY").toString();
		
		modelView = (ListView) this.findViewById(R.id.models);
		
		displayModels(models);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

	public void displayModels(String modelsJSON){
		//ArrayList<String> modelList = new ArrayList<String>();
		
		try {
			JSONArray jsonArray = new JSONArray(modelsJSON);
			for(int i=0; i< jsonArray.length(); i++){
				JSONObject modelsObj = jsonArray.getJSONObject(i);
				//Log.i("MODELS", modelsObj.toString());
				String modelName = modelsObj.getString("name");
				
				String tempString = new String(make + " " + modelName);
				
				modelList.add(tempString);
			}
			
			Log.i("MODEL_ARRAY", modelList.toString());
			
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, modelList);
			
			modelView.setAdapter(listAdapter);
			
			modelView.setOnItemClickListener(new OnItemClickListener() {

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
				
				
			});
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i("MODELS_RESULTS", e.getMessage().toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public void finish(){
		Intent data = new Intent();
		data.putExtra("model", model);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
