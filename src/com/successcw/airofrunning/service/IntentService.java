package com.successcw.airofrunning.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.successcw.airofrunning.nettool.NetTool;

public class IntentService extends Service{
	private String[] cityWeather = {
			"CHXX0008",
			"CHXX0133",
			"CHXX0122",
			"WMXX1038",
			"WMXX1037",
			"CHXX0266",
			"CHXX0308",
			"CHXX0302",
			"WMXX1041",
			"CHXX0300",
			"WMXX1039",
			"CHXX0131",
			"WMXX1040",
			"CHXX0129",
			"CHXX0249",
			"CHXX0119",
			"CHXX0019",
			"CHXX0010",
			"CHXX0046",
			"CHXX0116",
			"CHXX0099",
			"WMXX1009",
			"CHXX0437",
			"CHXX0015",
			"WMXX1007",
			"CHXX0101",
			"WMXX1012",
			"WMXX1011",
			"WMXX1014",
			"WMXX1013",
			"CHXX0166",
			"WMXX1008",
			"WMXX1010",
			"CHXX0044",
			"WMXX1016",
			"CHXX0462",
			"CHXX0062",
			"CHXX0056",
			"CHXX0460",
			"CHXX0455",
			"WMXX1263",
			"CHXX0461",
			"CHXX0117",
			"WMXX1017",
			"CHXX0448",
			"CHXX0031",
			"CHXX0140",
			"CHXX0097",
			"CHXX0064",
			"CHXX0110",
			"CHXX0165",
			"CHXX0138",
			"CHXX0013",
			"CHXX0037",
			"CHXX0120",
			"WMXX1000",
			"CHXX0028",
			"CHXX0058",
			"WMXX1003",
			"CHXX0053",
			"CHXX0123",
			"WMXX1002",
			"CHXX0100",
			"CHXX0502",
			"CHXX0017",
			"CHXX0016",
			"CHXX0039",
			"CHXX0076",
			"CHXX0080",
			"CHXX0141",
			"CHXX0079",
			"CHXX0236",
			"CHXX0259",
			"CHXX0135"
		};
	private String[] SHStation = {
			"上海平均",
			"普陀监测站",
			"杨浦四漂",
			"卢湾师专附小",
			"青浦淀山湖",
			"虹口凉城",
			"静安监测站",
			"徐汇上师大",
			"浦东川沙",
			"浦东监测站",
			"浦东张江"
	};
	private int[] SHStationValue = {
			0,
			201,
			209,
			185,
			203,
			215,
			183,
			207,
			193,
			228,
			195
	};
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
		Log.i("parserData",Integer.toString(city));
		Log.i("parserData",Integer.toString(station));
		new MyThread().start();
	}
	class MyThread extends Thread {	
		@Override
		public void run() {
			ArrayList<String> WEATHER = null;
			String USAQI = "";
			String SHAQI = "";
			Intent intentStart = new Intent("com.successcw.airofrunning.refresh");
			sendBroadcast(intentStart);
			Log.i("service","send refresh");
			Intent intent = new Intent("com.successcw.airofrunning.entity");

			intent.putExtra("CITY_SETTING",city);
			intent.putExtra("STATION_SETTING",station);
			
			try{
				
				WEATHER = NetTool.getWeatherFromSina(cityWeather[city-1]);
				if (WEATHER == null) {
					intent.putExtra("SHISHITEMPRATURE", "无数据");
					intent.putExtra("AIRCONDITION", "");
					intent.putExtra("TEMPRATURE", "");
					intent.putExtra("WIND", "");
					intent.putExtra("WEATHERICON", "32");
					intent.putExtra("TEMPRATUREUPDATETIME", "");
					intent.putExtra("WEATHERFORECASE", "");
				}else {
					//Log.i("IntertService1", WEATHER.get(0));
					intent.putExtra("SHISHITEMPRATURE", WEATHER.get(1));
					intent.putExtra("AIRCONDITION", WEATHER.get(3));
					intent.putExtra("TEMPRATURE", WEATHER.get(5) +"°/" + WEATHER.get(4) + "°");
					intent.putExtra("WIND", WEATHER.get(2));
					intent.putExtra("WEATHERICON", WEATHER.get(6));
					intent.putExtra("TEMPRATUREUPDATETIME", WEATHER.get(0).split(" ")[1]);
					intent.putExtra("WEATHERFORECASE", WEATHER);
				}
				if(city == 20) { //shanghai		
					USAQI = NetTool.getHtml("http://www.aqicn.info/?json&key=v5&city=Shanghai", "UTF-8");
					if (USAQI == null) {
						intent.putExtra("USAQIVALUE", "无数据");
						intent.putExtra("USAQITIME", "");
						intent.putExtra("CITYAREA", " ");
						
					}else{
						JSONObject jsonObjRecv = new JSONObject(USAQI);
						Log.i("USAQI",jsonObjRecv.toString());
						intent.putExtra("USAQIVALUE", jsonObjRecv.getString("aqi").toString());
						intent.putExtra("USAQITIME", jsonObjRecv.getJSONObject("time").getString("u").split(" ")[1]);
						intent.putExtra("CITYAREA", "上海");
					}
					
					SHAQI= NetTool.getSHAQI("GetSiteAQIData",SHStationValue[station]);
					if (SHAQI == null) {
						intent.putExtra("SHAQILEVEL", "");
						intent.putExtra("SHUPDATEDATE", "");
						intent.putExtra("SHUPDATETIME", "");
						intent.putExtra("SHAQIVALUE", "无数据");
						intent.putExtra("STATION", " ");
					}else{
						SHAQI = SHAQI.replace("$", " ").replace("#", " ").replace("*"," ");
						String []SHAQIArray = SHAQI.split(" ");
						intent.putExtra("SHAQILEVEL", SHAQIArray[SHAQIArray.length - 3].toString());
						intent.putExtra("SHUPDATEDATE", SHAQIArray[SHAQIArray.length - 2].toString());
						intent.putExtra("SHUPDATETIME", SHAQIArray[SHAQIArray.length - 1].toString());
						intent.putExtra("SHAQIVALUE", SHAQIArray[1].toString());
						intent.putExtra("SHPM2_5", SHAQIArray[SHAQIArray.length - 6].toString());
						intent.putExtra("STATION", SHStation[station]);
						//Log.i("IntentService SHPM2_5",SHAQIArray[SHAQIArray.length - 6].toString());
					}
				} else if(city == 1 || city == 54 || city == 66) { //beijing,guangzhou,chengdu
					switch(city){
					case 1:
						USAQI = NetTool.getHtml("http://www.aqicn.info/?json&key=v5&city=beijing", "UTF-8");
						intent.putExtra("CITYAREA", "北京");
						break;
					case 54:
						USAQI = NetTool.getHtml("http://www.aqicn.info/?json&key=v5&city=guangzhou", "UTF-8");
						intent.putExtra("CITYAREA", "广州");
						break;
					case 66:
						USAQI = NetTool.getHtml("http://www.aqicn.info/?json&key=v5&city=chengdu", "UTF-8");
						intent.putExtra("CITYAREA", "成都");
						break;
					}
					
					if (USAQI == null) {
						intent.putExtra("USAQIVALUE", "无数据");
						intent.putExtra("USAQITIME", "");
						intent.putExtra("CITYAREA", " ");
						
					}else{
						JSONObject jsonObjRecv = new JSONObject(USAQI);
						Log.i("USAQI",jsonObjRecv.toString());
						intent.putExtra("USAQIVALUE", jsonObjRecv.getString("aqi").toString());
						intent.putExtra("USAQITIME", jsonObjRecv.getJSONObject("time").getString("u").split(" ")[1]);
					}
					
					SHAQI = NetTool.getHtml("http://airofrunning-server.cloudfoundry.com?city=" + city + "&station=" + station, "UTF-8");
					//Log.i("URL","http://airofrunning-server.cloudfoundry.com?city=" + city + "&station=" + station);
					Log.i("USQAI",SHAQI);
					if (SHAQI == null) {
						intent.putExtra("SHAQILEVEL", "");
						intent.putExtra("SHUPDATEDATE", "");
						intent.putExtra("SHUPDATETIME", "");
						intent.putExtra("SHAQIVALUE", "无数据");	
						intent.putExtra("CITYAREA", "");
						intent.putExtra("STATION", "无数据");					
					}else{
						JSONObject jsonObjRecv = new JSONObject(SHAQI);
						Log.i("USAQI",jsonObjRecv.toString());						
						intent.putExtra("SHAQILEVEL", jsonObjRecv.getString("quality").toString());
						
						if(city == 1) {
							intent.putExtra("SHUPDATEDATE", jsonObjRecv.getString("time").toString().split(" ")[0]);
							intent.putExtra("SHUPDATETIME", jsonObjRecv.getString("time").toString().split(" ")[1]);
						} else {
							intent.putExtra("SHUPDATEDATE", jsonObjRecv.getString("time").toString().split("T")[0]);
							intent.putExtra("SHUPDATETIME", jsonObjRecv.getString("time").toString().split("T")[1]);
						}
						intent.putExtra("SHAQIVALUE", jsonObjRecv.getString("AQI").toString());
						intent.putExtra("SHPM2_5", jsonObjRecv.getString("PM2_5").toString());
						intent.putExtra("CITYAREA", jsonObjRecv.getString("area").toString());
						intent.putExtra("STATION", jsonObjRecv.getString("station").toString());

					}
				} else { //the other cities
					USAQI = NetTool.getHtml("http://airofrunning-server.cloudfoundry.com?city=" + city + "&station=" + station, "UTF-8");
					Log.i("URL","http://airofrunning-server.cloudfoundry.com?city=" + city + "&station=" + station);
					Log.i("USQAI",USAQI);
					if (USAQI == null) {
						intent.putExtra("USAQIVALUE", "无数据");
						intent.putExtra("USAQITIME", "");
						intent.putExtra("SHAQILEVEL", "");
						intent.putExtra("SHUPDATEDATE", "");
						intent.putExtra("SHUPDATETIME", "");
						intent.putExtra("SHAQIVALUE", "无数据");	
						intent.putExtra("CITYAREA", "");
						intent.putExtra("STATION", "无数据");

						
					}else{
						JSONObject jsonObjRecv = new JSONObject(USAQI);
						Log.i("USAQI",jsonObjRecv.toString());
						intent.putExtra("USAQIVALUE", jsonObjRecv.getString("averageAQI").toString());
						intent.putExtra("USAQITIME", jsonObjRecv.getString("time").toString().split("T")[1]);
						
						intent.putExtra("SHAQILEVEL", jsonObjRecv.getString("quality").toString());
						intent.putExtra("SHUPDATEDATE", jsonObjRecv.getString("time").toString().split("T")[0]);
						intent.putExtra("SHUPDATETIME", jsonObjRecv.getString("time").toString().split("T")[1]);
						intent.putExtra("SHAQIVALUE", jsonObjRecv.getString("AQI").toString());
						intent.putExtra("SHPM2_5", jsonObjRecv.getString("PM2_5").toString());
						intent.putExtra("CITYAREA", jsonObjRecv.getString("area").toString());
						intent.putExtra("STATION", jsonObjRecv.getString("station").toString());

					}
					
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
