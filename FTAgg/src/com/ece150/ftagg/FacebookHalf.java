package com.ece150.ftagg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class FacebookHalf extends Activity {
	ListView fbNewsFeed;
	String[] listContent = {
			"January",
			"February",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December",
	};

	

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fbside);	

		fbNewsFeed = (ListView)findViewById(R.id.fbFeedListView);
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listContent);
		fbNewsFeed.setAdapter(adapter);



		Button fbLogin = (Button) findViewById(R.id.facebookLogin);

		fbLogin.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Session.openActiveSession(FacebookHalf.this, true, new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {

							// make request to the /me API
							Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

								// callback after Graph API response with user object
								@Override
								public void onCompleted(GraphUser user, Response response) {
									if (user != null) {}
								}
							});
						}
					}
				});
			}
		});  




	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}
