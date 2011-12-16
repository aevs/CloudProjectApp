package com.cloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class PingHomePage extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		String fullName=getIntent().getExtras().getString(CloudProjectActivity.FULL_NAME);
		Log.e("PingHomePage FullName: ",fullName);
		setContentView(R.layout.homepage);
		WebView homePageWebView=(WebView) findViewById(R.id.homepageWebView);
		homePageWebView.getSettings().setJavaScriptEnabled(true);
		homePageWebView.getSettings().setSupportZoom(true);
		homePageWebView.getSettings().setBuiltInZoomControls(true);
		homePageWebView.loadUrl("http://www.google.com");
	}

	

}
