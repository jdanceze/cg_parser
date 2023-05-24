package javax.management.modelmbean;

import android.content.Context;
import android.provider.ContactsContract;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.Descriptor;
import javax.management.DescriptorAccess;
import javax.management.MBeanNotificationInfo;
import javax.management.RuntimeOperationsException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/ModelMBeanNotificationInfo.class */
public class ModelMBeanNotificationInfo extends MBeanNotificationInfo implements DescriptorAccess, Cloneable {
    private static final long oldSerialVersionUID = -5211564525059047097L;
    private static final long newSerialVersionUID = -7445681389570207141L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor notificationDescriptor;
    private static final String currClass = "ModelMBeanNotificationInfo";
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
        objectStreamFieldArr[0] = new ObjectStreamField("notificationDescriptor", cls);
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
        objectStreamFieldArr2[0] = new ObjectStreamField("notificationDescriptor", cls3);
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

    public ModelMBeanNotificationInfo(String[] strArr, String str, String str2) {
        this(strArr, str, str2, null);
    }

    public ModelMBeanNotificationInfo(String[] strArr, String str, String str2, Descriptor descriptor) {
        super(strArr, str, str2);
        if (tracing()) {
            trace("ModelMBeanNotificationInfo()", "Executed");
        }
        applyDescriptor(descriptor, "ModelMBeanNotificationInfo()");
    }

    public ModelMBeanNotificationInfo(ModelMBeanNotificationInfo modelMBeanNotificationInfo) {
        this(modelMBeanNotificationInfo.getNotifTypes(), modelMBeanNotificationInfo.getName(), modelMBeanNotificationInfo.getDescription(), modelMBeanNotificationInfo.getDescriptor());
    }

    @Override // javax.management.MBeanNotificationInfo
    public Object clone() {
        if (tracing()) {
            trace("ModelMBeanNotificationInfo.clone()", "Executed");
        }
        return new ModelMBeanNotificationInfo(this);
    }

    @Override // javax.management.DescriptorAccess
    public Descriptor getDescriptor() {
        if (tracing()) {
            trace("ModelMBeanNotificationInfo.getDescriptor()", "Executed");
        }
        if (this.notificationDescriptor == null) {
            if (tracing()) {
                trace("ModelMBeanNotificationInfo.getDescriptor()", "Received null for new descriptor value, setting descriptor to default values");
            }
            this.notificationDescriptor = createDefaultDescriptor();
        }
        return (Descriptor) this.notificationDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        if (tracing()) {
            trace("setDescriptor(Descriptor)", "Executed");
        }
        applyDescriptor(descriptor, "setDescriptor(Descriptor)");
    }

    public String toString() {
        if (tracing()) {
            trace("toString()", "Executed");
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ModelMBeanNotificationInfo: ").append(getName());
        stringBuffer.append(" ; Description: ").append(getDescription());
        stringBuffer.append(" ; Descriptor: ").append(getDescriptor());
        stringBuffer.append(" ; Types: ");
        String[] notifTypes = getNotifTypes();
        for (int i = 0; i < notifTypes.length; i++) {
            if (i > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(notifTypes[i]);
        }
        return stringBuffer.toString();
    }

    private final Descriptor createDefaultDescriptor() {
        if (tracing()) {
            trace("createDefaultDescriptor()", "Executed");
        }
        return new DescriptorSupport(new String[]{"descriptorType=notification", new StringBuffer().append("name=").append(getName()).toString(), new StringBuffer().append("displayName=").append(getName()).toString(), "severity=5"});
    }

    private boolean isValid(Descriptor descriptor) {
        boolean z = true;
        String str = "none";
        if (descriptor == null) {
            return false;
        }
        if (!descriptor.isValid()) {
            str = "invalidDescriptor";
            z = false;
        } else if (!((String) descriptor.getFieldValue("name")).equalsIgnoreCase(getName())) {
            str = "name";
            z = false;
        } else if (!((String) descriptor.getFieldValue("descriptorType")).equalsIgnoreCase(Context.NOTIFICATION_SERVICE)) {
            str = "descriptorType";
            z = false;
        }
        if (tracing()) {
            trace("isValid()", new StringBuffer().append("Returning ").append(z).append(": Invalid field is ").append(str).toString());
        }
        return z;
    }

    private final Descriptor setDefaults(Descriptor descriptor) {
        if (descriptor.getFieldValue(ContactsContract.Directory.DISPLAY_NAME) == null) {
            descriptor.setField(ContactsContract.Directory.DISPLAY_NAME, getName());
        }
        if (descriptor.getFieldValue("severity") == null) {
            descriptor.setField("severity", "5");
        }
        return descriptor;
    }

    private final void applyDescriptor(Descriptor descriptor, String str) {
        if (descriptor == null) {
            if (tracing()) {
                trace(str, "Received null for new descriptor value, setting descriptor to default values");
            }
            this.notificationDescriptor = createDefaultDescriptor();
        } else if (isValid(descriptor)) {
            this.notificationDescriptor = setDefaults((Descriptor) descriptor.clone());
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"), new StringBuffer().append("Exception occured in ModelMBeanNotificationInfo ").append(str).toString());
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
            putFields.put("notificationDescriptor", this.notificationDescriptor);
            putFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
