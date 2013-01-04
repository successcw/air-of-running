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
	private ViewPager mPager;//ҳ������
    private List<View> listViews; // Tabҳ���б�
    private TextView t1, t2, t3;// ҳ��ͷ��
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
	String SHISHITEMPRATURE = "";
	String AIRCONDITION = "";
	String TEMPRATURE = "";
	String WIND = "";
	String WEATHERICON = "";
	String TEMPRATUREUPDATETIME = "";
    
    /**
     * ��ʼ��ͷ��
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
     * ͷ��������
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
     * ��ʼ��ViewPager
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
     * ViewPager������
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
     * ҳ���л�����
*/
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
            case 0:
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
    		    R.drawable.b_30,R.drawable.b_31
    	};
		
    	TextView shishitemprature = (TextView) ViewTemp.findViewById(R.id.shishitemprature);
    	shishitemprature.setText(SHISHITEMPRATURE);

    	TextView aircondition = (TextView) ViewTemp.findViewById(R.id.aircondition);
    	aircondition.setText(AIRCONDITION);   	     	
    	
    	TextView temprature = (TextView) ViewTemp.findViewById(R.id.temprature);
    	temprature.setText(TEMPRATURE);       	

    	TextView wind = (TextView) ViewTemp.findViewById(R.id.wind);
    	wind.setText(WIND);  

    	ImageView weathericon = (ImageView) ViewTemp.findViewById(R.id.weathericon);
    	weathericon.setImageResource(icon[Integer.valueOf(WEATHERICON)]);
    	
    	TextView tempratureupdatetime = (TextView) ViewTemp.findViewById(R.id.tempratureupdatetime);
    	tempratureupdatetime.setText("����" + TEMPRATUREUPDATETIME + "����");     	
    	
		TextView US = (TextView) ViewTemp.findViewById(R.id.USupdatetime);
		US.setText("����" + USAQITIME + "����  " );
		
		TextView USAQIVALUEView = (TextView)ViewTemp.findViewById(R.id.USAQIVALUE);
		TextView SHView = (TextView)ViewTemp.findViewById(R.id.SHupdatetime);
		SHView.setText("����" + SHUPDATETIME + "����" );

		TextView SHAQIVALUEView = (TextView)ViewTemp.findViewById(R.id.SHAQIVALUE);
	
		TextView USAQILEVELView = (TextView)ViewTemp.findViewById(R.id.USAQILEVEL);
		
		Integer USAQIVALUETemp = TryParseInt(USAQIVALUE.toString());
		
		//update US AQI
		if (USAQIVALUETemp == null) {
			USAQIVALUEView.setText("AQI���ݼ��ش������Ժ�����");
			USAQILEVELView.setText(" ");
		}else if(Integer.valueOf(USAQIVALUETemp) <= 50){
			USAQIVALUEView.setText(USAQIVALUE);
			USAQILEVELView.setText("����");
			USAQIVALUEView.setTextColor(Color.rgb(0,228,0));
			USAQILEVELView.setTextColor(Color.rgb(0,228,0));
			
		}else if (Integer.valueOf(USAQIVALUETemp) <= 100 && Integer.valueOf(USAQIVALUETemp) >= 51){
			USAQIVALUEView.setText(USAQIVALUE);	
			USAQILEVELView.setText("�е�");
			USAQIVALUEView.setTextColor(Color.rgb(255,255,0));
			USAQILEVELView.setTextColor(Color.rgb(255,255,0));
			
		}else if (Integer.valueOf(USAQIVALUETemp) <= 150 && Integer.valueOf(USAQIVALUETemp) >= 101){
			USAQIVALUEView.setText(USAQIVALUE);
			USAQIVALUEView.setTextColor(Color.rgb(255,165,0));
			USAQILEVELView.setText("��������Ⱥ������");
			USAQILEVELView.setTextColor(Color.rgb(255,165,0));
			
		}else if (Integer.valueOf(USAQIVALUETemp) <= 200 && Integer.valueOf(USAQIVALUETemp) >= 151){
			USAQIVALUEView.setTextColor(Color.RED);
			USAQIVALUEView.setText(USAQIVALUE);
			USAQILEVELView.setText("������");
			USAQILEVELView.setTextColor(Color.RED);
			
		}else if (Integer.valueOf(USAQIVALUETemp) <= 300 && Integer.valueOf(USAQIVALUETemp) >= 201){
			USAQIVALUEView.setText(USAQIVALUE);
			USAQILEVELView.setText("�ǳ�������");
			USAQIVALUEView.setTextColor(Color.rgb(176,48,96));
			USAQILEVELView.setTextColor(Color.rgb(176,48,96));

		}else if (Integer.valueOf(USAQIVALUETemp) <= 500 && Integer.valueOf(USAQIVALUETemp) >= 301){
			USAQIVALUEView.setText(USAQIVALUE);
			USAQILEVELView.setText("Σ��");
			USAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			USAQILEVELView.setTextColor(Color.rgb(139,69,19));

		}else{
			USAQIVALUEView.setText(USAQIVALUE);
			USAQILEVELView.setText("����");
			USAQIVALUEView.setTextColor(Color.rgb(139,69,19));
			USAQILEVELView.setTextColor(Color.rgb(139,69,19));

		}
		
		Integer SHAQIVALUETemp = TryParseInt(SHAQIVALUE.toString());
		
		TextView SHAQILEVELView = (TextView)ViewTemp.findViewById(R.id.SHAQILEVEL);
		if (SHAQIVALUETemp == null) {
			SHAQIVALUEView.setText("AQI���ݼ��ش������Ժ�����");
			SHAQILEVELView.setText(" ");
			
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
			SHAQILEVELView.setText("����");
			SHAQILEVELView.setTextColor(Color.rgb(139,69,19));					
		}
    }

    private void loadJianyi() {
    	ViewTemp = ViewJianyi;
    	TextView textjianyi = (TextView) ViewTemp.findViewById(R.id.textjianyi);
    	Integer USAQIVALUETemp = TryParseInt(USAQIVALUE.toString());
		
		//update US AQI
		if (USAQIVALUETemp == null) {
			textjianyi.setText("���ݼ��ش���");
		}else if(Integer.valueOf(USAQIVALUETemp) <= 50){
			textjianyi.setText("�������޽���");		
		}else if (Integer.valueOf(USAQIVALUETemp) <= 100 && Integer.valueOf(USAQIVALUETemp) >= 51){
			textjianyi.setText("�еȣ��ر����е���ȺӦ�ÿ��Ǽ��ٳ��ڻ���صĸ��ɡ�");					
		}else if (Integer.valueOf(USAQIVALUETemp) <= 150 && Integer.valueOf(USAQIVALUETemp) >= 101){
			textjianyi.setText("��������Ⱥ���������������β��������ˡ����˺�С��Ӧ�ü��ٳ��ڻ���صĸ��ɡ�");
		}else if (Integer.valueOf(USAQIVALUETemp) <= 200 && Integer.valueOf(USAQIVALUETemp) >= 151){
			textjianyi.setText("���������������β��������ˡ����˺�С��Ӧ�ñ��ⳤ�ڻ���صĸ��ɡ�������ҲӦ�ü��ٳ��ڻ���صĸ��ɡ�");
		}else if (Integer.valueOf(USAQIVALUETemp) <= 300 && Integer.valueOf(USAQIVALUETemp) >= 201){
			textjianyi.setText("�ǳ����������������β��������ˡ����˺�С��Ӧ�ñ������л�����������ҲӦ�ñ��ⳤ�ڻ���صĸ��ɡ�");
		}else if (Integer.valueOf(USAQIVALUETemp) <= 500 && Integer.valueOf(USAQIVALUETemp) >= 301){
			textjianyi.setText("Σ�գ������˶�Ӧ�ñ��⻧�����������β����ˡ����˺�С��Ӧ�ñ��������ڣ����ٻ��");
		}else{
			textjianyi.setText("�����ˣ��ǳ��ֲ���");
		}
    }
    private void loadZixun() {
    	ViewTemp = ViewZixun;
    	TextView textjianyi = (TextView) ViewTemp.findViewById(R.id.textzixun);
    	textjianyi.setText("�Ϻ�pm2.5�ɼ����������¹ݲɼ���������������������ִ�еı�׼��ͬ���Ϻ���������������֯��WHO���� �� ʱ �� Ŀ �� -1���������ù� �� ʱ �� Ŀ �� -3 ����ϸ����ͼ��"
    						+ "����ǵ������߿�������ָ��(AQI)���ϴ��ԭ�������Ϻ�������pm2.5�ɼ�����࣬���������ϱ��������¹����ݸ��ܴ�����ʵֵ������������Ϻ�������pm2.5����,����������׼"
    						+ "���¼���ó���������ָ��(AQI)��������ؽ��鶼�ǻ������ֵ����"
    						+ "WHO��������׼����ο���http://apps.who.int/iris/bitstream/10665/69477/3/WHO_SDE_PHE_OEH_06.02_chi.pdf");
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
		
		SHISHITEMPRATURE = (String) intent.getSerializableExtra("SHISHITEMPRATURE");
		AIRCONDITION = (String) intent.getSerializableExtra("AIRCONDITION");
		TEMPRATURE = (String) intent.getSerializableExtra("TEMPRATURE");
		WIND = (String) intent.getSerializableExtra("WIND");
		WEATHERICON = (String) intent.getSerializableExtra("WEATHERICON");
		TEMPRATUREUPDATETIME = (String) intent.getSerializableExtra("TEMPRATUREUPDATETIME");		
		

		InitTextView();
		InitViewPager();

	}
}