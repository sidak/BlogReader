package com.sidak.blogreader;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainListActivity extends ListActivity {
	
	protected String[] mBlogPostTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		//getstringarray() method isn't available from the context 
		// or activity subclass 
		//It is a part of the resources object which we can get using the get resources 
		//method which is available from the above classes 
		
		Resources resources =getResources();
		mBlogPostTitle= resources.getStringArray(R.array.serial_num);
		ArrayAdapter<String> adapter=
				  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mBlogPostTitle);
		setListAdapter(adapter);
		
		//String message=getString(R.string.no_items);
		//Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
