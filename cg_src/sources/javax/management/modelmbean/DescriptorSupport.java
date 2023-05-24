package javax.management.modelmbean;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.management.Descriptor;
import javax.management.MBeanException;
import javax.management.RuntimeOperationsException;
import org.apache.tools.ant.taskdefs.optional.ejb.EjbJar;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/DescriptorSupport.class */
public class DescriptorSupport implements Descriptor {
    private static final long oldSerialVersionUID = 8071560848919417985L;
    private static final long newSerialVersionUID = -6292969195866300415L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    HashMap descriptor;
    private static final int DEFAULT_SIZE = 20;
    private static String currClass;
    static Class class$java$util$HashMap;
    static Class class$java$lang$String;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[2];
        if (class$java$util$HashMap == null) {
            cls = class$("java.util.HashMap");
            class$java$util$HashMap = cls;
        } else {
            cls = class$java$util$HashMap;
        }
        objectStreamFieldArr[0] = new ObjectStreamField(EjbJar.NamingScheme.DESCRIPTOR, cls);
        if (class$java$lang$String == null) {
            cls2 = class$("java.lang.String");
            class$java$lang$String = cls2;
        } else {
            cls2 = class$java$lang$String;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("currClass", cls2);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[1];
        if (class$java$util$HashMap == null) {
            cls3 = class$("java.util.HashMap");
            class$java$util$HashMap = cls3;
        } else {
            cls3 = class$java$util$HashMap;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField(EjbJar.NamingScheme.DESCRIPTOR, cls3);
        newSerialPersistentFields = objectStreamFieldArr2;
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
        currClass = "DescriptorSupport";
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public DescriptorSupport() {
        if (tracing()) {
            trace("Descriptor()", "Constructor");
        }
        this.descriptor = new HashMap(20);
    }

    public DescriptorSupport(int i) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace(new StringBuffer().append("Descriptor(maxNumFields = ").append(i).append(")").toString(), "Constructor");
        }
        if (i <= 0) {
            if (tracing()) {
                trace("Descriptor(maxNumFields)", "Illegal arguments: initNumFields <= 0");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor field limit is invalid"), "Exception occured trying to construct a descriptor");
        }
        this.descriptor = new HashMap(i);
    }

    public DescriptorSupport(DescriptorSupport descriptorSupport) {
        if (tracing()) {
            trace("Descriptor(Descriptor)", "Constructor");
        }
        if (descriptorSupport == null || descriptorSupport.descriptor == null) {
            this.descriptor = new HashMap(20);
        } else {
            this.descriptor = new HashMap(descriptorSupport.descriptor);
        }
    }

    public DescriptorSupport(String str) throws MBeanException, RuntimeOperationsException, XMLParseException {
        if (tracing()) {
            trace(new StringBuffer().append("Descriptor(String ='").append(str).append("'").toString(), "Constructor");
        }
        if (str == null) {
            if (tracing()) {
                trace("Descriptor(String = null)", "Illegal arguments");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("String in parameter is null"), "Exception occured trying to construct a descriptor");
        } else if (str.toLowerCase().indexOf("<descriptor>") != 0 || str.toLowerCase().indexOf("</descriptor>") + 13 != str.length()) {
            throw new XMLParseException("No <descriptor> , </descriptor> pair");
        } else {
            this.descriptor = new HashMap(20);
            StringTokenizer stringTokenizer = new StringTokenizer(str, "<> \t\n\r\f");
            boolean z = false;
            boolean z2 = false;
            String str2 = null;
            String str3 = null;
            while (stringTokenizer.hasMoreTokens()) {
                String nextToken = stringTokenizer.nextToken();
                if (nextToken.equalsIgnoreCase("FIELD")) {
                    z = true;
                } else if (nextToken.equalsIgnoreCase("/FIELD")) {
                    if (str2 != null && str3 != null) {
                        setField(str2.substring(str2.indexOf(34) + 1, str2.lastIndexOf(34)).toLowerCase(), str3.substring(str3.indexOf(34) + 1, str3.lastIndexOf(34)));
                    }
                    str2 = null;
                    str3 = null;
                    z = false;
                } else if (nextToken.equalsIgnoreCase("DESCRIPTOR")) {
                    z2 = true;
                } else if (nextToken.equalsIgnoreCase("/DESCRIPTOR")) {
                    z2 = false;
                    str2 = null;
                    str3 = null;
                    z = false;
                } else if (z && z2) {
                    int indexOf = nextToken.indexOf("=");
                    if (indexOf > 0) {
                        String substring = nextToken.substring(0, indexOf);
                        String substring2 = nextToken.substring(indexOf + 1);
                        if (substring.equalsIgnoreCase("NAME")) {
                            str2 = substring2;
                        } else if (substring.equalsIgnoreCase("VALUE")) {
                            str3 = substring2;
                        } else {
                            throw new XMLParseException(new StringBuffer().append("expected a field value, received '").append(nextToken).append("'").toString());
                        }
                    } else {
                        throw new XMLParseException(new StringBuffer().append("expected keyword=value, received '").append(nextToken).append("'").toString());
                    }
                }
            }
            if (tracing()) {
                trace("Descriptor(XMLString)", "Exit");
            }
        }
    }

    public DescriptorSupport(String[] strArr, Object[] objArr) throws RuntimeOperationsException {
        if (tracing()) {
            trace("Descriptor(fieldNames, fieldObjects)", "Constructor");
        }
        if (strArr == null || objArr == null || strArr.length != objArr.length) {
            if (tracing()) {
                trace("Descriptor(String[],Object[])", "Illegal arguments");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("FieldNames or FieldValues are null or invalid"), "Exception occured trying to construct a descriptor");
        }
        this.descriptor = new HashMap(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            setField(strArr[i], objArr[i]);
        }
        if (tracing()) {
            trace("Descriptor(fieldNames, fieldObjects)", "Exit");
        }
    }

    public DescriptorSupport(String[] strArr) {
        if (tracing()) {
            trace("Descriptor(fields)", "Constructor");
        }
        if (strArr == null || strArr.length == 0) {
            this.descriptor = new HashMap(20);
            return;
        }
        this.descriptor = new HashMap(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] != null && !strArr[i].equals("")) {
                int indexOf = strArr[i].indexOf("=");
                if (indexOf < 0) {
                    if (tracing()) {
                        trace("Descriptor(String[])", "Illegal arguments: field does not have '=' as a name and value separator");
                    }
                    throw new RuntimeOperationsException(new IllegalArgumentException("Field in invalid format: no equals sign"), "Exception occured trying to construct a descriptor");
                }
                String substring = strArr[i].substring(0, indexOf);
                String substring2 = indexOf < strArr[i].length() ? strArr[i].substring(indexOf + 1) : null;
                if (substring == null || substring.equals("")) {
                    if (tracing()) {
                        trace("Descriptor(String[])", "Illegal arguments: fieldName is null");
                    }
                    throw new RuntimeOperationsException(new IllegalArgumentException("Field in invalid format: no fieldName"), "Exception occured trying to construct a descriptor");
                }
                setField(substring, substring2);
            }
        }
        if (tracing()) {
            trace("Descriptor(fields)", "Exit");
        }
    }

    @Override // javax.management.Descriptor
    public Object getFieldValue(String str) throws RuntimeOperationsException {
        if (str == null || str.equals("")) {
            if (tracing()) {
                trace("getField()", "Illegal arguments: null field name.");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("Fieldname requested is null"), "Exception occured trying to get a field from a descriptor");
        }
        Object obj = this.descriptor.get(str.toLowerCase());
        if (tracing()) {
            trace(new StringBuffer().append("getField(").append(str).append(")").toString(), new StringBuffer().append("Returns '").append(obj).append("'").toString());
        }
        return obj;
    }

    @Override // javax.management.Descriptor
    public void setField(String str, Object obj) throws RuntimeOperationsException {
        if (str == null || str.equals("")) {
            if (tracing()) {
                trace("setField(String,String)", "Illegal arguments: null or empty field name");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("Fieldname to be set is null or empty"), "Exception occured trying to set a field from a descriptor");
        } else if (obj != null && obj.equals("")) {
            if (tracing()) {
                trace("setField(String,String)", "Illegal arguments: empty field value");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("Field value is empty"), "Exception occured trying to set a field from a descriptor");
        } else {
            String lowerCase = str.toLowerCase();
            if (tracing() && obj != null) {
                trace("setField(fieldName, fieldValue)", new StringBuffer().append("Entry: setting '").append(lowerCase).append("' to '").append(obj).append("'.").toString());
            }
            if (validateField(lowerCase, obj)) {
                this.descriptor.put(lowerCase, obj);
                if (tracing()) {
                    trace("setField(fieldName, fieldValue)", "Exit");
                    return;
                }
                return;
            }
            if (tracing()) {
                trace("setField(fieldName,FieldValue)", "Illegal arguments");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException(new StringBuffer().append("Field value invalid: ").append(lowerCase).append("=").append(obj).toString()), new StringBuffer().append("Field ").append(lowerCase).append("=").append(obj).append(" is invalid. Exception occured trying to set a field from a descriptor").toString());
        }
    }

    @Override // javax.management.Descriptor
    public String[] getFields() {
        if (tracing()) {
            trace("getFields()", "Entry");
        }
        if (this.descriptor == null) {
            return new String[0];
        }
        int size = this.descriptor.size();
        if (size == 0) {
            return new String[0];
        }
        String[] strArr = new String[size];
        Set<Map.Entry> entrySet = this.descriptor.entrySet();
        if (entrySet == null) {
            if (tracing()) {
                trace("getFields()", "No fields found in descriptor");
            }
            return new String[0];
        }
        int i = 0;
        if (tracing()) {
            trace("getFields()", new StringBuffer().append("Returning ").append(size).append(" fields").toString());
        }
        for (Map.Entry entry : entrySet) {
            if (entry == null) {
                if (tracing()) {
                    trace("getFields()", "Element is null");
                }
            } else {
                Object value = entry.getValue();
                if (value == null) {
                    strArr[i] = new StringBuffer().append(entry.getKey()).append("=").toString();
                } else if (value instanceof String) {
                    strArr[i] = new StringBuffer().append(entry.getKey()).append("=").append(value.toString()).toString();
                } else {
                    strArr[i] = new StringBuffer().append(entry.getKey()).append("=(").append(value.toString()).append(")").toString();
                }
            }
            i++;
        }
        if (tracing()) {
            trace("getFields()", "Exit");
        }
        return strArr;
    }

    @Override // javax.management.Descriptor
    public String[] getFieldNames() {
        if (tracing()) {
            trace("getFieldNames()", "Entry");
        }
        if (this.descriptor == null) {
            return new String[0];
        }
        int size = this.descriptor.size();
        String[] strArr = new String[size];
        Set<Map.Entry> entrySet = this.descriptor.entrySet();
        if (entrySet == null || size == 0) {
            if (tracing()) {
                trace("getFieldNames()", "no descriptor fields found");
            }
            return new String[0];
        }
        int i = 0;
        if (tracing()) {
            trace("getFieldNames()", new StringBuffer().append("Returning ").append(size).append(" fields").toString());
        }
        for (Map.Entry entry : entrySet) {
            if (entry == null || entry.getKey() == null) {
                if (tracing()) {
                    trace("getFieldNames()", "Field is null");
                }
            } else {
                strArr[i] = entry.getKey().toString();
            }
            i++;
        }
        if (tracing()) {
            trace("getFieldNames()", "Exit");
        }
        return strArr;
    }

    @Override // javax.management.Descriptor
    public Object[] getFieldValues(String[] strArr) {
        Object[] objArr;
        if (tracing()) {
            trace("getFieldValues(fieldNames)", "Entry");
        }
        if (this.descriptor == null || (strArr != null && strArr.length == 0)) {
            return new Object[0];
        }
        int size = this.descriptor.size();
        Set<Map.Entry> entrySet = this.descriptor.entrySet();
        if (entrySet == null || size == 0) {
            if (tracing()) {
                trace("getFieldValues()", "no descriptor fields found");
            }
            return new Object[0];
        }
        if (strArr != null) {
            objArr = new Object[strArr.length];
        } else {
            objArr = new Object[size];
        }
        int i = 0;
        if (tracing()) {
            trace("getFieldValues()", new StringBuffer().append("Returning ").append(size).append(" fields").toString());
        }
        if (strArr == null) {
            for (Map.Entry entry : entrySet) {
                if (entry == null || entry.getKey() == null) {
                    if (tracing()) {
                        trace("getFieldValues()", "Field is null");
                    }
                    objArr[i] = null;
                } else {
                    objArr[i] = entry.getValue();
                }
                i++;
            }
        } else {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (strArr[i2] == null || strArr[i2].equals("")) {
                    objArr[i2] = null;
                } else {
                    objArr[i2] = getFieldValue(strArr[i2]);
                }
            }
        }
        if (tracing()) {
            trace("getFieldValues()", "Exit");
        }
        return objArr;
    }

    @Override // javax.management.Descriptor
    public void setFields(String[] strArr, Object[] objArr) throws RuntimeOperationsException {
        if (tracing()) {
            trace("setFields(fieldNames, ObjectValues)", "Entry");
        }
        if (strArr == null || objArr == null || strArr.length != objArr.length) {
            if (tracing()) {
                trace("Descriptor.setFields(String[],Object[])", "Illegal arguments");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("FieldNames and FieldValues are null or invalid"), "Exception occured trying to set object fields a descriptor");
        }
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] == null || strArr[i].equals("")) {
                if (tracing()) {
                    trace("Descriptor.setFields(String[],Object[])", new StringBuffer().append("Null field name encountered at ").append(i).append(" element").toString());
                }
                throw new RuntimeOperationsException(new IllegalArgumentException("FieldNames is null or invalid"), "Exception occured trying to set object fields a descriptor");
            }
            setField(strArr[i], objArr[i]);
        }
        if (tracing()) {
            trace("Descriptor.setFields(fieldNames, fieldObjects)", "Exit");
        }
    }

    @Override // javax.management.Descriptor
    public Object clone() throws RuntimeOperationsException {
        if (tracing()) {
            trace("Descriptor.clone()", "Executed");
        }
        return new DescriptorSupport(this);
    }

    @Override // javax.management.Descriptor
    public void removeField(String str) {
        if (str == null || str.equals("")) {
            return;
        }
        for (String str2 : this.descriptor.keySet()) {
            if (str2.equalsIgnoreCase(str)) {
                this.descriptor.remove(str2);
                return;
            }
        }
    }

    @Override // javax.management.Descriptor
    public boolean isValid() throws RuntimeOperationsException {
        if (tracing()) {
            trace("Descriptor.isValid()", "Executed");
        }
        Set<Map.Entry> entrySet = this.descriptor.entrySet();
        if (entrySet == null) {
            if (tracing()) {
                trace("Descriptor.isValid()", "returns false (null set)");
                return false;
            }
            return false;
        }
        String str = (String) getFieldValue("name");
        String str2 = (String) getFieldValue("descriptorType");
        if (str == null || str2 == null || str.equals("") || str2.equals("")) {
            return false;
        }
        for (Map.Entry entry : entrySet) {
            if (entry != null && entry.getValue() != null && !validateField(entry.getKey().toString(), entry.getValue().toString())) {
                if (tracing()) {
                    trace("isValid()", new StringBuffer().append("Field ").append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append(" is not valid").toString());
                    return false;
                }
                return false;
            }
        }
        if (tracing()) {
            trace("Descriptor.isValid()", "returns true");
            return true;
        }
        return true;
    }

    private boolean validateField(String str, Object obj) {
        long intValue;
        long intValue2;
        long intValue3;
        if (str == null || str.equals("")) {
            return false;
        }
        String str2 = "";
        boolean z = false;
        if (obj != null && (obj instanceof String)) {
            str2 = (String) obj;
            z = true;
        }
        if (str.equalsIgnoreCase("Name") || str.equalsIgnoreCase("DescriptorType") || str.equalsIgnoreCase("SetMethod") || str.equalsIgnoreCase("GetMethod") || str.equalsIgnoreCase("Role") || str.equalsIgnoreCase("Class")) {
            if (obj != null && z) {
                return true;
            }
            return false;
        } else if (str.equalsIgnoreCase("visibility")) {
            if (obj != null && z) {
                intValue3 = toNumeric(str2);
            } else if (obj != null && (obj instanceof Integer)) {
                intValue3 = ((Integer) obj).intValue();
            } else {
                return false;
            }
            if (intValue3 >= 1 && intValue3 <= 4) {
                return true;
            }
            return false;
        } else if (str.equalsIgnoreCase("severity")) {
            if (obj != null && z) {
                intValue2 = toNumeric(str2);
            } else if (obj != null && (obj instanceof Integer)) {
                intValue2 = ((Integer) obj).intValue();
            } else {
                return false;
            }
            if (intValue2 >= 1 && intValue2 <= 5) {
                return true;
            }
            return false;
        } else if (str.equalsIgnoreCase("PersistPolicy")) {
            if (obj == null || !z) {
                return false;
            }
            if (str2.equalsIgnoreCase("OnUpdate") || str2.equalsIgnoreCase("OnTimer") || str2.equalsIgnoreCase("NoMoreOftenThan") || str2.equalsIgnoreCase("Always") || str2.equalsIgnoreCase("Never")) {
                return true;
            }
            return false;
        } else if (str.equalsIgnoreCase("PersistPeriod") || str.equalsIgnoreCase("CurrencyTimeLimit") || str.equalsIgnoreCase("LastUpdatedTimeStamp") || str.equalsIgnoreCase("LastReturnedTimeStamp")) {
            if (obj != null && z) {
                intValue = toNumeric(str2);
            } else if (obj != null && (obj instanceof Integer)) {
                intValue = ((Integer) obj).intValue();
            } else {
                return false;
            }
            if (intValue >= -1) {
                return true;
            }
            return false;
        } else if (!str.equalsIgnoreCase("log") || (obj instanceof Boolean)) {
            return true;
        } else {
            if (z) {
                if (str2.equalsIgnoreCase("T") || str2.equalsIgnoreCase("true") || str2.equalsIgnoreCase("F") || str2.equalsIgnoreCase("false")) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    public String toXMLString() {
        if (tracing()) {
            trace("Descriptor.toXMLString()", "Executed");
        }
        String str = "<Descriptor>";
        Set<Map.Entry> entrySet = this.descriptor.entrySet();
        if (entrySet == null) {
            if (tracing()) {
                trace("Descriptor.toXMLString()", "returnedSet is null");
            }
            return new StringBuffer().append(str).append("</Descriptor>").toString();
        }
        int i = 0;
        for (Map.Entry entry : entrySet) {
            if (entry == null) {
                if (tracing()) {
                    trace("Descriptor.toXMLString()", "Element is null");
                }
            } else {
                Object value = entry.getValue();
                if (value == null) {
                    str = new StringBuffer().append(str).append("<field name=\"").append(entry.getKey().toString()).append("\" value=\"null\"></field>").toString();
                } else if (value instanceof String) {
                    str = new StringBuffer().append(str).append("<field name=\"").append(entry.getKey().toString()).append("\" value=\"").append(value).append("\"></field>").toString();
                } else {
                    str = new StringBuffer().append(str).append("<field name=\"").append(entry.getKey().toString()).append("\" value=\"").append("(").append(value.toString()).append(")").append("\"></field>").toString();
                }
            }
            i++;
        }
        String stringBuffer = new StringBuffer().append(str).append("</Descriptor>").toString();
        if (tracing()) {
            trace("Descriptor.toXMLString()", new StringBuffer().append("Returns ").append(stringBuffer).toString());
        }
        return stringBuffer;
    }

    public String toString() {
        String concat;
        if (tracing()) {
            trace("Descriptor.toString()", "Entry");
        }
        String str = "";
        String[] fields = getFields();
        if (tracing()) {
            trace("Descriptor.toString()", new StringBuffer().append("Printing ").append(fields.length).append(" fields").toString());
        }
        if (fields == null || fields.length == 0) {
            if (tracing()) {
                trace("Descriptor.toString()", "Empty Descriptor");
            }
            return str;
        }
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length - 1) {
                concat = str.concat(fields[i]);
            } else {
                concat = str.concat(new StringBuffer().append(fields[i]).append(", ").toString());
            }
            str = concat;
        }
        if (tracing()) {
            trace("Descriptor.toString()", new StringBuffer().append("Exit returning ").append(str).toString());
        }
        return str;
    }

    private long toNumeric(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return -2L;
        }
    }

    private boolean tracing() {
        return Trace.isSelected(1, 128);
    }

    private void trace(String str, String str2, String str3) {
        Trace.send(1, 128, str, str2, new StringBuffer().append(Integer.toHexString(hashCode())).append(Instruction.argsep).append(str3).toString());
    }

    private void trace(String str, String str2) {
        trace(currClass, str, str2);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put(EjbJar.NamingScheme.DESCRIPTOR, this.descriptor);
            putFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
