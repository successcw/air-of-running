package com.successcw.airofrunning.service;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.successcw.airofrunning.nettool.NetTool;

public class IntentService extends Service{
	
	boolean flag = false;
	public LocalBinder binder = new LocalBinder();
	
	public IntentService() {
		
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	public class LocalBinder extends Binder {
		public IntentService getService() {
			return IntentService.this;
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return false;
	}

	public void parserData() {
		new MyThread().start();
	}
	class MyThread extends Thread {	
		@Override
		public void run() {
			ArrayList<String> WEATHER = null;
			String USAQI = "";
			String SHAQI = "";
			Intent intent = new Intent("com.successcw.airofrunning.entity");
			
			try{
				WEATHER = NetTool.getWeatherFromSina("CHXX0116");
				if (WEATHER == null) {
					intent.putExtra("SHISHITEMPRATURE", "无数据");
					intent.putExtra("AIRCONDITION", "");
					intent.putExtra("TEMPRATURE", "");
					intent.putExtra("WIND", "");
					intent.putExtra("WEATHERICON", "32");
					intent.putExtra("TEMPRATUREUPDATETIME", "");
					intent.putExtra("WEATHERFORECASE", "");
				}else {
					Log.i("IntertService1", WEATHER.get(0));
					intent.putExtra("SHISHITEMPRATURE", WEATHER.get(1));
					intent.putExtra("AIRCONDITION", WEATHER.get(3));
					intent.putExtra("TEMPRATURE", WEATHER.get(5) +"°/" + WEATHER.get(4) + "°");
					intent.putExtra("WIND", WEATHER.get(2));
					intent.putExtra("WEATHERICON", WEATHER.get(6));
					intent.putExtra("TEMPRATUREUPDATETIME", WEATHER.get(0).split(" ")[1]);
					intent.putExtra("WEATHERFORECASE", WEATHER);
				}
				
				USAQI = NetTool.getHtml("http://www.aqicn.info/?json&key=v5&city=Shanghai", "UTF-8");
				if (USAQI == null) {
					intent.putExtra("USAQIVALUE", "无数据");
					intent.putExtra("USAQITIME", "");
					
				}else{
					JSONObject jsonObjRecv = new JSONObject(USAQI);
					Log.i("USAQI",jsonObjRecv.toString());
					intent.putExtra("USAQIVALUE", jsonObjRecv.getString("aqi").toString());
					intent.putExtra("USAQITIME", jsonObjRecv.getJSONObject("time").getString("u").toString());
				}
				
				SHAQI= NetTool.getSHAQI("GetSiteAQIData");
				if (SHAQI == null) {
					intent.putExtra("SHAQILEVEL", "");
					intent.putExtra("SHUPDATEDATE", "");
					intent.putExtra("SHUPDATETIME", "");
					intent.putExtra("SHAQIVALUE", "无数据");			
				}else{
					SHAQI = SHAQI.replace("$", " ").replace("#", " ").replace("*"," ");
					String []SHAQIArray = SHAQI.split(" ");
					intent.putExtra("SHAQILEVEL", SHAQIArray[SHAQIArray.length - 3].toString());
					intent.putExtra("SHUPDATEDATE", SHAQIArray[SHAQIArray.length - 2].toString());
					intent.putExtra("SHUPDATETIME", SHAQIArray[SHAQIArray.length - 1].toString());
					intent.putExtra("SHAQIVALUE", SHAQIArray[1].toString());
					intent.putExtra("SHPM2_5", SHAQIArray[SHAQIArray.length - 6].toString());
					Log.i("IntentService SHPM2_5",SHAQIArray[SHAQIArray.length - 6].toString());
				}
				sendBroadcast(intent);
			}catch(Exception e){
				Log.i("AQI",e.toString());
				intent = new Intent("com.successcw.airofrunning.noNet");
				intent.putExtra("ERRORMSG", e.toString());
				sendBroadcast(intent);
			}

			
			
			super.run();
		}
	}
}
