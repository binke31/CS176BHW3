

package com.ece150.ftagg;

import java.io.InputStream; 
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;

public class FacebookHalf extends Activity {
	private static final List<String> PERMISSIONS = Arrays.asList("user_photos", "read_stream");
	ListView fbNewsFeed;

	ArrayList<fbPost> fbNews = new ArrayList<fbPost>();
	JSONArray feedArray;
	Button fbLogin;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fbside);	
		Button twitterSide = (Button) findViewById(R.id.twitterSide);
		fbNewsFeed = (ListView)findViewById(R.id.fbFeedListView);
		//ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listContent);
		//newsAdapter adapter = new newsAdapter(this,R.layout.fblistview, null);
		fbLogin =(Button) findViewById(R.id.facebookLogin);


		Session session = Session.getActiveSession();

		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
			}
		}

		twitterSide.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View v1){
				Intent twitIntent = new Intent(v1.getContext(),TwitterHalf.class);
				v1.getContext().startActivity(twitIntent);
			}

		});
		updateView();

	}

	private void updateView() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			//textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
			fbLogin.setText("Logout of Facebook");
			fbLogin.setOnClickListener(new OnClickListener() {
				public void onClick(View view) { logout(); }
			});
		} else {
			//textInstructionsOrLink.setText(R.string.instructions);
			fbLogin.setText("Login to Facebook");
			fbLogin.setOnClickListener(new OnClickListener() {
				public void onClick(View view) { login(); }
			});
		}
	}

	private void login(){
		Session session = Session.getActiveSession();

		if (!session.isOpened() && !session.isClosed()) { 
			Session.OpenRequest openRequest = new Session.OpenRequest(this).setCallback(statusCallback);
			openRequest.setPermissions(PERMISSIONS);
			openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			session.openForRead(openRequest);
		} 
		else {
			//Session.openActiveSession(this, true, statusCallback);
			session = new Session(this);
			Session.setActiveSession(session);
			Session.OpenRequest openRequest = new Session.OpenRequest(this).setCallback(statusCallback);
			openRequest.setPermissions(PERMISSIONS);

			openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			session.openForRead(openRequest);
		}   
	}

	private void logout(){
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.close();
			session.closeAndClearTokenInformation();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			updateView();
			onSessionStateChange(session,state,exception);
		}
	}    

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		Session.setActiveSession(session);
		Session.restoreSession(this, null, statusCallback, null);
		updateView();
		if(session.getState().isOpened()){
			getFeed(session);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}



	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			fbNewsFeed.setVisibility(View.VISIBLE);
			getFeed(session);
		} else if (state.isClosed()) {
			fbNewsFeed.setVisibility(View.INVISIBLE);
		}
	}

	public class fbPost{
		String profileURLString;
		String name;
		String message;
		String picURLString=null;
	}

	private class newsAdapter extends ArrayAdapter<fbPost>{
		private ArrayList<fbPost> posts;
		public newsAdapter(Context context, int textViewResourceId,ArrayList<fbPost> items){
			super(context,textViewResourceId,items);
			this.posts=items;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = 
						new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService 
						(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.fblistview, null);
			}
			fbPost tSample = posts.get(position);
			TextView realn = (TextView) v.findViewById(R.id.fbName);
			TextView message = (TextView) v.findViewById(R.id.fbMessage);
			message.setText(tSample.message );
			realn.setText(tSample.name);

			ImageView pic =(ImageView) v.findViewById(R.id.fbPicture); //*************
			ImageView profile = (ImageView) v.findViewById(R.id.fbProfile);
			//URL profileURL = null;
			URL url=null;
			URL ulrn=null;
			try{
				ulrn = new URL(tSample.profileURLString);
				// = new downloadImageAsync().execute(ulrn).get();
				HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
				InputStream is = con.getInputStream();
				Bitmap bmpProfile = BitmapFactory.decodeStream(is);
				//if (null != bmpProfile){
				profile.setImageBitmap(bmpProfile);
				//				}
				//				else
				//					Toast.makeText(FacebookHalf.this, "BMP IS NULL", Toast.LENGTH_SHORT).show();
			}		
			catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(tSample.picURLString!="noimg"){
				//Toast.makeText(getApplicationContext(), tSample.picURLString, Toast.LENGTH_SHORT).show();
				url = new URL(tSample.picURLString);
				// = new downloadImageAsync().execute(url).get();	
				HttpURLConnection con1 = (HttpURLConnection)url.openConnection();
				InputStream is1 = con1.getInputStream();
				Bitmap bmpPicture = BitmapFactory.decodeStream(is1);
				pic.setImageBitmap(bmpPicture);
				pic.getLayoutParams().width=150;
				pic.getLayoutParams().height=150;
				pic.setVisibility(View.VISIBLE);
				}
				
				if(tSample.picURLString=="noimg"){
					//Toast.makeText(getApplicationContext(), tSample.picURLString, Toast.LENGTH_SHORT).show();
					pic.setImageDrawable(null);
					pic.getLayoutParams().width=1;
					pic.getLayoutParams().height=1;
					pic.setVisibility(View.INVISIBLE);
				}
				//}
				//				if (null != bmpPicture){
				//					pic.setImageBitmap(bmpPicture);
				//				}
				//				else
				//					Toast.makeText(FacebookHalf.this, "BMP IS NULL", Toast.LENGTH_SHORT).show();
			}
			catch(Exception e){  
				e.printStackTrace();
			}
			return v;
		}
	}

	public void getFeed(Session session){
		Request initialFeedRequest = new Request(session,"/me/home",null,HttpMethod.GET,new Request.Callback(){
			@Override
			public void onCompleted(Response response){
				JSONObject responseObject = response.getGraphObject().getInnerJSONObject();
				try{
					feedArray = responseObject.getJSONArray("data");
				}
				catch(JSONException e){
					e.printStackTrace();
				}
				ArrayList<fbPost> fbNews = new ArrayList<fbPost>();
				newsAdapter adapter = new newsAdapter(FacebookHalf.this,R.layout.fblistview, fbNews);
				for(int i = 0;i<feedArray.length();i++){
					try{
						fbPost newFbPost = new fbPost();
						JSONObject jObj = feedArray.getJSONObject(i);
						JSONObject nameObj = jObj.getJSONObject("from");
						String msgObj = jObj.getString("message");
						String name = nameObj.getString("name");
						String id = jObj.getJSONObject("from").getString("id");

						newFbPost.name = name;
						newFbPost.message=msgObj;
						newFbPost.profileURLString = "http://graph.facebook.com/"+id+"/picture?type=small";
						if(jObj.has("picture")){
							newFbPost.picURLString = jObj.getString("picture");}
						if(!jObj.has("picture")){
							newFbPost.picURLString="noimg";}
						fbNews.add(newFbPost);
					}
					catch(JSONException e){

					}
				}
				fbNewsFeed = (ListView)findViewById(R.id.fbFeedListView);
				fbNewsFeed.setAdapter(adapter);

			}
		});
		initialFeedRequest.executeAsync();
	}


	//	public class asyncFbPost{
	//		fbPost fbPost;
	//		String url;
	//	}


	//	private class downloadImageAsync extends AsyncTask<URL, Integer, Bitmap> {
	//		protected Bitmap doInBackground(URL... url) {
	//			int count = url.length;
	//			Bitmap bmp = null;
	//			try{
	//				for(int i = 0;i<count;i++){
	//					HttpURLConnection con = (HttpURLConnection)url[i].openConnection();
	//					InputStream is = con.getInputStream();
	//					bmp = BitmapFactory.decodeStream(is);
	//				}
	//			}
	//			catch(IOException e){
	//
	//			}
	//			return bmp;
	//		}
	//
	//	} 

}
