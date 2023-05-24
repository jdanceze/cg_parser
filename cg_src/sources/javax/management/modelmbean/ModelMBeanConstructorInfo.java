package javax.management.modelmbean;

import android.provider.ContactsContract;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.Descriptor;
import javax.management.DescriptorAccess;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanParameterInfo;
import javax.management.RuntimeOperationsException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/ModelMBeanConstructorInfo.class */
public class ModelMBeanConstructorInfo extends MBeanConstructorInfo implements DescriptorAccess, Cloneable {
    private static final long oldSerialVersionUID = -4440125391095574518L;
    private static final long newSerialVersionUID = 3862947819818064362L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor consDescriptor;
    private static final String currClass = "ModelMBeanConstructorInfo";
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
        objectStreamFieldArr[0] = new ObjectStreamField("consDescriptor", cls);
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
        objectStreamFieldArr2[0] = new ObjectStreamField("consDescriptor", cls3);
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

    public ModelMBeanConstructorInfo(String str, Constructor constructor) {
        super(str, constructor);
        this.consDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanConstructorInfo(String, Method)", "Executed");
        }
        this.consDescriptor = createDefaultDescriptor();
    }

    public ModelMBeanConstructorInfo(String str, Constructor constructor, Descriptor descriptor) {
        super(str, constructor);
        this.consDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanConstructorInfo(String, Method, Descriptor)", "Executed");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanConstructorInfo(String, Method, Descriptor)", "Descriptor passed in is null, setting descriptor to default values");
            }
            this.consDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.consDescriptor = (Descriptor) descriptor.clone();
        } else {
            this.consDescriptor = createDefaultDescriptor();
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanConstructorInfo constructor");
        }
    }

    public ModelMBeanConstructorInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr) {
        super(str, str2, mBeanParameterInfoArr);
        this.consDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanConstructorInfo(String, String, MBeanParameterInfo[])", "Executed");
        }
        this.consDescriptor = createDefaultDescriptor();
    }

    public ModelMBeanConstructorInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, Descriptor descriptor) {
        super(str, str2, mBeanParameterInfoArr);
        this.consDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanConstructorInfo(String, String, MBeanParameterInfo[], Descriptor)", "Executed");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanConstructorInfo(String, Method, Descriptor)", "Descriptor passed in is null, setting descriptor to default values");
            }
            this.consDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.consDescriptor = (Descriptor) descriptor.clone();
        } else {
            this.consDescriptor = createDefaultDescriptor();
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanConstructorInfo constructor");
        }
    }

    ModelMBeanConstructorInfo(ModelMBeanConstructorInfo modelMBeanConstructorInfo) {
        super(modelMBeanConstructorInfo.getName(), modelMBeanConstructorInfo.getDescription(), modelMBeanConstructorInfo.getSignature());
        this.consDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanConstructorInfo(ModelMBeanConstructorInfo)", "Executed");
        }
        if (modelMBeanConstructorInfo.consDescriptor == null) {
            if (tracing()) {
                trace("ModelMBeanConstructorInfo(String, Method, Descriptor)", "Existing descriptor passed in is null, setting new descriptor to default values");
            }
            this.consDescriptor = createDefaultDescriptor();
        } else if (isValid(this.consDescriptor)) {
            this.consDescriptor = (Descriptor) modelMBeanConstructorInfo.consDescriptor.clone();
        } else {
            this.consDescriptor = createDefaultDescriptor();
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanConstructorInfo constructor");
        }
    }

    @Override // javax.management.MBeanConstructorInfo
    public Object clone() {
        if (tracing()) {
            trace("ModelMBeanConstructorInfo.clone()", "Executed");
        }
        return new ModelMBeanConstructorInfo(this);
    }

    @Override // javax.management.DescriptorAccess
    public Descriptor getDescriptor() {
        if (tracing()) {
            trace("ModelMBeanConstructorInfo.getDescriptor()", "Executed");
        }
        if (this.consDescriptor == null) {
            this.consDescriptor = createDefaultDescriptor();
        }
        return (Descriptor) this.consDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        if (tracing()) {
            trace("ModelMBeanConstructorInfo.setDescriptor()", "Executed");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanConstructorInfo(String, Method, Descriptor)", "Descriptor passed in is null, setting descriptor to default values");
            }
            this.consDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.consDescriptor = (Descriptor) descriptor.clone();
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanConstructorInfo setDescriptor");
        }
    }

    public String toString() {
        if (tracing()) {
            trace("ModelMBeanConstructorInfo.toString()", "Executed");
        }
        String stringBuffer = new StringBuffer().append("ModelMBeanConstructorInfo: ").append(getName()).append(" ; Description: ").append(getDescription()).append(" ; Descriptor: ").append(getDescriptor()).append(" ; Signature: ").toString();
        for (MBeanParameterInfo mBeanParameterInfo : getSignature()) {
            stringBuffer = stringBuffer.concat(new StringBuffer().append(mBeanParameterInfo.getType()).append(", ").toString());
        }
        return stringBuffer;
    }

    private Descriptor createDefaultDescriptor() {
        if (tracing()) {
            trace("ModelMBeanConstructorInfo.createDefaultDescriptor()", "Executed");
        }
        return new DescriptorSupport(new String[]{"descriptorType=operation", "role=constructor", new StringBuffer().append("name=").append(getName()).toString(), new StringBuffer().append("displayname=").append(getName()).toString()});
    }

    private boolean isValid(Descriptor descriptor) {
        boolean z = true;
        String str = "none";
        if (descriptor == null) {
            str = "nullDescriptor";
            z = false;
        } else if (!descriptor.isValid()) {
            str = "invalidDescriptor";
            z = false;
        } else {
            if (!((String) descriptor.getFieldValue("name")).equalsIgnoreCase(getName())) {
                str = "name";
                z = false;
            }
            if (!((String) descriptor.getFieldValue("descriptorType")).equalsIgnoreCase("operation")) {
                str = "descriptorType";
                z = false;
            }
            if (descriptor.getFieldValue("role") == null) {
                descriptor.setField("role", "constructor");
            }
            if (!((String) descriptor.getFieldValue("role")).equalsIgnoreCase("constructor")) {
                str = "role";
                z = false;
            } else if (descriptor.getFieldValue(ContactsContract.Directory.DISPLAY_NAME) == null) {
                descriptor.setField(ContactsContract.Directory.DISPLAY_NAME, getName());
            }
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
            putFields.put("consDescriptor", this.consDescriptor);
            putFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
