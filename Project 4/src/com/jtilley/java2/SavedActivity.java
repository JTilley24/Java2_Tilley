package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 4

import java.util.ArrayList;
import java.util.Arrays;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SavedActivity extends Activity {
	ListView savedList;
	TextView noSaved;
	Context context;
	ArrayList<String> models = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved);
		context = this;
		savedList = (ListView) findViewById(R.id.saved);
		noSaved = (TextView)findViewById(R.id.nosave);
		
		
		
		displaySaved();
		
	}
	
	public void displaySaved(){
		noSaved.setVisibility(View.GONE);
		savedList.setVisibility(View.VISIBLE);
		
		JSONstorage storage = JSONstorage.getInstance();
		String savedString = storage.readStringFile(this, "saved_models");
		if(savedString != ""){
			models = new ArrayList<String>(Arrays.asList(savedString.split(",")));
			Log.i("MODELS", models.toString());
			
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, models);;
			
			savedList.setAdapter(listAdapter);
			
			savedList.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					// TODO Auto-generated method stub
					DialogFragment savedFragment = SavedDialog.newInstance(position);
					savedFragment.show(getFragmentManager(), "saved");
				}
				
				
			});
		}else{
			noSaved.setVisibility(View.VISIBLE);
			savedList.setVisibility(View.GONE);
		}
	}

	public void removeItem(int position){
		models.remove(position);
		String modelString;
		
		Log.i("MODELS", models.toString());
		
		if(models != null){
			modelString = models.toString().replace("[", "");
			modelString = modelString.replace("]", "");
			Log.i("MODELS", modelString);
			
			
		}else{
			modelString = "";
			Log.i("MODELS", modelString);
		}
		
		JSONstorage storage = JSONstorage.getInstance();
		storage.writeStringFile(context, "saved_models", modelString);
		
		displaySaved();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved, menu);
		return true;
	}

	public static class SavedDialog extends DialogFragment{
		Button okButton;
		Button cancelButton;
		int position;
		
		static SavedDialog newInstance(int pos){
			SavedDialog saved = new SavedDialog();
			
			Bundle args = new Bundle();
			args.putInt("position", pos);
			saved.setArguments(args);
			
			return saved;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			position = getArguments().getInt("position");
			
			View view = inflater.inflate(R.layout.saved_dialog, container);
			getDialog().setTitle("Would you like to Delete?");
			okButton = (Button) view.findViewById(R.id.okButton);
			cancelButton = (Button) view.findViewById(R.id.cancelButton);
			
			okButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((SavedActivity)getActivity()).removeItem(position);
					dismiss();
					
				}
			});
			
			cancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			
			return view;
		}
	
	}
}
