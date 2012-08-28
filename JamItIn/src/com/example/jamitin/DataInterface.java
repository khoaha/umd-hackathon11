package com.example.jamitin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

/**
 * Provides a method to communicate with a PHP script and return a JSONArray.
 */
public class DataInterface {

	/**
	 * Connect to the provided url with nameValuePairs and return the resulting JSONArray.
	 */
	public static JSONArray execute(String url, ArrayList<NameValuePair> nameValuePairs) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();

		//convert response to string
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null)
			sb.append(line + "\n");
		is.close();
		String result = sb.toString();

		// populate the data
		if (result.length() > 0)
			return new JSONArray(result);
		else
			return null;
	}

}
