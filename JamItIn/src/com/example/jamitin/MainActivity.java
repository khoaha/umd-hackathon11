package com.example.jamitin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Button requestButton;
	private EditText requestName;
	private Socket sockToSql;
	//private URL serverURL
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestButton = (Button) findViewById(R.id.requestButton);
        requestName = (EditText) findViewById(R.id.requestName);
        connectToServer();
    }

    private void connectToServer() {
		
		new ConnectTask().execute();
	}
    
    private class ConnectTask extends AsyncTask<String, Integer, Integer>{

		public ConnectTask() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			ContactLookerActivity contacts = new ContactLookerActivity();
			contacts.getData(getContentResolver());
			return null;
		}
    	
    }

	public void onResume(){
		super.onResume();
    	requestButton.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				String s = String.valueOf(requestName.getText().toString());
				requestContact(s);			
			}
    		
    	});
    }
    
    protected void requestContact(String s) {
		// TODO Auto-generated method stub
		
	}
    
    protected void callContact(String numberString){
    	if (!numberString.equals("")) {
    		  Uri number = Uri.parse("tel:" + numberString);
    		  Intent dial = new Intent(Intent.ACTION_CALL, number);
    		  startActivity(dial);
    		 }
    }
    	
}
