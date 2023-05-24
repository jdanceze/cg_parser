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
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.RuntimeOperationsException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/ModelMBeanOperationInfo.class */
public class ModelMBeanOperationInfo extends MBeanOperationInfo implements DescriptorAccess {
    private static final long oldSerialVersionUID = 9087646304346171239L;
    private static final long newSerialVersionUID = 6532732096650090465L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor operationDescriptor;
    private static final String currClass = "ModelMBeanOperationInfo";
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
        objectStreamFieldArr[0] = new ObjectStreamField("operationDescriptor", cls);
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
        objectStreamFieldArr2[0] = new ObjectStreamField("operationDescriptor", cls3);
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

    public ModelMBeanOperationInfo(String str, Method method) {
        super(str, method);
        this.operationDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanOperationInfo(String,Method)", "Executed");
        }
        this.operationDescriptor = createDefaultDescriptor();
    }

    public ModelMBeanOperationInfo(String str, Method method, Descriptor descriptor) {
        super(str, method);
        this.operationDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanOperationInfo(String,Method,Descriptor)", "Executed");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanOperationInfo()", "Received null for new descriptor value, setting descriptor to default values");
            }
            this.operationDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.operationDescriptor = (Descriptor) descriptor.clone();
        } else {
            this.operationDescriptor = createDefaultDescriptor();
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanOperationInfo constructor");
        }
    }

    public ModelMBeanOperationInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, String str3, int i) {
        super(str, str2, mBeanParameterInfoArr, str3, i);
        this.operationDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanOperationInfo(String,String,MBeanParameterInfo[],String,int)", "Executed");
        }
        this.operationDescriptor = createDefaultDescriptor();
    }

    public ModelMBeanOperationInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, String str3, int i, Descriptor descriptor) {
        super(str, str2, mBeanParameterInfoArr, str3, i);
        this.operationDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanOperationInfo(String,String,MBeanParameterInfo[],String,int,Descriptor)", "Executed");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanOperationInfo()", "Received null for new descriptor value, setting descriptor to default values");
            }
            this.operationDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.operationDescriptor = (Descriptor) descriptor.clone();
        } else {
            this.operationDescriptor = createDefaultDescriptor();
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanOperationInfo constructor");
        }
    }

    public ModelMBeanOperationInfo(ModelMBeanOperationInfo modelMBeanOperationInfo) {
        super(modelMBeanOperationInfo.getName(), modelMBeanOperationInfo.getDescription(), modelMBeanOperationInfo.getSignature(), modelMBeanOperationInfo.getReturnType(), modelMBeanOperationInfo.getImpact());
        this.operationDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanOperationInfo(ModelMBeanOperationInfo)", "Executed");
        }
        Descriptor descriptor = modelMBeanOperationInfo.getDescriptor();
        if (descriptor == null) {
            this.operationDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.operationDescriptor = (Descriptor) descriptor.clone();
        } else {
            this.operationDescriptor = createDefaultDescriptor();
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanOperationInfo constructor");
        }
    }

    @Override // javax.management.MBeanOperationInfo
    public Object clone() {
        if (tracing()) {
            trace("ModelMBeanOperationInfo.clone()", "Executed");
        }
        return new ModelMBeanOperationInfo(this);
    }

    @Override // javax.management.DescriptorAccess
    public Descriptor getDescriptor() {
        if (tracing()) {
            trace("ModelMBeanOperationInfo.getDescriptor()", "Executed");
        }
        if (this.operationDescriptor == null) {
            this.operationDescriptor = createDefaultDescriptor();
        }
        return (Descriptor) this.operationDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        if (tracing()) {
            trace("ModelMBeanOperationInfo.setDescriptor(Descriptor)", "Executed");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanOperationInfo.setDescriptor()", "Received null for new descriptor value, setting descriptor to default values");
            }
            this.operationDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.operationDescriptor = (Descriptor) descriptor.clone();
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), "Exception occured in ModelMBeanOperationInfo setDescriptor");
        }
    }

    public String toString() {
        if (tracing()) {
            trace("ModelMBeanConstructorInfo.toString()", "Executed");
        }
        String stringBuffer = new StringBuffer().append("ModelMBeanOperationInfo: ").append(getName()).append(" ; Description: ").append(getDescription()).append(" ; Descriptor: ").append(getDescriptor()).append(" ; ReturnType: ").append(getReturnType()).append(" ; Signature: ").toString();
        for (MBeanParameterInfo mBeanParameterInfo : getSignature()) {
            stringBuffer = stringBuffer.concat(new StringBuffer().append(mBeanParameterInfo.getType()).append(", ").toString());
        }
        return stringBuffer;
    }

    private Descriptor createDefaultDescriptor() {
        if (tracing()) {
            trace("ModelMBeanOperationInfo.createDefaultDescriptor()", "Executed");
        }
        return new DescriptorSupport(new String[]{"descriptorType=operation", new StringBuffer().append("name=").append(getName()).toString(), "role=operation", new StringBuffer().append("displayname=").append(getName()).toString()});
    }

    private boolean isValid(Descriptor descriptor) {
        boolean z = true;
        String str = "none";
        if (descriptor == null) {
            z = false;
        } else if (!descriptor.isValid()) {
            z = false;
        } else {
            if (!((String) descriptor.getFieldValue("name")).equalsIgnoreCase(getName())) {
                z = false;
            }
            if (!((String) descriptor.getFieldValue("descriptorType")).equalsIgnoreCase("operation")) {
                z = false;
            }
            Object fieldValue = descriptor.getFieldValue("role");
            if (fieldValue == null) {
                descriptor.setField("role", "operation");
            } else if (!((String) fieldValue).equalsIgnoreCase("operation")) {
                z = false;
                str = "role";
            }
            Object fieldValue2 = descriptor.getFieldValue("targetType");
            if (fieldValue2 != null) {
                if (!(fieldValue2 instanceof String)) {
                    z = false;
                    str = "targetType";
                } else if (!((String) fieldValue2).equalsIgnoreCase("ObjectReference") && !((String) fieldValue2).equalsIgnoreCase("Handle") && !((String) fieldValue2).equalsIgnoreCase("EJBHandle") && !((String) fieldValue2).equalsIgnoreCase("IOR") && !((String) fieldValue2).equalsIgnoreCase("RMIReference")) {
                    z = false;
                    str = "targetType";
                }
            }
            if (descriptor.getFieldValue(ContactsContract.Directory.DISPLAY_NAME) == null) {
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
            putFields.put("operationDescriptor", this.operationDescriptor);
            putFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
