package com.jtilley.java2;
//Justin Tilley 
//Java 2
//Project 4


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.util.Log;

public class JSONstorage {

	private static JSONstorage m_instance;
	
	private JSONstorage(){
		
	}
	
	public static JSONstorage getInstance(){
		if(m_instance == null){
			m_instance = new JSONstorage();
		}
		return m_instance;
	}
	//Save Data to Internal Storage
	public Boolean writeStringFile (Context context, String filename, String content){
		Boolean result = false;
		
		FileOutputStream output = null;
		
		try{
			output = context.openFileOutput(filename, Context.MODE_PRIVATE);
			output.write(content.getBytes());
			Log.i("WRITE STRING FILE", "success");
		}catch(Exception e){
			Log.e("WRITE STRING FILE", e.toString());
		}
		
		return result;
	} 
	//Access Data from Internal Storage
	public String readStringFile(Context context, String filename){
		String content = "";
		
		FileInputStream input = null;
		
		try{
			input = context.openFileInput(filename);
			 BufferedInputStream buffInput = new BufferedInputStream(input);
			 byte[] contentBytes = new byte[1024];
			 int bytesRead = 0;
			 StringBuffer contentBuffer = new StringBuffer();
			 
			 while((bytesRead = buffInput.read(contentBytes)) != -1){
				 content = new String(contentBytes, 0, bytesRead);
				 contentBuffer.append(content);
			 }
			 content = contentBuffer.toString();
			 Log.i("READ STRING", "success");
		}catch(Exception e){
			
		}finally {
			try{
				input.close();
			}catch(Exception e){
				Log.e("CLOSE ERROR", e.toString());
			}
		}
		return content;
	}
}
