package javax.management.openmbean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenType.class */
public abstract class OpenType implements Serializable {
    static final long serialVersionUID = -9195195325186646468L;
    public static final String[] ALLOWED_CLASSNAMES;
    private String className;
    private String description;
    private String typeName;
    private transient boolean isArray;
    static Class class$javax$management$openmbean$CompositeData;
    static Class class$javax$management$openmbean$TabularData;

    public abstract boolean isValue(Object obj);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract String toString();

    static {
        Class cls;
        Class cls2;
        String[] strArr = new String[16];
        strArr[0] = "java.lang.Void";
        strArr[1] = JavaBasicTypes.JAVA_LANG_BOOLEAN;
        strArr[2] = JavaBasicTypes.JAVA_LANG_CHARACTER;
        strArr[3] = JavaBasicTypes.JAVA_LANG_BYTE;
        strArr[4] = JavaBasicTypes.JAVA_LANG_SHORT;
        strArr[5] = JavaBasicTypes.JAVA_LANG_INTEGER;
        strArr[6] = JavaBasicTypes.JAVA_LANG_LONG;
        strArr[7] = JavaBasicTypes.JAVA_LANG_FLOAT;
        strArr[8] = JavaBasicTypes.JAVA_LANG_DOUBLE;
        strArr[9] = "java.lang.String";
        strArr[10] = "java.math.BigDecimal";
        strArr[11] = "java.math.BigInteger";
        strArr[12] = "java.util.Date";
        strArr[13] = "javax.management.ObjectName";
        if (class$javax$management$openmbean$CompositeData == null) {
            cls = class$("javax.management.openmbean.CompositeData");
            class$javax$management$openmbean$CompositeData = cls;
        } else {
            cls = class$javax$management$openmbean$CompositeData;
        }
        strArr[14] = cls.getName();
        if (class$javax$management$openmbean$TabularData == null) {
            cls2 = class$("javax.management.openmbean.TabularData");
            class$javax$management$openmbean$TabularData = cls2;
        } else {
            cls2 = class$javax$management$openmbean$TabularData;
        }
        strArr[15] = cls2.getName();
        ALLOWED_CLASSNAMES = strArr;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OpenType(String str, String str2, String str3) throws OpenDataException {
        String str4;
        this.isArray = false;
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument className cannot be null or empty.");
        }
        if (str2 == null || str2.trim().equals("")) {
            throw new IllegalArgumentException("Argument typeName cannot be null or empty.");
        }
        if (str3 == null || str3.trim().equals("")) {
            throw new IllegalArgumentException("Argument description cannot be null or empty.");
        }
        String trim = str.trim();
        String trim2 = str2.trim();
        String trim3 = str3.trim();
        int i = 0;
        while (trim.startsWith("[", i)) {
            i++;
        }
        boolean z = false;
        if (i > 0) {
            str4 = trim.substring(i + 1, trim.length() - 1);
            z = true;
        } else {
            str4 = trim;
        }
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= ALLOWED_CLASSNAMES.length) {
                break;
            } else if (!ALLOWED_CLASSNAMES[i2].equals(str4)) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        if (!z2) {
            throw new OpenDataException(new StringBuffer().append("Argument className=\"").append(trim).append("\" is not one of the allowed Java class names for open data.").toString());
        }
        this.className = trim;
        this.typeName = trim2;
        this.description = trim3;
        this.isArray = z;
    }

    public String getClassName() {
        return this.className;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isArray() {
        return this.isArray;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.isArray = this.className.startsWith("[");
    }
}
