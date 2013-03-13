package com.successcw.airofrunning.tool;

import android.util.Log;

public class AQITool {

    public static int Linear(int AQIhigh, int AQIlow, float Conchigh, float Conclow, float Concentration)
	{
		int linear;
		float temp;
		temp = ((Concentration-Conclow)/(Conchigh-Conclow))*(AQIhigh-AQIlow)+AQIlow;
		Log.i("Linear", Float.toString(temp));
		linear=Math.round(temp);
		return linear;
	}
    public static int CHLinear(int AQIhigh, int AQIlow, float Conchigh, float Conclow, float Concentration)
	{
		int linear;
		float temp;
		temp = ((Concentration-Conclow)/(Conchigh-Conclow))*(AQIhigh-AQIlow)+AQIlow;
		Log.i("Linear", Float.toString(temp));
		linear=Math.round(temp+0.5f);
		return linear;
	}

	public static int USAQIPM25(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
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
			AQI=(int)c;
		}
		return AQI;
	}
	public static int USAQIPM10(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQIPM10",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQIPM10", Float.toString(Conc));
			c = (float)(Math.floor(Conc));
			//Log.i("AQIPM10", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;
		
		if (c>=0 && c<55)
		{
			AQI=Linear(50,0,54,0,c);
		}
		else if (c>=55 && c<155)
		{
			AQI=Linear(100,51,154,55,c);
		}
		else if (c>=155 && c<255)
		{
			AQI=Linear(150,101,254,155,c);
		}
		else if (c>=255 && c<355)
		{
			AQI=Linear(200,151,354,255,c);
		}
		else if (c>=355 && c<425)
		{
			AQI=Linear(300,201,424,355,c);
		}
		else if (c>=425 && c<505)
		{
			AQI=Linear(400,301,504,425,c);
		}
		else if (c>=505 && c<605)
		{
			AQI=Linear(500,401,604,505,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	//美国是8小时标准
	public static int USAQICO(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQICO",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQICO", Float.toString(Conc));
			c = (float)(Math.floor(10*Conc))/10;
			//Log.i("AQICO", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;
		if (c>=0 && c<4.5)
		{
			AQI=Linear(50,0,4.4f,0,c);
		}
		else if (c>=4.5 && c<9.5)
		{
			AQI=Linear(100,51,9.4f,4.5f,c);
		}
		else if (c>=9.5 && c<12.5)
		{
			AQI=Linear(150,101,12.4f,9.5f,c);
		}
		else if (c>=12.5 && c<15.5)
		{
			AQI=Linear(200,151,15.4f,12.5f,c);
		}
		else if (c>=15.5 && c<30.5)
		{
			AQI=Linear(300,201,30.4f,15.5f,c);
		}
		else if (c>=30.5 && c<40.5)
		{
			AQI=Linear(400,301,40.4f,30.5f,c);
		}
		else if (c>=40.5 && c<50.5)
		{
			AQI=Linear(500,401,50.4f,40.5f,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	public static int USAQISO21hr(String Concentration)
	{
		
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQISO21hr",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQISO21hr", Float.toString(Conc));
			c = (float)(Math.floor(Conc));
			//Log.i("AQISO21hr", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}

		int AQI = 0;
		if (c>=0 && c<36)
		{
			AQI=Linear(50,0,35,0,c);
		}
		else if (c>=36 && c<76)
		{
			AQI=Linear(100,51,75,36,c);
		}
		else if (c>=76 && c<186)
		{
			AQI=Linear(150,101,185,76,c);
		}
		else if (c>=186 && c<=304)
		{
			AQI=Linear(200,151,304,186,c);
		}
		else if (c>=304 && c<605)
		{
			AQI=Linear(300,201,604,305,c);
		}
		else if (c>=605 && c<805)
		{
			AQI=Linear(400,301,804,605,c);
		}
		else if (c>=805 && c<=1004)
		{	
			AQI=Linear(500,401,1004,805,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	// 美国已经废除O3 1小时标准
	public static int USAQIOzone8hr(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQIOzone8hr",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQIOzone8hr", Float.toString(Conc));
			c = (float)(Math.floor(Conc))/1000;
			//Log.i("AQIOzone8hr", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;

		if (c>=0 && c<.060)
		{
			AQI=Linear(50,0,0.059f,0,c);
		}
		else if (c>=.060 && c<.076)
		{
			AQI=Linear(100,51,.075f,.060f,c);
		}
		else if (c>=.076 && c<.096)
		{
			AQI=Linear(150,101,.095f,.076f,c);
		}
		else if (c>=.096 && c<.116)
		{
			AQI=Linear(200,151,.115f,.096f,c);
		}
		else if (c>=.116 && c<.375)
		{
			AQI=Linear(300,201,.374f,.116f,c);
		}
		else if (c>=.375 && c<.405)
		{
			AQI=Linear(300,201,.404f,.205f,c);
		}
		else if (c>=.405 && c<.505)
		{
			AQI=Linear(400,301,.504f,.405f,c);
		}
		else if (c>=.505 && c<.605)
		{
			AQI=Linear(500,401,.604f,.505f,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	public static int USAQINO2(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("USAQINO2",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("USAQINO2", Float.toString(Conc));
			c = (float)(Math.floor(Conc))/1000;
			//Log.i("USAQINO2", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;
		if (c>=0 && c<.054)
		{
			AQI=Linear(50,0,.053f,0,c);
		}
		else if (c>=.054 && c<.101)
		{
			AQI=Linear(100,51,.100f,.054f,c);
		}
		else if (c>=.101 && c<.361)
		{
			AQI=Linear(150,101,.360f,.101f,c);
		}
		else if (c>=.361 && c<.650)
		{
			AQI=Linear(200,151,.649f,.361f,c);
		}
		else if (c>=.650 && c<1.250)
		{
			AQI=Linear(300,201,1.249f,.650f,c);
		}
		else if (c>=1.250 && c<1.650)
		{
			AQI=Linear(400,301,1.649f,1.250f,c);
		}
		else if (c>=1.650 && c<=2.049)
		{
			AQI=Linear(500,401,2.049f,1.650f,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	
	public static int CHAQIPM25(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
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
	
		if (c>=0 && c<35.5)
		{
			AQI=Linear(50,0,35.4f,0,c);
		}
		else if (c>=35.5 && c<75.5)
		{
			AQI=Linear(100,51,75.4f,35.5f,c);
		}
		else if (c>=75.5 && c<115.5)
		{
			AQI=Linear(150,101,115.4f,75.5f,c);
		}
		else if (c>=115.5 && c<150.5)
		{
			AQI=Linear(200,151,150.4f,115.5f,c);
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
			AQI=(int)c;
		}
		return AQI;
	}
	public static int CHAQIPM10(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQIPM10",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQIPM10", Float.toString(Conc));
			c = (float)(Math.floor(Conc));
			//Log.i("AQIPM10", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;
		
		if (c>=0 && c<51)
		{
			AQI=Linear(50,0,50,0,c);
		}
		else if (c>=51 && c<151)
		{
			AQI=Linear(100,51,150,51,c);
		}
		else if (c>=151 && c<251)
		{
			AQI=Linear(150,101,250,151,c);
		}
		else if (c>=251 && c<351)
		{
			AQI=Linear(200,151,350,251,c);
		}
		else if (c>=351 && c<421)
		{
			AQI=Linear(300,201,420,351,c);
		}
		else if (c>=421 && c<501)
		{
			AQI=Linear(400,301,500,421,c);
		}
		else if (c>=501 && c<601)
		{
			AQI=Linear(500,401,600,501,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	//中国CO为1小时标准
	public static int CHAQICO(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQICO",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQICO", Float.toString(Conc));
			c = (float)(Math.floor(10*Conc))/10;
			//Log.i("AQICO", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;
		if (c>=0 && c<5.5)
		{
			AQI=CHLinear(50,0,5.4f,0,c);
		}
		else if (c>=5.5 && c<10.5)
		{
			AQI=CHLinear(100,51,10.4f,5.5f,c);
		}
		else if (c>=10.5 && c<35.5)
		{
			AQI=CHLinear(150,101,35.4f,10.5f,c);
		}
		else if (c>=35.5 && c<60.5)
		{
			AQI=CHLinear(200,151,60.4f,35.5f,c);
		}
		else if (c>=60.5 && c<90.5)
		{
			AQI=CHLinear(300,201,90.4f,60.5f,c);
		}
		else if (c>=90.5 && c<120.5)
		{
			AQI=CHLinear(400,301,120.4f,90.5f,c);
		}
		else if (c>=120.5 && c<150.5)
		{
			AQI=CHLinear(500,401,150.4f,120.5f,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	public static int CHAQISO21hr(String Concentration)
	{		
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			Log.i("CHAQISO21hr",Concentration);
			float Conc = Float.parseFloat(Concentration);
			Log.i("CHAQISO21hr", Float.toString(Conc));
			//c = (float)(Math.floor(Conc));
			c = Conc;
			Log.i("CHAQISO21hr", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}

		int AQI = 0;
		if (c>=0 && c<151)
		{
			AQI=CHLinear(50,0,150,0,c);
		}
		else if (c>=151 && c<501)
		{
			AQI=CHLinear(100,51,500,151,c);
		}
		else if (c>=501 && c<651)
		{
			AQI=CHLinear(150,101,650f,501f,c);
		}
		else if (c>=651 && c<=801)
		{
			AQI=CHLinear(200,151,800f,651f,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}

	public static int CHAQIOzone8hr(String Concentration)
	{	
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQIOzone8hr",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQIOzone8hr", Float.toString(Conc));
			c = (float)(Conc)/1000;
			//Log.i("AQIOzone8hr", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;

		if (c>=0 && c<.101)
		{
			AQI=CHLinear(50,0,0.100f,0,c);
		}
		else if (c>=.101 && c<.161)
		{
			AQI=CHLinear(100,51,.160f,.101f,c);
		}
		else if (c>=.161 && c<.216)
		{
			AQI=CHLinear(150,101,.215f,.161f,c);
		}
		else if (c>=.216 && c<.266)
		{
			AQI=CHLinear(200,151,.265f,.216f,c);
		}
		else if (c>=.266 && c<.801)
		{
			AQI=CHLinear(300,201,.800f,.266f,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	public static int CHAQIOzone1hr(String Concentration)
	{	
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("AQIOzone8hr",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("AQIOzone8hr", Float.toString(Conc));
			c = (float)(Conc)/1000;
			//Log.i("AQIOzone8hr", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;

		if (c>=0 && c<.161)
		{
			AQI=CHLinear(50,0,0.160f,0,c);
		}
		else if (c>=.161 && c<.201)
		{
			AQI=CHLinear(100,51,.200f,.161f,c);
		}
		else if (c>=.201 && c<.301)
		{
			AQI=CHLinear(150,101,.300f,.201f,c);
		}
		else if (c>=.301 && c<.401)
		{
			AQI=CHLinear(200,151,.400f,.301f,c);
		}
		else if (c>=.401 && c<.801)
		{
			AQI=CHLinear(300,201,.800f,.201f,c);
		}
		else if (c>=.801 && c<1.001)
		{
			AQI=CHLinear(400,301,1.00f,.801f,c);
		}
		else if (c>=1.001 && c<1.201)
		{
			AQI=CHLinear(500,401,1.200f,1.001f,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	public static int CHAQINO2(String Concentration)
	{
		float c;
		if (Concentration.equals("-9999.0"))
			return 100001;
		try{
			//Log.i("USAQINO2",Concentration);
			float Conc = Float.parseFloat(Concentration);
			//Log.i("USAQINO2", Float.toString(Conc));
			c = (float)(Conc);
			//Log.i("USAQINO2", Float.toString(c));
		} catch(NumberFormatException e){
			//System.err.println(" PM2.5错误 ");
			//Log.e("Weather","PM2.5数据错误");
			return 100001;
		}
		
		int AQI = 0;
		if (c>=0 && c<101)
		{
			AQI=CHLinear(50,0,100,0,c);
		}
		else if (c>=101 && c<201)
		{
			AQI=CHLinear(100,51,200,101,c);
		}
		else if (c>=201 && c<701)
		{
			AQI=CHLinear(150,101,700,201,c);
		}
		else if (c>=701 && c<1201)
		{
			AQI=CHLinear(200,151,1200,701,c);
		}
		else if (c>=1201 && c<2341)
		{
			AQI=CHLinear(300,201,2340,1201,c);
		}
		else if (c>=2341 && c<3091)
		{
			AQI=CHLinear(400,301,3090,2341,c);
		}
		else if (c>=3091 && c<=3841)
		{
			AQI=CHLinear(500,401,3840,3091,c);
		}
		else
		{
			AQI=(int)c;
		}
		return AQI;
	}
	public static String Conc2AQI(String PM2_5, String PM10, String SO2, String NO2, String CO, String O3_1H, String O3_8H) {
		String Json = "";
		int MaxAQI = 0;
		int CHMaxAQI = 0;
		int index = 0;
		String PrimaryPollutant = "";
		int USPrimaryPollutant = 0;
		int CHPrimaryPollutant = 0;
		int USAQI[] = {USAQIPM25(PM2_5),USAQIPM10(PM10), USAQISO21hr(SO2), USAQINO2(NO2), USAQICO(CO), USAQIOzone8hr(O3_8H)};
		int CHAQI[] = {CHAQIPM25(PM2_5),CHAQIPM10(PM10), CHAQISO21hr(SO2), CHAQINO2(NO2), CHAQICO(CO), CHAQIOzone8hr(O3_8H), CHAQIOzone1hr(O3_1H)};
		
		//计算美国标准AQI最大值
		for(int i = 0; i < USAQI.length; i++)
		{
			if(USAQI[i] == 100001)
				continue;
			if(USAQI[i] > MaxAQI) {
				MaxAQI = USAQI[i];
				index = i;
			}
		}
		if(MaxAQI == 0)
			MaxAQI = 100001;
		switch(index) {
		case 0:
			PrimaryPollutant = "细微颗粒物（PM2.5）";
			USPrimaryPollutant = 0;
			break;
		case 1:
			PrimaryPollutant = "颗粒物（PM10）";
			USPrimaryPollutant = 1;
			break;
		case 2:
			PrimaryPollutant = "二氧化硫(SO2)";
			USPrimaryPollutant = 2;
			break;
		case 3:
			PrimaryPollutant = "二氧化氮(NO2)";
			USPrimaryPollutant = 3;
			break;
		case 4:
			PrimaryPollutant = "一氧化碳(CO)";
			USPrimaryPollutant = 4;
			break;
		case 5:
			PrimaryPollutant = "臭氧(8小时平均)";
			USPrimaryPollutant = 5;
			break;		
		}
		
		//计算中国标准AQI最大值
		index = 0;
		for(int i = 0; i < CHAQI.length; i++)
		{
			Log.i("for", String.valueOf(CHAQI[i]));
			if(CHAQI[i] == 100001)
				continue;
			if(CHAQI[i] > CHMaxAQI) {
				CHMaxAQI = CHAQI[i];
				index = i;
			}
		}
		if(CHMaxAQI == 0)
			CHMaxAQI = 100001;
		switch(index) {
		case 0:
			//PrimaryPollutant = "细微颗粒物（PM2.5）";
			CHPrimaryPollutant = 0;
			break;
		case 1:
			//PrimaryPollutant = "颗粒物（PM10）";
			CHPrimaryPollutant = 1;
			break;
		case 2:
			//PrimaryPollutant = "二氧化硫(SO2)";
			CHPrimaryPollutant = 2;
			break;
		case 3:
			//PrimaryPollutant = "二氧化氮(NO2)";
			CHPrimaryPollutant = 3;
			break;
		case 4:
			//PrimaryPollutant = "一氧化碳(CO)";
			CHPrimaryPollutant = 4;
			break;
		case 5:
			//PrimaryPollutant = "臭氧(8小时平均)";
			CHPrimaryPollutant = 5;
			break;
		case 6:
			//PrimaryPollutant = "臭氧(1小时平均)";
			CHPrimaryPollutant = 6;
			break;
		}
		Log.i("US PM2_5", String.valueOf(USAQIPM25(PM2_5)));
		Log.i("US PM10", String.valueOf(USAQIPM10(PM10)));
		Log.i("US SO2", String.valueOf(USAQISO21hr(SO2)));
		Log.i("US NO2", String.valueOf(USAQINO2(NO2)));
		Log.i("US CO", String.valueOf(USAQICO(CO)));
		Log.i("US O3_8H", String.valueOf(USAQIOzone8hr(O3_8H)));
		
		Log.i("CH PM2_5", String.valueOf(CHAQIPM25(PM2_5)));
		Log.i("CH PM10", String.valueOf(CHAQIPM10(PM10)));
		Log.i("CH SO2", String.valueOf(CHAQISO21hr(SO2)));
		Log.i("CH NO2", String.valueOf(CHAQINO2(NO2)));
		Log.i("CH CO", String.valueOf(CHAQICO(CO)));
		Log.i("CH O3_8H", String.valueOf(CHAQIOzone8hr(O3_8H)));
		Log.i("CH O3_1H", String.valueOf(CHAQIOzone1hr(O3_1H)));
		Json = "{\"PrimaryPollutant\":" + "\"" + PrimaryPollutant + "\"" + ", \"MaxAQI\":" + MaxAQI + ",\"USPrimaryPollutant\":" + USPrimaryPollutant +
				",\"CHPrimaryPollutant\":" + CHPrimaryPollutant + ",\"Content\":[" +
				"{\"name\": \"PM2_5\"," + "\"value\":" + PM2_5 + ",\"USAQI\":" + USAQIPM25(PM2_5) + ",\"CHAQI\":" + CHAQIPM25(PM2_5) + "}," +
				"{\"name\": \"PM10\"," + "\"value\":" + PM10 + ",\"USAQI\":" + USAQIPM10(PM10) + ",\"CHAQI\":" + CHAQIPM10(PM10) + "}," +
				"{\"name\": \"SO2\"," + "\"value\":" + SO2 + ",\"USAQI\":" + USAQISO21hr(SO2) + ",\"CHAQI\":" + CHAQISO21hr(SO2) + "}," +
				"{\"name\": \"NO2\"," + "\"value\":" + NO2 + ",\"USAQI\":" + USAQINO2(NO2) + ",\"CHAQI\":" + CHAQINO2(NO2) + "}," +
				"{\"name\": \"CO\"," + "\"value\":" + CO + ",\"USAQI\":" + USAQICO(CO) + ",\"CHAQI\":" + USAQICO(CO) + "}," +
				"{\"name\": \"O3_8H\"," + "\"value\":" + O3_8H + ",\"USAQI\":" + USAQIOzone8hr(O3_8H) + ",\"CHAQI\":" + CHAQIOzone8hr(O3_8H) + "}," +
				"{\"name\": \"O3_1H\"," + "\"value\":" + O3_1H + ",\"USAQI\": 100001" + ",\"CHAQI\":" + CHAQIOzone1hr(O3_1H) + "}" +
				"]}";
		Log.i("json", Json);		
		return Json;
	}
}
