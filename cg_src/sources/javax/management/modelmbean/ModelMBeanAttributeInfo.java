package javax.management.modelmbean;

import android.provider.ContactsContract;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.Descriptor;
import javax.management.DescriptorAccess;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.RuntimeOperationsException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/ModelMBeanAttributeInfo.class */
public class ModelMBeanAttributeInfo extends MBeanAttributeInfo implements DescriptorAccess, Cloneable {
    private static final long oldSerialVersionUID = 7098036920755973145L;
    private static final long newSerialVersionUID = 6181543027787327345L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor attrDescriptor;
    private static final String currClass = "ModelMBeanAttributeInfo";
    static Class class$javax$management$Descriptor;
    static Class class$java$lang$String;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[2];
        if (class$javax$management$Descriptor == null) {
            cls = class$("javax.management.Descriptor");
            class$javax$management$Descriptor = cls;
        } else {
            cls = class$javax$management$Descriptor;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("attrDescriptor", cls);
        if (class$java$lang$String == null) {
            cls2 = class$("java.lang.String");
            class$java$lang$String = cls2;
        } else {
            cls2 = class$java$lang$String;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("currClass", cls2);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[1];
        if (class$javax$management$Descriptor == null) {
            cls3 = class$("javax.management.Descriptor");
            class$javax$management$Descriptor = cls3;
        } else {
            cls3 = class$javax$management$Descriptor;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("attrDescriptor", cls3);
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
            return;
        }
        serialPersistentFields = newSerialPersistentFields;
        serialVersionUID = newSerialVersionUID;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public ModelMBeanAttributeInfo(String str, String str2, Method method, Method method2) throws IntrospectionException {
        super(str, str2, method, method2);
        this.attrDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace(new StringBuffer().append("ModelMBeanAttributeInfo(").append(str).append(",String,Method,Method)").toString(), "Entry");
        }
        this.attrDescriptor = createDefaultDescriptor();
    }

    public ModelMBeanAttributeInfo(String str, String str2, Method method, Method method2, Descriptor descriptor) throws IntrospectionException {
        super(str, str2, method, method2);
        this.attrDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace(new StringBuffer().append("ModelMBeanAttributeInfo(").append(str).append(", String, Method, Method, Descriptor)").toString(), "Entry");
        }
        if (descriptor == null) {
            this.attrDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.attrDescriptor = (Descriptor) descriptor.clone();
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanAttributeInfo constructor");
        }
    }

    public ModelMBeanAttributeInfo(String str, String str2, String str3, boolean z, boolean z2, boolean z3) {
        super(str, str2, str3, z, z2, z3);
        this.attrDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace(new StringBuffer().append("ModelMBeanAttributeInfo(").append(str).append(",String,String,boolean,boolean)").toString(), "Entry");
        }
        this.attrDescriptor = createDefaultDescriptor();
    }

    public ModelMBeanAttributeInfo(String str, String str2, String str3, boolean z, boolean z2, boolean z3, Descriptor descriptor) {
        super(str, str2, str3, z, z2, z3);
        this.attrDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace(new StringBuffer().append("ModelMBeanAttributeInfo(").append(str).append(",String,String,boolean,boolean,Descriptor)").toString(), "Entry");
        }
        if (descriptor == null) {
            this.attrDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.attrDescriptor = (Descriptor) descriptor.clone();
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanAttributeInfo constructor");
        }
    }

    public ModelMBeanAttributeInfo(ModelMBeanAttributeInfo modelMBeanAttributeInfo) {
        super(modelMBeanAttributeInfo.getName(), modelMBeanAttributeInfo.getType(), modelMBeanAttributeInfo.getDescription(), modelMBeanAttributeInfo.isReadable(), modelMBeanAttributeInfo.isWritable(), modelMBeanAttributeInfo.isIs());
        this.attrDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanAttributeInfo(ModelMBeanAttributeInfo)", "Entry");
        }
        Descriptor descriptor = modelMBeanAttributeInfo.getDescriptor();
        if (descriptor != null && isValid(descriptor)) {
            this.attrDescriptor = descriptor;
        } else {
            this.attrDescriptor = createDefaultDescriptor();
        }
    }

    @Override // javax.management.DescriptorAccess
    public Descriptor getDescriptor() {
        if (tracing()) {
            trace("ModelMBeanAttributeInfo.getDescriptor()", "Entry");
        }
        if (this.attrDescriptor == null) {
            this.attrDescriptor = createDefaultDescriptor();
        }
        return (Descriptor) this.attrDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        if (descriptor != null && tracing()) {
            trace("ModelMBeanAttributeInfo.setDescriptor()", new StringBuffer().append("Executed for ").append(descriptor.toString()).toString());
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanAttributeInfo.setDescriptor()", "Received null for new descriptor value, setting descriptor to default values");
            }
            this.attrDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.attrDescriptor = (Descriptor) descriptor.clone();
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanAttributeInfo setDescriptor");
        }
    }

    @Override // javax.management.MBeanAttributeInfo
    public Object clone() {
        if (tracing()) {
            trace("ModelMBeanAttributeInfo.clone()", "Entry");
        }
        return new ModelMBeanAttributeInfo(this);
    }

    public String toString() {
        return new StringBuffer().append("ModelMBeanAttributeInfo: ").append(getName()).append(" ; Description: ").append(getDescription()).append(" ; Types: ").append(getType()).append(" ; isReadable: ").append(isReadable()).append(" ; isWritable: ").append(isWritable()).append(" ; Descriptor: ").append(getDescriptor()).toString();
    }

    private Descriptor createDefaultDescriptor() {
        if (tracing()) {
            trace("ModelMBeanAttributeInfo.createDefaultDescriptor()", "Entry");
        }
        return new DescriptorSupport(new String[]{"descriptorType=attribute", new StringBuffer().append("name=").append(getName()).toString(), new StringBuffer().append("displayName=").append(getName()).toString()});
    }

    private boolean isValid(Descriptor descriptor) {
        boolean z = true;
        String str = "none";
        if (descriptor == null) {
            str = "nullDescriptor";
            z = false;
        } else if (!descriptor.isValid()) {
            str = "inValidDescriptor";
            z = false;
        } else if (!((String) descriptor.getFieldValue("name")).equalsIgnoreCase(getName())) {
            str = "name";
            z = false;
        } else if (!((String) descriptor.getFieldValue("descriptorType")).equalsIgnoreCase("attribute")) {
            str = "desriptorType";
            z = false;
        } else if (descriptor.getFieldValue(ContactsContract.Directory.DISPLAY_NAME) == null) {
            descriptor.setField(ContactsContract.Directory.DISPLAY_NAME, getName());
        }
        if (tracing()) {
            trace("isValid()", new StringBuffer().append("Returning ").append(z).append(": Invalid field is ").append(str).toString());
        }
        return z;
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
            putFields.put("attrDescriptor", this.attrDescriptor);
            putFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
