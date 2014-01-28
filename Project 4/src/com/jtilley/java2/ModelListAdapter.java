package com.jtilley.java2;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ModelListAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> modelsArray;
	public ArrayList<String> savedModel = new ArrayList<String>();
	
	public ModelListAdapter(Context mContext, ArrayList<String> models){
		context = mContext;
		modelsArray = models;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modelsArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return modelsArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		view = inflater.inflate(R.layout.mlist_item, parent, false);
		final String modelName = modelsArray.get(position);
		
		TextView modelText = (TextView) view.findViewById(R.id.modelName);
		modelText.setText(modelName);
		
		Button saveButton =(Button) view.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//MainActivity main = new MainActivity();
				if(saveModel(modelName) == true){
					Log.i("SAVED", "Model Saved!");
				}else{
					Log.i("SAVED", "Model Not Saved!");
				}
			}
		});
		
		return view;
	}

	public Boolean saveModel(String model){
		JSONstorage storage = JSONstorage.getInstance();
		String savedString = storage.readStringFile(context, "saved_models");
		ArrayList<String> savedModels = new ArrayList<String>(Arrays.asList(savedString.split(" , ")));
		
		if(savedString.length() > 0){
			if(savedModels.contains(model) == true){
				return false;
			}
			savedModels.add(model);
			
			storage.writeStringFile(context, "saved_models", savedModels.toString());
		}else{
			savedModel.add(model);
			storage.writeStringFile(context, "saved_models", savedModel.toString());
		}
		
		
		
		return true;
	}
}
