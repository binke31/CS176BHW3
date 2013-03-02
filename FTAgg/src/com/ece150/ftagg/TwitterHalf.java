package com.ece150.ftagg;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;



public class TwitterHalf extends Activity {

	private static final String TAG = "com.ece150.ftagg"; //*********************double check this....************//
	/** Name to store the users access token */

	private static final String PREF_ACCESS_TOKEN = "1222970167-wjikCBx1k0bMYODQt87lTA6jdoS490RjzxDAJAg";
	/** Name to store the users access token secret */

	private static final String PREF_ACCESS_TOKEN_SECRET = "UCnMn74mbNv82NlKrBSnXaPEemeRUmAwGRHOUofYrqg";
	/** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */

	private static final String CONSUMER_KEY = "5ybPvsRprGMzak4ZUnjMg";
	/** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/  */

	private static final String CONSUMER_SECRET = "wO1XsDwCIfhjvyCMcWlehHlMSB4c6RooyShgiwwWXa0"; // XXX Encode in your app
	/** The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity */
	private static final String CALLBACK_URL = "https://github.com/binke31/CS176BHW3.git";
	/** Preferences to store a logged in users credentials */

	private SharedPreferences twitPrefs;
	/** Twitter4j object */

	private Twitter twitSession;   /** The request token signifies the unique ID of the request you are sending to twitter  */

	private RequestToken rToken;
	private Button twitterLogin;
	//****************************************************************************************//
	ListView twitterNewsFeed;
	String[] listContent = {
			"Janu",
			"Febru",
			"Mar",
			"April",
			"Ma",
			"Ju",
			"Jul",
			"Augu",
			"Septe",
			"Octo",
			"Novem",
			"Deber",
	};
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitside);		
		twitPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		twitSession = new TwitterFactory().getInstance();
		twitSession.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

		twitterNewsFeed = (ListView)findViewById(R.id.twitFeedListView);
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listContent);
		twitterNewsFeed.setAdapter(adapter);

		twitterLogin = (Button) findViewById(R.id.twitterLogin);

		twitterLogin.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				if(twitPrefs.contains(PREF_ACCESS_TOKEN)){

					loginAuthorizedUser();
				}
				else{

					loginNewUser();
				}
			}
		});
	}

	private void loginAuthorizedUser() {
		String token = twitPrefs.getString(PREF_ACCESS_TOKEN, null);
		String secret = twitPrefs.getString(PREF_ACCESS_TOKEN_SECRET, null);

		// Create the twitter access token from the credentials we got previously
		AccessToken at = new AccessToken(token, secret);

		twitSession.setOAuthAccessToken(at);
		Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
	}

	private void loginNewUser() {
		try {
			rToken = twitSession.getOAuthRequestToken(CALLBACK_URL);

			WebView twitterSite = new WebView(this);
			twitterSite.loadUrl(rToken.getAuthenticationURL());
			setContentView(twitterSite);

		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter Login error, try again later", Toast.LENGTH_SHORT).show();
		}
	}	

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		dealWithTwitterResponse(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void dealWithTwitterResponse(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) { // If the user has just logged in
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");
			authoriseNewUser(oauthVerifier);
		}
	}

	private void authoriseNewUser(String oauthVerifier) {
		try {
			AccessToken at = twitSession.getOAuthAccessToken(rToken, oauthVerifier);
			twitSession.setOAuthAccessToken(at);
			saveAccessToken(at);
			// Set the content view back after we changed to a webview
			setContentView(R.layout.twitside);

		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter auth error x01, try again later", Toast.LENGTH_SHORT).show();
		}
	}
	private void saveAccessToken(AccessToken at) {
		String token = at.getToken();
		String secret = at.getTokenSecret();
		Editor editor = twitPrefs.edit();
		editor.putString(PREF_ACCESS_TOKEN, token);
		editor.putString(PREF_ACCESS_TOKEN_SECRET, secret);
		editor.commit();
	}


}
