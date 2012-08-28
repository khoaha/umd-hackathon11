package com.example.jamitin;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactLookerActivity {

	protected final String phpURL = "http://zaphodbeeblebrox.student.umd.edu/test.php";	

	/** Called when the activity is first created. */
	
	public ContactLookerActivity() {		
	}

	protected void getData(ContentResolver content) {
		ArrayList<ArrayList<Long>> numbers = new ArrayList<ArrayList<Long>>();
		ArrayList<String> fullnames = new ArrayList<String>();
		
		Cursor cursor = content.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); 
		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor names = content.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?", new String[] {contactId}, null);
			String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhone.equals("1")){
				while (names.moveToNext()) {
					//String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					String firstField = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
					String secondField = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
					if(firstField == null)
						continue;
					else if(firstField.matches("\\d"))
						continue;
					else if(secondField == null){
						fullnames.add(firstField.replaceAll("\\s", ""));
					}
					else{
						fullnames.add((firstField+""+secondField).replaceAll("\\s", ""));
					}
					
					ArrayList<Long> n = new ArrayList<Long>();
					Cursor phones = content.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null); 
					while (phones.moveToNext())
						n.add(Long.parseLong(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\D", "")));                 
					numbers.add(n);
					phones.close();

				}}
			names.close();
		}
		cursor.close();
		
		for (int i=0; i<numbers.size(); i++)
			System.out.println(fullnames.get(i)+" => "+numbers.get(i));
		System.out.println("Done! "+fullnames.size()+" "+numbers.size());
		
		//http post
		try{
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			String numbersString = "";
			for (int i=0; i<numbers.size(); i++) {
				for (int n=0; n<numbers.size(); n++) {
					if (n > 0)
						numbersString += ",";
					numbersString += numbers.get(n);
				}
				numbersString += " ";
			}
			String fullnamesString = "";
			for (int i=0; i<fullnames.size(); i++) {
				if (i > 0)
					fullnamesString += ",";
				fullnamesString += numbers.get(i);
			}
			nameValuePairs.add(new BasicNameValuePair("numbers", numbersString));
			nameValuePairs.add(new BasicNameValuePair("fullnames", ""+fullnamesString));
			JSONArray jArray = DataInterface.execute(phpURL, nameValuePairs);

			//parse json data
			try{
				// populate the orders
				for(int i=0; i<jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					String o = json_data.getString("output");
					System.out.println(o);
				}
			}catch(JSONException e2){
				Log.e("log_tag", "Error parsing data "+e2.toString());
			}
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
}