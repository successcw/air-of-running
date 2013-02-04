package com.successcw.airofrunning.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
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
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.successcw.airofrunning.service.IntentService;

public class MainActivity extends TabActivity {

	private static final int DIALOG = 1;
	private Intent service;
	private IntentService AirOfRunningService;
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
	String CITYAREA = "";
	String STATION = "";
	String[] WEATHERFORECASE;
	private TabHost tabHost;
	private int VERSIONCODE = 0;
	private String VERSIONNAME = null;
	private String VERSIONCONTENT = null;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				showDialog(DIALOG);
				break;
			case 2:
				removeDialog(DIALOG);
				//Log.i("main received", "createMainView");
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
/*				Toast.makeText(context, "出错了,请稍后重试" + ERRORMSG.toString(), Toast.LENGTH_LONG)
				.show();*/
				Toast.makeText(context, "网络错误,请稍后重试", Toast.LENGTH_LONG)
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
				CITYAREA = (String) intent.getSerializableExtra("CITYAREA");
				STATION = (String) intent.getSerializableExtra("STATION");
				WEATHERFORECASE = intent.getSerializableExtra("WEATHERFORECASE").toString().replace("[", "").replace("]", "").split(",");
				VERSIONCODE = Integer.valueOf((String) intent.getSerializableExtra("VERSIONCODE"));
				VERSIONNAME = (String) intent.getSerializableExtra("VERSIONNAME");
				VERSIONCONTENT = (String) intent.getSerializableExtra("VERSIONCONTENT");
				
				getPreferences(MODE_PRIVATE).edit().putString("CITY_SETTING",String.valueOf(intent.getSerializableExtra("CITY_SETTING"))).commit();
				getPreferences(MODE_PRIVATE).edit().putString("STATION_SETTING",String.valueOf(intent.getSerializableExtra("STATION_SETTING"))).commit();
				
				////Log.i("main received", "entity");
				handler.sendEmptyMessage(2);
			}
			if (action.equals("com.successcw.airofrunning.share")) {
				share();
				//Log.i("MainActivity","received com.successcw.airofrunning.share");
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
			AirOfRunningService.parserData(Integer.valueOf(CITYAREA), Integer.valueOf(STATION)); //默认加载上海数据
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
		filter.addAction("com.successcw.airofrunning.share");
		registerReceiver(receiver, filter);

		//获取setting中的信息，如果没有的话默认加载上海数据（应用程序第一次启动）
		CITYAREA = getPreferences(MODE_PRIVATE).getString("CITY_SETTING","20");
		STATION = getPreferences(MODE_PRIVATE).getString("STATION_SETTING","0");
		//Log.i("OnCreate",CITYAREA);
		//Log.i("OnCreate",STATION);
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
		  // menu.add(0,1,0,"分享")
	      //  .setIcon(R.drawable.pointer);
		return true;
	}
	


	private void share() {
	    DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    int width = dm.widthPixels; 
	    int height = dm.heightPixels;//获取屏幕的宽和高
	    Rect rect = new Rect();
	    View view= getWindow().getDecorView();
	    view.setDrawingCacheEnabled(true);
	    view.buildDrawingCache();
	    Bitmap b1 = view.getDrawingCache();
	    //Log.i("bitmap.height", Integer.toString(b1.getHeight()));
	    
	    view.getWindowVisibleDisplayFrame(rect);
	    int statusBarHeight = rect.top;
	    //Log.i("状态栏高度",Integer.toString(statusBarHeight));
	     
	    int wintop = getWindow().findViewById(android.R.id.content).getTop();
	    int titleBarHeight = wintop - statusBarHeight;
	    //Log.i("标题栏高度",Integer.toString(titleBarHeight));
	    //Log.i("Hight", Integer.toString(height));
	    
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
	    	//e.printStackTrace();
	    } 
	    
	    //view.draw(new Canvas(b));
	    String url = "file:///" + "sdcard/AirOfRunning/airofrunning.jpg";
	    //String url = "http://www.semc.gov.cn/aqi/home/images/landscape.jpg";
	    Intent intent=new Intent(Intent.ACTION_SEND);  
	    intent.setType("image/*");  //分享的数据类型       <-----只要改為intent.setType("image/*");就可以了
	    intent.putExtra(Intent.EXTRA_SUBJECT, "Air of running");  //主题
	    intent.putExtra(Intent.EXTRA_TEXT,  "多久没跑步了？来一次太极跑吧，跑步也可以轻松愉快，不过要先看看空气情况哦。 分享自：@airOfRunning，^_^。 友情提示：科学跑步，拒绝污染。");  //内容
	    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));   //圖片
	    startActivity(Intent.createChooser(intent, "标题"));  //目标应用选择对话框的标题
	}
	public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
        	verCode = context.getPackageManager().getPackageInfo(
        			context.getPackageName(), 0).versionCode;
            Log.i("getVersionCode", String.valueOf(verCode));
        } catch (NameNotFoundException e) {
            Log.e("NewVersionUpdate", e.getMessage());
        }
        return verCode;
    }

	private void showNoticeDialog(){
		Dialog noticeDialog;
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("软件版本更新");
        builder.setMessage("有最新的软件包V1.3，快下载更新吧~\n 更新如下：\n" + VERSIONCONTENT.replace(";", "\n"));
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Uri uri = Uri.parse("https://air-of-running.googlecode.com/files/AirOfRunningV"+ VERSIONNAME + ".apk");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                //showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //getPreferences(MODE_PRIVATE).edit().putString("UPDATE_SETTING","1").commit();
            }
        });
        builder.setCancelable(false); //不响应返回键
        noticeDialog = builder.create();
        noticeDialog.show();
    }

	public void checkUpdate() {
		//Log.e("checkUpdate",String.valueOf(VERSIONCODE));
		if(VERSIONCODE > getVersionCode(this)) {
			//Log.i("checkUpdate", "need update!");
			showNoticeDialog();
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
		intentWeather.putExtra("CITYAREA",CITYAREA);
		intentWeather.putExtra("STATION",STATION);
		intentWeather.putExtra("VERSIONCODE",VERSIONCODE);
		intentWeather.putExtra("VERSIONNAME",VERSIONNAME);
		intentWeather.putExtra("VERSIONCONTENT",VERSIONCONTENT);
		//if(getPreferences(MODE_PRIVATE).getString("UPDATE_SETTING","0") == "0")
			checkUpdate();
		
		tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(intentWeather));
		ImageButton optionMenu = (ImageButton)findViewById(R.id.option_menu);

	}

}
