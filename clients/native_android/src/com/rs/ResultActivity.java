package com.rs;

import com.rs.core.Locator;
import com.rs.core.Model;
import com.rs.view.ResultAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ResultActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        TextView resultDetails = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(8, 8, 8, 8);
        resultDetails.setLayoutParams(lp);
        resultDetails.setText(Locator.model.searchRequest);
        
        ListView listview = new ListView(this);
        ResultAdapter adapter = new ResultAdapter(this);
        adapter.setData(Locator.model.searchResults);
        listview.setAdapter(adapter);
        listview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        ll.addView(resultDetails);
        ll.addView(listview);
        setContentView(ll);
        
    }
    
}