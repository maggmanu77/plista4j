package com.feistypeach.plista4j;

import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.feistypeach.util.ClassHelper;

public class PListReader {


	@SuppressWarnings({"unchecked"})
	private class PListHandler extends DefaultHandler {

		private List queue = new ArrayList();
		
		private int ignore = 0;
		
		private boolean shouldIgnore() {
			return ignore > 0;
		}
		
		private void ignore() {
			ignore++;
		}
		
		private void dontIgnore() {
			if (ignore > 0)
				ignore--;
		}
		
		public PListHandler(Object target) throws Exception {
			push(target);
		}

		@Override
		public void characters(char[] data, int start, int length) throws SAXException {
			String str = String.valueOf(data, start, length);
			//System.out.println(str);
			if (!shouldIgnore()) {
				push(str);
			}
		}

		@Override
		public void startElement(String uri, String lName, String qName, Attributes attributes) throws SAXException {
			//System.out.println("<" + qName + ">");
			try {
				if (!shouldIgnore() && (qName.equals("dict") || qName.equals("array"))) {
					if (queue.size() > 1) {
						Object fieldOrMethod = peek();
						if (fieldOrMethod != null) {
							if (fieldOrMethod instanceof Field) {
								Field f = (Field) fieldOrMethod;
								push(f.getType().newInstance());
							} else if (fieldOrMethod instanceof Method) {
								Method m = (Method) fieldOrMethod;
								push(m.getParameterTypes()[0].newInstance());
							}
						}
					}	
				} 
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}

		@Override
		public void endElement(String uri, String lName, String qName) throws SAXException {
			//System.out.println("</" + qName + ">");
			if (qName.equals("key") && !shouldIgnore()) {
				Object key = pop();
				Object accessor = accessorForKey(peek(), key);
				if (accessor == null) {
					ignore();
				} else {
					dontIgnore();
					push(accessor);
				}
			}
				
			if (qName.equals("true") && !shouldIgnore())
				push(Boolean.TRUE);
			
			if (qName.equals("false") && !shouldIgnore())
				push(Boolean.FALSE);
			
			if (!qName.equals("key")) {
				if (!shouldIgnore()) {
					addToDict();					
				}
				if (qName.equals("dict") || qName.equals("array")) {
					dontIgnore();
				}
			}
		}
		
		private AccessibleObject accessorForKey(Object context, Object key) {
			if (context != null) {
				Class contextType = context.getClass();
			
				for (Field f : contextType.getDeclaredFields()) {
					f.setAccessible(true);
					if (f.isAnnotationPresent(PListValue.class)) {
						PListValue plv = f.getAnnotation(PListValue.class);
						if (plv.key().equals(key)) {
							return f;
						} else if (f.getName().equals(key)) {
							return f;
						}
					}
				}
				
				for (Method m : contextType.getDeclaredMethods()) {
					m.setAccessible(true);
					if (m.isAnnotationPresent(PListValue.class)) {
						PListValue plv = m.getAnnotation(PListValue.class);
						if (plv.key().equals(key) && ClassHelper.isSetter(m)) {
							return m;
						} else if (m.getName().equalsIgnoreCase("set" + key)) {
							return m;
						}
					}
				}
			}
			
			return null;
		}
		
		private void push(Object obj) {
			queue.add(obj);
		}
		
		private Object pop() {
			Object obj = queue.remove(queue.size()-1);
			return obj;
		}
		
		private Object peek() {
			Object obj = queue.get(queue.size()-1);
			return obj;
		}
		
		private void printQueue() {
			System.out.println(this);
		}

		private void addToDict() {
			printQueue();
			Object value = pop();
			Object accessor = pop();
			Object dict = peek();
			printQueue();
			
			// TODO add to dict
		}
		
		private void addToArray() {
			Object value = pop();
			Object dict = peek();
			// TODO add to array
		}
		
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append("[ ");
			for (Object o : queue) {
				if (o instanceof Field) {
					buf.append("Field(" + ((Field)o).getName()+")");					
				} else 
				if (o instanceof Method) {
					buf.append("Method(" + ((Method)o).getName()+")");
				} else if (o instanceof String) {
					buf.append(o);
				}
				else {
					buf.append(o != null ? o.getClass().getSimpleName() : "null");
				}
				buf.append(" ");
			}
			buf.append("]");
			return buf.toString();
		}

	};
	
	
	
	public <T> T read(Class<T> type, InputStream in) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			Object target = type.newInstance();
			System.out.println(">> Starting ...");
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, new PListHandler(target));
			System.out.println(">> Done.");
		} catch (Throwable err) {
			err.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
}
