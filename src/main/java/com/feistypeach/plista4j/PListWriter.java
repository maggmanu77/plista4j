package com.feistypeach.plista4j;

import com.feistypeach.util.ClassHelper;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PListWriter {

    private static final String HEADER = "<plist version=\"1.0\">";
//    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">" +
//            "<plist version=\"1.0\">";

    private static final String FOOTER = "</plist>";

    public void write(Object obj, PrintStream out) throws Exception {
        if (obj.getClass().isAnnotationPresent(PListObject.class)) {
            out.print(HEADER);

            Class c = obj.getClass();
            PListObject plo = (PListObject) c.getAnnotation(PListObject.class);
            switch (plo.value()) {
                case DictType:
                    writeDictionary(obj, out);
            }
            out.print(FOOTER);
        }
    }

    private void writeArray(Object source, PrintStream out) throws Exception {
        out.print("<array>");
        for (Object obj : (Collection) source) {
            writeValue(obj, PListType.autoDetect(obj), out);
        }
        out.print("</array>");
    }

    private void writeDictionary(Object source, PrintStream out) throws Exception {
        out.print("<dict>");

        if (source instanceof Map) {
            Map map = (Map) source;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                // Key
                out.print("<key>");
                out.print(key);
                out.print("</key>");

                // Value
                PListType type = PListType.autoDetect(value);
                writeValue(value, type, out);
            }
        } else {
            for (Field f : source.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object value = f.get(source);
                if (value != null && f.isAnnotationPresent(PListValue.class)) {

                    // Key
                    PListValue plv = f.getAnnotation(PListValue.class);
                    out.print("<key>");
                    String key = plv.key().trim();
                    out.print((key.length() == 0) ? f.getName() : key);
                    out.print("</key>");

                    // Value
                    PListType type = (plv.type() == PListType.AutoDetect) ? PListType.autoDetect(value) : plv.type();
                    writeValue(value, type, out);

                }
            }

            for (Method m : source.getClass().getDeclaredMethods()) {
                m.setAccessible(true);
                if (ClassHelper.isGetter(m) && m.isAnnotationPresent(PListValue.class)) {
                    Object value = m.invoke(source, (Object[]) null);
                    if (value != null) {
                        // Key
                        PListValue plv = m.getAnnotation(PListValue.class);
                        out.print("<key>");
                        String key = plv.key().trim();
                        out.print((key.length() == 0) ? ClassHelper.getName(m) : key);
                        out.print("</key>");

                        // Value
                        PListType type = (plv.type() == PListType.AutoDetect) ? PListType.autoDetect(value) : plv.type();
                        writeValue(value, type, out);
                    }
                }
            }
        }
        out.print("</dict>");
    }

    private void writeValue(Object value, PListType type, PrintStream out) throws Exception {
        switch (type) {
            case RealType:
                out.print("<real>");
                out.print(value.toString());
                out.print("</real>");
                break;
            case IntegerType:
                out.print("<integer>");
                out.print(value.toString());
                out.print("</integer>");
                break;
            case BooleanType:
                out.print(Boolean.parseBoolean(value.toString()) ? "<true/>" : "<false/>");
                break;
            case DictType:
                writeDictionary(value, out);
                break;
            case DateType:
                out.print("<date>");
                out.print(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format((Date) value));
                out.print("</date>");
                break;
            case DataType:
                out.print("<data>");
                //out.print("<data><![CDATA[");
                out.print(Base64.getEncoder().encodeToString((byte[]) value));
                //out.print("]]></data>");
                out.print("</data>");
                break;
            case ArrayType:
                writeArray(value, out);
                break;
            case StringType:
            default:
                out.print("<string>");
                out.print(value.toString());
                out.print("</string>");
                break;
        }
    }
}
