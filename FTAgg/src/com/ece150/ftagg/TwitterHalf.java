package com.ece150.ftagg;

import java.io.IOException;  
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TwitterHalf extends Activity {
	@SuppressWarnings("unused")
	private static final String TAG = "com.ece150.ftagg"; //	/** Name to store the users access token */

	private static final String ACCESS_TOKEN = "1222970167-wjikCBx1k0bMYODQt87lTA6jdoS490RjzxDAJAg";  /** Name to store the users access token secret */
	private static final String ACCESS_TOKEN_SECRET = "UCnMn74mbNv82NlKrBSnXaPEemeRUmAwGRHOUofYrqg"; /** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */
	private static final String CONSUMER_KEY = "5ybPvsRprGMzak4ZUnjMg"; /** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/  */
	private static final String CONSUMER_SECRET = "wO1XsDwCIfhjvyCMcWlehHlMSB4c6RooyShgiwwWXa0"; //** The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity */
	private static final String CALLBACK_URL = "FTAgg:///";  //** Preferences to store a logged in users credentials */
	private SharedPreferences twitPrefs;
	private Twitter twitSession;  
	private RequestToken reqToken;   /** The request token signifies the unique ID of the request you are sending to twitter  */
	private Button twitterLogin;
	ListView twitterNewsFeed;
	List<Status> statuses;
	private boolean isLoggedin =false;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitside);		
		startNewSession();
	}

	@Override
	protected void onResume() {
		super.onResume();

		twitterNewsFeed = (ListView)findViewById(R.id.twitFeedListView);
		simpleTweet tweet = new simpleTweet();
		ArrayList<simpleTweet> items = new ArrayList<simpleTweet>();
		items.add(tweet);

		twitterLogin = (Button) findViewById(R.id.twitterLogin);
		if(isLoggedin==true){
			twitterLogin.setText("Log out");
		}
		if(isLoggedin==false){
			twitterLogin.setText("Login to Twitter");
		}
		twitterLogin.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				if(twitPrefs.contains(ACCESS_TOKEN)){
					twitterLogin.setText("Login to Twitter");
					logOutAuth();
				}
				else{
					twitterLogin.setText("Log out");
					loginNewUser();}}
		});

		Button facebookSide = (Button) findViewById(R.id.facebookSide);
		facebookSide.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View v){
				Intent fbIntent = new Intent(v.getContext(), FacebookHalf.class);
				v.getContext().startActivity(fbIntent);}
		});

		Button twitterSide = (Button) findViewById(R.id.twitterSide); 

		twitterSide.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View v1){ //on click, fill newsfeed with twitter data
				feedAdapter adaptor = new feedAdapter(v1.getContext(),R.layout.twitterfeed, getHomeTimeline()); //fills listView with Twitter adapter
				twitterNewsFeed.setAdapter(adaptor); //
			}

		});
	}

	private void startNewSession(){
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(CONSUMER_KEY);
		builder.setOAuthConsumerSecret(CONSUMER_SECRET);
		Configuration conf = builder.build();
		twitSession = new TwitterFactory(conf).getInstance();
		twitPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
	}


	private ArrayList<simpleTweet> getHomeTimeline(){
		ArrayList<simpleTweet> tweets = new ArrayList<simpleTweet>();
		String token = twitPrefs.getString(ACCESS_TOKEN, null);
		String secret = twitPrefs.getString(ACCESS_TOKEN_SECRET, null);
		if (token==null || secret==null){
			Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
		}

		else{
			AccessToken at = new AccessToken(token, secret);
			twitterNewsFeed.setBackgroundColor(Color.WHITE);
			twitSession.setOAuthAccessToken(at); //set twitter access token from the credentials we got previously
			try {
				List <Status> statuses = twitSession.getHomeTimeline();
				for (Status status : statuses) {
					simpleTweet currentTweet = new simpleTweet();
					currentTweet.realName = status.getUser().getName();
					currentTweet.username = "@"+status.getUser().getScreenName();
					currentTweet.pictureURL = status.getUser().getProfileImageURLHttps();
					currentTweet.message = status.getText();
					currentTweet.time = status.getCreatedAt();
					tweets.add(currentTweet);
				}
			} catch (TwitterException e) {
				System.out.println("Error: Could not get Home Timeline ");
				System.exit(-1);
			}

		}
		return tweets;
	}
	private void logOutAuth() {
		Editor edit = twitPrefs.edit();
		edit.remove(ACCESS_TOKEN);
		edit.remove(ACCESS_TOKEN_SECRET);
		edit.commit();
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();
		twitSession.setOAuthAccessToken(null);
		twitSession.shutdown();
		Toast.makeText(this, "Goodbye", Toast.LENGTH_SHORT).show();
	}


	private void loginNewUser() {
		try {
			reqToken = twitSession.getOAuthRequestToken(CALLBACK_URL);        //sets request token when logging into Twitter for the first time
			WebView twitterSite = new WebView(this);
			twitterSite.requestFocus(View.FOCUS_DOWN);
			twitterSite.loadUrl(reqToken.getAuthenticationURL());

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


	private void dealWithTwitterResponse(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) { // If the user has just logged in
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");
			authorizeNewUser(oauthVerifier);
		}
	}


	private void authorizeNewUser(String oauthVerifier) {
		try {
			AccessToken at = twitSession.getOAuthAccessToken(reqToken, oauthVerifier);
			twitSession.setOAuthAccessToken(at);
			saveAccessToken(at);
			isLoggedin=true;
			setContentView(R.layout.twitside); // Set the content view back after we changed to a webview

		} catch (TwitterException e) {
			Toast.makeText(this, "Could not Authorize", Toast.LENGTH_SHORT).show();
		}
	}


	private void saveAccessToken(AccessToken at) {
		String token = at.getToken();
		String secret = at.getTokenSecret();
		Editor editor = twitPrefs.edit();
		editor.putString(ACCESS_TOKEN, token);
		editor.putString(ACCESS_TOKEN_SECRET, secret);
		editor.commit();
	}

	public class simpleTweet{
		String realName;
		String username;
		String message;
		Date time;
		String pictureURL;
	}

	private class feedAdapter extends ArrayAdapter<simpleTweet> {
		private ArrayList<simpleTweet> tweets;
		public feedAdapter(Context context,int textViewResourceId, ArrayList<simpleTweet> items) {
			super(context, textViewResourceId, items);
			this.tweets = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) { //take Twitter feed and display on twitterfeed.xml
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService                        
						(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.twitterfeed, null);
			}
			simpleTweet tSample = tweets.get(position);
			TextView realn = (TextView) v.findViewById(R.id.realname);
			TextView usern = (TextView) v.findViewById(R.id.username);
			TextView message = (TextView) v.findViewById(R.id.tweetMessage);
			TextView date = (TextView) v.findViewById(R.id.dateCreated);
			ImageView pPicture = (ImageView) v.findViewById(R.id.profilePicture);
			String dateIssued = DateFormat.getDateTimeInstance().format(tSample.time);

			//Toast.makeText(getApplicationContext(), tSample.pictureURL, Toast.LENGTH_SHORT).show();
			message.setText(tSample.message);
			date.setText(dateIssued);
			realn.setText(tSample.realName + " ");
			usern.setText(tSample.username + " ");
			URL newurl = null;
			try {
				newurl = new URL(tSample.pictureURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				pPicture.setImageBitmap(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return v;
		}
	}

}