package com.successcw.airofrunning.service;

import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.successcw.airofrunning.nettool.NetTool;

public class IntentService extends Service{

	boolean flag = false;
	private int city = 0;
	private int station = 0;
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

	public void parserData(int argCity, int argStation) {
		city = argCity;
		station = argStation;
		//Log.i("parserData",Integer.toString(city));
		//Log.i("parserData",Integer.toString(station));
		new MyThread().start();
	}
	class MyThread extends Thread {	
		@Override
		public void run() {
			
			String message = "";
			Intent intentStart = new Intent("com.successcw.airofrunning.refresh");
			sendBroadcast(intentStart);
			//Log.i("service","send refresh");
			Intent intent = new Intent("com.successcw.airofrunning.entity");

			intent.putExtra("CITY_SETTING",city);
			intent.putExtra("STATION_SETTING",station);
			
			try{
				message = NetTool.getHtml("http://airofrunning-serverv13.cloudfoundry.com/?city=" + city + "&station=" + station, "UTF-8");
				//message = NetTool.getHtml("http://airofrunning-serverv13.cloudfoundry.com", "UTF-8");
				JSONObject jsonObjRecv = new JSONObject(message);

				//Log.i("USAQI",jsonObjRecv.toString());
				intent.putExtra("SHISHITEMPRATURE", jsonObjRecv.getString("SHISHITEMPRATURE").toString());
				intent.putExtra("AIRCONDITION", jsonObjRecv.getString("AIRCONDITION").toString());
				intent.putExtra("TEMPRATURE", jsonObjRecv.getString("TEMPRATURE").toString());
				intent.putExtra("WIND", jsonObjRecv.getString("WIND").toString());
				intent.putExtra("WEATHERICON", jsonObjRecv.getString("WEATHERICON").toString());
				intent.putExtra("TEMPRATUREUPDATETIME", jsonObjRecv.getString("TEMPRATUREUPDATETIME").toString());
				intent.putExtra("WEATHERFORECASE", jsonObjRecv.getString("WEATHERFORECASE").toString());
				
				if(city == 1 || city == 20 || city == 54 || city == 66) { //beijing,shanghai,guangzhou,chengdu
					intent.putExtra("USAQIVALUE", jsonObjRecv.getString("USAQIVALUE").toString());
					intent.putExtra("USAQITIME", jsonObjRecv.getString("USAQITIME").toString());
				}
				else {
					intent.putExtra("USAQIVALUE", jsonObjRecv.getString("averageAQI").toString());
					intent.putExtra("USAQITIME", jsonObjRecv.getString("time").toString());
				}
				intent.putExtra("SHAQILEVEL", jsonObjRecv.getString("quality").toString());
				intent.putExtra("SHUPDATEDATE", jsonObjRecv.getString("time").toString());
				intent.putExtra("SHUPDATETIME", jsonObjRecv.getString("time").toString());
				intent.putExtra("SHAQIVALUE", jsonObjRecv.getString("AQI").toString());
				intent.putExtra("SHPM2_5", jsonObjRecv.getString("PM2_5").toString());
				intent.putExtra("CITYAREA", jsonObjRecv.getString("area").toString());
				intent.putExtra("STATION", jsonObjRecv.getString("station").toString());

				message = NetTool.getHtml("http://airofrunning-serverv13.cloudfoundry.com/?checkNewVersion=1", "UTF-8");
				jsonObjRecv = new JSONObject(message);
	            //Log.i("versioncode",jsonObjRecv.toString());
	            intent.putExtra("VERSIONCODE", jsonObjRecv.getString("versionCode").toString());
	            intent.putExtra("VERSIONNAME", jsonObjRecv.getString("versionName").toString());
	            intent.putExtra("VERSIONCONTENT", jsonObjRecv.getString("contents").toString());
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
