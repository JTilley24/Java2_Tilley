package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 1


import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
public class CarService extends IntentService{

	public String responseString = "";
	public String urlString = "https://api.edmunds.com/api/vehicle/v2/makes?state=new&year=2014&view=full&fmt=json&api_key=saw2xy7wdxjqfueuxkv5hm8w";
	JSONstorage storage;
	
	public CarService(){
		super("CarService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String results = getJSON(urlString);
		
		//Check for Connection
		if(results.equals("Not Connected")){
			Log.i("CONNECT", "No Connection");
		}else{
			//Save JSON Data to Internal Storage
			storage = JSONstorage.getInstance();
			storage.writeStringFile(this, "cars_json", results);
		}
		
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
		
	}
	
	public String getJSON(String urlString){
		try {
			getData data = new getData();
			responseString = data.execute(urlString).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
		
	}
	
	public static String getResponse(URL url){
		String response = "";
		
		try {
			URLConnection conn;
			conn = url.openConnection();
			BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
			byte[] contextByte = new byte[1024];
			int byteRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			while ((byteRead = bin.read(contextByte)) != -1){
				response = new String(contextByte, 0, byteRead);
				responseBuffer.append(response);
			}
			response = responseBuffer.toString();
		} catch (IOException e) {
			response = "Not Connected";
			e.printStackTrace();
		}
		
		return response;
	}
	
	public class getData extends AsyncTask<String, Void, String>{
		
		protected String doInBackground(String... params){
			String responseString = "";
			try {
				URL url = new URL (urlString);
				responseString = getResponse(url);
			} catch (Exception e) {
				responseString = "No info";
				e.printStackTrace();
			}
			return responseString;
		}
		
		protected void onPostExecute(String results){
			super.onPostExecute(results);
		}
	}
	

}
