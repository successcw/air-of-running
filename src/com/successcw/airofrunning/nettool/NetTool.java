package com.successcw.airofrunning.nettool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.util.Log;

public class NetTool {
	public static byte[] readStream(InputStream inStream) throws Exception{
        //ʵ����һ�������������ֽ�����
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //��������Ĵ�С
        byte[] buffer = new byte[1024];
        int len = -1 ;
        //����Ϊֹ��ֱ�����inStream.read()��ȡ�����Ǹ�������
        while((len=inStream.read(buffer)) !=-1){
            //������д�뵽ByteArrayOutputStream��
            outStream.write(buffer, 0, len);
        }
        //�ر������
        outStream.close();
        //�ر�������
        inStream.close();
        //����������������
        return outStream.toByteArray();
    }
    public static byte[] getImage(String urlpath)throws Exception{
        //ʵ����һ��URL����
        URL url = new URL(urlpath);
        //��һ��HttpURLConnection
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        //�������󷽷���Ĭ����GET
        conn.setRequestMethod("GET");
        //���ó�ʱʱ��
        conn.setConnectTimeout(6*1000);//10//�����ʱandroid�����ʱ�䣬�ͻᱻϵͳ����
        //System.out.println(conn.getResponseCode());
        if(conn.getResponseCode()== 200){
            //�õ�һ�������������
           InputStream inStream = conn.getInputStream();
           return readStream(inStream);
        }
        return null;
      }
   public static String getHtml(String urlPath,String encoding)throws Exception{
        //ʵ����һ��URL����
        URL url = new URL(urlPath);
        //��һ��HttpURLConnection
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        //�������󷽷���Ĭ����GET
        conn.setRequestMethod("GET");
        //���ó�ʱʱ��
        conn.setConnectTimeout(6*1000);//10//�����ʱandroid�����ʱ�䣬�ͻᱻϵͳ����
//        System.out.println(conn.getResponseCode());
        if((conn.getResponseCode())== 200){
            //�õ�һ�������������
            InputStream inStream = conn.getInputStream();
            //��ȡ��������
            byte[] data = readStream(inStream);
            return new String(data,encoding);
        }
        return null;
      }
   public static String getSHAQI(String MethodName) {
		String NAMESPACE = "http://tempuri.org/";
		String SOAP_ACTION = NAMESPACE + MethodName;
		String URL = "http://202.136.217.188/AQIWS4Moblie/WebService.asmx";
		
		SoapObject request = new SoapObject(NAMESPACE, MethodName);        
		
		Element[] header = new Element[1];
		header[0] = new Element().createElement(NAMESPACE, "MySoapHeader");
		Element username = new Element().createElement(NAMESPACE, "UserName");
		username.addChild(4, "8564879f-3d1a-4c4f-9219-47f1fa5a6790");
		header[0].addChild(2, username);
		
		Element pass = new Element().createElement(NAMESPACE, "PassWord");
		pass.addChild(4, "a1ea259d-068c-4aab-a795-0191f3b5e6f3");
		header[0].addChild(2, pass);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);    	
    	envelope.headerOut = header;
    	envelope.bodyOut = request;
    	envelope.dotNet = true;
	
    	try {
    		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		
    		androidHttpTransport.debug = true;
    		//this is the actual part that will call the webservice
    		androidHttpTransport.call(SOAP_ACTION, envelope);
    		
    		
    		// Get the SoapResult from the envelope body.
        	SoapObject result = (SoapObject)envelope.bodyIn;
        	if(result != null)
        	{
        		Log.i("NetTool", result.getProperty(0).toString());
        		return result.getProperty(0).toString();
        		//Get the first property and change the label text
	    	}
        	else
        	{
        		Log.e("NetTool", "Nothing");
        		//Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		
		return null;
   }
   
   public static String getWeather(String CityName) {
	   String NAMESPACE = "http://WebXml.com.cn/";
	   String METHOD_NAME = "getWeatherbyCityName";
	   String URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
	   String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";
	   SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	   
	   request.addProperty("theCityName", CityName);
	   SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	   envelope.bodyOut = request;
	   envelope.dotNet = true;

   	
	   try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject)envelope.bodyIn;
		
			if(result != null)
			{
				Log.e("NET", result.getProperty(0).toString());
				return result.getProperty(0).toString();
			}
			else
			{
				Log.e("NET", "weather:Nothing");
				//Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	   return null;
   }
   
   public static ArrayList<String> getWeatherFromSina(String CityName) {
	   XMLGettersSetters data;
	   try {   
		   SAXParserFactory saxPF = SAXParserFactory.newInstance();
		   SAXParser saxP = saxPF.newSAXParser();
		   XMLReader xmlR = saxP.getXMLReader();
		   URL url = new URL("http://forecast.sina.cn/app/update.php?city=" + CityName);
		   
		   XMLHandler myXMLHandler = new XMLHandler();
		   xmlR.setContentHandler(myXMLHandler);
		   xmlR.parse(new InputSource(url.openStream()));
		   data = XMLHandler.data;
		   
		   Log.i("NetTool", data.getDescription().toString());
		   
		   return data.getDescription();
	   } catch (Exception e) {
		   System.out.println(e);
		   Log.e("NET", e.toString());
	   }
	   return null;
   }
   
}



