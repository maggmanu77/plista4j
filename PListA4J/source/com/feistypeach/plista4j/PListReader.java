package com.feistypeach.plista4j;

import java.io.InputStream;
import java.util.concurrent.SynchronousQueue;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PListReader {

	@SuppressWarnings({"unchecked"})
	private class PListHandler extends DefaultHandler {
		
		SynchronousQueue queue = new SynchronousQueue();
		
		public PListHandler(Object target) throws Exception {
			queue.put(target);
		}

		@Override
		public void characters(char[] data, int start, int length) throws SAXException {
			try {
				queue.put(String.valueOf(data, start, length));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (localName.equals("dict")) {
				
			} else if (localName.equals("key")) {
				
			}
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (localName.equals("dict")) {
				
			}
		}
		
	};
	
	public <T> T read(Class<T> type, InputStream in) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			Object target = type.newInstance();
			
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, new PListHandler(target));

		} catch (Throwable err) {
			err.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
}
