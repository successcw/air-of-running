package com.successcw.airofrunning.ui;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.successcw.airofrunning.service.IntentService;

public class MainActivity extends Activity {

	private static final int DIALOG = 1;
	private Intent service;
	public IntentService AirOfRunningService;
	String USAQI = "";
	byte[] USAQIIMAGE = null;
	Bitmap bm;
	
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
				Toast.makeText(context, "网络异常,请检查您的网络!", Toast.LENGTH_LONG)
				.show();
				handler.sendEmptyMessage(2);
			}
			if (action.equals("com.successcw.airofrunning.entity")) {
				USAQI = (String) intent.getSerializableExtra("USAQI");
				USAQIIMAGE = (byte[]) intent.getSerializableExtra("USAQIIMAGE");
				bm = BitmapFactory.decodeByteArray(USAQIIMAGE, 0, USAQIIMAGE.length);
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
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	private void createMainView() {
		View layout = findViewById(R.id.layout);
		
		ImageView USAQIimageView = (ImageView)findViewById(R.id.USAQIimageView);
		USAQIimageView.setImageBitmap(bm);
		TextView loading = (TextView)findViewById(R.id.loading);
		loading.setText(USAQI);
		setContentView(layout);
	}

}
