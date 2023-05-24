package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.WeakHashMap;
import org.apache.tools.ant.types.selectors.DateSelector;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/ObjectName.class */
public class ObjectName implements QueryExp, Serializable {
    private static final long oldSerialVersionUID = -5467795090068647408L;
    private static final long newSerialVersionUID = 1081892073854801359L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private static final Property[] _Empty_property_array;
    private static final Hashtable _EmptyPropertyList;
    private transient String _canonicalName;
    private transient Property[] _kp_array;
    private transient Property[] _ca_array;
    private transient Hashtable _propertyList;
    static Class class$java$lang$String;
    static Class class$java$util$Hashtable;
    static Class class$javax$management$ObjectName;
    private transient int _domain_length = 0;
    private transient boolean _domain_pattern = false;
    private transient boolean _property_pattern = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: javax.management.ObjectName$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/ObjectName$1.class */
    public class AnonymousClass1 {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/ObjectName$PropertyLengthCouple.class */
    public static final class PropertyLengthCouple {
        int _key_length;
        int _value_length;

        PropertyLengthCouple(int i, int i2, AnonymousClass1 anonymousClass1) {
            this(i, i2);
        }

        private PropertyLengthCouple(int i, int i2) {
            this._key_length = 0;
            this._value_length = 0;
            this._key_length = i;
            this._value_length = i2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof PropertyLengthCouple) {
                PropertyLengthCouple propertyLengthCouple = (PropertyLengthCouple) obj;
                if (this._key_length != propertyLengthCouple._key_length || this._value_length != propertyLengthCouple._value_length) {
                    return false;
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this._key_length << 5) ^ this._value_length;
        }

        void setLengths(int i, int i2) {
            this._key_length = i;
            this._value_length = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/ObjectName$Property.class */
    public static final class Property {
        private static WeakHashMap _PropertyLengthCouples = new WeakHashMap(89);
        int _key_index;
        PropertyLengthCouple _lengths;

        Property(int i, PropertyLengthCouple propertyLengthCouple) {
            PropertyLengthCouple propertyLengthCouple2;
            this._key_index = i;
            WeakReference weakReference = (WeakReference) _PropertyLengthCouples.get(propertyLengthCouple);
            if (weakReference != null && (propertyLengthCouple2 = (PropertyLengthCouple) weakReference.get()) != null) {
                this._lengths = propertyLengthCouple2;
                return;
            }
            this._lengths = new PropertyLengthCouple(propertyLengthCouple._key_length, propertyLengthCouple._value_length, null);
            _PropertyLengthCouples.put(this._lengths, new WeakReference(this._lengths));
        }

        void setKeyIndex(int i) {
            this._key_index = i;
        }

        String getKeyString(String str) {
            return str.substring(this._key_index, this._key_index + this._lengths._key_length);
        }

        String getValueString(String str) {
            int i = this._key_index + this._lengths._key_length + 1;
            return str.substring(i, i + this._lengths._value_length);
        }
    }

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[6];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("domain", cls);
        if (class$java$util$Hashtable == null) {
            cls2 = class$("java.util.Hashtable");
            class$java$util$Hashtable = cls2;
        } else {
            cls2 = class$java$util$Hashtable;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("propertyList", cls2);
        if (class$java$lang$String == null) {
            cls3 = class$("java.lang.String");
            class$java$lang$String = cls3;
        } else {
            cls3 = class$java$lang$String;
        }
        objectStreamFieldArr[2] = new ObjectStreamField("propertyListString", cls3);
        if (class$java$lang$String == null) {
            cls4 = class$("java.lang.String");
            class$java$lang$String = cls4;
        } else {
            cls4 = class$java$lang$String;
        }
        objectStreamFieldArr[3] = new ObjectStreamField("canonicalName", cls4);
        objectStreamFieldArr[4] = new ObjectStreamField(DateSelector.PATTERN_KEY, Boolean.TYPE);
        objectStreamFieldArr[5] = new ObjectStreamField("propertyPattern", Boolean.TYPE);
        oldSerialPersistentFields = objectStreamFieldArr;
        newSerialPersistentFields = new ObjectStreamField[0];
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged((PrivilegedAction<Object>) new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
        _Empty_property_array = new Property[0];
        _EmptyPropertyList = new Hashtable(1);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0342, code lost:
        r24 = r24 + 1;
        r28 = r24 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x00d9, code lost:
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0335, code lost:
        if (r24 != r0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0341, code lost:
        throw new javax.management.MalformedObjectNameException("ObjectName: unterminated quoted value");
     */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0220 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x022a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void construct(java.lang.String r9) throws javax.management.MalformedObjectNameException {
        /*
            Method dump skipped, instructions count: 1150
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.ObjectName.construct(java.lang.String):void");
    }

    private void construct(String str, Hashtable hashtable) throws MalformedObjectNameException {
        if (str == null) {
            throw new MalformedObjectNameException("ObjectName: domain must be specified");
        }
        if (hashtable == null) {
            throw new MalformedObjectNameException("ObjectName: property list cannot be empty");
        }
        if (!isDomain(str)) {
            throw new MalformedObjectNameException(new StringBuffer().append("ObjectName: Invalid domain -> ").append(str).toString());
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str).append(':');
        this._domain_length = str.length();
        int size = hashtable.size();
        this._kp_array = new Property[size];
        PropertyLengthCouple propertyLengthCouple = new PropertyLengthCouple(-1, -1, null);
        String[] strArr = new String[size];
        Enumeration keys = hashtable.keys();
        HashMap hashMap = new HashMap();
        int i = 0;
        while (keys.hasMoreElements()) {
            if (i > 0) {
                stringBuffer.append(",");
            }
            String str2 = "";
            try {
                str2 = (String) keys.nextElement();
                String str3 = "";
                try {
                    str3 = (String) hashtable.get(str2);
                    int length = stringBuffer.length();
                    checkKey(str2);
                    stringBuffer.append(str2);
                    strArr[i] = str2;
                    stringBuffer.append("=");
                    checkValue(str3);
                    stringBuffer.append(str3);
                    propertyLengthCouple.setLengths(str2.length(), str3.length());
                    addProperty(new Property(length, propertyLengthCouple), i, hashMap, str2);
                    i++;
                } catch (Exception e) {
                    throw new MalformedObjectNameException(new StringBuffer().append("ObjectName: Invalid value `").append(str3).append("'").toString());
                }
            } catch (Exception e2) {
                throw new MalformedObjectNameException(new StringBuffer().append("ObjectName: Invalid key `").append(str2).append("'").toString());
            }
        }
        int length2 = stringBuffer.length();
        char[] cArr = new char[length2];
        stringBuffer.getChars(0, length2, cArr, 0);
        char[] cArr2 = new char[length2];
        System.arraycopy(cArr, 0, cArr2, 0, this._domain_length + 1);
        setCanonicalName(cArr, cArr2, strArr, hashMap, this._domain_length + 1, this._kp_array.length);
    }

    private void addProperty(Property property, int i, HashMap hashMap, String str) throws MalformedObjectNameException {
        if (hashMap.containsKey(str)) {
            throw new MalformedObjectNameException(new StringBuffer().append("ObjectName: key `").append(str).append("' already defined.").toString());
        }
        if (i == this._kp_array.length) {
            Property[] propertyArr = new Property[i + 10];
            System.arraycopy(this._kp_array, 0, propertyArr, 0, i);
            this._kp_array = propertyArr;
        }
        this._kp_array[i] = property;
        hashMap.put(str, property);
    }

    private void setCanonicalName(char[] cArr, char[] cArr2, String[] strArr, HashMap hashMap, int i, int i2) {
        if (this._kp_array != _Empty_property_array) {
            String[] strArr2 = new String[i2];
            Property[] propertyArr = new Property[i2];
            System.arraycopy(strArr, 0, strArr2, 0, i2);
            Arrays.sort(strArr2);
            System.arraycopy(this._kp_array, 0, propertyArr, 0, i2);
            this._kp_array = propertyArr;
            this._ca_array = new Property[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                this._ca_array[i3] = (Property) hashMap.get(strArr2[i3]);
            }
            int i4 = i2 - 1;
            for (int i5 = 0; i5 <= i4; i5++) {
                Property property = this._ca_array[i5];
                int i6 = property._lengths._key_length + property._lengths._value_length + 1;
                System.arraycopy(cArr, property._key_index, cArr2, i, i6);
                property.setKeyIndex(i);
                i += i6;
                if (i5 != i4) {
                    cArr2[i] = ',';
                    i++;
                }
            }
        }
        if (this._property_pattern) {
            if (this._kp_array != _Empty_property_array) {
                int i7 = i;
                i++;
                cArr2[i7] = ',';
            }
            int i8 = i;
            i++;
            cArr2[i8] = '*';
        }
        this._canonicalName = new String(cArr2, 0, i).intern();
    }

    private static final int parseKey(char[] cArr, int i) throws MalformedObjectNameException {
        int i2 = i;
        int i3 = i;
        int length = cArr.length;
        while (true) {
            if (i2 < length) {
                int i4 = i2;
                i2++;
                char c = cArr[i4];
                switch (c) {
                    case '\n':
                    case '*':
                    case ',':
                    case ':':
                    case '?':
                        throw new MalformedObjectNameException(new StringBuffer().append("ObjectName: `").append(c).append("'").append(" Invalid character in key").toString());
                    case '=':
                        i3 = i2 - 1;
                        break;
                    default:
                        if (i2 >= length) {
                            i3 = i2;
                            break;
                        }
                }
            }
        }
        return i3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x010a, code lost:
        r8 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x010f, code lost:
        if (r7 >= r0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0112, code lost:
        r1 = r7;
        r7 = r7 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x011a, code lost:
        if (r5[r1] == ',') goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0126, code lost:
        throw new javax.management.MalformedObjectNameException("ObjectName: Invalid quote.");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final int parseValue(char[] r5, int r6) throws javax.management.MalformedObjectNameException {
        /*
            Method dump skipped, instructions count: 426
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.ObjectName.parseValue(char[], int):int");
    }

    private String checkValue(String str) throws MalformedObjectNameException {
        if (str == null) {
            throw new MalformedObjectNameException("ObjectName: Invalid value (null).");
        }
        int length = str.length();
        if (length == 0) {
            throw new MalformedObjectNameException("ObjectName: Invalid value (empty).");
        }
        char[] charArray = str.toCharArray();
        int parseValue = parseValue(charArray, 0);
        if (parseValue < length) {
            throw new MalformedObjectNameException(new StringBuffer().append("ObjectName: `").append(charArray[parseValue]).append("'").append(" Invalid character in value").toString());
        }
        return str;
    }

    private String checkKey(String str) throws MalformedObjectNameException {
        if (str == null) {
            throw new MalformedObjectNameException("ObjectName: Invalid key (null)");
        }
        int length = str.length();
        if (length == 0) {
            throw new MalformedObjectNameException("ObjectName: Invalid key (empty)");
        }
        char[] charArray = str.toCharArray();
        int parseKey = parseKey(charArray, 0);
        if (parseKey < length) {
            throw new MalformedObjectNameException(new StringBuffer().append("ObjectName: `").append(charArray[parseKey]).append("'").append(" Invalid character in value").toString());
        }
        return str;
    }

    private static boolean wildmatch(char[] cArr, char[] cArr2, int i, int i2) {
        int length = cArr.length;
        int length2 = cArr2.length;
        while (i2 < length2) {
            int i3 = i2;
            i2++;
            char c = cArr2[i3];
            if (c == '?') {
                i++;
                if (i > length) {
                    return false;
                }
            } else if (c == '*') {
                if (i2 >= length2) {
                    return true;
                }
                while (!wildmatch(cArr, cArr2, i, i2)) {
                    i++;
                    if (i >= length) {
                        return false;
                    }
                }
                return true;
            } else if (i >= length) {
                return false;
            } else {
                int i4 = i;
                i++;
                if (c != cArr[i4]) {
                    return false;
                }
            }
        }
        return i == length;
    }

    private boolean isDomain(String str) {
        if (str == null) {
            return true;
        }
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        int i = 0;
        while (i < length) {
            int i2 = i;
            i++;
            switch (charArray[i2]) {
                case '\n':
                case ':':
                    return false;
                case '*':
                case '?':
                    this._domain_pattern = true;
                    break;
            }
        }
        return true;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            objectInputStream.defaultReadObject();
            return;
        }
        objectInputStream.defaultReadObject();
        try {
            construct((String) objectInputStream.readObject());
        } catch (MalformedObjectNameException e) {
            throw new InvalidObjectException(e.toString());
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("domain", this._canonicalName.substring(0, this._domain_length));
            putFields.put("propertyList", getKeyPropertyList());
            putFields.put("propertyListString", getKeyPropertyListString());
            putFields.put("canonicalName", this._canonicalName);
            putFields.put(DateSelector.PATTERN_KEY, this._domain_pattern || this._property_pattern);
            putFields.put("propertyPattern", this._property_pattern);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this._canonicalName);
    }

    public static ObjectName getInstance(String str) throws MalformedObjectNameException, NullPointerException {
        return new ObjectName(str);
    }

    public static ObjectName getInstance(String str, String str2, String str3) throws MalformedObjectNameException, NullPointerException {
        return new ObjectName(str, str2, str3);
    }

    public static ObjectName getInstance(String str, Hashtable hashtable) throws MalformedObjectNameException, NullPointerException {
        return new ObjectName(str, hashtable);
    }

    public static ObjectName getInstance(ObjectName objectName) throws NullPointerException {
        Class cls;
        Class<?> cls2 = objectName.getClass();
        if (class$javax$management$ObjectName == null) {
            cls = class$("javax.management.ObjectName");
            class$javax$management$ObjectName = cls;
        } else {
            cls = class$javax$management$ObjectName;
        }
        if (cls2.equals(cls)) {
            return objectName;
        }
        try {
            return new ObjectName(objectName.getCanonicalName());
        } catch (MalformedObjectNameException e) {
            throw new IllegalArgumentException(new StringBuffer().append("Unexpected: ").append(e).toString());
        }
    }

    public ObjectName(String str) throws MalformedObjectNameException, NullPointerException {
        construct(str);
    }

    public ObjectName(String str, String str2, String str3) throws MalformedObjectNameException, NullPointerException {
        Hashtable hashtable = new Hashtable(1);
        if (str2 == null || str3 == null) {
            throw new MalformedObjectNameException("ObjectName: Neither the key nor the value can be null");
        }
        hashtable.put(str2, str3);
        construct(str, hashtable);
    }

    public ObjectName(String str, Hashtable hashtable) throws MalformedObjectNameException, NullPointerException {
        if (hashtable == null) {
            throw new MalformedObjectNameException("ObjectName: Hashtable is null");
        }
        if (hashtable.isEmpty()) {
            throw new MalformedObjectNameException("ObjectName: Empty Hashtable");
        }
        construct(str, hashtable);
    }

    public boolean isPattern() {
        return this._domain_pattern || this._property_pattern;
    }

    public boolean isDomainPattern() {
        return this._domain_pattern;
    }

    public boolean isPropertyPattern() {
        return this._property_pattern;
    }

    public String getCanonicalName() {
        return this._canonicalName;
    }

    public String getDomain() {
        return this._canonicalName.substring(0, this._domain_length);
    }

    public String getKeyProperty(String str) throws NullPointerException {
        return (String) _getKeyPropertyList().get(str);
    }

    private final Hashtable _getKeyPropertyList() {
        synchronized (this) {
            if (this._propertyList == null) {
                this._propertyList = new Hashtable();
                for (int length = this._ca_array.length - 1; length >= 0; length--) {
                    Property property = this._ca_array[length];
                    this._propertyList.put(property.getKeyString(this._canonicalName), property.getValueString(this._canonicalName));
                }
            }
        }
        return this._propertyList;
    }

    public Hashtable getKeyPropertyList() {
        return (Hashtable) _getKeyPropertyList().clone();
    }

    public String getKeyPropertyListString() {
        if (this._kp_array.length == 0) {
            return "";
        }
        int length = (this._canonicalName.length() - this._domain_length) - 1;
        if (this._property_pattern) {
            length -= 2;
        }
        char[] cArr = new char[length];
        char[] charArray = this._canonicalName.toCharArray();
        int i = 0;
        int length2 = this._kp_array.length;
        for (int i2 = 0; i2 < length2; i2++) {
            Property property = this._kp_array[i2];
            int i3 = property._lengths._key_length + property._lengths._value_length + 1;
            System.arraycopy(charArray, property._key_index, cArr, i, i3);
            i += i3;
            if (i < length) {
                i++;
                cArr[i] = ',';
            }
        }
        return new String(cArr);
    }

    public String getCanonicalKeyPropertyListString() {
        if (this._ca_array.length == 0) {
            return "";
        }
        int length = this._canonicalName.length();
        if (this._property_pattern) {
            length -= 2;
        }
        return this._canonicalName.substring(this._domain_length + 1, length);
    }

    public String toString() {
        String keyPropertyListString = getKeyPropertyListString();
        StringBuffer stringBuffer = new StringBuffer(this._canonicalName.length());
        stringBuffer.append(this._canonicalName.substring(0, this._domain_length + 1));
        stringBuffer.append(keyPropertyListString);
        if (this._property_pattern) {
            if (keyPropertyListString.length() == 0) {
                stringBuffer.append('*');
            } else {
                stringBuffer.append(",*");
            }
        }
        return stringBuffer.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ObjectName) {
            return this._canonicalName == ((ObjectName) obj)._canonicalName;
        }
        return false;
    }

    public int hashCode() {
        return this._canonicalName.hashCode();
    }

    public static String quote(String str) throws NullPointerException {
        StringBuffer stringBuffer = new StringBuffer("\"");
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '\n':
                    charAt = 'n';
                    break;
                case '\"':
                case '*':
                case '?':
                case '\\':
                    break;
                default:
                    stringBuffer.append(charAt);
            }
            stringBuffer.append('\\');
            stringBuffer.append(charAt);
        }
        stringBuffer.append('\"');
        return stringBuffer.toString();
    }

    public static String unquote(String str) throws IllegalArgumentException, NullPointerException {
        StringBuffer stringBuffer = new StringBuffer();
        int length = str.length();
        if (length < 2 || str.charAt(0) != '\"' || str.charAt(length - 1) != '\"') {
            throw new IllegalArgumentException("Argument not quoted");
        }
        int i = 1;
        while (i < length - 1) {
            char charAt = str.charAt(i);
            if (charAt == '\\') {
                if (i == length - 2) {
                    throw new IllegalArgumentException("Trailing backslash");
                }
                i++;
                charAt = str.charAt(i);
                switch (charAt) {
                    case '\"':
                    case '*':
                    case '?':
                    case '\\':
                        break;
                    case 'n':
                        charAt = '\n';
                        continue;
                    default:
                        throw new IllegalArgumentException(new StringBuffer().append("Bad character '").append(charAt).append("' after backslash").toString());
                }
            } else {
                switch (charAt) {
                    case '\n':
                    case '\"':
                    case '*':
                    case '?':
                        throw new IllegalArgumentException(new StringBuffer().append("Invalid unescaped character '").append(charAt).append("' in the string to unquote").toString());
                }
            }
            stringBuffer.append(charAt);
            i++;
        }
        return stringBuffer.toString();
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws NullPointerException {
        if (objectName == null) {
            throw new NullPointerException();
        }
        if (objectName._domain_pattern || objectName._property_pattern) {
            return false;
        }
        if (this._domain_pattern || this._property_pattern) {
            return matchDomains(objectName) && matchKeys(objectName);
        }
        return this._canonicalName.equals(objectName._canonicalName);
    }

    private final boolean matchDomains(ObjectName objectName) {
        if (this._domain_pattern) {
            return wildmatch(objectName.getDomain().toCharArray(), getDomain().toCharArray(), 0, 0);
        }
        return getDomain().equals(objectName.getDomain());
    }

    private final boolean matchKeys(ObjectName objectName) {
        if (this._property_pattern) {
            Hashtable _getKeyPropertyList = objectName._getKeyPropertyList();
            Property[] propertyArr = this._ca_array;
            String str = this._canonicalName;
            for (int length = propertyArr.length - 1; length >= 0; length--) {
                Property property = propertyArr[length];
                String str2 = (String) _getKeyPropertyList.get(property.getKeyString(str));
                if (str2 == null || !str2.equals(property.getValueString(str))) {
                    return false;
                }
            }
            return true;
        }
        return objectName.getCanonicalKeyPropertyListString().equals(getCanonicalKeyPropertyListString());
    }

    @Override // javax.management.QueryExp
    public void setMBeanServer(MBeanServer mBeanServer) {
    }
}
