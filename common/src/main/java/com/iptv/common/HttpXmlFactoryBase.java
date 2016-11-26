package com.iptv.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 */
public abstract class HttpXmlFactoryBase<T extends Object> extends HttpFactoryBase<T> {

	@Override
	protected T AnalysisContent(InputStream stream) throws IOException {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			MyContentHandler handler = new MyContentHandler();
			xr.setContentHandler(handler);
			
			//
			ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream(); 
	        int   i=-1; 
	        while((i=stream.read())!=-1){ 
	        baos.write(i); 
	        } 
	        InputStream   in_nocode   =   new   ByteArrayInputStream(baos.toString().getBytes());  
	        InputSource inputSource = new InputSource(in_nocode);
            xr.parse(inputSource);
            return handler.getContent();
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} finally {
			
		}
		return null;
	}
	
	protected abstract void xmlStartElement(String nodeName, T content, Attributes attributes) throws SAXException;
	
	protected abstract void xmlEndElement(String nodeName, String value, T content) throws SAXException;
	
	protected abstract T createContent();
	
	private class MyContentHandler extends DefaultHandler {
		
		private StringBuilder mValue;
		private T mContent;
		
		public T getContent() {
			return mContent;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			mContent = createContent();
			mValue = new StringBuilder(20);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			mValue.append(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			String name = localName;
			if (name.length() <= 0) {
	            name = qName;
	        }
			xmlEndElement(name, mValue.toString(), mContent);
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			String name = localName;
			if (name.length() <= 0) {
	            name = qName;
	        }
			xmlStartElement(name, mContent, attributes);
			mValue.delete(0, mValue.length());
		}
	}

}
