package com.successcw.airofrunning.nettool;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XMLHandler extends DefaultHandler {

	String elementValue = null;
	Boolean elementOn = false;
	public static XMLGettersSetters data = null;

	public static XMLGettersSetters getXMLData() {
		return data;
	}

	public static void setXMLData(XMLGettersSetters data) {
		XMLHandler.data = data;
	}

	/** 
	 * This will be called when the tags of the XML starts.
	 **/
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		elementOn = true;

		if (localName.equals("weather"))
		{
			//Log.i("XML","get weather");
			data = new XMLGettersSetters();
			data.setDescription(attributes.getValue("pubdate"));
		} 
		if (localName.equals("condition"))
		{
			//Log.i("XML","get condition");
			data.setDescription(attributes.getValue("temp"));
			data.setDescription(attributes.getValue("wind"));
		}
		if (localName.equals("foreca"))
		{
			//Log.i("XML","get foreca");
			data.setDescription(attributes.getValue("text"));
			data.setDescription(attributes.getValue("high"));
			data.setDescription(attributes.getValue("low"));
			data.setDescription(attributes.getValue("code2"));
			data.setDescription(attributes.getValue("date"));
		}
	}

	/** 
	 * This will be called when the tags of the XML end.
	 **/
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		elementOn = false;

		/** 
		 * Sets the values after retrieving the values from the XML tags
		 * */ 
		if (localName.equalsIgnoreCase("condition")) {
			
			//Log.i("XML", "get condition");
			//data.setDescription(elementValue);
		}
	}

	/** 
	 * This is called to get the tags value
	 **/
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (elementOn) {
			elementValue = new String(ch, start, length);
			elementOn = false;
		}

	}

}
