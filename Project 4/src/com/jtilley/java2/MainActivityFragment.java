package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 3

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivityFragment extends Fragment{

	ListView list;
	Button filterButton;
	Button queryButton;
	public Bundle savedInstanceState;
	public String savedString;
	public String filterString;
	public String JSONString;
	public ArrayList<HashMap<String, Object>> makeList;
	SimpleAdapter listAdapter;
	
	public interface OnListItemClicked{
		void onListItemClicked(String makeItem, String modelsItem);
		ArrayList<HashMap<String, Object>> getJSONCars(String JSONString);
		void showDialog();
	}
	
	private OnListItemClicked parentActivity;
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		if(activity instanceof OnListItemClicked) {
			parentActivity = (OnListItemClicked) activity;
		}
		else{
			throw new ClassCastException(activity.toString() + "must implement onListItemClicked");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.activity_main, container);
		
		filterButton = (Button) view.findViewById(R.id.filter);
		queryButton = (Button) view.findViewById(R.id.query);
		
		//Create ListView Headers
		list = (ListView) view.findViewById(R.id.list);
		View listHeader = inflater.inflate(R.layout.list_header, null);
		list.addHeaderView(listHeader);
		list.setTextFilterEnabled(true);
		
		//Retain Data During Change
		if(savedInstanceState != null){
			savedString = savedInstanceState.getString("saved");
			
			Log.i("MAIN", "Restoring Saved State");
		}		
		
		//Filter ListView from User Input
		filterButton.setOnClickListener(new OnClickListener() {
					
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parentActivity.showDialog();
			}
		});
		
		
		//Display All Data in ListView
		queryButton.setOnClickListener(new OnClickListener() {
				
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list.clearTextFilter();
				savedString = null;
			}
		});
		
		return view;
		
	}
	
	//Display ListView of Makes
	public void displayCars(ArrayList<HashMap<String, Object>> makeList){
		
		//Add JSON Data to ListView
		listAdapter = new SimpleAdapter(getActivity(), makeList, R.layout.list_row, new String[] {"name", "count"}, new int[] {R.id.makes, R.id.models});
		listAdapter.notifyDataSetChanged();
		list.setAdapter(listAdapter);
		
		//Select Item and Send Data to Second Activity
		list.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> makeListItem, View view, int position, long row){
				HashMap<String, Object> makeList = (HashMap<String, Object>)makeListItem.getItemAtPosition(position);
				String modelsItem = makeList.get("models").toString();
				String makeItem = makeList.get("name").toString();
				
				parentActivity.onListItemClicked(makeItem, modelsItem);
			}
		});
		
		if(savedString != null){
			list.setFilterText(savedString);
		}
		
	}
	//Save User Input and Last Searched
		public void onSaveInstanceState(Bundle outState){
			super.onSaveInstanceState(outState);
			if(filterString != null){
				outState.putString("saved", filterString);
			}
			Log.i("MAIN", "Saving Instance State");
		
		}
		
		public void filterList(String inputString){
			filterString = inputString;
			list.setFilterText(inputString);
		}
}
