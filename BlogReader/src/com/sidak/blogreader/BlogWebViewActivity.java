package com.sidak.blogreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class BlogWebViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_web_view);
		
		Intent intent= getIntent();
		Uri blogUri = intent.getData();
		
		WebView webView =(WebView)findViewById(R.id.webView1);
		webView.loadUrl(blogUri.toString());
		
	}
}
