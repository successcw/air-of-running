package com.successcw.airofrunning.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StationActivity extends Activity{
	private Spinner province_spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.station);
        initView();
    }
    private void initView() {
    	
    	province_spinner = (Spinner)findViewById(R.id.province_spinner);
    	try {
    	InputStream in= this.getResources().openRawResource(R.raw.station);
    
    	BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8")); 
    	StringBuilder responseStrBuilder = new StringBuilder();
    	String inputStr;
    	while ((inputStr = streamReader.readLine()) != null)
    		responseStrBuilder.append(inputStr);
    	JSONObject json = new JSONObject(responseStrBuilder.toString());
    	JSONArray province = json.getJSONArray("provinces");
    	String[] items = new String[province.length()];
    	
    	for(int i = 0; i < province.length(); i++)
    	{
    		Log.e("Station", province.getJSONObject(i).getString("name"));
    		items[i]=province.getJSONObject(i).getString("name");
    	}
    	ArrayAdapter adapter = 
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, items);       
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province_spinner.setAdapter(adapter);
    	
    	}catch(JSONException e)        {
            Log.e("log_tag", "Error parsing data "+e.toString());
          }
    	catch(IOException e) {
    		Log.e("log_tag", "Error open" + e.toString());
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
		super.onDestroy();
	}

}
