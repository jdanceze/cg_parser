package javax.management.openmbean;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/CompositeType.class */
public class CompositeType extends OpenType implements Serializable {
    static final long serialVersionUID = -5366242454346948798L;
    private TreeMap nameToDescription;
    private TreeMap nameToType;
    private transient Integer myHashCode;
    private transient String myToString;
    private transient Set myNamesSet;
    static Class class$javax$management$openmbean$CompositeData;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public CompositeType(java.lang.String r7, java.lang.String r8, java.lang.String[] r9, java.lang.String[] r10, javax.management.openmbean.OpenType[] r11) throws javax.management.openmbean.OpenDataException {
        /*
            Method dump skipped, instructions count: 287
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.openmbean.CompositeType.<init>(java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], javax.management.openmbean.OpenType[]):void");
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static void checkForNullElement(Object[] objArr, String str) {
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Argument ").append(str).append("[] cannot be null or empty.").toString());
        }
        for (int i = 0; i < objArr.length; i++) {
            if (objArr[i] == null) {
                throw new IllegalArgumentException(new StringBuffer().append("Argument's element ").append(str).append("[").append(i).append("] cannot be null.").toString());
            }
        }
    }

    private static void checkForEmptyString(String[] strArr, String str) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].trim().equals("")) {
                throw new IllegalArgumentException(new StringBuffer().append("Argument's element ").append(str).append("[").append(i).append("] cannot be an empty string.").toString());
            }
        }
    }

    public boolean containsKey(String str) {
        if (str == null) {
            return false;
        }
        return this.nameToDescription.containsKey(str);
    }

    public String getDescription(String str) {
        if (str == null) {
            return null;
        }
        return (String) this.nameToDescription.get(str);
    }

    public OpenType getType(String str) {
        if (str == null) {
            return null;
        }
        return (OpenType) this.nameToType.get(str);
    }

    public Set keySet() {
        if (this.myNamesSet == null) {
            this.myNamesSet = Collections.unmodifiableSet(this.nameToDescription.keySet());
        }
        return this.myNamesSet;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return equals(((CompositeData) obj).getCompositeType());
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            CompositeType compositeType = (CompositeType) obj;
            if (!getTypeName().equals(compositeType.getTypeName()) || !this.nameToType.equals(compositeType.nameToType)) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.OpenType
    public int hashCode() {
        if (this.myHashCode == null) {
            int hashCode = 0 + getTypeName().hashCode();
            for (String str : this.nameToDescription.keySet()) {
                hashCode = hashCode + str.hashCode() + this.nameToType.get(str).hashCode();
            }
            this.myHashCode = new Integer(hashCode);
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
            stringBuffer.append(",items=(");
            int i = 0;
            for (String str : this.nameToType.keySet()) {
                if (i > 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append("(itemName=");
                stringBuffer.append(str);
                stringBuffer.append(",itemType=");
                stringBuffer.append(new StringBuffer().append(this.nameToType.get(str).toString()).append(")").toString());
                i++;
            }
            stringBuffer.append("))");
            this.myToString = stringBuffer.toString();
        }
        return this.myToString;
    }
}
