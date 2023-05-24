package javax.management.openmbean;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/ArrayType.class */
public class ArrayType extends OpenType implements Serializable {
    static final long serialVersionUID = 720504429830309770L;
    private int dimension;
    private OpenType elementType;
    private transient Integer myHashCode;
    private transient String myToString;
    static Class class$javax$management$openmbean$TabularData;
    static Class class$javax$management$openmbean$CompositeData;

    public ArrayType(int i, OpenType openType) throws OpenDataException {
        super(buildArrayClassName(i, openType.getClassName()), buildArrayClassName(i, openType.getClassName()), new StringBuffer().append(String.valueOf(i)).append("-dimension array of ").append(openType.getClassName()).toString());
        this.myHashCode = null;
        this.myToString = null;
        this.dimension = i;
        this.elementType = openType;
    }

    private static String buildArrayClassName(int i, String str) throws OpenDataException {
        if (i < 1) {
            throw new IllegalArgumentException("Value of argument dimension must be greater than 0");
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 1; i2 < i; i2++) {
            stringBuffer.append('[');
        }
        stringBuffer.append("[L");
        stringBuffer.append(str);
        stringBuffer.append(';');
        return stringBuffer.toString();
    }

    public int getDimension() {
        return this.dimension;
    }

    public OpenType getElementOpenType() {
        return this.elementType;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        Class cls;
        Class cls2;
        if (obj == null) {
            return false;
        }
        Class<?> cls3 = obj.getClass();
        String name = cls3.getName();
        if (!cls3.isArray()) {
            return false;
        }
        if (getClassName().equals(name)) {
            return true;
        }
        String className = this.elementType.getClassName();
        if (class$javax$management$openmbean$TabularData == null) {
            cls = class$("javax.management.openmbean.TabularData");
            class$javax$management$openmbean$TabularData = cls;
        } else {
            cls = class$javax$management$openmbean$TabularData;
        }
        if (!className.equals(cls.getName())) {
            String className2 = this.elementType.getClassName();
            if (class$javax$management$openmbean$CompositeData == null) {
                cls2 = class$("javax.management.openmbean.CompositeData");
                class$javax$management$openmbean$CompositeData = cls2;
            } else {
                cls2 = class$javax$management$openmbean$CompositeData;
            }
            if (!className2.equals(cls2.getName())) {
                return false;
            }
        }
        try {
            if (!Class.forName(getClassName()).isAssignableFrom(cls3) || !checkElementsType((Object[]) obj, this.dimension)) {
                return false;
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private boolean checkElementsType(Object[] objArr, int i) {
        if (i > 1) {
            for (Object obj : objArr) {
                if (!checkElementsType((Object[]) obj, i - 1)) {
                    return false;
                }
            }
            return true;
        }
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (objArr[i2] != null && !getElementOpenType().isValue(objArr[i2])) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            ArrayType arrayType = (ArrayType) obj;
            if (arrayType.dimension != this.dimension) {
                return false;
            }
            return this.elementType.equals(arrayType.elementType);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.OpenType
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = new Integer(0 + this.dimension + this.elementType.hashCode());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenType
    public String toString() {
        if (this.myToString == null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(getClass().getName());
            stringBuffer.append("(name=");
            stringBuffer.append(getTypeName());
            stringBuffer.append(",dimension=");
            stringBuffer.append(String.valueOf(this.dimension));
            stringBuffer.append(",elementType=");
            stringBuffer.append(this.elementType.toString());
            stringBuffer.append(")");
            this.myToString = stringBuffer.toString();
        }
        return this.myToString;
    }
}
