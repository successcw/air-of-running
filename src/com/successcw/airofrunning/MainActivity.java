package com.successcw.airofrunning;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	XMLGettersSetters data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View layout = findViewById(R.id.layout);
		try {
			
			/**
			 * Create a new instance of the SAX parser
			 **/
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();

			
			URL url = new URL("http://www.beijingaqifeed.com/shanghaiaqi/shanghaiairrss.xml"); // URL of the XML
			
			/** 
			 * Create the Handler to handle each of the XML tags. 
			 **/
			XMLHandler myXMLHandler = new XMLHandler();
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(url.openStream()));
			
		} catch (Exception e) {
			System.out.println(e);
		}

		data = XMLHandler.data;
		
		TextView loading = (TextView)findViewById(R.id.loading);
		loading.setText(data.getDescription().get(1));
		setContentView(layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
