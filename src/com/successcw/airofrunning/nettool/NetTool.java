package com.successcw.airofrunning.nettool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetTool {
	public static byte[] readStream(InputStream inStream) throws Exception{
        //实例化一个存放输出流的字节数组
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //定义数组的大小
        byte[] buffer = new byte[1024];
        int len = -1 ;
        //读完为止，直到最后；inStream.read()读取缓冲那个区数据
        while((len=inStream.read(buffer)) !=-1){
            //把数据写入到ByteArrayOutputStream中
            outStream.write(buffer, 0, len);
        }
        //关闭输出流
        outStream.close();
        //关闭输入流
        inStream.close();
        //返回输入流的数据
        return outStream.toByteArray();
    }
    public static byte[] getImage(String urlpath)throws Exception{
        //实例化一个URL对象
        URL url = new URL(urlpath);
        //打开一个HttpURLConnection
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        //设置请求方法，默认是GET
        conn.setRequestMethod("GET");
        //设置超时时间
        conn.setConnectTimeout(6*1000);//10//如果超时android的组件时间，就会被系统回收
        //System.out.println(conn.getResponseCode());
        if(conn.getResponseCode()== 200){
            //得到一个缓存的流对像
           InputStream inStream = conn.getInputStream();
           return readStream(inStream);
        }
        return null;
      }
   public static String getHtml(String urlPath,String encoding)throws Exception{
        //实例化一个URL对象
        URL url = new URL(urlPath);
        //打开一个HttpURLConnection
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        //设置请求方法，默认是GET
        conn.setRequestMethod("GET");
        //设置超时时间
        conn.setConnectTimeout(6*1000);//10//如果超时android的组件时间，就会被系统回收
//        System.out.println(conn.getResponseCode());
        if((conn.getResponseCode())== 200){
            //得到一个缓存的流对像
            InputStream inStream = conn.getInputStream();
            //读取流的数据
            byte[] data = readStream(inStream);
            return new String(data,encoding);
        }
        return null;
      }
}
	
