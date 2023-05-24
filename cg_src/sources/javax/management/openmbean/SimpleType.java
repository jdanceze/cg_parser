package javax.management.openmbean;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/SimpleType.class */
public final class SimpleType extends OpenType implements Serializable {
    static final long serialVersionUID = 2215577471957694503L;
    public static final SimpleType VOID;
    public static final SimpleType BOOLEAN;
    public static final SimpleType CHARACTER;
    public static final SimpleType BYTE;
    public static final SimpleType SHORT;
    public static final SimpleType INTEGER;
    public static final SimpleType LONG;
    public static final SimpleType FLOAT;
    public static final SimpleType DOUBLE;
    public static final SimpleType STRING;
    public static final SimpleType BIGDECIMAL;
    public static final SimpleType BIGINTEGER;
    public static final SimpleType DATE;
    public static final SimpleType OBJECTNAME;
    private static final SimpleType[] typeArray;
    private transient Integer myHashCode;
    private transient String myToString;
    private static final Map canonicalTypes;

    static {
        SimpleType simpleType;
        SimpleType simpleType2;
        SimpleType simpleType3;
        SimpleType simpleType4;
        SimpleType simpleType5;
        SimpleType simpleType6;
        SimpleType simpleType7;
        SimpleType simpleType8;
        SimpleType simpleType9;
        SimpleType simpleType10;
        SimpleType simpleType11;
        SimpleType simpleType12;
        SimpleType simpleType13;
        SimpleType simpleType14;
        try {
            simpleType = new SimpleType("java.lang.Void");
        } catch (OpenDataException e) {
            simpleType = null;
        }
        VOID = simpleType;
        try {
            simpleType2 = new SimpleType(JavaBasicTypes.JAVA_LANG_BOOLEAN);
        } catch (OpenDataException e2) {
            simpleType2 = null;
        }
        BOOLEAN = simpleType2;
        try {
            simpleType3 = new SimpleType(JavaBasicTypes.JAVA_LANG_CHARACTER);
        } catch (OpenDataException e3) {
            simpleType3 = null;
        }
        CHARACTER = simpleType3;
        try {
            simpleType4 = new SimpleType(JavaBasicTypes.JAVA_LANG_BYTE);
        } catch (OpenDataException e4) {
            simpleType4 = null;
        }
        BYTE = simpleType4;
        try {
            simpleType5 = new SimpleType(JavaBasicTypes.JAVA_LANG_SHORT);
        } catch (OpenDataException e5) {
            simpleType5 = null;
        }
        SHORT = simpleType5;
        try {
            simpleType6 = new SimpleType(JavaBasicTypes.JAVA_LANG_INTEGER);
        } catch (OpenDataException e6) {
            simpleType6 = null;
        }
        INTEGER = simpleType6;
        try {
            simpleType7 = new SimpleType(JavaBasicTypes.JAVA_LANG_LONG);
        } catch (OpenDataException e7) {
            simpleType7 = null;
        }
        LONG = simpleType7;
        try {
            simpleType8 = new SimpleType(JavaBasicTypes.JAVA_LANG_FLOAT);
        } catch (OpenDataException e8) {
            simpleType8 = null;
        }
        FLOAT = simpleType8;
        try {
            simpleType9 = new SimpleType(JavaBasicTypes.JAVA_LANG_DOUBLE);
        } catch (OpenDataException e9) {
            simpleType9 = null;
        }
        DOUBLE = simpleType9;
        try {
            simpleType10 = new SimpleType("java.lang.String");
        } catch (OpenDataException e10) {
            simpleType10 = null;
        }
        STRING = simpleType10;
        try {
            simpleType11 = new SimpleType("java.math.BigDecimal");
        } catch (OpenDataException e11) {
            simpleType11 = null;
        }
        BIGDECIMAL = simpleType11;
        try {
            simpleType12 = new SimpleType("java.math.BigInteger");
        } catch (OpenDataException e12) {
            simpleType12 = null;
        }
        BIGINTEGER = simpleType12;
        try {
            simpleType13 = new SimpleType("java.util.Date");
        } catch (OpenDataException e13) {
            simpleType13 = null;
        }
        DATE = simpleType13;
        try {
            simpleType14 = new SimpleType("javax.management.ObjectName");
        } catch (OpenDataException e14) {
            simpleType14 = null;
        }
        OBJECTNAME = simpleType14;
        typeArray = new SimpleType[]{VOID, BOOLEAN, CHARACTER, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, STRING, BIGDECIMAL, BIGINTEGER, DATE, OBJECTNAME};
        canonicalTypes = new HashMap();
        for (int i = 0; i < typeArray.length; i++) {
            SimpleType simpleType15 = typeArray[i];
            canonicalTypes.put(simpleType15, simpleType15);
        }
    }

    private SimpleType(String str) throws OpenDataException {
        super(str, str, str);
        this.myHashCode = null;
        this.myToString = null;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        if (obj == null) {
            return false;
        }
        return getClassName().equals(obj.getClass().getName());
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleType)) {
            return false;
        }
        return getClassName().equals(((SimpleType) obj).getClassName());
    }

    @Override // javax.management.openmbean.OpenType
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = new Integer(getClassName().hashCode());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenType
    public String toString() {
        if (this.myToString == null) {
            this.myToString = new StringBuffer().append(getClass().getName()).append("(name=").append(getTypeName()).append(")").toString();
        }
        return this.myToString;
    }

    public Object readResolve() throws ObjectStreamException {
        SimpleType simpleType = (SimpleType) canonicalTypes.get(this);
        if (simpleType == null) {
            throw new InvalidObjectException(new StringBuffer().append("Invalid SimpleType: ").append(this).toString());
        }
        return simpleType;
    }
}
