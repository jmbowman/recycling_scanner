package com.rs.view;

import java.util.List;

import com.rs.vo.Result;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ResultAdapter extends BaseAdapter {
	
	    private Context mContext;
	    
	    private List<Result> data;
	    
	    public ResultAdapter(Context c) {
	        mContext = c;
	    }
	    
	    public void setData(List<Result> data){
	    	this.data = data;
	    }
	    
	    public int getCount() {
	        return data.size();
	    }
	    
	    public Object getItem(int position) {
	        return data.get(position);
	    }
	    
	    public long getItemId(int position) {
	        return -1;
	    }
	    
	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        
	    	TextView textView;
	        
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	textView = new TextView(mContext);
	            textView.setLayoutParams(new ListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	            textView.setPadding(8, 8, 8, 8);
	        } else {
	            textView = (TextView) convertView;
	        }
	        Result item = data.get(position);
	        
	        String str = "Name: "+item.name+"\n"+"Distance: "+item.distance+"\n"+"Address: "+item.address;
	        textView.setText(str);
	        return textView;
	    }
	    
}
