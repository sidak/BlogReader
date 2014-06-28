package com.sidak.blogreader;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class MainListActivity extends ListActivity {
	
	protected String[] mBlogPostTitle;
	public static final int NUMBER_OF_POSTS=20;
	public static final String TAG =MainListActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		try {
			URL blogFeedUrl= new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count="+ NUMBER_OF_POSTS);
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.e(TAG, "Exception caught:",e);	
		}

		
		//String message=getString(R.string.no_items);
		//Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
