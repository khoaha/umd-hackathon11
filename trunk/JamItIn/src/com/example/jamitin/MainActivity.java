package com.example.jamitin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button requestButton;
	private EditText requestName;
	private Socket sockToSql;
	private ListView listofcontacts;
	//private URL serverURL
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestButton = (Button) findViewById(R.id.requestButton);
        requestName = (EditText) findViewById(R.id.requestName);
        listofcontacts = (ListView) findViewById(R.id.listView);
        connectToServer();
       
        
    }
    
    private String getPhoneNumber(){
    	TelephonyManager tMgr =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        
    	return tMgr.getLine1Number();
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
    
    public void populateListView(ArrayList<String> list){
    	ArrayAdapter<String> madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
    	listofcontacts.setAdapter(madapter);
    	listofcontacts.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
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
