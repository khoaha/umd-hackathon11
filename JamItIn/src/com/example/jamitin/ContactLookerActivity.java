package com.example.jamitin;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

public class ContactLookerActivity extends Activity {
	
	protected ArrayList<ArrayList<Long>> numbers;
	protected ArrayList<String> first;
	protected ArrayList<String> last;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        numbers = new ArrayList<ArrayList<Long>>();
        first = new ArrayList<String>();
        last = new ArrayList<String>();

        getData();
        for (int i=0; i<numbers.size(); i++)
        	System.out.println(first.get(i)+" "+last.get(i)+" => "+numbers.get(i));
        System.out.println("Done! "+first.size()+" "+numbers.size());
    }
    
    protected void getData() {
    	Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); 
        while (cursor.moveToNext()) {
        	String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        	Cursor names = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?", new String[] {contactId}, null);
        	while (names.moveToNext()) {
        		String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        		if (hasPhone.equals("1")) { 
                	first.add(names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)));
                    last.add(names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)));
                    ArrayList<Long> n = new ArrayList<Long>();
                	Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null); 
                	while (phones.moveToNext())
                		n.add(Long.parseLong(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\D", "")));                 
                	numbers.add(n);
                	phones.close();
                }
        	}
        	names.close();
        }
        cursor.close();
    }
}