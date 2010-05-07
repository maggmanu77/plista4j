package com.feistypeach.plista4j;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.feistypeach.util.Base64Util;

public class PListWriter {
	
	private static final String HEADER = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
		"<plist version=\"1.0\">";
	
	private static final String FOOTER = "</plist>";
	
	private int tabs = 0;
	
	@SuppressWarnings("unchecked")
	public void write(Object obj, PrintStream out) throws Exception {
		if (obj.getClass().isAnnotationPresent(PListObject.class)) {
			out.println(HEADER);

			Class c = obj.getClass();
			PListObject plo = (PListObject) c.getAnnotation(PListObject.class);
			switch (plo.value()) {
			case DictType:
				writeDictionary(obj, out);
			}
			out.println(FOOTER);
		}
	}
	
	private void writeArray(Object source, PrintStream out) throws Exception {
		indent(out);
		out.println("<array>");
		tabs++;
		for (Object obj : (Collection) source) {
			writeValue(obj, autoDetect(obj), out);
		}
		tabs--;
		indent(out);
		out.println("</array>");
	}
	
	private void writeDictionary(Object source, PrintStream out) throws Exception {
		indent(out);
		out.println("<dict>");
		tabs++;
		for (Field f : source.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			Object value = f.get(source);
			if (value != null && f.isAnnotationPresent(PListValue.class)) {
				
				// Key
				indent(out);
				PListValue plv = f.getAnnotation(PListValue.class);
				out.print("<key>");
				String key = plv.key().trim();
				out.print((key.length() == 0)? f.getName() : key);
				out.println("</key>");
				
				// Value
				PListType type = (plv.type() == PListType.AutoDetect)? autoDetect(value) : plv.type();
				writeValue(value, type, out);
				
			}
		}
		tabs--;
		indent(out);
		out.println("</dict>");
	}
	
	private void writeValue(Object value, PListType type, PrintStream out) throws Exception {
		switch (type) {
		case RealType :
			indent(out);
			out.print("<real>");
			out.print(value.toString());
			out.println("</real>");
			break;
		case IntegerType :
			indent(out);
			out.print("<integer>");
			out.print(value.toString());
			out.println("</integer>");
			break;
		case BooleanType :
			indent(out);
			out.println(Boolean.parseBoolean(value.toString())? "<true/>" : "<false/>");
			break;
		case DictType :
			writeDictionary(value, out);
			break;
		case DateType :
			indent(out);
			out.print("<date>");
			out.print(new SimpleDateFormat().format((Date)value));
			out.println("</date>");
			break;
		case DataType :
			indent(out);
			out.print("<data><![CDATA[");
			out.print(Base64Util.byteArrayToBase64((byte[])value, false));
			out.println("]]></data>");
			break;
		case ArrayType :
			writeArray(value, out);
			break;
		case StringType :
		default :
			indent(out);
			out.print("<string>");
			out.print(value.toString());
			out.println("</string>");
			break;
		}
	}
	
	private PListType autoDetect(Object value) {
		if (value instanceof Integer || value instanceof Long) {
			return PListType.IntegerType;
		} else if (value instanceof Float || value instanceof Double) {
			return PListType.RealType;
		} else if (value instanceof String) {
			return PListType.StringType;
		} else if (value.getClass().isAnnotationPresent(PListObject.class)) {
			PListObject plo = value.getClass().getAnnotation(PListObject.class);
			return plo.value();
		} else if (value instanceof Boolean) {
			return PListType.BooleanType;
		} else if (value instanceof Date) {
			return PListType.DateType;
		} else if (value.getClass().isArray()) {
			Class dataType = value.getClass().getComponentType();
			return dataType.getSimpleName().equals("byte")? PListType.DataType : PListType.ArrayType;
		} else if (value instanceof Collection) {
			return PListType.ArrayType;
		}
		
		return PListType.StringType;
	}
	
	private void indent(PrintStream out) {
		for(int ndx=0; ndx < tabs; ndx++) {
			out.print("    ");
		}
	}
}
