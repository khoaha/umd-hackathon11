package com.example.jamitin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button requestButton;
	private EditText requestName;
	private Socket sockToSql;
	private ListView listofcontacts;
	private Context context;
	//private URL serverURL

	protected final String phpURL = "http://zaphodbeeblebrox.student.umd.edu/jamitout.php";	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		requestButton = (Button) findViewById(R.id.requestButton);
		requestName = (EditText) findViewById(R.id.requestName);
		listofcontacts = (ListView) findViewById(R.id.listView);
		context = this;
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
			contacts.getData(getContentResolver(), (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE));
			return null;
		}

	}

	public void populateListView(ArrayList<String> list){
		ArrayAdapter<String> madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
		listofcontacts.setAdapter(madapter);
		for(String s: list){
			Log.d("Main Activity", s);
		}
		
		listofcontacts.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				String number = String.valueOf((String) ((TextView) arg1).getText());
				Log.d("MainActivity", number);
				number.trim();
				callContact(number);
			}

			

		});
		listofcontacts.setOnItemLongClickListener(new OnItemLongClickListener(){

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String number = String.valueOf((String) ((TextView) arg1).getText());
				number.trim();
				addContact("fakku", number);
				return true;
			}

		});
	}
	
	public void contactPressed(View v){
		
		addContact(requestName.getText().toString(), (((Button)((LinearLayout) v.getParent()).getChildAt(0)).getText()).toString());
	}

	protected void addContact(String name, String number) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();

		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, name) // Name of the person
				.build());
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(
						ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
						.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, number) // Number of the person
						.withValue(Phone.TYPE, Phone.TYPE_MOBILE).build()); // Type of mobile number                    
		try
		{
			ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (RemoteException e)
		{ 
			// error

			Toast.makeText(this, "Contact not added", Toast.LENGTH_SHORT).show();
		}
		catch (OperationApplicationException e) 
		{
			// error
			Toast.makeText(this, "Contact not added", Toast.LENGTH_SHORT).show();
		}       

		Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();

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
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("idnumber", ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number().replaceAll("\\D", "")));
			nameValuePairs.add(new BasicNameValuePair("fullname", s.replaceAll("\\s", "")));
			new ExecuteTask().execute(nameValuePairs);


			

		} catch(Exception e){
			Log.e("log_tag", "Error in http connection ", e);
		}
	}

	private class ExecuteTask extends AsyncTask<ArrayList<NameValuePair>, String, String>{

		@Override
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			
			String o = null;
			try {
				o = DataInterface.execute(phpURL, params[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return o;
			//Implement Execute PhP here for above method
		}
		
		protected void onPostExecute(String o){
			String[] numStrings;
			if(o == null){
				//does
				Toast.makeText(context, "No Results", Toast.LENGTH_LONG).show();
				numStrings = new String[0];
			}
			else{
				numStrings = o.split(",");
			}
			/*
				for(int j = 0; j < numStrings.length; j++){
					numStrings[j] = numStrings[j].substring(0, 3) + "-" + numStrings[j].substring(3, 6) + "-" + numStrings[j].substring(6);
				}*/

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
					R.layout.my_list_item, android.R.id.text1, numStrings);

			listofcontacts.setAdapter(adapter);
		}

	}

	public void itemClicked(View v){
		Log.e("Main", "item clicked");
		String number = String.valueOf((String) ((TextView) v).getText());
		Log.d("MainActivity", number);
		number.trim();
		callContact(number);
	}
	
	protected void callContact(String numberString){
		if (!numberString.equals("")) {
			Uri number = Uri.parse("tel:" + numberString);
			Intent dial = new Intent(Intent.ACTION_CALL, number);
			startActivity(dial);
		}
	}

}
