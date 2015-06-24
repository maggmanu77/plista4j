package com.feistypeach.plista4j;

import com.feistypeach.util.ClassHelper;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;

public class PListReader {
    public static final String PUBLIC_ID = "-//Apple//DTD PLIST 1.0//EN";
    public static final String SYSTEM_ID = "http://www.apple.com/DTDs/PropertyList-1.0.dtd";


    @SuppressWarnings({"unchecked"})
    private class PListHandler extends DefaultHandler {

        private StringBuilder valueBuilder = new StringBuilder();

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
            valueBuilder.append(str);
        }

        @Override
        public void startElement(String uri, String lName, String qName, Attributes attributes) throws SAXException {
            valueBuilder.setLength(0);
            try {
                if(shouldIgnore()) {
                    ignore();
                } else
                if (!shouldIgnore() && (qName.equals("dict") || qName.equals("array"))) {
                    if (queue.size() > 1) {
                        Object fieldOrMethod = peek();
                        if (fieldOrMethod != null) {
                            if (fieldOrMethod instanceof Field) {
                                Field f = (Field) fieldOrMethod;
                                Class type = f.getType();
                                push(newInstance(type));
                            } else if (fieldOrMethod instanceof Method) {
                                Method m = (Method) fieldOrMethod;
                                Class type = m.getParameterTypes()[0];
                                push(newInstance(type));
                            } else if(queue.get(queue.size()-2) instanceof Field) { //Adds a new object for arrays
                                try {
                                    Type type = ((ParameterizedType)((Field)queue.get(queue.size()-2)).getGenericType()).getActualTypeArguments()[0];
                                    push(Class.forName(type.getTypeName()).newInstance());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if(queue.get(queue.size()-3) instanceof Field) { //Adds a new object for maps
                                try {
                                    Type type = ((ParameterizedType)((Field)queue.get(queue.size()-3)).getGenericType()).getActualTypeArguments()[1];
                                    push(Class.forName(type.getTypeName()).newInstance());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Object newInstance(Class clazz) throws IllegalAccessException, InstantiationException {
            if(clazz.isInterface()) {
                if(clazz.equals(List.class)) {
                    return new ArrayList<>();
                } else if(clazz.equals(Map.class)) {
                    return new HashMap<>();
                }
            }
            return clazz.newInstance();
        }

        @Override
        public void endElement(String uri, String lName, String qName) throws SAXException {
            String value = valueBuilder.toString();
            if (!shouldIgnore() && value.length() > 0) {
                push(value);
                valueBuilder.setLength(0);
            }

            if(shouldIgnore()) {
                dontIgnore();
                if(ignore == 1) {
                    dontIgnore();
                }
                return;
            }

            if (queue.size() > 1) {
                if (qName.equals("key") && !shouldIgnore()) {
                    Object key = pop();
                    Object accessor = accessorForKey(peek(), key);
                    if (accessor == null) {
                        Object map = peek();
                        if(map instanceof Map) {
                            push(key);
                        } else {
                            ignore();
                        }
                    } else {
                        dontIgnore();
                        push(accessor);
                    }
                }

                if (qName.equals("true") && !shouldIgnore())
                    push(Boolean.TRUE);

                if (qName.equals("false") && !shouldIgnore())
                    push(Boolean.FALSE);

                if (qName.equals("data") && !shouldIgnore()) {
                    String data = pop().toString().trim();
                    data = data.replaceAll(" ", "").replaceAll("\t", "").replaceAll("\n", "");
                    push(Base64.getDecoder().decode(data));
                }

                if (!qName.equals("key")) {
                    if (!shouldIgnore()) {
                        Object val = pop();
                        Object accessor = peek();

                        if (accessor instanceof ArrayList && !((ArrayList) accessor).isEmpty() && AccessibleObject.class.isAssignableFrom(((ArrayList) accessor).get(0).getClass())) {
                            try {
                                addToDict(val);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else if (AccessibleObject.class.isAssignableFrom(accessor.getClass())) {
                            try {
                                addToDict(val);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            addToArray(val);
                        }
                    }
                    if (qName.equals("dict") || qName.equals("array")) {
                        dontIgnore();
                    }
                }
            }
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
            return new InputSource(this.getClass().getClassLoader().getResourceAsStream("PropertyList-1.0.dtd"));
        }

        private boolean isPListDTDPresent(String publicId, String systemId) {
            return SYSTEM_ID.equalsIgnoreCase(systemId) ||
                    PUBLIC_ID.equalsIgnoreCase(publicId);
        }

        private Object accessorForKey(Object context, Object key) {
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

                List<Object> setterList = new ArrayList<>();
                for (Method m : contextType.getDeclaredMethods()) {
                    m.setAccessible(true);
                    if (m.isAnnotationPresent(PListValue.class)) {
                        PListValue plv = m.getAnnotation(PListValue.class);
                        if (plv.key().equals(key) && ClassHelper.isSetter(m)) {
                            setterList.add(m);
                        } else if (m.getName().equalsIgnoreCase("set" + key)) {
                            setterList.add(m);
                        }
                    }
                }
                if(setterList.isEmpty()) {
                    return null;
                }
                return setterList;
            }

            return null;
        }

        private void push(Object obj) {
            queue.add(obj);
        }

        private Object pop() {
            Object obj = queue.remove(queue.size() - 1);
            return obj;
        }

        private Object peek() {
            Object obj = queue.get(queue.size() - 1);
            return obj;
        }

        private void addToDict(Object val) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Object accessor = pop();
            if (accessor instanceof ArrayList) {
                for (Object accessorObject : (ArrayList) accessor) {
                    setValueRead(val, accessorObject);
                }
            } else {
                setValueRead(val, accessor);
            }
        }

        private void setValueRead(Object val, Object accessor) throws IllegalAccessException, InvocationTargetException {
            if (AccessibleObject.class.isAssignableFrom(accessor.getClass())) {
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
                    if (val.getClass().equals(pType)) {
                        m.invoke(dict, ClassHelper.convert(val, pType));
                    }
                }
            }
        }

        private void addToArray(Object val) {
            Object dict = peek();
            if (dict.getClass().isArray()) {
                // TODO Handle Array types?
            } else if (List.class.isAssignableFrom(dict.getClass())) {
                List list = (List) dict;
                list.add(val);
            } else if (dict instanceof String){
                String key = (String) pop();
                dict = peek();
                if(Map.class.isAssignableFrom(dict.getClass())) {
                    Map map = (Map) dict;
                    map.put(key, val);
                }
            }
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("[ ");
            for (Object o : queue) {
                if (o instanceof Field) {
                    buf.append("Field(" + ((Field) o).getName() + ")");
                } else if (o instanceof Method) {
                    buf.append("Method(" + ((Method) o).getName() + ")");
                } else if (o instanceof String) {
                    buf.append(o);
                } else {
                    buf.append(o != null ? o.getClass().getSimpleName() : "null");
                }
                buf.append(" ");
            }
            buf.append("]");
            return buf.toString();
        }
    }



    public <T> T read(Class<T> type, InputStream in) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        try {
            Object target = type.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(in, new PListHandler(target));
            return (T)target;
        } catch (Throwable err) {
            err.printStackTrace();
        }
        return null;
    }
}
