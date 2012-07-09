package com.rs;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rs.core.Locator;
import com.rs.core.Model;
import com.rs.core.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, LocationListener {
	
	public TextView locationText;
	public Button scanBtn;
	public EditText searchInput;
	public Button searchBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Locator.model = new Model();
        Locator.service = new Service("http://192.168.1.42:8000/recycling/search/");
        Locator.main = this;
        
        int BUTTON_SIZE = 200;
        
        locationText = new TextView(this);
        FrameLayout.LayoutParams locationLP = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        locationLP.gravity = Gravity.TOP|Gravity.LEFT;
        locationLP.setMargins(8, 8, 8, 8);
        locationText.setLayoutParams(locationLP);
        
        //scan btn
        scanBtn = new Button(this);
        FrameLayout.LayoutParams scanLP = new FrameLayout.LayoutParams(BUTTON_SIZE, BUTTON_SIZE);
        scanLP.setMargins(8, 8, 8, 8);
        scanLP.gravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
        //scanLP.weight = 1;
        scanBtn.setLayoutParams(scanLP);
        scanBtn.setText("Scan");
        
        //search layout
        LinearLayout searchLayout = new LinearLayout(this);
        searchLayout.setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout.LayoutParams searchLayoutLP = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        searchLayoutLP.gravity = Gravity.BOTTOM;
        searchLayout.setLayoutParams(searchLayoutLP);
        
        //search input
        searchInput = new EditText(this);
        LinearLayout.LayoutParams inputLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        inputLP.setMargins(8, 8, 8, 8);
        inputLP.weight = 1;
        searchInput.setHint("Type Search");
        searchInput.setLayoutParams(inputLP);
        searchLayout.addView(searchInput);
        
        //search btn
        searchBtn = new Button(this);
        searchBtn.setText("Search");
        LinearLayout.LayoutParams searchLP = new LinearLayout.LayoutParams(BUTTON_SIZE,LayoutParams.WRAP_CONTENT);
        searchLP.setMargins(8, 8, 8, 8);
        searchBtn.setLayoutParams(searchLP);
        searchLayout.addView(searchBtn);
        
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        FrameLayout rootView = new FrameLayout(this);
        rootView.setLayoutParams(lp);
        
        //add views // view z order
        rootView.addView(locationText);
        rootView.addView(scanBtn);
        rootView.addView(searchLayout);
        setContentView(rootView);
        
        //handlers
        searchBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        
        //location services
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if(resultCode == RESULT_OK){
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
			  	Locator.model.searchRequest = scanResult.getContents();
			  	doSearch();
		  }
    	}
	}
    
    public void launchResults(){
	  Intent viewResults = new Intent(this, ResultActivity.class);
	  startActivity(viewResults);
    }
    
	@Override
	public void onClick(View v) {
		Locator.model.searchRequest = searchInput.getText().toString();
		if(v.equals(searchBtn)){
			doSearch();
		}
		
		if(v.equals(scanBtn)){
	        IntentIntegrator integrator = new IntentIntegrator((Activity)v.getContext());
	        integrator.initiateScan();
		}
		
	}
	
	public void doSearch(){
        Locator.service.search(Locator.model.searchRequest, 
				String.valueOf(Locator.model.latitude), 
				String.valueOf(Locator.model.longitude), 
				"50");
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Locator.model.latitude = location.getLatitude();
		Locator.model.longitude = location.getLongitude();
		locationText.setText("latitude "+location.getLatitude()+"\nlongitude "+location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
    
}