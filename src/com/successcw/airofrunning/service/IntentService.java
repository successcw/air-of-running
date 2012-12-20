package com.successcw.airofrunning.service;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.successcw.airofrunning.nettool.NetTool;

public class IntentService extends Service{
	
	XMLGettersSetters data;
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
			try {	
				/**
				 * Create a new instance of the SAX parser
				 **/
				SAXParserFactory saxPF = SAXParserFactory.newInstance();
				SAXParser saxP = saxPF.newSAXParser();
				XMLReader xmlR = saxP.getXMLReader();

				
				URL url = new URL("http://www.beijingaqifeed.com/shanghaiaqi/shanghaiairrss.xml"); // URL of the XML
				
				/** 
				 * Create the Handler to handle each of the XML tags. 
				 **/
				XMLHandler myXMLHandler = new XMLHandler();
				xmlR.setContentHandler(myXMLHandler);
				xmlR.parse(new InputSource(url.openStream()));
				
			} catch (Exception e) {
				System.out.println(e);
			}

			data = XMLHandler.data;
			byte[] USAQIImageData = null;
			
			//获取美国上海领事馆AQI图片
			String path = "http://www.beijingaqifeed.com/shanghaiaqi/CurrentAQI.jpg";
			
			try{
				USAQIImageData = NetTool.getImage(path);
                //Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                //imageView.setImageBitmap(bm);
            }catch(Exception e){
                Log.i("获取图片失败",e.toString());
            //e.printStackTrace();
                //Toast.makeText(DataActivity.this, "获取图片失败", 1);
            }
			
			
			
			Intent intent = new Intent("com.successcw.airofrunning.entity");
			intent.putExtra("USAQI", data.getDescription().get(1));
			intent.putExtra("USAQIIMAGE", USAQIImageData);
			sendBroadcast(intent);
			super.run();
		}
	}
}
