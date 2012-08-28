package com.example.jamitin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
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
		
		
	}
    
    private class ConnectTask extends AsyncTask<Integer, Integer, Socket>{

		@Override
		protected Socket doInBackground(Integer... arg0) {
			Socket temp = new Socket();
			//temp.connect()
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
    
    	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
