package com.successcw.airofrunning.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.successcw.airofrunning.service.IntentService;

public class MainActivity extends Activity {

	private static final int DIALOG = 1;
	private Intent service;
	public IntentService AirOfRunningService;
	String USAQIVALUE = "";
	String USAQITIME = "";
	String SHUPDATETIME = "";
	String SHUPDATEDATE = "";
	String SHPM2_5 = "";
	String SHAQIVALUE = "";
	String SHAQILEVEL = "";
	byte[] SHLANDSCAPE = null;
	Bitmap bm;
	Toast UStoast;
	Toast SHtoast;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				showDialog(DIALOG);
				break;
			case 2:
				removeDialog(DIALOG);
				createMainView();
				break;
			case 3:
				removeDialog(DIALOG);
				break;
			default:
				break;
			}
		};
	};
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.successcw.airofrunning.noNet")) {
				String ERRORMSG = (String) intent.getSerializableExtra("ERRORMSG");
				Toast.makeText(context, "出错了,请稍后重试" + ERRORMSG.toString(), Toast.LENGTH_LONG)
				.show();
				handler.sendEmptyMessage(3);
			}
			if (action.equals("com.successcw.airofrunning.entity")) {
				USAQIVALUE = (String) intent.getSerializableExtra("USAQIVALUE");
				USAQITIME = (String) intent.getSerializableExtra("USAQITIME");
				SHUPDATEDATE = (String) intent.getSerializableExtra("SHUPDATEDATE");
				SHUPDATETIME = (String) intent.getSerializableExtra("SHUPDATETIME");
				SHAQILEVEL = (String) intent.getSerializableExtra("SHAQILEVEL");
				SHAQIVALUE = (String) intent.getSerializableExtra("SHAQIVALUE");
				SHLANDSCAPE = (byte[]) intent.getSerializableExtra("SHLANDSCAPE");
				//SHPM2_5 = (String) intent.getSerializableExtra("SHPM2_5");
				
				handler.sendEmptyMessage(2);
			}
		}
	};
	
	private ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			AirOfRunningService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			AirOfRunningService = ((IntentService.LocalBinder) service)
					.getService();
			AirOfRunningService.parserData();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View layout = findViewById(R.id.layout); 
		layout.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	Log.i("layout", "onclick");
	        	if (UStoast != null)
	        		UStoast.cancel();
	        	if (SHtoast != null)
	        	{	        		
	        		SHtoast.cancel();
	        		Log.i("SHtoast", "cancel SHtoast");
	        	}
	        }
	    });
		handler.sendEmptyMessage(1);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.successcw.airofrunning.entity");
		filter.addAction("com.successcw.airofrunning.noNet");
		registerReceiver(receiver, filter);
		
		service = new Intent("com.successcw.airofrunning.intentservice");
		startService(service);
		bindService(service, conn, BIND_AUTO_CREATE);
		
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG) {
			return DialogPro();
		} else {
			return null;
		}
	}
	
	/*
	 * 创建进度条
	 */
	private ProgressDialog DialogPro() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("加载中...");
		return dialog;
	}

	@Override
	protected void onStop() {
		//if (input != null) {
			//stopService(service);
			//unbindService(conn);
			//unregisterReceiver(receiver);
		//}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		//if (input != null) {
			stopService(service);
			unbindService(conn);
			unregisterReceiver(receiver);
		//}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		   menu.add(0,1,0,"weibo")
	        .setIcon(R.drawable.pointer)
	        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case 1:
	            Log.i("MENU", "1 clicked");
	            DisplayMetrics dm = new DisplayMetrics();
	            getWindowManager().getDefaultDisplay().getMetrics(dm);
	            int width = dm.widthPixels; 
	            int height = dm.heightPixels;//获取屏幕的宽和高
	            Rect rect = new Rect();
	            View view= getWindow().getDecorView();
	            view.getWindowVisibleDisplayFrame(rect);
	            int statusBarHeight = rect.top;
	            Log.i("状态栏高度",Integer.toString(statusBarHeight));
	             
	            int wintop = getWindow().findViewById(android.R.id.content).getTop();
	            int titleBarHeight = wintop - statusBarHeight;
	            Log.i("标题栏高度",Integer.toString(titleBarHeight));
	            
	       
	            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	            view.draw(new Canvas(bmp));
	            
	            File SpicyDirectory = new File("/sdcard/AirOfRunning/");
	            SpicyDirectory.mkdirs();
	            
	            String filename="/sdcard/AirOfRunning/airofrunning.jpg";
	            FileOutputStream out = null;
	            try {
	            out = new FileOutputStream(filename);
	            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
	            } catch (Exception e) {
	            e.printStackTrace();
	            } finally {
	            try {
	            out.flush();
	            } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            }
	            try {
	            out.close();
	            } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            }
	            out=null;
	            }
	            
	            //view.draw(new Canvas(b));
	            String url = "file:///" + "sdcard/AirOfRunning/airofrunning.jpg";
	            //String url = "http://www.semc.gov.cn/aqi/home/images/landscape.jpg";
	            Intent intent=new Intent(Intent.ACTION_SEND);  
	            intent.setType("image/*");  //分享的数据类型       <-----只要改為intent.setType("image/*");就可以了
	            intent.putExtra(Intent.EXTRA_SUBJECT, "Air of running");  //主题
	            intent.putExtra(Intent.EXTRA_TEXT,  "跑步之前看看空气质量再决定跑不跑哦，高污染跑了不如不跑。 分享自：我的第一个应用：Air of Running，^_^。 友情提示：科学跑步，拒绝污染。");  //内容
	            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));   //圖片
	            startActivity(Intent.createChooser(intent, "标题"));  //目标应用选择对话框的标题
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
	
	public int Linear(int AQIhigh, int AQIlow, float Conchigh, float Conclow, float Concentration)
	{
		int linear;
		//float Conc =  Float.parseFloat(Concentration);
		float temp;
		temp = ((Concentration-Conclow)/(Conchigh-Conclow))*(AQIhigh-AQIlow)+AQIlow;
		linear=Math.round(temp);
		return linear;
	}
	public int AQIPM25(String Concentration)
	{
		float Conc = Float.parseFloat(Concentration);
		Log.i("AQIPM25", Float.toString(Conc));
		float c = (float)(Math.floor(10*Conc))/10;
		
		int AQI = 0;
	
		if (c>=0 && c<15.5)
		{
			AQI=Linear(50,0,15.4f,0,c);
		}
		else if (c>=15.5 && c<35.5)
		{
			AQI=Linear(100,51,35.4f,15.5f,c);
		}
		else if (c>=35.5 && c<65.5)
		{
			AQI=Linear(150,101,65.4f,35.5f,c);
		}
		else if (c>=65.5 && c<150.5)
		{
			AQI=Linear(200,151,150.4f,65.5f,c);
		}
		else if (c>=150.5 && c<250.5)
		{
			AQI=Linear(300,201,250.4f,150.5f,c);
		}
		else if (c>=250.5 && c<350.5)
		{
			AQI=Linear(400,301,350.4f,250.5f,c);
		}
		else if (c>=350.5 && c<500.5)
		{
			AQI=Linear(500,401,500.4f,350.5f,c);
		}
		else
		{
			AQI=100000;
		}
		return AQI;
	}

	public static Integer TryParseInt(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException ex) {
            return null;
        }
	}
	
	private void createMainView() {
		View layout = findViewById(R.id.layout);
		Bitmap bm = BitmapFactory.decodeByteArray(SHLANDSCAPE, 0, SHLANDSCAPE.length);
	
		//ImageView USAQIimageView = (ImageView)findViewById(R.id.USAQIimageView);
		//USAQIimageView.setImageBitmap(bm);

		String []Array = USAQITIME.split(" ");
		USAQITIME = Array[1];
		TextView US = (TextView)findViewById(R.id.US);
		US.setText("美国上海领事馆 " + SHUPDATEDATE + " " + USAQITIME + "发布" );
		//US.setTextSize(20);
		
		TextView USAQIVALUEView = (TextView)findViewById(R.id.USAQIVALUE);
		TextView SHView = (TextView)findViewById(R.id.SH);
		SHView.setText("上海官方 " + SHUPDATEDATE + " " + SHUPDATETIME + "发布" );
		//SHView.setTextSize(20);
		TextView SHAQIVALUEView = (TextView)findViewById(R.id.SHAQIVALUE);

		ImageView USAQILevel = (ImageView)findViewById(R.id.usaqilevel);
		
		USAQILevel.setOnClickListener(new View.OnClickListener() {
			   //@Override
		   public void onClick(View v) {
			  Log.i("USToast", " click");
			  UStoast = Toast.makeText(MainActivity.this,   
			            "美国标准", Toast.LENGTH_LONG);
			  ImageView imageView = new ImageView(MainActivity.this);  
			  imageView.setImageResource(R.drawable.usaqilevel);
			  
			  View toastView = UStoast.getView();  
			  
			  LinearLayout linearLayout = new LinearLayout(MainActivity.this);  
			  linearLayout.setOrientation(LinearLayout.HORIZONTAL);  
			  
			  linearLayout.addView(imageView);  
			  linearLayout.addView(toastView);  
			  
			  UStoast.setView(linearLayout);  
			  UStoast.show();
		   }        
		});
		
		TextView SHShiJing = (TextView)findViewById(R.id.shshijing);
		SHShiJing.setText("实景照片");
		
		ImageView SHLandscape = (ImageView)findViewById(R.id.shlandscape);
		SHLandscape.setImageBitmap(bm);
		
		Integer USAQIVALUETemp = TryParseInt(USAQIVALUE.toString());
		
		//update US AQI
		if (USAQIVALUETemp == null) {
			USAQIVALUEView.setText("AQI数据加载错误，请稍候重试");
		}else if(Integer.valueOf(USAQIVALUETemp) <= 50){
			USAQIVALUEView.setText("AQI:" + USAQIVALUE+" "+ "健康");
			USAQIVALUEView.setTextColor(Color.rgb(0,228,0));
			USAQIVALUEView.setTextSize(20);
			USAQILevel.setImageResource(R.drawable.aqi_1);
			
		}else if (Integer.valueOf(USAQIVALUETemp) <= 100 && Integer.valueOf(USAQIVALUETemp) >= 51){
			USAQIVALUEView.setText("AQI:" + USAQIVALUE+" "+ "中等");			
			USAQIVALUEView.setTextColor(Color.rgb(255,255,0));
			USAQIVALUEView.setTextSize(20);
			USAQILevel.setImageResource(R.drawable.aqi_2);
			
		}else if (Integer.valueOf(USAQIVALUETemp) <= 150 && Integer.valueOf(USAQIVALUETemp) >= 101){
			USAQIVALUEView.setText("AQI:" + USAQIVALUE+" "+ "对敏感人群不健康");
			USAQIVALUEView.setTextColor(Color.rgb(255,165,0));
			USAQIVALUEView.setTextSize(20);		
			USAQILevel.setImageResource(R.drawable.aqi_3);
		}else if (Integer.valueOf(USAQIVALUETemp) <= 200 && Integer.valueOf(USAQIVALUETemp) >= 151){
			USAQIVALUEView.setTextColor(Color.RED);
			USAQIVALUEView.setText("AQI:" + USAQIVALUE+" "+ "不健康");
			USAQIVALUEView.setTextSize(20);		
			USAQILevel.setImageResource(R.drawable.aqi_4);
		}else if (Integer.valueOf(USAQIVALUETemp) <= 300 && Integer.valueOf(USAQIVALUETemp) >= 201){
			USAQIVALUEView.setText("AQI:" + USAQIVALUE+" "+ "非常不健康");
			USAQIVALUEView.setTextColor(Color.rgb(176,48,96));
			USAQIVALUEView.setTextSize(20);
			USAQILevel.setImageResource(R.drawable.aqi_5);
		}else if (Integer.valueOf(USAQIVALUETemp) <= 500 && Integer.valueOf(USAQIVALUETemp) >= 301){
			USAQIVALUEView.setText("AQI:" + USAQIVALUE+" "+ "危险");
			USAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			USAQIVALUEView.setTextSize(20);
			USAQILevel.setImageResource(R.drawable.aqi_6);
		}else{
			USAQIVALUEView.setText("AQI:"+USAQIVALUE+" "+ "爆表");
			USAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			USAQIVALUEView.setTextSize(20);		
			USAQILevel.setImageResource(R.drawable.aqi);
		}
		
		ImageView SHAQILevel = (ImageView)findViewById(R.id.shaqilevel);
		SHAQILevel.setOnClickListener(new View.OnClickListener() {
			   //@Override
		   public void onClick(View v) {
			  Log.i("SHToast", " click");
			  SHtoast = Toast.makeText(MainActivity.this,   
			            "上海标准", Toast.LENGTH_LONG);
			  ImageView imageView = new ImageView(MainActivity.this);  
			  imageView.setImageResource(R.drawable.shaqilevel);
			  
			  View toastView = SHtoast.getView();  
			  
			  LinearLayout linearLayout = new LinearLayout(MainActivity.this);  
			  linearLayout.setOrientation(LinearLayout.HORIZONTAL);  
			  
			  linearLayout.addView(imageView);  
			  linearLayout.addView(toastView);  
			  
			  SHtoast.setView(linearLayout);  
			  SHtoast.show();
		   }        
		});
		
		Integer SHAQIVALUETemp = TryParseInt(SHAQIVALUE.toString());
		//update SH AQI value
		SHAQIVALUEView.setTextSize(20);
		
		if (SHAQIVALUETemp == null) {
			SHAQIVALUEView.setText("AQI数据加载错误，请稍候重试");
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 50){
			SHAQIVALUEView.setText("AQI:" + SHAQIVALUE+" "+ SHAQILEVEL);
			SHAQIVALUEView.setTextColor(Color.rgb(0,228,0));	
			SHAQILevel.setImageResource(R.drawable.aqi_1);
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 100 && Integer.valueOf(SHAQIVALUETemp) >= 51){
			SHAQIVALUEView.setText("AQI:" + SHAQIVALUE+" "+ SHAQILEVEL);
			SHAQIVALUEView.setTextColor(Color.rgb(255,255,0));		
			SHAQILevel.setImageResource(R.drawable.aqi_2);
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 150 && Integer.valueOf(SHAQIVALUETemp) >= 101){
			SHAQIVALUEView.setText("AQI:" + SHAQIVALUE+" "+ SHAQILEVEL);
			SHAQIVALUEView.setTextColor(Color.rgb(255,165,0));
			SHAQILevel.setImageResource(R.drawable.aqi_3);
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 200 && Integer.valueOf(SHAQIVALUETemp) >= 151){
			SHAQIVALUEView.setText("AQI:" + SHAQIVALUE+" "+ SHAQILEVEL);
			SHAQIVALUEView.setTextColor(Color.RED);	
			SHAQILevel.setImageResource(R.drawable.aqi_4);
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 300 && Integer.valueOf(SHAQIVALUETemp) >= 201){
			SHAQIVALUEView.setText("AQI:" + SHAQIVALUE+" "+ SHAQILEVEL);
			SHAQIVALUEView.setTextColor(Color.rgb(176,48,96));
			SHAQILevel.setImageResource(R.drawable.aqi_5);
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 500 && Integer.valueOf(SHAQIVALUETemp) >= 301){
			SHAQIVALUEView.setText("AQI:" + SHAQIVALUE+" "+ SHAQILEVEL);
			SHAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			SHAQILevel.setImageResource(R.drawable.aqi_6);
		}else{			
			SHAQIVALUEView.setText("AQI:" + SHAQIVALUE+" "+ "爆表");
			SHAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			SHAQILevel.setImageResource(R.drawable.aqi);
		}	
		setContentView(layout);
	}

}
