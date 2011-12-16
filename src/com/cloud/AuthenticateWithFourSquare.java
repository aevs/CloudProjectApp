package com.cloud;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AuthenticateWithFourSquare extends Activity{

	private static final String TAG="AuthenticateWithFourSquare";
	public static final String CALLBACK_URL = "your callback url";	
	public static final String CLIENT_ID = "X4KFIR5JOLZPYRU0KUHTUNM5K4E4HPHLQISYT1NE0IUNRWWM";
    
    public static String accessToken;
    public static String ACCESS_TOKEN="ACCESS_TOKEN";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		 
        String url =
            "https://foursquare.com/oauth2/authenticate" + 
                "?client_id=" + CLIENT_ID + 
                "&response_type=token"+
                "&redirect_uri=" + "android://only";
        
//        String url="http://";
        
        // If authentication works, we'll get redirected to a url with a pattern like:  
        //
        //    http://YOUR_REGISTERED_REDIRECT_URI/#access_token=ACCESS_TOKEN
        //
        // We can override onPageStarted() in the web client and grab the token out.

//        WebView webView = new WebView(CloudProjectActivity.this);

        setContentView(R.layout.login);
//        TextView tv=(TextView)findViewById(R.id.hello);
//        addContentView(tv,null);
        
         WebView webView = (WebView)findViewById(R.id.foursquarelogin);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "#access_token=";
                Log.e("Returned URL:",url);
//                Toast.makeText(CloudProjectActivity.this, "URL: "+url , Toast.LENGTH_SHORT).show();
                int start = url.indexOf(fragment);
                if (start > -1) {
                    // You can use the accessToken for api calls now.
                    accessToken = url.substring(start + fragment.length(), url.length());
        			
                    Log.e(TAG, "OAuth complete, token: [" + accessToken + "].");
                	
                    Toast.makeText(AuthenticateWithFourSquare.this, "Token: " + accessToken, Toast.LENGTH_SHORT).show();
                    String selfUrl = "http://api.foursquare.com/v2/users/self?oauth_token=" + accessToken;
//                    webView.loadUrl(selfUrl);
//                    String venuesUrl 	= new URL(API_URL + "/venues/search?ll=" + ll + "&oauth_token=" + mAccessToken);
                    
                    Intent intent = new Intent();
        			intent.putExtra(ACCESS_TOKEN, accessToken);
        			setResult(CloudProjectActivity.AUTHENTICATE_ACTIVITY_REQUEST_CODE, intent);
        			finish();
                    
                    finish();
                    
                    
                }
            }
        });
        webView.loadUrl(url);
		
		
		
		
	}
 
}
