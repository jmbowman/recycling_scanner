package com.rs.core;

import com.rs.service.SearchTask;

public class Service {
    
	String baseUrl;
	
	public Service(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
    public void search(String q, String latitude, String longitude, String radius) {
    	
    	SearchTask rt = new SearchTask(q, latitude, longitude, radius);
    	rt.execute(baseUrl);
    	
    }
    
}