package com.feistypeach.util;

import java.lang.reflect.Method;

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
}
