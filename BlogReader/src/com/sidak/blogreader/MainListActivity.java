package com.sidak.blogreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainListActivity extends ListActivity {
	
	protected String[] mBlogPostTitle;
	public static final int NUMBER_OF_POSTS=20;
	public static final String TAG =MainListActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		if (isNetworkAvailable()){
			GetBlogPostsTask getBlogPostsTask = new GetBlogPostsTask();
			getBlogPostsTask.execute();
		}else{
			Toast.makeText(this, "network not available", Toast.LENGTH_LONG).show();
		}

		
		//String message=getString(R.string.no_items);
		//Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager manager= (ConnectivityManager)
				getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		boolean isAvailable=false;
		if(networkInfo!=null && networkInfo.isConnected()){
			 isAvailable=true;
		}
		return isAvailable;
	}

	private class GetBlogPostsTask extends AsyncTask<Object, Void, String>{

		@Override
		protected String doInBackground(Object... params) {
			int responseCode =-1;	
			try {
					URL blogFeedUrl= new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count="+ NUMBER_OF_POSTS);
					HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
					connection.connect();
					
					responseCode =connection.getResponseCode();
					if (responseCode==HttpURLConnection.HTTP_OK){
						InputStream inputStream = connection.getInputStream();
						Reader reader = new InputStreamReader(inputStream);
						int contentLength=connection.getContentLength();
						char[] charArray= new char[contentLength];
						reader.read(charArray); // it modifies the charArray
						String responseData = new String(charArray);
						
						JSONObject jsonResponse = new JSONObject(responseData);
						String status =jsonResponse.getString("status");
						Log.v(TAG, status);
						
						JSONArray jsonPosts = jsonResponse.getJSONArray("posts");
						for(int i=0; i<jsonPosts.length(); i++){
							JSONObject jsonPost= jsonPosts.getJSONObject(i);
							String title =jsonPost.getString("title");
							Log.v(TAG, "Post "+i + ": "+title);
						}
						 
					}else{
						Log.i(TAG, "unsuccessful Http response-code: "+responseCode);

					}
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
					Log.e(TAG, "Exception caught:",e);	
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "Exception caught:",e);	
	
				}catch (Exception e) {
					Log.e(TAG, "Exception caught:",e);	
				}
				return "code: "+responseCode;
		}
		
	}
}
