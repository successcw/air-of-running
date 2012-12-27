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

// this method sometimes can't work because http://www.beijingaqifeed.com/shanghaiaqi/shanghaiairrss.xml is not stable.
//			try {	
//				/**
//				 * Create a new instance of the SAX parser
//				 **/
//				SAXParserFactory saxPF = SAXParserFactory.newInstance();
//				SAXParser saxP = saxPF.newSAXParser();
//				XMLReader xmlR = saxP.getXMLReader();
//
//				
//				URL url = new URL("http://www.beijingaqifeed.com/shanghaiaqi/shanghaiairrss.xml"); // URL of the XML
//				
//				/** 
//				 * Create the Handler to handle each of the XML tags. 
//				 **/
//				XMLHandler myXMLHandler = new XMLHandler();
//				xmlR.setContentHandler(myXMLHandler);
//				xmlR.parse(new InputSource(url.openStream()));
//				flag = true;
//				
//			} catch (Exception e) {
//				System.out.println(e);
//				Intent intent = new Intent("com.successcw.airofrunning.noNet");
//				sendBroadcast(intent);
//				Log.e(ACTIVITY_SERVICE, "noNet");
//			}
//
//			if(flag) {
//				data = XMLHandler.data;
//				byte[] USAQIImageData = null;
//				
//				//获取美国上海领事馆AQI图片
//				String path = "http://www.beijingaqifeed.com/shanghaiaqi/CurrentAQI.jpg";
//				
//				try{
//					USAQIImageData = NetTool.getImage(path);
//	                //Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
//	                //imageView.setImageBitmap(bm);
//	            }catch(Exception e){
//	                Log.i("获取图片失败",e.toString());
//	            //e.printStackTrace();
//	                //Toast.makeText(DataActivity.this, "获取图片失败", 1);
//	            }
//				flag = false;
//				
//				
//				Intent intent = new Intent("com.successcw.airofrunning.entity");
//				intent.putExtra("USAQI", data.getDescription().get(1));
//				intent.putExtra("USAQIIMAGE", USAQIImageData);
//				sendBroadcast(intent);
//			}
			String USAQI = "";
			String SHAQI = "";
			
			try{
				USAQI = NetTool.getHtml("http://www.aqicn.info/?json&key=v5&city=Shanghai", "UTF-8");
				JSONObject jsonObjRecv = new JSONObject(USAQI);
				
				SHAQI= NetTool.getSHAQI("GetSiteAQIData").replace("$", " ").replace("#", " ").replace("*"," ");
				String []SHAQIArray = SHAQI.split(" ");
				//for(String name:SHAQIArray)
				//	Log.i("AQI:",name.toString());
				byte[] SHLandscape = null;
				
				//获取上海实时图片
				String path = "http://www.semc.gov.cn/aqi/home/images/landscape.jpg";		

				SHLandscape = NetTool.getImage(path);
				
				Intent intent = new Intent("com.successcw.airofrunning.entity");
				intent.putExtra("USAQIVALUE", jsonObjRecv.getString("aqi").toString());
				intent.putExtra("USAQITIME", jsonObjRecv.getJSONObject("time").getString("u").toString());
				intent.putExtra("SHAQILEVEL", SHAQIArray[SHAQIArray.length - 3].toString());
				intent.putExtra("SHUPDATEDATE", SHAQIArray[SHAQIArray.length - 2].toString());
				intent.putExtra("SHUPDATETIME", SHAQIArray[SHAQIArray.length - 1].toString());
				intent.putExtra("SHAQIVALUE", SHAQIArray[1].toString());
				intent.putExtra("SHLANDSCAPE", SHLandscape);
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
