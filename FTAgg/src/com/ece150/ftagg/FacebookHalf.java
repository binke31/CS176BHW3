package com.ece150.ftagg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;

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
	Button fbLogin;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fbside);	
		Button twitterSide = (Button) findViewById(R.id.twitterSide);
		fbNewsFeed = (ListView)findViewById(R.id.fbFeedListView);
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listContent);
		fbNewsFeed.setAdapter(adapter);

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
    		openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
    		session.openForRead(openRequest);
        } 
        else {
            //Session.openActiveSession(this, true, statusCallback);
        	session = new Session(this);
        	Session.setActiveSession(session);
            Session.OpenRequest openRequest = new Session.OpenRequest(this).setCallback(statusCallback);
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
        }
    }    
    
    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

}