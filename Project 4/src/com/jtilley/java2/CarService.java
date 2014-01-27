package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 3


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class CarService extends IntentService{

	public String responseString = "";
	public String urlString = "https://api.edmunds.com/api/vehicle/v2/makes?state=new&year=2014&view=full&fmt=json&api_key=saw2xy7wdxjqfueuxkv5hm8w";
	JSONstorage storage;
	
	public static final String MESSENGER_KEY = "messenger";
	public static final String REQUEST_KEY = "";

	
	public CarService(){
		super("CarService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String url = intent.getStringExtra(REQUEST_KEY);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		String responseData = "";
		try {
			HttpResponse response = httpClient.execute(request);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				InputStream input = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				StringBuilder builder = new StringBuilder();
				String line;
				if(reader != null){
					while ((line = reader.readLine()) != null){
						builder.append(line);
					}
				}
				input.close();
				responseData = builder.toString();
				Log.i("RESPONSE", "Response Recieved");
				
				storage = JSONstorage.getInstance();
				storage.writeStringFile(this, "cars_json", responseData);
				
				boolean flag = true;
				Messenger messenger = (Messenger) intent.getExtras().get("messenger");
				Message message = Message.obtain();
				message.arg1 = Activity.RESULT_OK;
				message.obj = flag;
				
				try{
					messenger.send(message);
				} catch(RemoteException e){
					Log.e("onHandelIntent", e.getMessage().toString());
					e.printStackTrace();
				}
				
				reader.close();
			}else{
				Log.i("RESPONSE", response.getStatusLine().getReasonPhrase());
				response.getEntity().getContent().close();	
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("HTTPS", e.getMessage().toString());
			e.printStackTrace();
		}
				
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra("DATA", responseData);
		sendBroadcast(broadcastIntent);
	
	}
}
