package com.rs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.rs.core.Locator;
import com.rs.core.Model;
import com.rs.vo.Result;
import com.rs.vo.SearchResponse;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SearchTask extends AsyncTask<String, String, List<Result>>{

	protected String q;
	protected String latitude;
	protected String longitude;
	protected String radius;
	
	public SearchTask(String q, String latitude, String longitude, String radius) {
		this.q = q;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}
	
    @Override
    protected List<Result> doInBackground(String... uri) {
        String url = uri[0];
        
        HttpClient httpclient = new DefaultHttpClient();
        
        List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("q", q));
        nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
        nameValuePairs.add(new BasicNameValuePair("radius", radius));
        
        if(!url.endsWith("?")) url += "?";
        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        url += paramString;
        
        HttpGet httpGet = new HttpGet(url);
        
        HttpResponse response;
        List<Result> result = null;
        
        try {
            response = httpclient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(response.getEntity().getContent());
                SearchResponse searchResponse = gson.fromJson(reader, SearchResponse.class);
                result = searchResponse.results;
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        
        return result;
    }
    
    @Override
    protected void onPostExecute(List<Result> results) {
        super.onPostExecute(results);
        
        Locator.model.searchResults = results;
        Locator.main.launchResults();
        
        /*
        for (Result result : results) {
            Toast.makeText(context, result.fromUser, Toast.LENGTH_SHORT).show();
        }
        */
        
    }
}
