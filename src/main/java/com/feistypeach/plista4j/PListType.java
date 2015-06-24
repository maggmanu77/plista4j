package com.feistypeach.plista4j;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public enum PListType {
    StringType,
    RealType,
    IntegerType,
    DateType,
    DataType,
    ArrayType,
    DictType,
    BooleanType,
    AutoDetect;

    public static PListType autoDetect(Object value) {
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
            return dataType.getSimpleName().equals("byte") ? PListType.DataType : PListType.ArrayType;
        } else if (value instanceof Map) {
            return PListType.DictType;
        } else if (value instanceof Collection) {
            return PListType.ArrayType;
        }

        return PListType.StringType;
    }

}
