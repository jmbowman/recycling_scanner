package com.rs.vo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {
    
    public List<Result> results;
    
    @SerializedName("page_count")
    int page_count;
    
}