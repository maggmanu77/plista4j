package com.feistypeach.plista4j;

import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.feistypeach.util.Base64Util;
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
			if (!shouldIgnore()) {
				push(str);
			}
			debug("'%s' \n\t%s", str, this);
		}

		@Override
		public void startElement(String uri, String lName, String qName, Attributes attributes) throws SAXException {
			try {
				if (!shouldIgnore() && (qName.equals("dict") || qName.equals("array"))) {
					if (queue.size() > 1) {
						Object fieldOrMethod = peek();
						if (fieldOrMethod != null) {
							if (fieldOrMethod instanceof Field) {
								Field f = (Field) fieldOrMethod;
								Class type = f.getType();
								// TODO check if type is interface and create concrete equivalent
								push(type.newInstance());
							} else if (fieldOrMethod instanceof Method) {
								Method m = (Method) fieldOrMethod;
								Class type = m.getParameterTypes()[0];
								// TODO check if type is interface and create concrete equivalent
								push(type.newInstance());
							}
						}
					}	
				} 
			} catch (Exception e) {
				e.printStackTrace();
			} 
			debug("<%s> \n\t%s", qName, this);
		}

		@Override
		public void endElement(String uri, String lName, String qName) throws SAXException {
			if (queue.size() > 1) {
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
				
				if (qName.equals("data") && !shouldIgnore())
					push(Base64Util.base64ToByteArray(pop().toString()));
				
				if (!qName.equals("key")) {
					if (!shouldIgnore()) {
						Object val = pop();
						Object accessor = peek();
						if (AccessibleObject.class.isAssignableFrom(accessor.getClass()))
							try {
								addToDict(val);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
							addToArray(val);
					}
					if (qName.equals("dict") || qName.equals("array")) {
						dontIgnore();
					}
				}
			}
			debug("</%s> \n\t%s", qName, this);
		}
		
		private void debug(String format, Object...args) {
			System.out.println(String.format(format, args));
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

		private void addToDict(Object val) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
			Object accessor = pop();
			Object dict = peek();
			if (accessor instanceof Field) {
				Field f = (Field) accessor;
				f.setAccessible(true);
				
				Class fType = f.getType();
				
				f.set(dict, ClassHelper.convert(val, fType));
			} else if (accessor instanceof Method) {
				Method m = (Method) accessor;
				m.setAccessible(true);
				
				Class pType = m.getParameterTypes()[0];
				m.invoke(dict, ClassHelper.convert(val, pType));
			}
		}
		
		private void addToArray(Object val) {
			Object dict = peek();
			if (dict.getClass().isArray()) {
				// TODO Handle Array types?
			} else if (List.class.isAssignableFrom(dict.getClass())) {
				List list = (List) dict;
				list.add(val);
			}
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
		factory.setValidating(false);
		try {
			Object target = type.newInstance();
			System.out.println(">> Starting ...");
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(in, new PListHandler(target));
			System.out.println(">> Done.");
			return (T)target;
		} catch (Throwable err) {
			err.printStackTrace();
		}
		return null;
	}
	
	
	
	
}
