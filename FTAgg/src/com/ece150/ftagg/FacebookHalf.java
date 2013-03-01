package com.ece150.ftagg;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class FacebookHalf extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fbside);	
//		final TextView b = new TextView(this);
//		b.setText("HELLO");
//    	FacebookHalf.this.addContentView(b,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
        	                if (user != null) {
        	                }
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
