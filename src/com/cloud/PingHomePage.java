package com.cloud;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class PingHomePage extends Activity{

	private static final String link="http://recrusocial.phpfogapp.com/linkedInPeopleSearch2.php?";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		String firstName=null;
		String lastName=null;
		String fullName=getIntent().getExtras().getString(CloudProjectActivity.FULL_NAME);
		if(!fullName.equals("")){
			String[] nameArray=fullName.split(" ");
			
			firstName=nameArray[0];
			lastName=nameArray[1];
		}
		
		String finalLink=null;
		if(firstName!=null || lastName!=null){
			finalLink=link+"&firstName="+firstName+"&lastName="+lastName;
		
			Log.e("PingHomePage FullName: ",fullName);
			setContentView(R.layout.homepage);
			WebView homePageWebView=(WebView) findViewById(R.id.homepageWebView);
			homePageWebView.getSettings().setJavaScriptEnabled(true);
			homePageWebView.getSettings().setSupportZoom(true);
			homePageWebView.getSettings().setBuiltInZoomControls(true);
			homePageWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			homePageWebView.getSettings().setPluginsEnabled(true);
			homePageWebView.getSettings().setSupportMultipleWindows(true);

			
//			homePageWebView.loadUrl(finalLink);
			
			Uri uri=Uri.parse(finalLink);
			Intent intent=new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
		}
		
	}

	

}
