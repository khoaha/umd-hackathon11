package com.example.jamitin;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

public class ContactLookerActivity extends Activity {

	protected ArrayList<ArrayList<Long>> numbers;
	protected ArrayList<String> fullnames;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		numbers = new ArrayList<ArrayList<Long>>();
		fullnames = new ArrayList<String>();
		

		getData();
		for (int i=0; i<numbers.size(); i++)
			System.out.println(fullnames.get(i)+" => "+numbers.get(i));
		System.out.println("Done! "+fullnames.size()+" "+numbers.size());
	}

	protected void getData() {
		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); 
		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor names = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?", new String[] {contactId}, null);
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
					Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null); 
					while (phones.moveToNext())
						n.add(Long.parseLong(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\D", "")));                 
					numbers.add(n);
					phones.close();

				}}
			names.close();
		}
		cursor.close();
	}
}