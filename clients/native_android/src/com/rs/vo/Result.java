package com.rs.vo;

import com.google.gson.annotations.SerializedName;

//{"page_count": 1, "results": [{"url": "http://www.cambridgema.gov/theworks/ourservices/recyclingandtrash/faqrecyclingandrubbish/curbsiderecyclingfaq.aspx", "distance": null, "name": "City of Cambridge Curbside Recycling Program"}]}

public class Result {
    
	@SerializedName("name")
    public String name;
	
	@SerializedName("distance")
	public String distance;
	
	@SerializedName("url")
	public String url;
	
	@SerializedName("address")
	public String address;
	
}