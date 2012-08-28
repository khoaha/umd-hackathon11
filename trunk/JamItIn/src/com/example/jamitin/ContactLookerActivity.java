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
import android.telephony.TelephonyManager;
import android.util.Log;

public class ContactLookerActivity {

	protected final String phpURL = "http://zaphodbeeblebrox.student.umd.edu/jamitin.php";	

	/** Called when the activity is first created. */
	
	public ContactLookerActivity() {		
	}

	protected void getData(ContentResolver content, TelephonyManager tMgr) {
		ArrayList<ArrayList<Long>> numbers = new ArrayList<ArrayList<Long>>();
		ArrayList<String> fullnames = new ArrayList<String>();
		
		Cursor cursor = content.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); 
		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor names = content.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?", new String[] {contactId}, null);
			String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhone.equals("1")){
				while (names.moveToNext()) {
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
		
		// delete dupes
		for (int i=1; i<numbers.size(); i++) {
			boolean flag = false;
			ArrayList<Long> a = numbers.get(i-1);
			ArrayList<Long> b = numbers.get(i);
			if (a.size() != b.size())
				continue;
			for (int n=0; n<a.size(); n++) {
				if (!a.get(n).equals(b.get(n))) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				numbers.remove(i);
				fullnames.remove(i);
				i--;
			}
		}
		
		for (int i=0; i<numbers.size(); i++)
			System.out.println(fullnames.get(i)+" => "+numbers.get(i));
		System.out.println("Done! "+fullnames.size()+" "+numbers.size());
		
		//http post
		try{
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			String numbersString = "";
			for (int i=0; i<numbers.size(); i++) {
				ArrayList<Long> q = numbers.get(i);
				for (int n=0; n<q.size(); n++) {
					if (n > 0)
						numbersString += ",";
					numbersString += q.get(n);
				}
				numbersString += " ";
			}
			String fullnamesString = "";
			for (int i=0; i<fullnames.size(); i++) {
				if (i > 0)
					fullnamesString += ",";
				fullnamesString += fullnames.get(i);
			}
			nameValuePairs.add(new BasicNameValuePair("idnumber", tMgr.getLine1Number().replaceAll("\\D", "")));
			nameValuePairs.add(new BasicNameValuePair("numbers", numbersString));
			nameValuePairs.add(new BasicNameValuePair("fullnames", ""+fullnamesString));
			DataInterface.execute(phpURL, nameValuePairs);

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection ", e);
		}
		Log.d("Tag", "Done Json");
	}
	
}