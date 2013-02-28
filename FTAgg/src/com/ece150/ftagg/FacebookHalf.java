package com.ece150.ftagg;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class FacebookHalf extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fbside);	

		Button fbLogin = (Button) findViewById(R.id.facebookLogin);
		//
		//		fbLogin.setOnClickListener(new OnClickListener(){			
		//			@Override
		//			public void onClick(View v){ // start Facebook Login
		//				Session.openActiveSession(this, true, new Session.StatusCallback() {					
		//					@Override // callback when session changes state
		//					public void call(Session session, SessionState state, Exception exception) {
		//						if (session.isOpened()) {
		//							Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
		//
		//								@Override
		//								public void onCompleted(GraphUser user, Response response) {
		//									if (user != null) {
		//										//								TextView welcome = (TextView) findViewById(R.id.welcome);
		//										//								welcome.setText("Hello " + user.getName() + "!");
		//									}
		//								}
		//							});
		//						}
		//					}
		//				});
		//
		//			}
		//
		//		});
	}
}
