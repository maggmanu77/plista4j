package com.feistypeach.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassHelper {

	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set"))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		if (method.getReturnType() != void.class)
			return false;
		return true;
	}
	
	public static boolean isGetter(Method method) {
		String name = method.getName();
		if (!(name.startsWith("get") || name.startsWith("is") || name.startsWith("has")))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (method.getReturnType() == void.class)
			return false;
		return true;
	}
	
	public static String getName(Method method) {
		if (isGetter(method) || isSetter(method)) {
			String name = method.getName();
			if (name.startsWith("get") || name.startsWith("has") || name.startsWith("set")) {
				name = name.substring(3);
			} else {
				name = name.substring(2);
			}
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Object convert(Object val, Class type) {
		if (val.getClass().isAssignableFrom(type) == false) {
			if (type.equals(Date.class)) {
				try {
					val = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").parse(val.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				Method valueOf;
				try {
					valueOf = type.getMethod("valueOf", String.class);
					val = valueOf.invoke(type, val.toString());
				} catch (Exception e) {
					try {
						Constructor constructor = type.getConstructor(String.class);
						val = constructor.newInstance(val);
					} catch (Exception e2) {
						
					}
				} 
				
					
			}
		}
		return val;
	}
}
