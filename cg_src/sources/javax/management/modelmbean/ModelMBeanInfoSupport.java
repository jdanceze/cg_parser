package javax.management.modelmbean;

import android.provider.ContactsContract;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.Descriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.RuntimeOperationsException;
import javax.resource.spi.work.WorkException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/ModelMBeanInfoSupport.class */
public class ModelMBeanInfoSupport extends MBeanInfo implements ModelMBeanInfo, Serializable {
    private static final long oldSerialVersionUID = -3944083498453227709L;
    private static final long newSerialVersionUID = -1935722590756516193L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor modelMBeanDescriptor;
    private MBeanAttributeInfo[] modelMBeanAttributes;
    private MBeanConstructorInfo[] modelMBeanConstructors;
    private MBeanNotificationInfo[] modelMBeanNotifications;
    private MBeanOperationInfo[] modelMBeanOperations;
    private static final String ATTR = "attribute";
    private static final String OPER = "operation";
    private static final String NOTF = "notification";
    private static final String CONS = "constructor";
    private static final String MMB = "mbean";
    private static final String ALL = "all";
    private static final String currClass = "ModelMBeanInfoSupport";
    static Class class$javax$management$Descriptor;
    static Class array$Ljavax$management$MBeanAttributeInfo;
    static Class array$Ljavax$management$MBeanConstructorInfo;
    static Class array$Ljavax$management$MBeanNotificationInfo;
    static Class array$Ljavax$management$MBeanOperationInfo;
    static Class class$java$lang$String;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        Class cls10;
        Class cls11;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[6];
        if (class$javax$management$Descriptor == null) {
            cls = class$("javax.management.Descriptor");
            class$javax$management$Descriptor = cls;
        } else {
            cls = class$javax$management$Descriptor;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("modelMBeanDescriptor", cls);
        if (array$Ljavax$management$MBeanAttributeInfo == null) {
            cls2 = class$("[Ljavax.management.MBeanAttributeInfo;");
            array$Ljavax$management$MBeanAttributeInfo = cls2;
        } else {
            cls2 = array$Ljavax$management$MBeanAttributeInfo;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("mmbAttributes", cls2);
        if (array$Ljavax$management$MBeanConstructorInfo == null) {
            cls3 = class$("[Ljavax.management.MBeanConstructorInfo;");
            array$Ljavax$management$MBeanConstructorInfo = cls3;
        } else {
            cls3 = array$Ljavax$management$MBeanConstructorInfo;
        }
        objectStreamFieldArr[2] = new ObjectStreamField("mmbConstructors", cls3);
        if (array$Ljavax$management$MBeanNotificationInfo == null) {
            cls4 = class$("[Ljavax.management.MBeanNotificationInfo;");
            array$Ljavax$management$MBeanNotificationInfo = cls4;
        } else {
            cls4 = array$Ljavax$management$MBeanNotificationInfo;
        }
        objectStreamFieldArr[3] = new ObjectStreamField("mmbNotifications", cls4);
        if (array$Ljavax$management$MBeanOperationInfo == null) {
            cls5 = class$("[Ljavax.management.MBeanOperationInfo;");
            array$Ljavax$management$MBeanOperationInfo = cls5;
        } else {
            cls5 = array$Ljavax$management$MBeanOperationInfo;
        }
        objectStreamFieldArr[4] = new ObjectStreamField("mmbOperations", cls5);
        if (class$java$lang$String == null) {
            cls6 = class$("java.lang.String");
            class$java$lang$String = cls6;
        } else {
            cls6 = class$java$lang$String;
        }
        objectStreamFieldArr[5] = new ObjectStreamField("currClass", cls6);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[5];
        if (class$javax$management$Descriptor == null) {
            cls7 = class$("javax.management.Descriptor");
            class$javax$management$Descriptor = cls7;
        } else {
            cls7 = class$javax$management$Descriptor;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("modelMBeanDescriptor", cls7);
        if (array$Ljavax$management$MBeanAttributeInfo == null) {
            cls8 = class$("[Ljavax.management.MBeanAttributeInfo;");
            array$Ljavax$management$MBeanAttributeInfo = cls8;
        } else {
            cls8 = array$Ljavax$management$MBeanAttributeInfo;
        }
        objectStreamFieldArr2[1] = new ObjectStreamField("modelMBeanAttributes", cls8);
        if (array$Ljavax$management$MBeanConstructorInfo == null) {
            cls9 = class$("[Ljavax.management.MBeanConstructorInfo;");
            array$Ljavax$management$MBeanConstructorInfo = cls9;
        } else {
            cls9 = array$Ljavax$management$MBeanConstructorInfo;
        }
        objectStreamFieldArr2[2] = new ObjectStreamField("modelMBeanConstructors", cls9);
        if (array$Ljavax$management$MBeanNotificationInfo == null) {
            cls10 = class$("[Ljavax.management.MBeanNotificationInfo;");
            array$Ljavax$management$MBeanNotificationInfo = cls10;
        } else {
            cls10 = array$Ljavax$management$MBeanNotificationInfo;
        }
        objectStreamFieldArr2[3] = new ObjectStreamField("modelMBeanNotifications", cls10);
        if (array$Ljavax$management$MBeanOperationInfo == null) {
            cls11 = class$("[Ljavax.management.MBeanOperationInfo;");
            array$Ljavax$management$MBeanOperationInfo = cls11;
        } else {
            cls11 = array$Ljavax$management$MBeanOperationInfo;
        }
        objectStreamFieldArr2[4] = new ObjectStreamField("modelMBeanOperations", cls11);
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

    public ModelMBeanInfoSupport(ModelMBeanInfo modelMBeanInfo) {
        super(modelMBeanInfo.getClassName(), modelMBeanInfo.getDescription(), modelMBeanInfo.getAttributes(), modelMBeanInfo.getConstructors(), modelMBeanInfo.getOperations(), modelMBeanInfo.getNotifications());
        this.modelMBeanDescriptor = null;
        this.modelMBeanAttributes = modelMBeanInfo.getAttributes();
        this.modelMBeanConstructors = modelMBeanInfo.getConstructors();
        this.modelMBeanOperations = modelMBeanInfo.getOperations();
        this.modelMBeanNotifications = modelMBeanInfo.getNotifications();
        try {
            Descriptor mBeanDescriptor = modelMBeanInfo.getMBeanDescriptor();
            if (mBeanDescriptor != null && isValidDescriptor(mBeanDescriptor)) {
                if (tracing()) {
                    trace("ModelMBeanInfo(ModelMBeanInfo)", new StringBuffer().append("ModelMBeanDescriptor is valid, cloning Descriptor *").append(mBeanDescriptor.toString()).append("*").toString());
                }
                this.modelMBeanDescriptor = (Descriptor) mBeanDescriptor.clone();
            } else {
                if (tracing()) {
                    trace("ModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanDescriptor in ModelMBeanInfo is null or invalid, setting to default value");
                }
                this.modelMBeanDescriptor = createDefaultDescriptor();
            }
        } catch (MBeanException e) {
            this.modelMBeanDescriptor = createDefaultDescriptor();
            if (tracing()) {
                trace("ModelMBeanInfo(ModelMBeanInfo)", "Could not get modelMBeanDescriptor, setting to default value");
            }
        }
        if (tracing()) {
            trace("ModelMBeanInfo(ModelMBeanInfo)", "Executed");
        }
    }

    public ModelMBeanInfoSupport(String str, String str2, ModelMBeanAttributeInfo[] modelMBeanAttributeInfoArr, ModelMBeanConstructorInfo[] modelMBeanConstructorInfoArr, ModelMBeanOperationInfo[] modelMBeanOperationInfoArr, ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr) {
        super(str, str2, modelMBeanAttributeInfoArr, modelMBeanConstructorInfoArr, modelMBeanOperationInfoArr, modelMBeanNotificationInfoArr);
        this.modelMBeanDescriptor = null;
        this.modelMBeanAttributes = modelMBeanAttributeInfoArr;
        this.modelMBeanConstructors = modelMBeanConstructorInfoArr;
        this.modelMBeanOperations = modelMBeanOperationInfoArr;
        this.modelMBeanNotifications = modelMBeanNotificationInfoArr;
        this.modelMBeanDescriptor = createDefaultDescriptor();
        if (tracing()) {
            trace("ModelMBeanInfo(String,String,ModelMBeanAttributeInfo[],ModelMBeanConstructorInfo[],ModelMBeanOperationInfo[],ModelMBeanNotificationInfo[])", "Executed");
        }
    }

    public ModelMBeanInfoSupport(String str, String str2, ModelMBeanAttributeInfo[] modelMBeanAttributeInfoArr, ModelMBeanConstructorInfo[] modelMBeanConstructorInfoArr, ModelMBeanOperationInfo[] modelMBeanOperationInfoArr, ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr, Descriptor descriptor) {
        super(str, str2, modelMBeanAttributeInfoArr, modelMBeanConstructorInfoArr, modelMBeanOperationInfoArr, modelMBeanNotificationInfoArr);
        this.modelMBeanDescriptor = null;
        this.modelMBeanAttributes = modelMBeanAttributeInfoArr;
        this.modelMBeanConstructors = modelMBeanConstructorInfoArr;
        this.modelMBeanOperations = modelMBeanOperationInfoArr;
        this.modelMBeanNotifications = modelMBeanNotificationInfoArr;
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanInfo(String,String,ModelMBeanAttributeInfo[],ModelMBeanConstructorInfo[],ModelMBeanOperationInfo[],ModelMBeanNotificationInfo[],Descriptor)", "MBeanDescriptor is null, setting default descriptor");
            }
            this.modelMBeanDescriptor = createDefaultDescriptor();
        } else if (isValidDescriptor(descriptor)) {
            this.modelMBeanDescriptor = (Descriptor) descriptor.clone();
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"));
        }
        if (tracing()) {
            trace("ModelMBeanInfo(String,String,ModelMBeanAttributeInfo[],ModelMBeanConstructorInfo[],ModelMBeanOperationInfo[],ModelMBeanNotificationInfo[],Descriptor)", "Executed");
        }
    }

    @Override // javax.management.MBeanInfo, javax.management.modelmbean.ModelMBeanInfo
    public Object clone() {
        return new ModelMBeanInfoSupport(this);
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public Descriptor[] getDescriptors(String str) throws MBeanException, RuntimeOperationsException {
        Descriptor[] descriptorArr;
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getDescriptors()", "Entry");
        }
        if (str == null || str == "") {
            str = ALL;
        }
        if (str.equalsIgnoreCase(MMB)) {
            new Descriptor[1][0] = this.modelMBeanDescriptor;
        }
        if (str.equalsIgnoreCase(ATTR)) {
            MBeanAttributeInfo[] mBeanAttributeInfoArr = this.modelMBeanAttributes;
            int length = mBeanAttributeInfoArr != null ? mBeanAttributeInfoArr.length : 0;
            descriptorArr = new Descriptor[length];
            for (int i = 0; i < length; i++) {
                descriptorArr[i] = ((ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase(OPER)) {
            MBeanOperationInfo[] mBeanOperationInfoArr = this.modelMBeanOperations;
            int length2 = mBeanOperationInfoArr != null ? mBeanOperationInfoArr.length : 0;
            descriptorArr = new Descriptor[length2];
            for (int i2 = 0; i2 < length2; i2++) {
                descriptorArr[i2] = ((ModelMBeanOperationInfo) mBeanOperationInfoArr[i2]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase(CONS)) {
            MBeanConstructorInfo[] mBeanConstructorInfoArr = this.modelMBeanConstructors;
            int length3 = mBeanConstructorInfoArr != null ? mBeanConstructorInfoArr.length : 0;
            descriptorArr = new Descriptor[length3];
            for (int i3 = 0; i3 < length3; i3++) {
                descriptorArr[i3] = ((ModelMBeanConstructorInfo) mBeanConstructorInfoArr[i3]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase("notification")) {
            MBeanNotificationInfo[] mBeanNotificationInfoArr = this.modelMBeanNotifications;
            int length4 = mBeanNotificationInfoArr != null ? mBeanNotificationInfoArr.length : 0;
            descriptorArr = new Descriptor[length4];
            for (int i4 = 0; i4 < length4; i4++) {
                descriptorArr[i4] = ((ModelMBeanNotificationInfo) mBeanNotificationInfoArr[i4]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase(ALL)) {
            MBeanAttributeInfo[] mBeanAttributeInfoArr2 = this.modelMBeanAttributes;
            int length5 = mBeanAttributeInfoArr2 != null ? mBeanAttributeInfoArr2.length : 0;
            MBeanOperationInfo[] mBeanOperationInfoArr2 = this.modelMBeanOperations;
            int length6 = mBeanOperationInfoArr2 != null ? mBeanOperationInfoArr2.length : 0;
            MBeanConstructorInfo[] mBeanConstructorInfoArr2 = this.modelMBeanConstructors;
            int length7 = mBeanConstructorInfoArr2 != null ? mBeanConstructorInfoArr2.length : 0;
            MBeanNotificationInfo[] mBeanNotificationInfoArr2 = this.modelMBeanNotifications;
            int length8 = mBeanNotificationInfoArr2 != null ? mBeanNotificationInfoArr2.length : 0;
            descriptorArr = new Descriptor[length5 + length7 + length6 + length8];
            int i5 = 0;
            for (int i6 = 0; i6 < length5; i6++) {
                descriptorArr[i5] = ((ModelMBeanAttributeInfo) mBeanAttributeInfoArr2[i6]).getDescriptor();
                i5++;
            }
            for (int i7 = 0; i7 < length7; i7++) {
                descriptorArr[i5] = ((ModelMBeanConstructorInfo) mBeanConstructorInfoArr2[i7]).getDescriptor();
                i5++;
            }
            for (int i8 = 0; i8 < length6; i8++) {
                descriptorArr[i5] = ((ModelMBeanOperationInfo) mBeanOperationInfoArr2[i8]).getDescriptor();
                i5++;
            }
            for (int i9 = 0; i9 < length8; i9++) {
                descriptorArr[i5] = ((ModelMBeanNotificationInfo) mBeanNotificationInfoArr2[i9]).getDescriptor();
                i5++;
            }
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Type is invalid"), "Exception occured trying find the descriptors of the MBean");
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getDescriptors()", "Exit");
        }
        return descriptorArr;
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public void setDescriptors(Descriptor[] descriptorArr) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("ModelMBeanInfoSupport.setDescriptors(Descriptor[])", "Entry");
        }
        if (descriptorArr == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor list is invalid"), "Exception occured trying set the descriptors of the MBeanInfo");
        }
        if (descriptorArr.length == 0) {
            return;
        }
        for (Descriptor descriptor : descriptorArr) {
            setDescriptor(descriptor, null);
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.setDescriptors(Descriptor[])", "Exit");
        }
    }

    public Descriptor getDescriptor(String str) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getDescriptor(String)", "Entry");
        }
        return getDescriptor(str, null);
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public Descriptor getDescriptor(String str, String str2) throws MBeanException, RuntimeOperationsException {
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor is invalid"), "Exception occured trying set the descriptors of the MBeanInfo");
        }
        if (MMB.equalsIgnoreCase(str2)) {
            return (Descriptor) this.modelMBeanDescriptor.clone();
        }
        if (ATTR.equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanAttributeInfo attribute = getAttribute(str);
            if (attribute != null) {
                return attribute.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if (OPER.equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanOperationInfo operation = getOperation(str);
            if (operation != null) {
                return operation.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if (CONS.equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanConstructorInfo constructor = getConstructor(str);
            if (constructor != null) {
                return constructor.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if ("notification".equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanNotificationInfo notification = getNotification(str);
            if (notification != null) {
                return notification.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if (str2 == null) {
            return null;
        }
        throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Type is invalid"), "Exception occured trying find the descriptors of the MBean");
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public void setDescriptor(Descriptor descriptor, String str) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("ModelMBeanInfoSupport.setDescriptor(Descriptor,String)", "Entry");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanInfoSupport.setDescriptor(Descriptor,String)", "Descriptor is null");
                return;
            }
            return;
        }
        if (str == null || str == "") {
            str = (String) descriptor.getFieldValue("descriptorType");
            if (str == null) {
                throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Type is invalid"), "Exception occured trying set the descriptors of the MBean");
            }
        }
        String str2 = (String) descriptor.getFieldValue("name");
        if (str2 == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Name is invalid"), "Exception occured trying set the descriptors of the MBean");
        }
        boolean z = false;
        if (str.equalsIgnoreCase(MMB)) {
            setMBeanDescriptor(descriptor);
        } else if (str.equalsIgnoreCase(ATTR)) {
            MBeanAttributeInfo[] mBeanAttributeInfoArr = this.modelMBeanAttributes;
            int length = mBeanAttributeInfoArr != null ? mBeanAttributeInfoArr.length : 0;
            for (int i = 0; i < length; i++) {
                if (str2.equals(mBeanAttributeInfoArr[i].getName())) {
                    z = true;
                    ((ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i]).setDescriptor(descriptor);
                    if (tracing()) {
                        trace("ModelMBeanInfoSupport.setDescriptor", new StringBuffer().append("setting descriptor to ").append(descriptor.toString()).toString());
                        trace("ModelMBeanInfoSupport.setDescriptor", new StringBuffer().append("local: AttributeInfo descriptor is ").append(((ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i]).getDescriptor().toString()).toString());
                        trace("ModelMBeanInfoSupport.setDescriptor", new StringBuffer().append("modelMBeanInfo: AttributeInfo descriptor is ").append(getDescriptor(str2, ATTR).toString()).toString());
                    }
                }
            }
        } else if (str.equalsIgnoreCase(OPER)) {
            MBeanOperationInfo[] mBeanOperationInfoArr = this.modelMBeanOperations;
            int length2 = mBeanOperationInfoArr != null ? mBeanOperationInfoArr.length : 0;
            for (int i2 = 0; i2 < length2; i2++) {
                if (str2.equals(mBeanOperationInfoArr[i2].getName())) {
                    z = true;
                    ((ModelMBeanOperationInfo) mBeanOperationInfoArr[i2]).setDescriptor(descriptor);
                }
            }
        } else if (str.equalsIgnoreCase(CONS)) {
            MBeanConstructorInfo[] mBeanConstructorInfoArr = this.modelMBeanConstructors;
            int length3 = mBeanConstructorInfoArr != null ? mBeanConstructorInfoArr.length : 0;
            for (int i3 = 0; i3 < length3; i3++) {
                if (str2.equals(mBeanConstructorInfoArr[i3].getName())) {
                    z = true;
                    ((ModelMBeanConstructorInfo) mBeanConstructorInfoArr[i3]).setDescriptor(descriptor);
                }
            }
        } else if (str.equalsIgnoreCase("notification")) {
            MBeanNotificationInfo[] mBeanNotificationInfoArr = this.modelMBeanNotifications;
            int length4 = mBeanNotificationInfoArr != null ? mBeanNotificationInfoArr.length : 0;
            for (int i4 = 0; i4 < length4; i4++) {
                if (str2.equals(mBeanNotificationInfoArr[i4].getName())) {
                    z = true;
                    ((ModelMBeanNotificationInfo) mBeanNotificationInfoArr[i4]).setDescriptor(descriptor);
                }
            }
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Type is invalid"), "Exception occured trying set the descriptors of the MBean");
        }
        if (!z) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Name is invalid"), "Exception occured trying set the descriptors of the MBean");
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.setDescriptor(Descriptor,String)", "Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public ModelMBeanAttributeInfo getAttribute(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanAttributeInfo modelMBeanAttributeInfo = null;
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getAttributeInfo(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute Name is null"), "Exception occured trying get the ModelMBeanAttributeInfo of the MBean");
        }
        MBeanAttributeInfo[] mBeanAttributeInfoArr = this.modelMBeanAttributes;
        int length = mBeanAttributeInfoArr != null ? mBeanAttributeInfoArr.length : 0;
        for (int i = 0; i < length && modelMBeanAttributeInfo == null; i++) {
            if (tracing()) {
                trace("ModelMBeanInfoSupport.getAttribute", new StringBuffer().append("this.getAttributes() MBeanAttributeInfo Array ").append(i).append(":").append(((ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i]).getDescriptor().toString()).toString());
                trace("ModelMBeanInfoSupport.getAttribute", new StringBuffer().append("this.modelMBeanAttributes MBeanAttributeInfo Array ").append(i).append(":").append(((ModelMBeanAttributeInfo) this.modelMBeanAttributes[i]).getDescriptor().toString()).toString());
            }
            if (str.equals(mBeanAttributeInfoArr[i].getName())) {
                modelMBeanAttributeInfo = (ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i].clone();
            }
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getAttribute()", "Exit");
        }
        return modelMBeanAttributeInfo;
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public ModelMBeanOperationInfo getOperation(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanOperationInfo modelMBeanOperationInfo = null;
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getOperation(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("inName is null"), "Exception occured trying get the ModelMBeanOperationInfo of the MBean");
        }
        MBeanOperationInfo[] mBeanOperationInfoArr = this.modelMBeanOperations;
        int length = mBeanOperationInfoArr != null ? mBeanOperationInfoArr.length : 0;
        for (int i = 0; i < length && modelMBeanOperationInfo == null; i++) {
            if (str.equals(mBeanOperationInfoArr[i].getName())) {
                modelMBeanOperationInfo = (ModelMBeanOperationInfo) mBeanOperationInfoArr[i].clone();
            }
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getOperation(String)", "Exit");
        }
        return modelMBeanOperationInfo;
    }

    public ModelMBeanConstructorInfo getConstructor(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanConstructorInfo modelMBeanConstructorInfo = null;
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getConstructor(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Constructor name is null"), "Exception occured trying get the ModelMBeanConstructorInfo of the MBean");
        }
        MBeanConstructorInfo[] mBeanConstructorInfoArr = this.modelMBeanConstructors;
        int length = mBeanConstructorInfoArr != null ? mBeanConstructorInfoArr.length : 0;
        for (int i = 0; i < length && modelMBeanConstructorInfo == null; i++) {
            if (str.equals(mBeanConstructorInfoArr[i].getName())) {
                modelMBeanConstructorInfo = (ModelMBeanConstructorInfo) mBeanConstructorInfoArr[i].clone();
            }
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getConstructor(String)", "Exit");
        }
        return modelMBeanConstructorInfo;
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public ModelMBeanNotificationInfo getNotification(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanNotificationInfo modelMBeanNotificationInfo = null;
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getNotification(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Notification name is null"), "Exception occured trying get the ModelMBeanNotificationInfo of the MBean");
        }
        MBeanNotificationInfo[] mBeanNotificationInfoArr = this.modelMBeanNotifications;
        int length = mBeanNotificationInfoArr != null ? mBeanNotificationInfoArr.length : 0;
        for (int i = 0; i < length && modelMBeanNotificationInfo == null; i++) {
            if (str.equals(mBeanNotificationInfoArr[i].getName())) {
                modelMBeanNotificationInfo = (ModelMBeanNotificationInfo) mBeanNotificationInfoArr[i].clone();
            }
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getNotification(String)", "Exit");
        }
        return modelMBeanNotificationInfo;
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public Descriptor getMBeanDescriptor() throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getMBeanDescriptor()", "Executed");
        }
        if (this.modelMBeanDescriptor == null) {
            return null;
        }
        if (tracing()) {
            trace("ModelMBeanInfoSupport.getMBeanDesriptor()", new StringBuffer().append("Returning ").append(this.modelMBeanDescriptor.toString()).toString());
        }
        return (Descriptor) this.modelMBeanDescriptor.clone();
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public void setMBeanDescriptor(Descriptor descriptor) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("ModelMBeanInfoSupport.setMBeanDescriptor(Descriptor)", "Executed");
        }
        if (descriptor == null) {
            if (tracing()) {
                trace("ModelMBeanInfoSupport.setMBeanDescriptor(Descriptor)", "MBean Descriptor is not valid");
            }
            this.modelMBeanDescriptor = createDefaultDescriptor();
        } else if (isValidDescriptor(descriptor)) {
            this.modelMBeanDescriptor = (Descriptor) descriptor.clone();
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor passed in parameter"));
        }
    }

    private Descriptor createDefaultDescriptor() {
        return new DescriptorSupport(new String[]{new StringBuffer().append("name=").append(getClassName()).toString(), "descriptorType=mbean", new StringBuffer().append("displayName=").append(getClassName()).toString(), "persistPolicy=never", "log=F", "visibility=1"});
    }

    private boolean isValidDescriptor(Descriptor descriptor) {
        boolean z = true;
        String str = "none";
        if (tracing()) {
            trace("isValidDescriptor", new StringBuffer().append("Validating descriptor: ").append(descriptor.toString()).toString());
        }
        if (descriptor == null) {
            str = "nullDescriptor";
            z = false;
        } else if (!descriptor.isValid()) {
            str = "InvalidDescriptor";
            z = false;
        }
        if (((String) descriptor.getFieldValue("name")) == null) {
            str = "name";
            z = false;
        } else if (!((String) descriptor.getFieldValue("descriptorType")).equalsIgnoreCase(MMB)) {
            str = "descriptorType";
            z = false;
        } else if (descriptor.getFieldValue(ContactsContract.Directory.DISPLAY_NAME) == null) {
            descriptor.setField(ContactsContract.Directory.DISPLAY_NAME, getClassName());
        } else if (descriptor.getFieldValue("persistPolicy") == null) {
            descriptor.setField("persistPolicy", "never");
        } else if (descriptor.getFieldValue("log") == null) {
            descriptor.setField("log", "F");
        } else if (descriptor.getFieldValue("visiblity") == null) {
            descriptor.setField("visibility", WorkException.START_TIMED_OUT);
        }
        if (tracing()) {
            trace("isValidDescriptor", new StringBuffer().append("returning ").append(z).append(": Invalid field is ").append(str).toString());
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
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.modelMBeanDescriptor = (Descriptor) readFields.get("modelMBeanDescriptor", (Object) null);
            if (readFields.defaulted("modelMBeanDescriptor")) {
                throw new NullPointerException("modelMBeanDescriptor");
            }
            this.modelMBeanAttributes = (MBeanAttributeInfo[]) readFields.get("mmbAttributes", (Object) null);
            if (readFields.defaulted("mmbAttributes")) {
                throw new NullPointerException("mmbAttributes");
            }
            this.modelMBeanConstructors = (MBeanConstructorInfo[]) readFields.get("mmbConstructors", (Object) null);
            if (readFields.defaulted("mmbConstructors")) {
                throw new NullPointerException("mmbConstructors");
            }
            this.modelMBeanNotifications = (MBeanNotificationInfo[]) readFields.get("mmbNotifications", (Object) null);
            if (readFields.defaulted("mmbNotifications")) {
                throw new NullPointerException("mmbNotifications");
            }
            this.modelMBeanOperations = (MBeanOperationInfo[]) readFields.get("mmbOperations", (Object) null);
            if (readFields.defaulted("mmbOperations")) {
                throw new NullPointerException("mmbOperations");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("modelMBeanDescriptor", this.modelMBeanDescriptor);
            putFields.put("mmbAttributes", this.modelMBeanAttributes);
            putFields.put("mmbConstructors", this.modelMBeanConstructors);
            putFields.put("mmbNotifications", this.modelMBeanNotifications);
            putFields.put("mmbOperations", this.modelMBeanOperations);
            putFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
