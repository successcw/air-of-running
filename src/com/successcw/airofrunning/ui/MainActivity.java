package com.successcw.airofrunning.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;

import com.successcw.airofrunning.service.IntentService;

public class MainActivity extends TabActivity {

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
	String SHISHITEMPRATURE = "";
	String AIRCONDITION = "";
	String TEMPRATURE = "";
	String WIND = "";
	String WEATHERICON = "";
	String TEMPRATUREUPDATETIME = "";
	ArrayList <String> WEATHERFORECASE;
	private TabHost tabHost;
	
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
				Toast.makeText(context, "������,���Ժ�����" + ERRORMSG.toString(), Toast.LENGTH_LONG)
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
				SHPM2_5 = (String) intent.getSerializableExtra("SHPM2_5");
				SHISHITEMPRATURE = (String) intent.getSerializableExtra("SHISHITEMPRATURE");
				AIRCONDITION = (String) intent.getSerializableExtra("AIRCONDITION");
				TEMPRATURE = (String) intent.getSerializableExtra("TEMPRATURE");
				WIND = (String) intent.getSerializableExtra("WIND");
				WEATHERICON = (String) intent.getSerializableExtra("WEATHERICON");
				TEMPRATUREUPDATETIME = (String) intent.getSerializableExtra("TEMPRATUREUPDATETIME");
				WEATHERFORECASE = (ArrayList) intent.getSerializableExtra("WEATHERFORECASE");		
				
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);	
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
	 * ����������
	 */
	private ProgressDialog DialogPro() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("������...");
		return dialog;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		stopService(service);
		unbindService(conn);
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		   menu.add(0,1,0,"����")
	        .setIcon(R.drawable.pointer);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case 1:
	            DisplayMetrics dm = new DisplayMetrics();
	            getWindowManager().getDefaultDisplay().getMetrics(dm);
	            int width = dm.widthPixels; 
	            int height = dm.heightPixels;//��ȡ��Ļ�Ŀ�͸�
	            Rect rect = new Rect();
	            View view= getWindow().getDecorView();
	            view.setDrawingCacheEnabled(true);
	            view.buildDrawingCache();
	            Bitmap b1 = view.getDrawingCache();
	            
	            view.getWindowVisibleDisplayFrame(rect);
	            int statusBarHeight = rect.top;
	            Log.i("״̬���߶�",Integer.toString(statusBarHeight));
	             
	            int wintop = getWindow().findViewById(android.R.id.content).getTop();
	            int titleBarHeight = wintop - statusBarHeight;
	            Log.i("�������߶�",Integer.toString(titleBarHeight));
	            
	       
	            //Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	            Bitmap bmp = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
	            //view.draw(new Canvas(bmp));
	            view.destroyDrawingCache();
	            
	            File SpicyDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/AirOfRunning/");
	            SpicyDirectory.mkdirs();
	            
	            String filename=Environment.getExternalStorageDirectory().getPath() + "/AirOfRunning/airofrunning.jpg";
	            FileOutputStream out = null;
	            try {
	            	out = new FileOutputStream(filename);
	            	bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
	            } catch (Exception e) {
	            	e.printStackTrace();
	            } 
	            
	            //view.draw(new Canvas(b));
	            String url = "file:///" + "sdcard/AirOfRunning/airofrunning.jpg";
	            //String url = "http://www.semc.gov.cn/aqi/home/images/landscape.jpg";
	            Intent intent=new Intent(Intent.ACTION_SEND);  
	            intent.setType("image/*");  //�������������       <-----ֻҪ�Ğ�intent.setType("image/*");�Ϳ�����
	            intent.putExtra(Intent.EXTRA_SUBJECT, "Air of running");  //����
	            intent.putExtra(Intent.EXTRA_TEXT,  "���û�ܲ��ˣ���һ��̫���ܰɣ��ܲ�Ҳ����������죬����Ҫ�ȿ����������Ŷ�� �����ԣ�@airOfRunning��^_^�� ������ʾ����ѧ�ܲ����ܾ���Ⱦ��");  //����
	            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));   //�DƬ
	            startActivity(Intent.createChooser(intent, "����"));  //Ŀ��Ӧ��ѡ��Ի���ı���
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
		
	private void createMainView() {
        tabHost = getTabHost(); 
		Intent intentWeather = new Intent(this, weatheractivity.class);
		intentWeather.putExtra("USAQIVALUE", USAQIVALUE);
		intentWeather.putExtra("USAQITIME", USAQITIME);
		intentWeather.putExtra("SHUPDATEDATE", SHUPDATEDATE);
		intentWeather.putExtra("SHUPDATETIME", SHUPDATETIME);
		intentWeather.putExtra("SHAQILEVEL", SHAQILEVEL);
		intentWeather.putExtra("SHAQIVALUE", SHAQIVALUE);
		intentWeather.putExtra("SHPM2_5", SHPM2_5);
		intentWeather.putExtra("SHISHITEMPRATURE", SHISHITEMPRATURE);
		intentWeather.putExtra("AIRCONDITION", AIRCONDITION);
		intentWeather.putExtra("TEMPRATURE", TEMPRATURE);
		intentWeather.putExtra("WIND", WIND);
		intentWeather.putExtra("WEATHERICON", WEATHERICON);
		intentWeather.putExtra("TEMPRATUREUPDATETIME",TEMPRATUREUPDATETIME);
		intentWeather.putExtra("WEATHERFORECASE",WEATHERFORECASE);
		
		tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(intentWeather));
	}

}
