package com.successcw.airofrunning.service;

import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.successcw.airofrunning.nettool.NetTool;

public class IntentService extends Service{
	
	XMLGettersSetters data;
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
			String WEATHER = "";
			String USAQI = "";
			String SHAQI = "";
			
			try{
				WEATHER = NetTool.getWeather("…œ∫£");
				String []WEATHERArray = WEATHER.split(";");
				Log.i("IntertService1", WEATHERArray[4].split(" ")[2]);
				Log.i("IntertService2", WEATHERArray[5].split("=")[1]);
				Log.i("IntertService3", WEATHERArray[6].split(" ")[2]);
				Log.i("IntertService4", WEATHERArray[8].trim().split("=")[1].replace(".", " ").split(" ")[0]);
				
				Log.i("IntertService5", WEATHERArray[10].split("£∫")[2].split("£ª")[0].replace("°Ê", "°„"));
				Log.i("IntertService6", WEATHERArray[10].split("£∫")[3].split("£ª")[0]);

				USAQI = NetTool.getHtml("http://www.aqicn.info/?json&key=v5&city=Shanghai", "UTF-8");
				JSONObject jsonObjRecv = new JSONObject(USAQI);
				
				SHAQI= NetTool.getSHAQI("GetSiteAQIData").replace("$", " ").replace("#", " ").replace("*"," ");
				String []SHAQIArray = SHAQI.split(" ");
				
				Intent intent = new Intent("com.successcw.airofrunning.entity");
				Log.i("USAQI",jsonObjRecv.toString());
				intent.putExtra("USAQIVALUE", jsonObjRecv.getString("aqi").toString());
				intent.putExtra("USAQITIME", jsonObjRecv.getJSONObject("time").getString("u").toString());
				intent.putExtra("SHAQILEVEL", SHAQIArray[SHAQIArray.length - 3].toString());
				intent.putExtra("SHUPDATEDATE", SHAQIArray[SHAQIArray.length - 2].toString());
				intent.putExtra("SHUPDATETIME", SHAQIArray[SHAQIArray.length - 1].toString());
				intent.putExtra("SHAQIVALUE", SHAQIArray[1].toString());
				intent.putExtra("SHISHITEMPRATURE", WEATHERArray[10].split("£∫")[2].split("£ª")[0].replace("°Ê", "°„"));
				intent.putExtra("AIRCONDITION", WEATHERArray[6].split(" ")[2]);
				intent.putExtra("TEMPRATURE", WEATHERArray[5].split("=")[1]);
				intent.putExtra("WIND", WEATHERArray[10].split("£∫")[3].split("£ª")[0]);
				intent.putExtra("WEATHERICON", WEATHERArray[8].trim().split("=")[1].replace(".", " ").split(" ")[0]);
				intent.putExtra("TEMPRATUREUPDATETIME", WEATHERArray[4].split(" ")[2]);
				
				sendBroadcast(intent);
			}catch(Exception e){
				Log.i("AQI",e.toString());
				Intent intent = new Intent("com.successcw.airofrunning.noNet");
				intent.putExtra("ERRORMSG", e.toString());
				sendBroadcast(intent);
			}

			
			
			super.run();
		}
	}
}
