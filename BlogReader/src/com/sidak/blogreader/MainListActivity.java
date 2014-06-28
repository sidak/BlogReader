package com.sidak.blogreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainListActivity extends ListActivity {
	
	protected String[] mBlogPostTitle;
	public static final int NUMBER_OF_POSTS=20;
	public static final String TAG =MainListActivity.class.getSimpleName();
	protected JSONObject mBlogData;
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
	
	public void updateList() {
		 if(mBlogData==null){
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 builder.setTitle(getString(R.string.error_title));
			 builder.setMessage(getString(R.string.error_message));
			 builder.setPositiveButton(android.R.string.ok, null);
			 AlertDialog dialog = builder.create();
			 dialog.show();
		 }else{
			 try {
				JSONArray jsonPosts= mBlogData.getJSONArray("posts");
				mBlogPostTitle =new String[jsonPosts.length()];
				for(int i=0; i<mBlogPostTitle.length; i++){
					JSONObject post = jsonPosts.getJSONObject(i);
					String title= post.getString("title");
					title=Html.fromHtml(title).toString();
					mBlogPostTitle[i]=title;
				}
				ArrayAdapter<String> adapter= 
						new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mBlogPostTitle);
				setListAdapter(adapter);
				
			} catch (JSONException e) {
				Log.e(TAG, "exception: ", e);
			}
			 
		 }
	}

	private class GetBlogPostsTask extends AsyncTask<Object, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Object... params) {
			int responseCode =-1;	
			JSONObject jsonResponse= null;
			try {
					URL blogFeedUrl= new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count="+ NUMBER_OF_POSTS);
					HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
					connection.connect();
					
					responseCode =connection.getResponseCode();
					if (responseCode==HttpURLConnection.HTTP_OK){
						InputStream inputStream = connection.getInputStream();
						Reader reader = new InputStreamReader(inputStream);
						int nextCharacter; 
						String responseData = "";
						while(true){ 
						    nextCharacter = reader.read(); 
						    if(nextCharacter == -1)  
						        break;
						    responseData += (char) nextCharacter; 
						}
						
						jsonResponse = new JSONObject(responseData);
						
					}
						 
					else{
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
				return jsonResponse;
		}
		@Override
		protected void onPostExecute(JSONObject result){
			mBlogData= result;
			updateList();
		}
		
	}

	
}
