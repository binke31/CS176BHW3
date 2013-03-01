package com.ece150.ftagg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TwitterHalf extends Activity {
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

		twitterNewsFeed = (ListView)findViewById(R.id.twitFeedListView);
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listContent);
		twitterNewsFeed.setAdapter(adapter);

		Button twitterLogin = (Button) findViewById(R.id.twitterLogin);

		twitterLogin.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Toast.makeText(getApplicationContext(), "Twitter!", 2).show();
			}
		});
	}

}
