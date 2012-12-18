package com.successcw.airofrunning;

import java.util.ArrayList;

import android.util.Log;

/**
 *  This class contains all getter and setter methods to set and retrieve data.
 *  
 **/
public class XMLGettersSetters {

	private ArrayList<String> description = new ArrayList<String>();


	public ArrayList<String> getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description.add(description);
		Log.i("This is the description", description);
	}
}
