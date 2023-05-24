package org.powermock.reflect.internal;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import javax.resource.spi.work.WorkException;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/TypeUtils.class */
public class TypeUtils {
    public static Object getDefaultValue(Class<?> type) {
        return getDefaultValue(type.getName());
    }

    public static Object getDefaultValue(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName == null) {
            return "";
        }
        if (fullyQualifiedTypeName.equals(Byte.TYPE.getName())) {
            return (byte) 0;
        }
        if (fullyQualifiedTypeName.equals(Integer.TYPE.getName())) {
            return 0;
        }
        if (fullyQualifiedTypeName.equals(Short.TYPE.getName())) {
            return (short) 0;
        }
        if (fullyQualifiedTypeName.equals(Long.TYPE.getName())) {
            return 0L;
        }
        if (fullyQualifiedTypeName.equals(Float.TYPE.getName())) {
            return Float.valueOf(0.0f);
        }
        if (fullyQualifiedTypeName.equals(Double.TYPE.getName())) {
            return Double.valueOf((double) Const.default_value_double);
        }
        if (fullyQualifiedTypeName.equals(Boolean.TYPE.getName())) {
            return false;
        }
        if (fullyQualifiedTypeName.equals(Character.TYPE.getName())) {
            return ' ';
        }
        return null;
    }

    public static String getDefaultValueAsString(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName == null) {
            return "";
        }
        if (fullyQualifiedTypeName.equals(Byte.TYPE.getName())) {
            return "(byte) 0";
        }
        if (fullyQualifiedTypeName.equals(Integer.TYPE.getName())) {
            return WorkException.UNDEFINED;
        }
        if (fullyQualifiedTypeName.equals(Short.TYPE.getName())) {
            return "(short) 0";
        }
        if (fullyQualifiedTypeName.equals(Long.TYPE.getName())) {
            return "0L";
        }
        if (fullyQualifiedTypeName.equals(Float.TYPE.getName())) {
            return "0.0F";
        }
        if (fullyQualifiedTypeName.equals(Double.TYPE.getName())) {
            return "0.0D";
        }
        if (fullyQualifiedTypeName.equals(Boolean.TYPE.getName())) {
            return "false";
        }
        if (fullyQualifiedTypeName.equals(Character.TYPE.getName())) {
            return "' '";
        }
        return Jimple.NULL;
    }
}
