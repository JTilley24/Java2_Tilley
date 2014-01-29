package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 4

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SecondActivityFragment extends Fragment {
	ListView modelView;
	TextView selectMake;
	
	public interface onModelSelected{
		public void googleSearch(String modelURL);
		public Boolean isLandscape();
	}
	
	private onModelSelected parentActivity;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if(activity instanceof onModelSelected) {
			parentActivity = (onModelSelected) activity;
		}
		else{
			throw new ClassCastException(activity.toString() + "must implement onModelSelected");
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.activity_second, container);
		
		selectMake = (TextView) view.findViewById(R.id.select);
		selectMake.setVisibility(View.VISIBLE);
		
		modelView = (ListView) view.findViewById(R.id.models);
		modelView.setVisibility(View.GONE);
		
		return view;
	}

	//Display Models in ListView
	public void displayModels(ArrayList<String> modelList){
		//ArrayAdapter<String> modelsListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, modelList);
		ModelListAdapter adapter = new ModelListAdapter(getActivity(), modelList);
		
		//Display ListView When Data is Received
		selectMake.setVisibility(View.GONE);
		modelView.setVisibility(View.VISIBLE);
		
		modelView.setAdapter(adapter);
		
		modelView.setOnItemClickListener(new OnItemClickListener() {
			//Open Google Search for Selected Item
			public void onItemClick(AdapterView<?> modelListItem, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("MODELLIST", (String) modelListItem.getItemAtPosition(position));
				String model = modelListItem.getItemAtPosition(position).toString();
				String modelURL = model.replaceAll(" ", "+");
				Log.i("SELECTED", modelURL);
				if(parentActivity.isLandscape() == true){
					Intent google = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/#q=" + modelURL));
					startActivity(google);
				}else{
					parentActivity.googleSearch(model.toString());
				}	
			}	
		});
	}	
}
