package com.successcw.airofrunning.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.successcw.airofrunning.service.IntentService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;

public class StationActivity extends Activity{
	private Spinner provinceSpinner;
	private Spinner citySpinner;
	private ListView stationList;
	private JSONArray province;
	private JSONArray city;
	private JSONArray station;
	private int argCity = 0;
	private int argStation = 0;
	private Intent service = null;
	private IntentService AirOfRunningService;
	private int provinceDifferent = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.station);
        initView();
        ((ScrollView)findViewById(R.id.station_scrollview)).smoothScrollTo(0,20);
    }
	private ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			AirOfRunningService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			AirOfRunningService = ((IntentService.LocalBinder) service)
					.getService();
			AirOfRunningService.parserData(argCity,argStation);
		}
	};
    private void initView() {
    	
    	provinceSpinner = (Spinner)findViewById(R.id.province_spinner);
    	citySpinner = (Spinner)findViewById(R.id.city_spinner);
    	stationList = (ListView)findViewById(R.id.station_list);
    	int tempProvince = 0;
    	int tempCity = 0;

    	try {
	    	InputStream in= this.getResources().openRawResource(R.raw.station);
	    
	    	BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8")); 
	    	StringBuilder responseStrBuilder = new StringBuilder();
	    	String inputStr;
	    	while ((inputStr = streamReader.readLine()) != null)
	    		responseStrBuilder.append(inputStr);
	  
	    	JSONObject json = new JSONObject(responseStrBuilder.toString());
	    	//init province
	    	province = json.getJSONArray("provinces");
	    	String[] provinceItems = new String[province.length()];
    	
	    	for(int i = 0; i < province.length(); i++)
	    	{
	    		//Log.e("provinces", province.getJSONObject(i).getString("name"));
	    		provinceItems[i] = province.getJSONObject(i).getString("name");
	    	}
	    	ArrayAdapter provinceAdapter = 
	                new ArrayAdapter(this, android.R.layout.simple_spinner_item, provinceItems);       
	    	provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        provinceSpinner.setAdapter(provinceAdapter);
	        provinceSpinner.setOnItemSelectedListener(new provinceSpinnerSelectedListener());  
	        
    	}catch(JSONException e)        {
            Log.e("log_tag", "Error parsing data "+e.toString());
          }
    	catch(IOException e) {
    		Log.e("log_tag", "Error open" + e.toString());
    	}	    
        
    	tempProvince = getPreferences(MODE_PRIVATE).getInt("PROVINCE_SETTING",0);
    	tempCity = Integer.valueOf(getPreferences(MODE_PRIVATE).getString("CITY_SETTING","0"));
    	Log.i("tempProvince", String.valueOf(tempProvince));
    	Log.i("tempCity",String.valueOf(tempCity));
        provinceSpinner.setSelection(tempProvince);
        //init city
        setCitySpinner(tempProvince);
        citySpinner.setOnItemSelectedListener(new citySpinnerSelectedListener());    
        citySpinner.setSelection(tempCity);
        //init station
        setStationList(0);
        stationList.setOnItemClickListener(new ListClickListener());
    	
    }

    public void finishActivity() {
    	this.finish();
		service = new Intent("com.successcw.airofrunning.intentservice");
		startService(service);
		bindService(service, conn, BIND_AUTO_CREATE);
    }
    
    public class ListClickListener implements OnItemClickListener {
    	   public void onItemClick(AdapterView<?> parent, View view,   
                   int position, long id) {
    		   Log.i("stationList clicked",Long.toString(id));
    		   argStation=(int)id;
    		   getPreferences(MODE_PRIVATE).edit().putString("STATION_SETTING",String.valueOf(id)).commit();
    		   finishActivity();
    	   }   
    }
 
    public class provinceSpinnerSelectedListener implements OnItemSelectedListener{  
  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {
        	String temp = "0";
        	if(arg0 == provinceSpinner) {
        		
        		Log.i("provinceSpinner clicked",Integer.toString(arg2));
        		if(Integer.valueOf(getPreferences(MODE_PRIVATE).getInt("PROVINCE_SETTING",0)) == arg2){
        			provinceDifferent = 0;
        		}
        		else
        			provinceDifferent = 1;
        		getPreferences(MODE_PRIVATE).edit().putInt("PROVINCE_SETTING",arg2).commit();
        		setCitySpinner(arg2);
        		
//        		if(provinceDifferent == 1) {
//	        		try {
//	        			temp = city.getJSONObject(0).getString("id");
//	        			//Log.i("id=",temp);
//	        		}catch(JSONException e){
//	         			Log.e("log_tag", "Error parsing data "+e.toString());
//	         		}
//	        		if(temp != "0") {
//	        			argCity = Integer.valueOf(temp);
//	        			getPreferences(MODE_PRIVATE).edit().putString("CITY_SETTING","0").commit();
//	        		}
//	        		Log.i("argCity=",Integer.toString(argCity));
//        		}
        		setStationList(0);
        		argStation = 0;
        	}
            
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    }
    public class citySpinnerSelectedListener implements OnItemSelectedListener{  
    	  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {
        	String temp = "0";
        	if(arg0 == citySpinner) {    		
				Log.i("citySpinner clicked",Integer.toString(arg2));
				setStationList(arg2);
				try {
        			temp = city.getJSONObject(arg2).getString("id");
        			Log.i("id=",temp);
        		}catch(JSONException e){
         			Log.e("log_tag", "Error parsing data "+e.toString());
         		}
        		if(temp != "0") {
        			argCity = Integer.valueOf(temp);
        			getPreferences(MODE_PRIVATE).edit().putString("CITY_SETTING",String.valueOf(arg2)).commit();
        		}
        		argStation = 0;
        		//Log.i("argCity=",Integer.toString(argCity));
        	}
            
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    } 
    private void setCitySpinner(int item) {
    	try {
	        //init city
	    	city = province.getJSONObject(item).getJSONArray("cities");
	    	String[] cityItems = new String[city.length()];
		
	    	for(int i = 0; i < city.length(); i++)
	    	{
	    		//Log.e("city", city.getJSONObject(i).getString("name"));
	    		cityItems[i] = city.getJSONObject(i).getString("name");
	    	}
	    	ArrayAdapter cityAdapter = 
	                new ArrayAdapter(this, android.R.layout.simple_spinner_item, cityItems);       
	    	cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        citySpinner.setAdapter(cityAdapter);
	        if(provinceDifferent == 1) {
	        	
	        }
	        else {
	        	citySpinner.setSelection(Integer.valueOf(getPreferences(MODE_PRIVATE).getString("CITY_SETTING","0")));
	        }
	        
       	}catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
       	}
        
    }
    private void setStationList(int item) {
    	try {
	        //init station
	    	station = city.getJSONObject(item).getJSONArray("stations");
	    	String[] stationItems = new String[station.length()];
    	
	    	for(int i = 0; i < station.length(); i++)
	    	{
	    		//Log.e("station", station.getJSONObject(i).getString("name"));
	    		stationItems[i] = station.getJSONObject(i).getString("name");
	    	}
	    	ArrayAdapter stationAdapter = 
	                new ArrayAdapter(this, android.R.layout.simple_list_item_1, stationItems);       
	    	stationList.setAdapter(stationAdapter);


	        int totalHeight = 0; 
	        for (int i = 0; i < stationAdapter.getCount(); i++) { 
	            View listItem = stationAdapter.getView(i, null, stationList); 
	            listItem.measure(0, 0); 
	            totalHeight += listItem.getMeasuredHeight(); 
	        } 

	        ViewGroup.LayoutParams params = stationList.getLayoutParams(); 
	        params.height = totalHeight + (stationList.getDividerHeight() * (stationList.getCount() - 1)); 
	        ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
	        stationList.setLayoutParams(params); 
       	}catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
       	}
        
    }
    
	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("onStart()");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("onStop()");
	}

	@Override
	protected void onDestroy() {
		if(service != null) {
			stopService(service);
			unbindService(conn);
		}
		super.onDestroy();
	}

}
