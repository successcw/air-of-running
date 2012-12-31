package com.successcw.airofrunning.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class weatheractivity extends Activity  {
	private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2, t3;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private TextView temp;
    private View ViewKongqi;
    private View ViewJianyi;
    private View ViewZixun;
    private View ViewTemp;
	String USAQIVALUE = "";
	String USAQITIME = "";
	String SHUPDATETIME = "";
	String SHUPDATEDATE = "";
	String SHPM2_5 = "";
	String SHAQIVALUE = "";
	String SHAQILEVEL = "";
	byte[] SHLANDSCAPE = null;
    
    /**
     * 初始化头标
*/
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.kongqi);
        t2 = (TextView) findViewById(R.id.jianyi);
        t3 = (TextView) findViewById(R.id.zixun);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
    }
   
    /**
     * 头标点击监听
*/
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
            Log.i("viewpager",Integer.toString(index));
        }
    };
    /**
     * 初始化ViewPager
*/
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        ViewKongqi = mInflater.inflate(R.layout.kongqi, null);
        ViewJianyi = mInflater.inflate(R.layout.jianyi, null);
        ViewZixun = mInflater.inflate(R.layout.zixun, null);
        listViews.add(ViewKongqi);
        listViews.add(ViewJianyi);
        listViews.add(ViewZixun);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        loadKongqi();
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

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                Log.i("PageChange","0");
                loadKongqi();
            	temp = (TextView) findViewById(R.id.kongqi);
            	temp.setBackgroundColor(0x4f000000);
            	temp = (TextView) findViewById(R.id.jianyi);
            	temp.setBackgroundColor(0x4f888888);
            	temp = (TextView) findViewById(R.id.zixun);
            	temp.setBackgroundColor(0x4f888888);
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }          
                Log.i("PageChange","1");
                loadJianyi();
            	temp = (TextView) findViewById(R.id.kongqi);
            	temp.setBackgroundColor(0x4f888888);
            	temp = (TextView) findViewById(R.id.jianyi);
            	temp.setBackgroundColor(0x4f000000);
            	temp = (TextView) findViewById(R.id.zixun);
            	temp.setBackgroundColor(0x4f888888);
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                Log.i("PageChange","2");
                loadZixun();
            	temp = (TextView) findViewById(R.id.kongqi);
            	temp.setBackgroundColor(0x4f888888);
            	temp = (TextView) findViewById(R.id.jianyi);
            	temp.setBackgroundColor(0x4f888888);
            	temp = (TextView) findViewById(R.id.zixun);
            	temp.setBackgroundColor(0x4f000000);                
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            //cursor.startAnimation(animation);
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
    
    private void loadKongqi() {
    	ViewTemp = ViewKongqi;

	    Bitmap bm = BitmapFactory.decodeByteArray(SHLANDSCAPE, 0, SHLANDSCAPE.length);
		
		//ImageView USAQIimageView = (ImageView)findViewById(R.id.USAQIimageView);
		//USAQIimageView.setImageBitmap(bm);
	
		
		TextView US = (TextView) ViewTemp.findViewById(R.id.US);
		US.setText("美国上海领事馆 " + SHUPDATEDATE + " " + USAQITIME + "发布" );
		//US.setTextSize(20);
		
		TextView USAQIVALUEView = (TextView)ViewTemp.findViewById(R.id.USAQIVALUE);
		TextView SHView = (TextView)ViewTemp.findViewById(R.id.SH);
		SHView.setText("上海官方 " + SHUPDATEDATE + " " + SHUPDATETIME + "发布" );
		//SHView.setTextSize(20);
		TextView SHAQIVALUEView = (TextView)ViewTemp.findViewById(R.id.SHAQIVALUE);
	
		ImageView USAQILevel = (ImageView)ViewTemp.findViewById(R.id.usaqilevel);
		
//		USAQILevel.setOnClickListener(new View.OnClickListener() {
//			   //@Override
//		   public void onClick(View v) {
//			  Log.i("USToast", " click");
//			  UStoast = Toast.makeText(weatheractivity.this,   
//			            "美国标准", Toast.LENGTH_LONG);
//			  ImageView imageView = new ImageView(weatheractivity.this);  
//			  imageView.setImageResource(R.drawable.usaqilevel);
//			  
//			  View toastView = UStoast.getView();  
//			  
//			  LinearLayout linearLayout = new LinearLayout(weatheractivity.this);  
//			  linearLayout.setOrientation(LinearLayout.HORIZONTAL);  
//			  
//			  linearLayout.addView(imageView);  
//			  linearLayout.addView(toastView);  
//			  
//			  UStoast.setView(linearLayout);  
//			  UStoast.show();
//		   }        
//		});
		
		TextView SHShiJing = (TextView)ViewTemp.findViewById(R.id.shshijing);
		SHShiJing.setText("实景照片");
		
		ImageView SHLandscape = (ImageView)ViewTemp.findViewById(R.id.shlandscape);
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
		
		ImageView SHAQILevel = (ImageView)ViewTemp.findViewById(R.id.shaqilevel);
//		SHAQILevel.setOnClickListener(new View.OnClickListener() {
//			   //@Override
//		   public void onClick(View v) {
//			  Log.i("SHToast", " click");
//			  SHtoast = Toast.makeText(weatheractivity.this,   
//			            "上海标准", Toast.LENGTH_LONG);
//			  ImageView imageView = new ImageView(weatheractivity.this);  
//			  imageView.setImageResource(R.drawable.shaqilevel);
//			  
//			  View toastView = SHtoast.getView();  
//			  
//			  LinearLayout linearLayout = new LinearLayout(weatheractivity.this);  
//			  linearLayout.setOrientation(LinearLayout.HORIZONTAL);  
//			  
//			  linearLayout.addView(imageView);  
//			  linearLayout.addView(toastView);  
//			  
//			  SHtoast.setView(linearLayout);  
//			  SHtoast.show();
//		   }        
//		});
		
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
    }

    private void loadJianyi() {
    	ViewTemp = ViewJianyi;
    	TextView textjianyi = (TextView) ViewTemp.findViewById(R.id.textjianyi);
    	//textjianyi.setText("abcdefg");
    	Integer USAQIVALUETemp = TryParseInt(USAQIVALUE.toString());
		
		//update US AQI
		if (USAQIVALUETemp == null) {
			textjianyi.setText("数据加载错误。");
		}else if(Integer.valueOf(USAQIVALUETemp) <= 50){
			textjianyi.setText("健康，无建议");		
		}else if (Integer.valueOf(USAQIVALUETemp) <= 100 && Integer.valueOf(USAQIVALUETemp) >= 51){
			textjianyi.setText("中等，特别敏感的人群应该考虑减少长期或沉重的负荷。");					
		}else if (Integer.valueOf(USAQIVALUETemp) <= 150 && Integer.valueOf(USAQIVALUETemp) >= 101){
			textjianyi.setText("对敏感人群不健康，有心脏或肺部疾病的人、老人和小孩应该减少长期或沉重的负荷。");
		}else if (Integer.valueOf(USAQIVALUETemp) <= 200 && Integer.valueOf(USAQIVALUETemp) >= 151){
			textjianyi.setText("不健康，有心脏或肺部疾病的人、老人和小孩应该避免长期或沉重的负荷。其他人也应该减少长期或沉重的负荷。");
		}else if (Integer.valueOf(USAQIVALUETemp) <= 300 && Integer.valueOf(USAQIVALUETemp) >= 201){
			textjianyi.setText("非常不健康，有心脏或肺部疾病的人、老人和小孩应该避免所有户外活动。其他人也应该避免长期或沉重的负荷。");
		}else if (Integer.valueOf(USAQIVALUETemp) <= 500 && Integer.valueOf(USAQIVALUETemp) >= 301){
			textjianyi.setText("危险，所有人都应该避免户外活动。有心脏或肺病的人、老人和小孩应该保持在室内，减少活动。");
		}else{
			textjianyi.setText("爆表了，非常恐怖！");
		}
    }
    private void loadZixun() {
    	ViewTemp = ViewZixun;
    	TextView textjianyi = (TextView) ViewTemp.findViewById(R.id.textzixun);
    	textjianyi.setText("上海pm2.5采集和美国领事馆采集数据相差不大，区别在于中美执行的标准不同。上海采用世界卫生组织（WHO）过 渡 时 期 目 标 -1，美国采用过 渡 时 期 目 标 -3 ，详细见下图，"
    						+ "这个是导致两者空气质量指数(AQI)相差较大的原因。由于上海发布的pm2.5采集点更多，所以理论上比美国领事馆数据更能代表真实值，本软件采用上海发布的pm2.5数据,根据美国标准"
    						+ "重新计算得出空气质量指数(AQI)，所以相关建议都是基于这个值给出"
    						+ "WHO空气质量准则，请参考：http://apps.who.int/iris/bitstream/10665/69477/3/WHO_SDE_PHE_OEH_06.02_chi.pdf");
    	Integer USAQIVALUETemp = TryParseInt(USAQIVALUE.toString());
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("weatheractivity","onCread");
		setContentView(R.layout.weather);
		Intent intent = getIntent();
		USAQIVALUE = (String) intent.getSerializableExtra("USAQIVALUE");
		USAQITIME = (String) intent.getSerializableExtra("USAQITIME");
		String []Array = USAQITIME.split(" ");
		USAQITIME = Array[1];
		SHUPDATEDATE = (String) intent.getSerializableExtra("SHUPDATEDATE");
		SHUPDATETIME = (String) intent.getSerializableExtra("SHUPDATETIME");
		SHAQILEVEL = (String) intent.getSerializableExtra("SHAQILEVEL");
		SHAQIVALUE = (String) intent.getSerializableExtra("SHAQIVALUE");
		SHLANDSCAPE = (byte[]) intent.getSerializableExtra("SHLANDSCAPE");
		InitTextView();
		InitViewPager();
        //loadKongqi();

		
		//InitImageView();
	}
}