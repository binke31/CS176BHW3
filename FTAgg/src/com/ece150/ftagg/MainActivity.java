package com.ece150.ftagg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.facebook.Session;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button facebookSide = (Button) findViewById(R.id.facebookSide);
		Button twitterSide = (Button) findViewById(R.id.twitterSide);
		
		
		facebookSide.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View v){
				Intent fbIntent = new Intent(v.getContext(), FacebookHalf.class);
				v.getContext().startActivity(fbIntent);
			}
			
		});
		
		twitterSide.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View v1){
				Intent twitIntent = new Intent(v1.getContext(),TwitterHalf.class);
				v1.getContext().startActivity(twitIntent);
			}
			
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}
