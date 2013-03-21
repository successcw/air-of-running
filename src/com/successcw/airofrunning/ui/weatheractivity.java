package com.successcw.airofrunning.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.successcw.airofrunning.service.IntentService;
import com.successcw.airofrunning.tool.AQITool;
import com.umeng.analytics.MobclickAgent;

public class weatheractivity extends Activity {
	private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private TextView t1, t2, t3;// 页卡头标
    private TextView temp;
    private View ViewKongqi;
    private View ViewAQIdetail;
    private View Viewtianqi;
    private View ViewZixun;
    private View ViewTemp;
	private PopupWindow popupWindow;
	private Dialog about_dialog;
	private IntentService AirOfRunningService;
	private int VERSIONCODE = 0;
	private String VERSIONNAME = null;
	private String VERSIONCONTENT = null;
    
	String USAQIVALUE = "";
	String USAQITIME = "";
	String SHUPDATETIME = "";
	String SHUPDATEDATE = "";
	String SHPM2_5 = "";
	String PM10 = "";
	String SO2 = "";
	String NO2 = "";
	String CO = "";
	String O3_1H = "";
	String O3_8H = "";
	String PrimaryPollutant = "";
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
	Integer[] icon=new Integer[]{ 
		    R.drawable.b_0,R.drawable.b_1,R.drawable.b_2, 
		    R.drawable.b_3,R.drawable.b_4,R.drawable.b_5,
		    R.drawable.b_6,R.drawable.b_7,R.drawable.b_8, 
		    R.drawable.b_9,R.drawable.b_10,R.drawable.b_11,
		    R.drawable.b_12,R.drawable.b_13,R.drawable.b_14, 
		    R.drawable.b_15,R.drawable.b_16,R.drawable.b_17,
		    R.drawable.b_18,R.drawable.b_19,R.drawable.b_20, 
		    R.drawable.b_21,R.drawable.b_22,R.drawable.b_23,
		    R.drawable.b_24,R.drawable.b_25,R.drawable.b_26, 
		    R.drawable.b_27,R.drawable.b_28,R.drawable.b_29,
		    R.drawable.b_30,R.drawable.b_31,R.drawable.b_nothing
	};

    /**
     * 初始化ViewPager
*/
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        ViewKongqi = mInflater.inflate(R.layout.kongqi, null);
        ViewAQIdetail = mInflater.inflate(R.layout.aqi_detail, null);
        Viewtianqi = mInflater.inflate(R.layout.tianqi, null);
        ViewZixun = mInflater.inflate(R.layout.zixun, null);
        listViews.add(ViewKongqi);
        listViews.add(ViewAQIdetail);
        listViews.add(Viewtianqi);
        listViews.add(ViewZixun);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    	temp = (TextView) findViewById(R.id.main_title);
    	temp.setText(CITYAREA + "空气质量");
        loadKongqi();
        ////Log.i("weather activity","initviewpager");
    }
    
    /**
     * ViewPager适配器
*/
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 页卡切换监听
*/
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
            case 0:
                //Log.i("PageChange","0");
                loadKongqi();
            	temp = (TextView) findViewById(R.id.main_title);
            	temp.setText(CITYAREA + "空气质量");
            	
                break;
            case 1:      
                //Log.i("PageChange","1");
                loadAQIdetail();
                temp = (TextView) findViewById(R.id.main_title);
				temp.setText(CITYAREA + "详细污染物");
                break;
            case 2:
                //Log.i("PageChange","1");
                loadtianqi();
                temp = (TextView) findViewById(R.id.main_title);
				temp.setText("天气");
                break;
            case 3:
                //Log.i("PageChange","2");
                loadZixun();
                temp = (TextView) findViewById(R.id.main_title);
            	temp.setText("资讯");               
                break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
	public static Integer TryParseInt(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException ex) {
            return null;
        }
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.successcw.airofrunning.noNet")) {
				String ERRORMSG = (String) intent.getSerializableExtra("ERRORMSG");
				Toast.makeText(context, "出错了,请稍后重试" + ERRORMSG.toString(), Toast.LENGTH_LONG)
				.show();		
				ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
				progressBar1.setVisibility(View.GONE);
			}
			if (action.equals("com.successcw.airofrunning.entity")) {
				USAQIVALUE = (String) intent.getSerializableExtra("USAQIVALUE");
				USAQITIME = (String) intent.getSerializableExtra("USAQITIME");
				SHUPDATEDATE = (String) intent.getSerializableExtra("SHUPDATEDATE");
				SHUPDATETIME = (String) intent.getSerializableExtra("SHUPDATETIME");
				SHAQILEVEL = (String) intent.getSerializableExtra("SHAQILEVEL");
				SHAQIVALUE = (String) intent.getSerializableExtra("SHAQIVALUE");
				SHPM2_5 = (String) intent.getSerializableExtra("SHPM2_5");
				PM10 = (String) intent.getSerializableExtra("PM10");
				SO2 = (String) intent.getSerializableExtra("SO2");
				NO2 = (String) intent.getSerializableExtra("NO2");
				CO = (String) intent.getSerializableExtra("CO");
				O3_1H = (String) intent.getSerializableExtra("O3_1H");
				O3_8H = (String) intent.getSerializableExtra("O3_8H");
				PrimaryPollutant = (String) intent.getSerializableExtra("PrimaryPollutant");
				SHISHITEMPRATURE = (String) intent.getSerializableExtra("SHISHITEMPRATURE");
				AIRCONDITION = (String) intent.getSerializableExtra("AIRCONDITION");
				TEMPRATURE = (String) intent.getSerializableExtra("TEMPRATURE");
				WIND = (String) intent.getSerializableExtra("WIND");
				WEATHERICON = (String) intent.getSerializableExtra("WEATHERICON");
				TEMPRATUREUPDATETIME = (String) intent.getSerializableExtra("TEMPRATUREUPDATETIME");
				WEATHERFORECASE = intent.getSerializableExtra("WEATHERFORECASE").toString().replace("[", "").replace("]", "").split(",");		
				CITYAREA = (String) intent.getSerializableExtra("CITYAREA");
				STATION = (String) intent.getSerializableExtra("STATION");
				
				InitViewPager();
				ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
				progressBar1.setVisibility(View.GONE);
			}
			if (action.equals("com.successcw.airofrunning.refresh")) {
				TextView mainTitle = (TextView) findViewById(R.id.main_title);
				mainTitle.setText("数据更新中...");
				ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
				progressBar1.setVisibility(View.VISIBLE);
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
			AirOfRunningService.parserData(20, 0); //默认加载上海数据
		}
	};
	
    private void loadKongqi() {
		int SHAQI;
		String temp;
		ViewTemp = ViewKongqi;
		//TextView USconst = (TextView) ViewTemp.findViewById(R.id.USconst);
		TextView SHconst = (TextView) ViewTemp.findViewById(R.id.SHconst);
		//TextView USAQIconst = (TextView) ViewTemp.findViewById(R.id.USAQIconst);
    	TextView SHAQIconst = (TextView) ViewTemp.findViewById(R.id.SHAQIconst);
    	////Log.i("loadKongqi",CITYAREA);
    	
    	if(CITYAREA.equalsIgnoreCase("上海")) {
			//USconst.setText("美国上海领事馆");
    		SHconst.setText("上海环境监测中心");
			//USAQIconst.setText("美国领事馆");
    		SHAQIconst.setText(STATION);
    	} else if(CITYAREA.equalsIgnoreCase("北京")) {
			//USconst.setText("美国北京大使馆");
    		SHconst.setText("北京环境保护监测中心");
			//USAQIconst.setText("美国大使馆");
    		SHAQIconst.setText(STATION);
    	} else if(CITYAREA.equalsIgnoreCase("广州")) {
			//USconst.setText("美国广州领事馆");
    		SHconst.setText("中国环境监测总站");
			//USAQIconst.setText("美国领事馆");
    		SHAQIconst.setText(STATION);
    	} else if(CITYAREA.equalsIgnoreCase("成都")) {
			//USconst.setText("美国成都领事馆");
    		SHconst.setText("中国环境监测总站");
			//USAQIconst.setText("美国领事馆");
    		SHAQIconst.setText(STATION);
		} else {
			//USconst.setText("中国环境监测总站");
    		SHconst.setText("中国环境监测总站");
			//USAQIconst.setText(CITYAREA + " 平均值");
    		SHAQIconst.setText(STATION);
    	}
    	TextView shishitemprature = (TextView) ViewTemp.findViewById(R.id.shishitemprature);
    	shishitemprature.setText(SHISHITEMPRATURE +"°");

    	TextView aircondition = (TextView) ViewTemp.findViewById(R.id.aircondition);
    	aircondition.setText(AIRCONDITION);   	     	
    	
    	TextView temprature = (TextView) ViewTemp.findViewById(R.id.temprature);
    	temprature.setText(TEMPRATURE);       	

    	TextView wind = (TextView) ViewTemp.findViewById(R.id.wind);
    	wind.setText(WIND);  

    	ImageView weathericon = (ImageView) ViewTemp.findViewById(R.id.weathericon);
    	
    	Integer weathericonTemp = TryParseInt(WEATHERICON.toString());
    	if(weathericonTemp == null || weathericonTemp > 32) {
    		weathericon.setImageResource(icon[32]);
    	}else{
    		weathericon.setImageResource(icon[weathericonTemp]);
    	}
    	
    	TextView tempratureupdatetime = (TextView) ViewTemp.findViewById(R.id.tempratureupdatetime);
    	tempratureupdatetime.setText("今天" + TEMPRATUREUPDATETIME + "发布");     	
    	
		//TextView US = (TextView) ViewTemp.findViewById(R.id.USupdatetime);
		//US.setText("今天" + USAQITIME + "发布  " );
		
		//TextView USAQIVALUEView = (TextView)ViewTemp.findViewById(R.id.USAQIVALUE);
		TextView SHView = (TextView)ViewTemp.findViewById(R.id.SHupdatetime);
		SHView.setText("今天" + SHUPDATETIME + "发布" );

		TextView SHAQIVALUEView = (TextView)ViewTemp.findViewById(R.id.SHAQIVALUE);
	
		//TextView USAQILEVELView = (TextView)ViewTemp.findViewById(R.id.USAQILEVEL);
		
		//Integer USAQIVALUETemp = TryParseInt(USAQIVALUE.toString());
		
//		//update US AQI
//		if (USAQIVALUETemp == null) {
//			USAQIVALUEView.setText("AQI数据加载错误，请稍候重试");
//			USAQIVALUEView.setTextSize(40);
//			USAQILEVELView.setText(" ");
//		}else if(Integer.valueOf(USAQIVALUETemp) <= 50){
//			USAQIVALUEView.setText(USAQIVALUE);
//			USAQILEVELView.setText("健康");
//			USAQIVALUEView.setTextColor(Color.rgb(0,228,0));
//			USAQILEVELView.setTextColor(Color.rgb(0,228,0));
//
//		}else if (Integer.valueOf(USAQIVALUETemp) <= 100 && Integer.valueOf(USAQIVALUETemp) >= 51){
//			USAQIVALUEView.setText(USAQIVALUE);
//			USAQILEVELView.setText("中等");
//			USAQIVALUEView.setTextColor(Color.rgb(255,255,0));
//			USAQILEVELView.setTextColor(Color.rgb(255,255,0));
//
//		}else if (Integer.valueOf(USAQIVALUETemp) <= 150 && Integer.valueOf(USAQIVALUETemp) >= 101){
//			USAQIVALUEView.setText(USAQIVALUE);
//			USAQIVALUEView.setTextColor(Color.rgb(255,165,0));
//			USAQILEVELView.setText("对敏感人群不健康");
//			USAQILEVELView.setTextColor(Color.rgb(255,165,0));
//
//		}else if (Integer.valueOf(USAQIVALUETemp) <= 200 && Integer.valueOf(USAQIVALUETemp) >= 151){
//			USAQIVALUEView.setTextColor(Color.RED);
//			USAQIVALUEView.setText(USAQIVALUE);
//			USAQILEVELView.setText("不健康");
//			USAQILEVELView.setTextColor(Color.RED);
//
//		}else if (Integer.valueOf(USAQIVALUETemp) <= 300 && Integer.valueOf(USAQIVALUETemp) >= 201){
//			USAQIVALUEView.setText(USAQIVALUE);
//			USAQILEVELView.setText("非常不健康");
//			USAQIVALUEView.setTextColor(Color.rgb(176,48,96));
//			USAQILEVELView.setTextColor(Color.rgb(176,48,96));
//
//		}else if (Integer.valueOf(USAQIVALUETemp) <= 500 && Integer.valueOf(USAQIVALUETemp) >= 301){
//			USAQIVALUEView.setText(USAQIVALUE);
//			USAQILEVELView.setText("危险");
//			USAQIVALUEView.setTextColor(Color.rgb(139,69,19));
//			USAQILEVELView.setTextColor(Color.rgb(139,69,19));
//
//		}else{
//			USAQIVALUEView.setText(USAQIVALUE);
//			USAQILEVELView.setText("爆表");
//			USAQIVALUEView.setTextColor(Color.rgb(139,69,19));
//			USAQILEVELView.setTextColor(Color.rgb(139,69,19));
//
//		}
		
		Integer SHAQIVALUETemp = TryParseInt(SHAQIVALUE.toString());
		
		TextView SHAQILEVELView = (TextView)ViewTemp.findViewById(R.id.SHAQILEVEL);

/*		if (SHAQIVALUETemp == null) {
			SHAQIVALUEView.setText("无当前数据");
			SHAQILEVELView.setText(" ");
			SHAQIVALUEView.setTextSize(40);
			
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 50){
			SHAQIVALUEView.setText(SHAQIVALUE);
			SHAQIVALUEView.setTextColor(Color.rgb(0,228,0));	
			SHAQILEVELView.setText(SHAQILEVEL);
			SHAQILEVELView.setTextColor(Color.rgb(0,228,0));
	
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 100 && Integer.valueOf(SHAQIVALUETemp) >= 51){
			SHAQIVALUEView.setText(SHAQIVALUE);
			SHAQIVALUEView.setTextColor(Color.rgb(255,255,0));	
			SHAQILEVELView.setText(SHAQILEVEL);
			SHAQILEVELView.setTextColor(Color.rgb(255,255,0));

		}else if (Integer.valueOf(SHAQIVALUETemp) <= 150 && Integer.valueOf(SHAQIVALUETemp) >= 101){
			SHAQIVALUEView.setText(SHAQIVALUE);
			SHAQIVALUEView.setTextColor(Color.rgb(255,165,0));
			SHAQILEVELView.setText(SHAQILEVEL);
			SHAQILEVELView.setTextColor(Color.rgb(255,165,0));

		}else if (Integer.valueOf(SHAQIVALUETemp) <= 200 && Integer.valueOf(SHAQIVALUETemp) >= 151){
			SHAQIVALUEView.setText(SHAQIVALUE);
			SHAQIVALUEView.setTextColor(Color.RED);	
			SHAQILEVELView.setText(SHAQILEVEL);
			SHAQILEVELView.setTextColor(Color.RED);

		}else if (Integer.valueOf(SHAQIVALUETemp) <= 300 && Integer.valueOf(SHAQIVALUETemp) >= 201){
			SHAQIVALUEView.setText(SHAQIVALUE);
			SHAQIVALUEView.setTextColor(Color.rgb(176,48,96));
			SHAQILEVELView.setText(SHAQILEVEL);
			SHAQILEVELView.setTextColor(Color.rgb(176,48,96));
			
		}else if (Integer.valueOf(SHAQIVALUETemp) <= 500 && Integer.valueOf(SHAQIVALUETemp) >= 301){
			SHAQIVALUEView.setText(SHAQIVALUE);
			SHAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			SHAQILEVELView.setText(SHAQILEVEL);
			SHAQILEVELView.setTextColor(Color.rgb(139,69,19));			

		}else{			
			SHAQIVALUEView.setText(SHAQIVALUE);
			SHAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			SHAQILEVELView.setText("爆表");
			SHAQILEVELView.setTextColor(Color.rgb(139,69,19));					
		}*/

		TextView PrimaryPollutantView = (TextView) ViewTemp.findViewById(R.id.PrimaryPollutant);
		TextView AQIjianyi = (TextView) ViewTemp.findViewById(R.id.AQIjianyi);
		TextView running = (TextView) ViewTemp.findViewById(R.id.running);
		Integer shishitempratureTemp = TryParseInt(SHISHITEMPRATURE.toString());
/*    	PrimaryPollutantView.setText("首要污染物：" + PrimaryPollutant);
		Log.i("SHPM2_5", SHPM2_5);
		if(SHPM2_5.equals(" ") || SHPM2_5.equals("-9999.0"))
			SHAQI = 100001;
		else
			SHAQI = AQIPM25(SHPM2_5);*/
/*		Log.i("Main SHPM2_5 ",SHPM2_5);
		Log.i("Main PM10",PM10);
		Log.i("Main SO2",SO2);
		Log.i("Main NO2",NO2);
		Log.i("Main CO",CO);
		Log.i("Main O3_1H",O3_1H);
		Log.i("Main O3_8H",O3_8H);*/
		temp = AQITool.Conc2AQI(SHPM2_5, PM10, SO2, NO2, CO, O3_1H, O3_8H);
		try {
			JSONObject json = new JSONObject(temp);
			PrimaryPollutantView.setText("首要污染物：" + json.getString("PrimaryPollutant"));
			SHAQI = json.getInt("MaxAQI");
		}catch(JSONException e){
			SHAQI = 100001;
            Log.e("log_tag", "Error parsing data "+e.toString());
		}
		if (SHAQI == 100001) {
			SHAQIVALUEView.setText("当前站点无数据");
			SHAQILEVELView.setText("");
			PrimaryPollutantView.setText("");
			SHAQIVALUEView.setTextSize(40);
		}else if(SHAQI <= 50){
			SHAQIVALUEView.setText(Integer.toString(SHAQI));
			SHAQIVALUEView.setTextColor(Color.rgb(0,228,0));
			SHAQILEVELView.setText("健康");
			SHAQILEVELView.setTextColor(Color.rgb(0,228,0));
			AQIjianyi.setText("空气不错，开窗尽情呼吸吧！");
			AQIjianyi.setTextColor(Color.rgb(0,228,0));
			if(shishitempratureTemp == null) {
				running.setText("可以跑步");
			}else if(Integer.valueOf(shishitempratureTemp) <= 10) {
				running.setText("可以跑步，但温度较低，为" + shishitempratureTemp + "°，请注意保暖");
			}else{
				running.setText("可以跑步");
			}
		}else if (SHAQI <= 100 && SHAQI >= 51){
			SHAQIVALUEView.setText(Integer.toString(SHAQI));
			SHAQILEVELView.setText("中等");
			SHAQIVALUEView.setTextColor(Color.rgb(255,255,0));
			SHAQILEVELView.setTextColor(Color.rgb(255,255,0));
			AQIjianyi.setText("特别敏感的人群应该考虑减少长期或沉重的负荷。");
			AQIjianyi.setTextColor(Color.rgb(255,255,0));
			if(shishitempratureTemp == null) {
				running.setText("正常人群可以跑步");
			}else if(Integer.valueOf(shishitempratureTemp) <= 10) {
				running.setText("正常人群可以跑步，但温度较低，为" + shishitempratureTemp + "°，请注意保暖");
			}else{
				running.setText("正常人群可以跑步");
			}
		}else if (SHAQI <= 150 && SHAQI >= 101){
			SHAQIVALUEView.setText(Integer.toString(SHAQI));
			SHAQILEVELView.setText("对敏感人群不健康");
			SHAQIVALUEView.setTextColor(Color.rgb(255,165,0));
			SHAQILEVELView.setTextColor(Color.rgb(255,165,0));
			AQIjianyi.setText("有心脏或肺部疾病的人、老人和小孩应该减少长期或沉重的负荷。有以上人群的家庭可以关闭门窗，打开空气净化器。");
			AQIjianyi.setTextColor(Color.rgb(255,165,0));
			if(shishitempratureTemp == null) {
				running.setText("正常人群可以跑步");
			}else if(Integer.valueOf(shishitempratureTemp) <= 10) {
				running.setText("正常人群可以跑步，但温度较低，为" + shishitempratureTemp + "°，请注意保暖");
			}else{
				running.setText("正常人群可以跑步");
			}
		}else if (SHAQI <= 200 && SHAQI >= 151){
			SHAQIVALUEView.setText(Integer.toString(SHAQI));
			SHAQILEVELView.setText("不健康");
			SHAQIVALUEView.setTextColor(Color.RED);
			SHAQILEVELView.setTextColor(Color.RED);
			AQIjianyi.setText("有心脏或肺部疾病的人、老人和小孩应该避免长期或沉重的负荷。其他人也应该减少长期或沉重的负荷。请关闭门窗，打开空气净化器。");
			AQIjianyi.setTextColor(Color.RED);
			running.setText("最好不要跑步");
		}else if (SHAQI <= 300 && SHAQI >= 201){
			SHAQIVALUEView.setText(Integer.toString(SHAQI));
			SHAQILEVELView.setText("非常不健康");
			SHAQIVALUEView.setTextColor(Color.rgb(176,48,96));
			SHAQILEVELView.setTextColor(Color.rgb(176,48,96));
			AQIjianyi.setText("非常不健康，有心脏或肺部疾病的人、老人和小孩应该避免所有户外活动。其他人也应该避免长期或沉重的负荷。请关闭门窗，打开空气净化器。");
			AQIjianyi.setTextColor(Color.rgb(176,48,96));
			running.setText("不要跑步");
		}else if (SHAQI <= 500 && SHAQI >= 301){
			SHAQIVALUEView.setText(Integer.toString(SHAQI));
			SHAQILEVELView.setText("危险");
			SHAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			SHAQILEVELView.setTextColor(Color.rgb(139,69,19));
			AQIjianyi.setText("危险，所有人都应该避免户外活动。有心脏或肺病的人、老人和小孩应该保持在室内，减少活动。请关闭门窗，打开空气净化器。");
			AQIjianyi.setTextColor(Color.rgb(139,69,19));
			running.setText("禁止跑步");
		}else{
			SHAQIVALUEView.setText(Integer.toString(SHAQI));
			SHAQILEVELView.setText("爆表了，非常恐怖！");
			SHAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			SHAQILEVELView.setTextColor(Color.rgb(139,69,19));
			AQIjianyi.setText("很恐怖，请关闭门窗，打开空气净化器。");
			AQIjianyi.setTextColor(Color.rgb(139,69,19));
			running.setText("禁止跑步");
		}
    }
    public int Linear(int AQIhigh, int AQIlow, float Conchigh, float Conclow, float Concentration)
	{
		int linear;
		float temp;
		temp = ((Concentration-Conclow)/(Conchigh-Conclow))*(AQIhigh-AQIlow)+AQIlow;
		//Log.i("AQIPM25", Float.toString(temp));
		linear=Math.round(temp);
		return linear;
	}

	public int AQIPM25(String Concentration)
	{
		float c;
		try{
			//Log.i("AQIPM25",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQIPM25", Float.toString(Conc));
			c = (float)(Math.floor(10*Conc))/10;
			//Log.i("AQIPM25", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
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
    private void loadAQIdetail() {
		ViewTemp = ViewAQIdetail;
				Integer[] Resourcetemp=new Integer[]{
				R.id.textView0,R.id.textView1,R.id.textView2,
				R.id.textView3,R.id.textView4,R.id.textView5,
				R.id.textView6,R.id.textView7,R.id.textView8,
				R.id.textView9,R.id.textView10,R.id.textView11,
				R.id.textView12,R.id.textView13,R.id.textView14,
				R.id.textView15,R.id.textView16,R.id.textView17,
				R.id.textView18,R.id.textView19,R.id.textView20
		};
		String temp = "";
		TextView textViewTemp;
		int index = 0;
		String jsonTemp = "";

		temp = AQITool.Conc2AQI(SHPM2_5, PM10, SO2, NO2, CO, O3_1H, O3_8H);
		try {
			JSONObject json = new JSONObject(temp);
			for(int i = 0; i < json.getJSONArray("Content").length(); i++) {
				textViewTemp = (TextView) ViewTemp.findViewById(Resourcetemp[index++]);

				jsonTemp = json.getJSONArray("Content").getJSONObject(i).getString("value");
				if(jsonTemp.equals("100001") || jsonTemp.equals("-9999.0")){
					textViewTemp.setText("-");
				} else {
					textViewTemp.setText(String.valueOf(jsonTemp));
				}
				textViewTemp = (TextView) ViewTemp.findViewById(Resourcetemp[index++]);
				jsonTemp = json.getJSONArray("Content").getJSONObject(i).getString("CHAQI");
				if(jsonTemp.equals("100001")){
					textViewTemp.setText("-");
				} else {
					textViewTemp.setText(String.valueOf(jsonTemp));
					if(json.getInt("CHPrimaryPollutant") == i)
						textViewTemp.setTextColor(Color.RED);
				}
				textViewTemp = (TextView) ViewTemp.findViewById(Resourcetemp[index++]);
				jsonTemp = json.getJSONArray("Content").getJSONObject(i).getString("USAQI");
				if(jsonTemp.equals("100001")){
					textViewTemp.setText("-");
				} else {
					textViewTemp.setText(String.valueOf(jsonTemp));
					if(json.getInt("USPrimaryPollutant") == i)
						textViewTemp.setTextColor(Color.RED);
				}
			}
		}catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
		}
    }
    private void loadtianqi() {
		ViewTemp = Viewtianqi;
		TextView weatherforecast = (TextView) ViewTemp.findViewById(R.id.weatherforecast);
		weatherforecast.setText(CITYAREA + "未来5天天气预报");
		TextView forecast = (TextView) ViewTemp.findViewById(R.id.forecast1);
		ImageView forcasticon = (ImageView) ViewTemp.findViewById(R.id.forecasticon1);

		try {
			forecast.setText(WEATHERFORECASE[12].split("-")[1] + "/" + WEATHERFORECASE[12].split("-")[2] 
								+ "      " + WEATHERFORECASE[10] +"°/" + WEATHERFORECASE[9] + "°"
								+ "\n" + WEATHERFORECASE[8]);
			Integer forcasticontemp = TryParseInt(WEATHERFORECASE[11].replace(" ", ""));
			if(forcasticontemp == null || forcasticontemp > 32) {
				forcasticon.setImageResource(icon[32]);
			}else{
				forcasticon.setImageResource(icon[forcasticontemp]);
			}
			
			forecast = (TextView) ViewTemp.findViewById(R.id.forecast2);
			forcasticon = (ImageView) ViewTemp.findViewById(R.id.forecasticon2);
			forecast.setText(WEATHERFORECASE[17].split("-")[1] + "/" + WEATHERFORECASE[17].split("-")[2] 
								+ "      " + WEATHERFORECASE[15] +"°/" + WEATHERFORECASE[14] + "°"
								+ "\n" + WEATHERFORECASE[13]);
			
			forcasticontemp = TryParseInt(WEATHERFORECASE[16].replace(" ", ""));
			if(forcasticontemp == null || forcasticontemp > 32) {
				forcasticon.setImageResource(icon[32]);
			}else{
				forcasticon.setImageResource(icon[forcasticontemp]);
			}
			
			forecast = (TextView) ViewTemp.findViewById(R.id.forecast3);
			forcasticon = (ImageView) ViewTemp.findViewById(R.id.forecasticon3);
			forecast.setText(WEATHERFORECASE[22].split("-")[1] + "/" + WEATHERFORECASE[22].split("-")[2] 
								+ "      " + WEATHERFORECASE[20] +"°/" + WEATHERFORECASE[19] + "°"
								+ "\n" + WEATHERFORECASE[18]);
			
			forcasticontemp = TryParseInt(WEATHERFORECASE[21].replace(" ", ""));
			if(forcasticontemp == null || forcasticontemp > 32) {
				forcasticon.setImageResource(icon[32]);
			}else{
				forcasticon.setImageResource(icon[forcasticontemp]);
			}
			
			forecast = (TextView) ViewTemp.findViewById(R.id.forecast4);
			forcasticon = (ImageView) ViewTemp.findViewById(R.id.forecasticon4);
			forecast.setText(WEATHERFORECASE[27].split("-")[1] + "/" + WEATHERFORECASE[27].split("-")[2] 
								+ "      " + WEATHERFORECASE[25] +"°/" + WEATHERFORECASE[24] + "°"
								+ "\n" + WEATHERFORECASE[23]);
			
			forcasticontemp = TryParseInt(WEATHERFORECASE[26].replace(" ", ""));
			if(forcasticontemp == null || forcasticontemp > 32) {
				forcasticon.setImageResource(icon[32]);
			}else{
				forcasticon.setImageResource(icon[forcasticontemp]);
			}
			
			forecast = (TextView) ViewTemp.findViewById(R.id.forecast5);
			forcasticon = (ImageView) ViewTemp.findViewById(R.id.forecasticon5);
			forecast.setText(WEATHERFORECASE[32].split("-")[1] + "/" + WEATHERFORECASE[32].split("-")[2] 
								+ "      " + WEATHERFORECASE[30] +"°/" + WEATHERFORECASE[29] + "°"
								+ "\n" + WEATHERFORECASE[28]);
			
			forcasticontemp = TryParseInt(WEATHERFORECASE[31].replace(" ", ""));
			if(forcasticontemp == null || forcasticontemp > 32) {
				forcasticon.setImageResource(icon[32]);
			}else{
				forcasticon.setImageResource(icon[forcasticontemp]);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			Log.e("error", e.getMessage());
		}
    }
    private void loadZixun() {
    	ViewTemp = ViewZixun;
		TextView textzixun = (TextView) ViewTemp.findViewById(R.id.textzixun);
		textzixun.setText("中美执行的环保标准不同导致两者空气质量指数(AQI)相差较大，有关部门解释：发展要有序，慢慢来。"
							+ "\n" + "但我们不得不说：生命是无价的。"
							+ "所以，本软件采用中国发布的污染物数据，根据美国标准"
    						+ "重新计算得出空气质量指数(AQI)，相关建议都是基于这个值给出，仅供参考。" + "\n"
							+ "数据来源：\n    北京：北京环境保护检测中心\n    上海：上海环境监测中心\n    其余城市：中国环境监测总站\n"
    						+ "WHO空气质量准则，请参考：");
    	Integer USAQIVALUETemp = TryParseInt(USAQIVALUE.toString());
    }
    
	
	  //点击弹出左侧菜单的显示方式
  OnClickListener popClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
		// TODO Auto-generated method stub
		getPopupWindow();
		// 这里是位置显示方式,在按钮的左下角
		popupWindow.showAsDropDown(v,50,0);
		// 这里可以尝试其它效果方式,如popupWindow.showAsDropDown(v,
		// (screenWidth-dialgoWidth)/2, 0);
		// popupWindow.showAtLocation(findViewById(R.id.layout),
		// Gravity.CENTER, 0, 0);
		}
  };
  
  protected void initPopuptWindow() {
  	// TODO Auto-generated method stub

  	// 获取自定义布局文件pop.xml的视图
  	View popupWindow_view = getLayoutInflater().inflate(R.layout.pop, null, false);
  	// 创建PopupWindow实例,200,150分别是宽度和高度
  	popupWindow = new PopupWindow(popupWindow_view,  LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT, true);
  	// 设置动画效果
  	popupWindow.setAnimationStyle(R.style.AnimationFade);
	
  	//响应返回键
  	popupWindow.setBackgroundDrawable(new BitmapDrawable());
  	//点击其他地方消失
  	popupWindow_view.setOnTouchListener(new OnTouchListener() {
  	@Override
	    	public boolean onTouch(View v, MotionEvent event) {
	    	// TODO Auto-generated method stub
		    	if (popupWindow != null && popupWindow.isShowing()) {
		    		popupWindow.dismiss();
		    		popupWindow = null;
		    	}
		    	return false;
	    	}
  	});
  	
  	Button changestation = (Button) popupWindow_view.findViewById(R.id.changestation);
  	Button share = (Button) popupWindow_view.findViewById(R.id.share);
	Button check = (Button) popupWindow_view.findViewById(R.id.check);
  	Button about = (Button) popupWindow_view.findViewById(R.id.about);
  	// pop.xml视图里面的控件触发的事件
  	// 打开
  	changestation.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		Intent intent = null;
		    	popupWindow.dismiss();
		    	changestation();
	    	}
  	});

  	share.setOnClickListener(new OnClickListener() {
  		@Override
  		public void onClick(View v) {
	    		popupWindow.dismiss();
	    		share();
  		}
  	});

  	check.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			popupWindow.dismiss();
			checkUpdate();
		}
	});

  	about.setOnClickListener(new OnClickListener() {
  		@Override
  		public void onClick(View v) {
	    		popupWindow.dismiss();
	    		initAboutDialog();
  		}
  	});

  }

  private void initAboutDialog() {
	  View diaView = View.inflate(this, R.layout.about_dialog, null);
	  about_dialog = new Dialog(this, R.style.about_dialog);
	  about_dialog.setContentView(diaView);
	  //点击其他地方消失
	  diaView.setOnTouchListener(new OnTouchListener() {
		  @Override
		  public boolean onTouch(View v, MotionEvent event) {
			  if (about_dialog != null && about_dialog.isShowing()) {
				  about_dialog.dismiss();
				  about_dialog = null;
			  }
			  return false;
		  }
	  });
	  about_dialog.setCanceledOnTouchOutside(true);
	  about_dialog.show();
  }
  private void getPopupWindow() {

  	if (null != popupWindow) {
  		popupWindow.dismiss();
  		return;
  	} else {
  		initPopuptWindow();
  	}
  }
    	
  private void changestation() {
	  Intent intent = null;
	  intent = new Intent(this, StationActivity.class);
	  startActivity(intent);
  }
  private void share() {
	  Intent intent = new Intent("com.successcw.airofrunning.share");
	  sendBroadcast(intent);
  }

  public static int getVersionCode(Context context) {
      int verCode = -1;
      try {
    	  verCode = context.getPackageManager().getPackageInfo(
    			  context.getPackageName(), 0).versionCode;
          //Log.i("getVersionCode", String.valueOf(verCode));
      } catch (NameNotFoundException e) {
          //Log.e("NewVersionUpdate", e.getMessage());
      }
      return verCode;
  }

  private void showNoticeDialog(){
	  Dialog noticeDialog;
      AlertDialog.Builder builder = new Builder(this);
      builder.setTitle("软件版本更新");
      builder.setMessage("有最新的软件包V1.3，快下载更新吧~\n 更新如下：\n" + VERSIONCONTENT.replace(";", "\n"));
      builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              Uri uri = Uri.parse("https://air-of-running.googlecode.com/files/AirOfRunningV"+ VERSIONNAME + ".apk");
              Intent intent = new Intent(Intent.ACTION_VIEW,uri);
              startActivity(intent);
              //showDownloadDialog();
          }
      });
      builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
          }
      });
      noticeDialog = builder.create();
      noticeDialog.show();
  }

  private void showNoNoticeDialog(){
	  Dialog noticeDialog;
      AlertDialog.Builder builder = new Builder(this);
      builder.setTitle("无更新");
      builder.setMessage("已经是最新版了，感谢支持");
      builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              //showDownloadDialog();
          }
      });
/*      builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
          }
      });  */
      noticeDialog = builder.create();
      noticeDialog.show();
  }
	public void checkUpdate() {
		//Log.e("checkUpdate",String.valueOf(VERSIONCODE));
		if(VERSIONCODE > getVersionCode(this)) {
			//Log.i("checkUpdate", "need update!");
			showNoticeDialog();
		}
		else
			showNoNoticeDialog();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i("weatheractivity","onCread");
		setContentView(R.layout.weather);
		Intent intent = getIntent();
		USAQIVALUE = (String) intent.getSerializableExtra("USAQIVALUE");
		USAQITIME = (String) intent.getSerializableExtra("USAQITIME");
//		String []Array = USAQITIME.split(" ");
//		USAQITIME = Array[1];
		SHUPDATEDATE = (String) intent.getSerializableExtra("SHUPDATEDATE");
		SHUPDATETIME = (String) intent.getSerializableExtra("SHUPDATETIME");
		SHAQILEVEL = (String) intent.getSerializableExtra("SHAQILEVEL");
		SHAQIVALUE = (String) intent.getSerializableExtra("SHAQIVALUE");
		SHPM2_5 = (String) intent.getSerializableExtra("SHPM2_5");
		PM10 = (String) intent.getSerializableExtra("PM10");
		SO2 = (String) intent.getSerializableExtra("SO2");
		NO2 = (String) intent.getSerializableExtra("NO2");
		CO = (String) intent.getSerializableExtra("CO");
		O3_1H = (String) intent.getSerializableExtra("O3_1H");
		O3_8H = (String) intent.getSerializableExtra("O3_8H");
		PrimaryPollutant = (String) intent.getSerializableExtra("PrimaryPollutant");
		
		SHISHITEMPRATURE = (String) intent.getSerializableExtra("SHISHITEMPRATURE");
		AIRCONDITION = (String) intent.getSerializableExtra("AIRCONDITION");
		TEMPRATURE = (String) intent.getSerializableExtra("TEMPRATURE");
		WIND = (String) intent.getSerializableExtra("WIND");
		WEATHERICON = (String) intent.getSerializableExtra("WEATHERICON");
		TEMPRATUREUPDATETIME = (String) intent.getSerializableExtra("TEMPRATUREUPDATETIME");		
		WEATHERFORECASE = (String[]) intent.getSerializableExtra("WEATHERFORECASE");
		CITYAREA = (String) intent.getSerializableExtra("CITYAREA");
		STATION = (String) intent.getSerializableExtra("STATION");
		
		VERSIONCODE = (Integer) intent.getSerializableExtra("VERSIONCODE");
		VERSIONNAME = (String) intent.getSerializableExtra("VERSIONNAME");
		VERSIONCONTENT = (String) intent.getSerializableExtra("VERSIONCONTENT");

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.successcw.airofrunning.entity");
		filter.addAction("com.successcw.airofrunning.refresh");
		registerReceiver(receiver, filter);
		//InitTextView();
		InitViewPager();
		ImageButton optionMenu = (ImageButton)findViewById(R.id.option_menu);
		optionMenu.setOnClickListener(popClick);
		ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.GONE);
	
	}
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		//unbindService(conn);
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	@Override
	public void onResume() {
	    super.onResume();
	    //MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
	    super.onPause();
	    //MobclickAgent.onPause(this);
	}
}
