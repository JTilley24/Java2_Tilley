package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 4

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
					Toast.makeText(context, "Selection was saved!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "Selection is already saved!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return view;
	}

	public Boolean saveModel(String model){
		JSONstorage storage = JSONstorage.getInstance();
		String savedString = storage.readStringFile(context, "saved_models");
		
		if(savedString != ""){
			if(savedString.contains(model) == true){
				return false;
			}else{
			StringBuilder builder = new StringBuilder();
			builder.append(savedString);
			builder.append(",");
			builder.append(model);
			storage.writeStringFile(context, "saved_models", builder.toString());
			}
		}else{
			storage.writeStringFile(context, "saved_models", model);
		}
		
		return true;
	}

}
