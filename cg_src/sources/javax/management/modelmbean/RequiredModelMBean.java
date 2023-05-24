package javax.management.modelmbean;

import android.content.Context;
import com.sun.jmx.trace.Trace;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;
import javax.management.ServiceNotFoundException;
import javax.management.loading.ClassLoaderRepository;
import javax.resource.spi.work.WorkException;
import soot.JavaBasicTypes;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/RequiredModelMBean.class */
public class RequiredModelMBean implements ModelMBean, MBeanRegistration, NotificationEmitter {
    ModelMBeanInfo modelMBeanInfo;
    private NotificationBroadcasterSupport generalBroadcaster = null;
    private NotificationBroadcasterSupport attributeBroadcaster = null;
    private Object managedResource = null;
    private String managedResourceType = "objectreference";
    private String currClass = "RequiredModelMBean";
    private boolean registered = false;
    private transient MBeanServer server = null;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$java$lang$Void;

    public RequiredModelMBean() throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("RequiredModelMBean()", "Entry and Exit");
        }
        this.modelMBeanInfo = createDefaultModelMBeanInfo();
    }

    public RequiredModelMBean(ModelMBeanInfo modelMBeanInfo) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("RequiredModelMBean(MBeanInfo)", "Entry");
        }
        setModelMBeanInfo(modelMBeanInfo);
        if (tracing()) {
            trace("RequiredModelMBean(MBeanInfo)", "Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBean
    public void setModelMBeanInfo(ModelMBeanInfo modelMBeanInfo) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("setModelMBeanInfo(ModelMBeanInfo)", "Entry");
        }
        if (modelMBeanInfo == null) {
            if (tracing()) {
                trace("setModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo is null: Raising exception.");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("ModelMBeanInfo must not be null"), "Exception occured trying to initialize the ModelMBeanInfo of the RequiredModelMBean");
        } else if (this.registered) {
            if (tracing()) {
                trace("setModelMBeanInfo(ModelMBeanInfo)", "RequiredMBean is registered: Raising exception.");
            }
            throw new RuntimeOperationsException(new IllegalStateException("cannot call setModelMBeanInfo while ModelMBean is registered"), "Exception occured trying to set the ModelMBeanInfo of the RequiredModelMBean");
        } else {
            if (tracing()) {
                trace("setModelMBeanInfo(ModelMBeanInfo)", new StringBuffer().append("Setting ModelMBeanInfo to ").append(printModelMBeanInfo(modelMBeanInfo)).toString());
                trace("setModelMBeanInfo(ModelMBeanInfo)", new StringBuffer().append("ModelMBeanInfo notifications has ").append(modelMBeanInfo.getNotifications().length).append(" elements").toString());
            }
            this.modelMBeanInfo = (ModelMBeanInfo) modelMBeanInfo.clone();
            if (tracing()) {
                trace("setModelMBeanInfo(ModelMBeanInfo)", new StringBuffer().append("set mbeanInfo to: ").append(printModelMBeanInfo(this.modelMBeanInfo)).toString());
            }
            if (tracing()) {
                trace("setModelMBeanInfo(ModelMBeanInfo)", "Exit");
            }
        }
    }

    @Override // javax.management.modelmbean.ModelMBean
    public void setManagedResource(Object obj, String str) throws MBeanException, RuntimeOperationsException, InstanceNotFoundException, InvalidTargetObjectTypeException {
        if (tracing()) {
            trace("setManagedResource(Object,String)", "Entry");
        }
        if (str == null || !str.equalsIgnoreCase("objectReference")) {
            if (tracing()) {
                trace("setManagedResource(Object,String)", new StringBuffer().append("Managed Resouce Type is not supported: ").append(str).toString());
            }
            throw new InvalidTargetObjectTypeException(str);
        }
        if (tracing()) {
            trace("setManagedResource(Object,String)", "Managed Resouce is valid");
        }
        this.managedResource = obj;
        if (tracing()) {
            trace("setManagedResource(Object, String)", "Exit");
        }
    }

    @Override // javax.management.PersistentMBean
    public void load() throws MBeanException, RuntimeOperationsException, InstanceNotFoundException {
        ServiceNotFoundException serviceNotFoundException = new ServiceNotFoundException("Persistence not supported for this MBean");
        throw new MBeanException(serviceNotFoundException, serviceNotFoundException.getMessage());
    }

    @Override // javax.management.PersistentMBean
    public void store() throws MBeanException, RuntimeOperationsException, InstanceNotFoundException {
        ServiceNotFoundException serviceNotFoundException = new ServiceNotFoundException("Persistence not supported for this MBean");
        throw new MBeanException(serviceNotFoundException, serviceNotFoundException.getMessage());
    }

    private Object resolveForCacheValue(Descriptor descriptor) throws MBeanException, RuntimeOperationsException {
        String str;
        boolean z;
        boolean z2;
        if (tracing()) {
            trace("resolveForCacheValue(Descriptor)", "Entry");
        }
        Object obj = null;
        if (descriptor == null) {
            if (tracing()) {
                trace("resolveForCacheValue(Descriptor)", "Input Descriptor is null");
            }
            return null;
        }
        if (tracing()) {
            trace("resolveForCacheValue(Descriptor)", new StringBuffer().append("descriptor is ").append(descriptor.toString()).toString());
        }
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        if (mBeanDescriptor == null && tracing()) {
            trace("resolveForCacheValue(Descriptor)", "MBean Descriptor is null");
        }
        Object fieldValue = descriptor.getFieldValue("currencyTimeLimit");
        if (fieldValue != null) {
            str = fieldValue.toString();
        } else {
            str = null;
        }
        if (str == null && mBeanDescriptor != null) {
            Object fieldValue2 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
            if (fieldValue2 != null) {
                str = fieldValue2.toString();
            } else {
                str = null;
            }
        }
        if (str != null) {
            if (tracing()) {
                trace("resolveForCacheValue(Descriptor)", new StringBuffer().append("currencyTimeLimit: ").append(str).toString());
            }
            long longValue = new Long(str).longValue() * 1000;
            if (longValue < 0) {
                z = false;
                z2 = true;
                if (tracing()) {
                    trace("resolveForCacheValue(Descriptor)", new StringBuffer().append(longValue).append(": never Cached").toString());
                }
            } else if (longValue == 0) {
                z = true;
                z2 = false;
                if (tracing()) {
                    trace("resolveForCacheValue(Descriptor)", "always valid Cache");
                }
            } else {
                Object fieldValue3 = descriptor.getFieldValue("lastUpdatedTimeStamp");
                String obj2 = fieldValue3 != null ? fieldValue3.toString() : null;
                if (tracing()) {
                    trace("resolveForCacheValue(Descriptor)", new StringBuffer().append("lastUpdatedTimeStamp: ").append(obj2).toString());
                }
                if (obj2 == null) {
                    obj2 = WorkException.UNDEFINED;
                }
                long longValue2 = new Long(obj2).longValue();
                if (tracing()) {
                    trace("resolveForCacheValue(Descriptor)", new StringBuffer().append(" currencyPeriod:").append(longValue).append(" lastUpdatedTimeStamp:").append(longValue2).toString());
                }
                long time = new Date().getTime();
                if (time < longValue2 + longValue) {
                    z = true;
                    z2 = false;
                    if (tracing()) {
                        trace("resolveForCacheValue(Descriptor)", new StringBuffer().append(" timed valid Cache for ").append(time).append(" < ").append(longValue2 + longValue).toString());
                    }
                } else {
                    z = false;
                    z2 = true;
                    if (tracing()) {
                        trace("resolveForCacheValue(Descriptor)", new StringBuffer().append("timed expired cache for ").append(time).append(" > ").append(longValue2 + longValue).toString());
                    }
                }
            }
            if (tracing()) {
                trace("resolveForCacheValue(Descriptor)", new StringBuffer().append("returnCachedValue:").append(z).append(" resetValue: ").append(z2).toString());
            }
            if (z) {
                Object fieldValue4 = descriptor.getFieldValue("value");
                if (fieldValue4 != null) {
                    obj = fieldValue4;
                    if (tracing()) {
                        trace("resolveForCacheValue(Descriptor)", new StringBuffer().append("valid Cache value: ").append(fieldValue4).toString());
                    }
                } else {
                    obj = null;
                    if (tracing()) {
                        trace("resolveForCacheValue(Descriptor)", "no Cached value");
                    }
                }
            }
            if (z2) {
                descriptor.removeField("lastUpdatedTimeStamp");
                descriptor.removeField("value");
                obj = null;
                this.modelMBeanInfo.setDescriptor(descriptor, null);
                if (tracing()) {
                    trace("resolveForCacheValue(Descriptor)", "reset cached value to null");
                }
            }
        }
        if (tracing()) {
            trace("resolveForCache(Descriptor)", "Exit");
        }
        return obj;
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        if (tracing()) {
            trace("getMBeanInfo()", "Entry and Exit");
        }
        if (this.modelMBeanInfo == null) {
            if (tracing()) {
                trace("getMBeanInfo()", "modelMBeanInfo is null");
            }
            this.modelMBeanInfo = createDefaultModelMBeanInfo();
        }
        if (tracing()) {
            trace("getMBeanInfo()", new StringBuffer().append("ModelMBeanInfo is ").append(this.modelMBeanInfo.getClassName()).append(" for ").append(this.modelMBeanInfo.getDescription()).toString());
            trace("getMBeanInfo()", printModelMBeanInfo(this.modelMBeanInfo));
        }
        return (MBeanInfo) this.modelMBeanInfo.clone();
    }

    private String printModelMBeanInfo(ModelMBeanInfo modelMBeanInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        if (modelMBeanInfo == null) {
            if (tracing()) {
                trace("printModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo to print is null, printing local ModelMBeanInfo");
            }
            modelMBeanInfo = this.modelMBeanInfo;
        }
        stringBuffer.append("\nMBeanInfo for ModelMBean is:");
        stringBuffer.append(new StringBuffer().append("\nCLASSNAME: \t").append(modelMBeanInfo.getClassName()).toString());
        stringBuffer.append(new StringBuffer().append("\nDESCRIPTION: \t").append(modelMBeanInfo.getDescription()).toString());
        try {
            stringBuffer.append(new StringBuffer().append("\nMBEAN DESCRIPTOR: \t").append(modelMBeanInfo.getMBeanDescriptor()).toString());
        } catch (Exception e) {
            stringBuffer.append("\nMBEAN DESCRIPTOR: \t is invalid");
        }
        stringBuffer.append("\nATTRIBUTES");
        MBeanAttributeInfo[] attributes = modelMBeanInfo.getAttributes();
        if (attributes != null && attributes.length > 0) {
            for (MBeanAttributeInfo mBeanAttributeInfo : attributes) {
                ModelMBeanAttributeInfo modelMBeanAttributeInfo = (ModelMBeanAttributeInfo) mBeanAttributeInfo;
                stringBuffer.append(new StringBuffer().append(" ** NAME: \t").append(modelMBeanAttributeInfo.getName()).toString());
                stringBuffer.append(new StringBuffer().append("    DESCR: \t").append(modelMBeanAttributeInfo.getDescription()).toString());
                stringBuffer.append(new StringBuffer().append("    TYPE: \t").append(modelMBeanAttributeInfo.getType()).append("    READ: \t").append(modelMBeanAttributeInfo.isReadable()).append("    WRITE: \t").append(modelMBeanAttributeInfo.isWritable()).toString());
                stringBuffer.append(new StringBuffer().append("    DESCRIPTOR: ").append(modelMBeanAttributeInfo.getDescriptor().toString()).toString());
            }
        } else {
            stringBuffer.append(" ** No attributes **");
        }
        stringBuffer.append("\nCONSTRUCTORS");
        MBeanConstructorInfo[] constructors = modelMBeanInfo.getConstructors();
        if (constructors != null && constructors.length > 0) {
            for (MBeanConstructorInfo mBeanConstructorInfo : constructors) {
                ModelMBeanConstructorInfo modelMBeanConstructorInfo = (ModelMBeanConstructorInfo) mBeanConstructorInfo;
                stringBuffer.append(new StringBuffer().append(" ** NAME: \t").append(modelMBeanConstructorInfo.getName()).toString());
                stringBuffer.append(new StringBuffer().append("    DESCR: \t").append(modelMBeanConstructorInfo.getDescription()).toString());
                stringBuffer.append(new StringBuffer().append("    PARAM: \t").append(modelMBeanConstructorInfo.getSignature().length).append(" parameter(s)").toString());
                stringBuffer.append(new StringBuffer().append("    DESCRIPTOR: ").append(modelMBeanConstructorInfo.getDescriptor().toString()).toString());
            }
        } else {
            stringBuffer.append(" ** No Constructors **");
        }
        stringBuffer.append("\nOPERATIONS");
        MBeanOperationInfo[] operations = modelMBeanInfo.getOperations();
        if (operations != null && operations.length > 0) {
            for (MBeanOperationInfo mBeanOperationInfo : operations) {
                ModelMBeanOperationInfo modelMBeanOperationInfo = (ModelMBeanOperationInfo) mBeanOperationInfo;
                stringBuffer.append(new StringBuffer().append(" ** NAME: \t").append(modelMBeanOperationInfo.getName()).toString());
                stringBuffer.append(new StringBuffer().append("    DESCR: \t").append(modelMBeanOperationInfo.getDescription()).toString());
                stringBuffer.append(new StringBuffer().append("    PARAM: \t").append(modelMBeanOperationInfo.getSignature().length).append(" parameter(s)").toString());
                stringBuffer.append(new StringBuffer().append("    DESCRIPTOR: ").append(modelMBeanOperationInfo.getDescriptor().toString()).toString());
            }
        } else {
            stringBuffer.append(" ** No operations ** ");
        }
        stringBuffer.append("\nNOTIFICATIONS");
        MBeanNotificationInfo[] notifications = modelMBeanInfo.getNotifications();
        if (notifications != null && notifications.length > 0) {
            for (MBeanNotificationInfo mBeanNotificationInfo : notifications) {
                ModelMBeanNotificationInfo modelMBeanNotificationInfo = (ModelMBeanNotificationInfo) mBeanNotificationInfo;
                stringBuffer.append(new StringBuffer().append(" ** NAME: \t").append(modelMBeanNotificationInfo.getName()).toString());
                stringBuffer.append(new StringBuffer().append("    DESCR: \t").append(modelMBeanNotificationInfo.getDescription()).toString());
                stringBuffer.append(new StringBuffer().append("    DESCRIPTOR: ").append(modelMBeanNotificationInfo.getDescriptor().toString()).toString());
            }
        } else {
            stringBuffer.append(" ** No notifications **");
        }
        stringBuffer.append(" ** ModelMBean: End of MBeanInfo ** ");
        return stringBuffer.toString();
    }

    private void echo(String str) {
        trace("echo(txt)", str);
    }

    @Override // javax.management.DynamicMBean
    public Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        Class<?>[] clsArr;
        Object[] objArr2;
        Class<?> cls;
        String str2;
        if (tracing()) {
            trace("invoke(String, Object[], String[])", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Method name must not be null"), "An exception occured while trying to invoke a method on a RequiredModelMBean");
        }
        Method method = null;
        boolean[] zArr = null;
        String str3 = "";
        String str4 = str;
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf > 0) {
            str3 = str.substring(0, lastIndexOf);
            str4 = str.substring(lastIndexOf + 1);
        }
        int indexOf = str4.indexOf("(");
        if (indexOf > 0) {
            str4 = str4.substring(0, indexOf);
        }
        if (tracing()) {
            trace("invoke(String, Object[], String[])", new StringBuffer().append("Finding operation ").append(str).append(" as ").append(str4).toString());
        }
        boolean z = true;
        if (objArr != null && objArr.length != 0) {
            objArr2 = objArr;
            clsArr = new Class[objArr.length];
            zArr = new boolean[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                try {
                    if (tracing()) {
                        trace("invoke(String, Object[], String[])", new StringBuffer().append("finding class for ").append(strArr[i]).toString());
                    }
                    if (strArr[i].equals(Boolean.TYPE.toString())) {
                        clsArr[i] = Boolean.TYPE;
                    } else if (strArr[i].equals(Integer.TYPE.toString())) {
                        clsArr[i] = Integer.TYPE;
                    } else if (strArr[i].equals(Character.TYPE.toString())) {
                        clsArr[i] = Character.TYPE;
                    } else if (strArr[i].equals(Float.TYPE.toString())) {
                        clsArr[i] = Float.TYPE;
                    } else if (strArr[i].equals(Long.TYPE.toString())) {
                        clsArr[i] = Long.TYPE;
                    } else if (strArr[i].equals(Double.TYPE.toString())) {
                        clsArr[i] = Double.TYPE;
                    } else if (strArr[i].equals(Byte.TYPE.toString())) {
                        clsArr[i] = Byte.TYPE;
                    } else if (strArr[i].equals(Short.TYPE.toString())) {
                        clsArr[i] = Short.TYPE;
                    } else {
                        zArr[i] = true;
                        clsArr[i] = loadClass(strArr[i]);
                    }
                    if (tracing()) {
                        trace("invoke(String, Object[], String[])", new StringBuffer().append("invoke method found a valid argument: ").append(clsArr[i].toString()).append(" is ").append(objArr2[i] == null ? Jimple.NULL : objArr2[i].toString()).toString());
                    }
                } catch (ClassNotFoundException e) {
                    if (tracing()) {
                        trace("invoke(String, Object[], String[])", new StringBuffer().append(str).append(" method ").append(str4).append("is not in RequiredModelMBean class:").append(e.getMessage()).toString());
                        trace("invoke(String, Object[], String[])", "The parameter class could not be found in default class loader");
                    }
                    zArr[i] = true;
                    clsArr[i] = null;
                    z = false;
                }
            }
        } else {
            clsArr = new Class[0];
            objArr2 = new Object[0];
        }
        if (z) {
            try {
                method = getClass().getMethod(str4, clsArr);
            } catch (NoSuchMethodException e2) {
                z = false;
            }
        }
        if (method != null || z) {
            try {
                if (tracing()) {
                    trace("invoke(String, Object[], String[])", new StringBuffer().append(str).append(" being invoked on ModelMBean").toString());
                }
                Object invoke = method.invoke(this, objArr2);
                if (tracing()) {
                    trace("invoke(String, Object[], String[])", new StringBuffer().append("invoke returning ").append(invoke == null ? Jimple.NULL : "non-null").append(" response from local invoke of ").append(str).toString());
                }
                return invoke;
            } catch (Error e3) {
                throw new RuntimeErrorException(e3, new StringBuffer().append("Error occured in RequiredModelMBean while trying to invoke operation ").append(str).toString());
            } catch (IllegalAccessException e4) {
                throw new ReflectionException(e4, new StringBuffer().append("IllegalAccessException occured in RequiredModelMBean while trying to invoke operation ").append(str).toString());
            } catch (RuntimeErrorException e5) {
                throw new RuntimeOperationsException(e5, new StringBuffer().append("RuntimeException occured in RequiredModelMBean while trying to invoke operation ").append(str).toString());
            } catch (RuntimeException e6) {
                throw new RuntimeOperationsException(e6, new StringBuffer().append("RuntimeException occured in RequiredModelMBean while trying to invoke operation ").append(str).toString());
            } catch (InvocationTargetException e7) {
                Throwable targetException = e7.getTargetException();
                if (targetException instanceof RuntimeException) {
                    throw new MBeanException((RuntimeException) targetException, new StringBuffer().append("RuntimeException thrown in RequiredModelMBean while trying to invoke operation ").append(str).toString());
                }
                if (targetException instanceof ReflectionException) {
                    throw ((ReflectionException) targetException);
                }
                throw new MBeanException((Exception) targetException, new StringBuffer().append("Exception thrown in RequiredModelMBean while trying to invoke operation ").append(str).toString());
            } catch (Exception e8) {
                throw new ReflectionException(e8, new StringBuffer().append("Exception occured in RequiredModelMBean while trying to invoke operation ").append(str).toString());
            }
        }
        if (tracing()) {
            trace("invoke(String, Object[], String[])", new StringBuffer().append(str).append(" is not on RequiredModelMBean, ").append("looking in descriptor for ").append(str4).toString());
        }
        ModelMBeanOperationInfo operation = this.modelMBeanInfo.getOperation(str4);
        if (operation == null) {
            throw new MBeanException(new ServiceNotFoundException(new StringBuffer().append("operation ").append(str).append(" execution not supported from mbeaninfo data").toString()), "An exception occured in RequiredModelMBean while trying to invoke an operation");
        }
        Descriptor descriptor = operation.getDescriptor();
        if (descriptor == null) {
            throw new MBeanException(new ServiceNotFoundException(new StringBuffer().append("operation ").append(str).append(" execution not supported from descriptor data").toString()), "An exception occured in RequiredModelMBean while trying to invoke an operation");
        }
        Object resolveForCacheValue = resolveForCacheValue(descriptor);
        if (resolveForCacheValue == null) {
            if (tracing()) {
                trace("invoke(String, Object[], String[])", "No cached value returned for operation");
            }
            Object fieldValue = descriptor.getFieldValue("targetObject");
            if (fieldValue != null) {
                if (tracing()) {
                    trace("invoke(String, Object[], String[])", "Found target object in descriptor");
                }
                String str5 = (String) descriptor.getFieldValue("targetType");
                if (str5 == null || !str5.equalsIgnoreCase("objectReference")) {
                    throw new MBeanException(new InvalidTargetObjectTypeException(str5), "An exception occured while trying to invoke an operation on a descriptor provided target");
                }
                if (tracing()) {
                    trace("invoke(String, Object[], String[]", "target object is a valid type");
                }
            } else if (this.managedResource != null) {
                if (tracing()) {
                    trace("invoke(String, Object[], String[])", "managedResource for invoke found");
                }
                fieldValue = this.managedResource;
            } else {
                if (tracing()) {
                    trace("invoke(String, Object[], String[])", "err managedResource for invoke is null");
                }
                throw new MBeanException(new ServiceNotFoundException("managedResource for invoke is null"), "An exception occured in RequiredModelMBean while trying to invoke an operation");
            }
            try {
                if (tracing()) {
                    trace("invoke(String, Object[], String[])", "getting class for operation");
                }
                if (str3 == null || str3.equals("")) {
                    str3 = (String) descriptor.getFieldValue("class");
                    if (str3 == null || str3.equals("")) {
                        cls = fieldValue.getClass();
                    } else {
                        cls = loadClass(str3);
                    }
                } else {
                    cls = loadClass(str3);
                }
                if (cls == null) {
                    throw new ReflectionException(new ClassNotFoundException(new StringBuffer().append("A valid class could not be found for ").append(str4).toString()), "Exception occured while trying to find class for method of invoke");
                }
                try {
                    if (tracing()) {
                        trace("invoke(String, Object[], String[])", "setting signature with correct class loaders for operation");
                    }
                    ClassLoader classLoader = fieldValue.getClass().getClassLoader();
                    if (classLoader != null) {
                        for (int i2 = 0; i2 < clsArr.length; i2++) {
                            if (zArr[i2]) {
                                try {
                                    clsArr[i2] = classLoader.loadClass(strArr[i2]);
                                } catch (ClassNotFoundException e9) {
                                    clsArr[i2] = loadClass(strArr[i2]);
                                }
                            }
                        }
                    }
                    if (tracing()) {
                        trace("invoke(String, Object[], String[])", new StringBuffer().append("Looking for operations class ").append(str3).append(" method ").append(str4).toString());
                    }
                    try {
                        Method method2 = cls.getMethod(str4, clsArr);
                        if (method2 == null) {
                            throw new ReflectionException(new Exception("null method handle"), "Retrieved null method handle for method");
                        }
                        if (tracing()) {
                            trace("invoke(String, Object[], String[])", new StringBuffer().append("Retrieved valid method handle for ").append(str3).append(".").append(str4).toString());
                        }
                        try {
                            resolveForCacheValue = method2.invoke(fieldValue, objArr2);
                            if (tracing()) {
                                trace("invoke(String, Object[], String[])", new StringBuffer().append("invoke done for ").append(str).toString());
                            }
                            if (resolveForCacheValue != null && descriptor != null) {
                                Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
                                Object fieldValue2 = descriptor.getFieldValue("currencyTimeLimit");
                                if (fieldValue2 != null) {
                                    str2 = fieldValue2.toString();
                                } else {
                                    str2 = null;
                                }
                                if (str2 == null && mBeanDescriptor != null) {
                                    Object fieldValue3 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
                                    if (fieldValue3 != null) {
                                        str2 = fieldValue3.toString();
                                    } else {
                                        str2 = null;
                                    }
                                }
                                if (str2 != null && !str2.equals(WorkException.INTERNAL)) {
                                    descriptor.setField("value", resolveForCacheValue);
                                    descriptor.setField("lastUpdatedTimeStamp", new Long(new Date().getTime()).toString());
                                    operation.setDescriptor(descriptor);
                                    this.modelMBeanInfo.setDescriptor(descriptor, "operation");
                                    if (tracing()) {
                                        trace("invoke(String,Object[],Object[])", new StringBuffer().append("new descriptor is ").append(descriptor.toString()).toString());
                                        trace("invoke(String,Object[],Object[])", new StringBuffer().append("OperationInfo descriptor is ").append(operation.getDescriptor().toString()).toString());
                                        trace("invoke(String,Object[],Object[])", new StringBuffer().append("OperationInfo descriptor is ").append(this.modelMBeanInfo.getDescriptor(str, "operation").toString()).toString());
                                    }
                                }
                                if (tracing()) {
                                    trace("invoke(String, Object[], String[])", new StringBuffer().append("invoke retrieved ").append(resolveForCacheValue.toString()).toString());
                                }
                            }
                        } catch (Error e10) {
                            throw new RuntimeErrorException(e10, new StringBuffer().append("Error occured in managed resource while trying to invoke operation ").append(str).toString());
                        } catch (IllegalAccessException e11) {
                            throw new ReflectionException(e11, new StringBuffer().append("IllegalAccessException occured in managed resource while trying to invoke operation ").append(str).toString());
                        } catch (RuntimeErrorException e12) {
                            throw new RuntimeOperationsException(e12, new StringBuffer().append("RuntimeException occured in managed resource while trying to invoke operation ").append(str).toString());
                        } catch (RuntimeException e13) {
                            throw new RuntimeOperationsException(e13, new StringBuffer().append("RuntimeException occured in managed resource while trying to invoke operation ").append(str).toString());
                        } catch (InvocationTargetException e14) {
                            Throwable targetException2 = e14.getTargetException();
                            if (targetException2 instanceof RuntimeException) {
                                throw new RuntimeMBeanException((RuntimeException) targetException2, new StringBuffer().append("RuntimeException thrown in managed resource while trying to invoke operation ").append(str).toString());
                            }
                            if (targetException2 instanceof Error) {
                                throw new RuntimeErrorException((Error) targetException2, new StringBuffer().append("Error thrown in managed resource while trying to invoke operation ").append(str).toString());
                            }
                            if (targetException2 instanceof ReflectionException) {
                                throw ((ReflectionException) targetException2);
                            }
                            throw new MBeanException((Exception) targetException2, new StringBuffer().append("Exception thrown in managed resource while trying to invoke operation ").append(str).toString());
                        } catch (Exception e15) {
                            throw new ReflectionException(e15, new StringBuffer().append("Exception occured in managed resource while trying to invoke operation ").append(str).toString());
                        }
                    } catch (NoSuchMethodException e16) {
                        throw new ReflectionException(e16, new StringBuffer().append("The method ").append(str3).append(".").append(str4).append(" could not be found").toString());
                    }
                } catch (ClassNotFoundException e17) {
                    throw new ReflectionException(e17, "The parameter class could not be found");
                }
            } catch (ClassNotFoundException e18) {
                throw new ReflectionException(e18, new StringBuffer().append("The target object class ").append(str3).append(" could not be found").toString());
            }
        }
        if (tracing()) {
            trace("invoke(String, Object[], String[])", "Exit");
        }
        return resolveForCacheValue;
    }

    @Override // javax.management.DynamicMBean
    public Object getAttribute(String str) throws AttributeNotFoundException, MBeanException, ReflectionException {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeName must not be null"), "Exception occured trying to get attribute of a RequiredModelMBean");
        }
        if (tracing()) {
            trace("getAttribute(String)", new StringBuffer().append("Entry with").append(str).toString());
        }
        try {
            if (this.modelMBeanInfo == null) {
                throw new AttributeNotFoundException(new StringBuffer().append("getAttribute failed: ModelMBeanInfo not found for ").append(str).toString());
            }
            ModelMBeanAttributeInfo attribute = this.modelMBeanInfo.getAttribute(str);
            Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
            if (attribute == null) {
                throw new AttributeNotFoundException(new StringBuffer().append("getAttribute failed: ModelMBeanAttributeInfo not found for ").append(str).toString());
            }
            Descriptor descriptor = attribute.getDescriptor();
            if (descriptor != null) {
                if (!attribute.isReadable()) {
                    throw new AttributeNotFoundException(new StringBuffer().append("getAttribute failed: ").append(str).append(" is not readable ").toString());
                }
                Object resolveForCacheValue = resolveForCacheValue(descriptor);
                if (tracing()) {
                    trace("getAttribute(String)", new StringBuffer().append("*** cached value is ").append(resolveForCacheValue).toString());
                }
                if (resolveForCacheValue == null) {
                    if (tracing()) {
                        trace("getAttribute(String)", "**** cached value is null - getting getMethod");
                    }
                    String str2 = (String) descriptor.getFieldValue("getMethod");
                    if (str2 != null) {
                        if (tracing()) {
                            trace("getAttribute(String)", new StringBuffer().append("invoking a getMethod for ").append(str).toString());
                        }
                        Object invoke = invoke(str2, new Object[0], new String[0]);
                        if (invoke != null) {
                            if (tracing()) {
                                trace("getAttribute(String)", "got a non-null response from getMethod\n");
                            }
                            resolveForCacheValue = invoke;
                            Object fieldValue = descriptor.getFieldValue("currencyTimeLimit");
                            String obj = fieldValue != null ? fieldValue.toString() : null;
                            if (obj == null && mBeanDescriptor != null) {
                                Object fieldValue2 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
                                obj = fieldValue2 != null ? fieldValue2.toString() : null;
                            }
                            if (obj != null && !obj.equals(WorkException.INTERNAL)) {
                                if (tracing()) {
                                    trace("getAttribute(String)", "setting cached value and lastUpdatedTime in descriptor");
                                }
                                descriptor.setField("value", resolveForCacheValue);
                                descriptor.setField("lastUpdatedTimeStamp", new Long(new Date().getTime()).toString());
                                attribute.setDescriptor(descriptor);
                                this.modelMBeanInfo.setDescriptor(descriptor, "attribute");
                                if (tracing()) {
                                    trace("getAttribute(String)", new StringBuffer().append("new descriptor is ").append(descriptor.toString()).toString());
                                    trace("getAttribute(String)", new StringBuffer().append("local: AttributeInfo descriptor is ").append(attribute.getDescriptor().toString()).toString());
                                    trace("getAttribute(String)", new StringBuffer().append("modelMBeanInfo: AttributeInfo descriptor is ").append(this.modelMBeanInfo.getDescriptor(str, "attribute").toString()).toString());
                                }
                            }
                        } else {
                            if (tracing()) {
                                trace("getAttribute(String)", "got a null response from getMethod\n");
                            }
                            resolveForCacheValue = null;
                        }
                    } else {
                        if (tracing()) {
                            trace("getAttribute(String)", new StringBuffer().append("could not find getMethod for ").append(str).append(", returning default value").toString());
                        }
                        resolveForCacheValue = descriptor.getFieldValue("default");
                    }
                }
                String type = attribute.getType();
                if (resolveForCacheValue != null) {
                    String name = resolveForCacheValue.getClass().getName();
                    if (!type.equals(name)) {
                        String[] strArr = {Boolean.TYPE.getName(), Byte.TYPE.getName(), Character.TYPE.getName(), Short.TYPE.getName(), Integer.TYPE.getName(), Long.TYPE.getName(), Float.TYPE.getName(), Double.TYPE.getName(), Void.TYPE.getName()};
                        String[] strArr2 = new String[9];
                        if (class$java$lang$Boolean == null) {
                            cls = class$(JavaBasicTypes.JAVA_LANG_BOOLEAN);
                            class$java$lang$Boolean = cls;
                        } else {
                            cls = class$java$lang$Boolean;
                        }
                        strArr2[0] = cls.getName();
                        if (class$java$lang$Byte == null) {
                            cls2 = class$(JavaBasicTypes.JAVA_LANG_BYTE);
                            class$java$lang$Byte = cls2;
                        } else {
                            cls2 = class$java$lang$Byte;
                        }
                        strArr2[1] = cls2.getName();
                        if (class$java$lang$Character == null) {
                            cls3 = class$(JavaBasicTypes.JAVA_LANG_CHARACTER);
                            class$java$lang$Character = cls3;
                        } else {
                            cls3 = class$java$lang$Character;
                        }
                        strArr2[2] = cls3.getName();
                        if (class$java$lang$Short == null) {
                            cls4 = class$(JavaBasicTypes.JAVA_LANG_SHORT);
                            class$java$lang$Short = cls4;
                        } else {
                            cls4 = class$java$lang$Short;
                        }
                        strArr2[3] = cls4.getName();
                        if (class$java$lang$Integer == null) {
                            cls5 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                            class$java$lang$Integer = cls5;
                        } else {
                            cls5 = class$java$lang$Integer;
                        }
                        strArr2[4] = cls5.getName();
                        if (class$java$lang$Long == null) {
                            cls6 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                            class$java$lang$Long = cls6;
                        } else {
                            cls6 = class$java$lang$Long;
                        }
                        strArr2[5] = cls6.getName();
                        if (class$java$lang$Float == null) {
                            cls7 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                            class$java$lang$Float = cls7;
                        } else {
                            cls7 = class$java$lang$Float;
                        }
                        strArr2[6] = cls7.getName();
                        if (class$java$lang$Double == null) {
                            cls8 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
                            class$java$lang$Double = cls8;
                        } else {
                            cls8 = class$java$lang$Double;
                        }
                        strArr2[7] = cls8.getName();
                        if (class$java$lang$Void == null) {
                            cls9 = class$("java.lang.Void");
                            class$java$lang$Void = cls9;
                        } else {
                            cls9 = class$java$lang$Void;
                        }
                        strArr2[8] = cls9.getName();
                        boolean z = false;
                        int i = 0;
                        while (true) {
                            if (i > 8) {
                                break;
                            } else if (!type.equals(strArr[i]) || !name.equals(strArr2[i])) {
                                i++;
                            } else {
                                z = true;
                                break;
                            }
                        }
                        if (!z) {
                            if (tracing()) {
                                trace("getAttribute(String)", new StringBuffer().append("wrong response type '").append(type).append("'").toString());
                            }
                            throw new MBeanException(new InvalidAttributeValueException("Wrong value type received for get attribute"), "An exception occured while trying to get an attribute value through a RequiredModelMBean");
                        }
                    }
                }
                if (tracing()) {
                    trace("getAttribute(String)", "Exit");
                }
                return resolveForCacheValue;
            }
            if (tracing()) {
                trace("getAttribute(String)", new StringBuffer().append("getMethod failed ").append(str).append(" not in attributeDescriptor\n").toString());
            }
            throw new MBeanException(new InvalidAttributeValueException("Unable to resolve attribute value, no getMethod defined in descriptor for attribute"), "An exception occured while trying to get an attribute value through a RequiredModelMBean");
        } catch (AttributeNotFoundException e) {
            throw e;
        } catch (MBeanException e2) {
            throw e2;
        } catch (Exception e3) {
            if (tracing()) {
                trace("getAttribute(String)", new StringBuffer().append("getMethod failed with ").append(e3.getMessage()).append(" exception type ").append(e3.getClass().toString()).toString());
            }
            throw new MBeanException(e3, new StringBuffer().append("An exception occured while trying to get an attribute value: ").append(e3.getMessage()).toString());
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    @Override // javax.management.DynamicMBean
    public AttributeList getAttributes(String[] strArr) {
        if (tracing()) {
            trace("getAttributes(String[])", "Entry");
        }
        if (strArr == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames must not be null"), "Exception occured trying to get attributes of a RequiredModelMBean");
        }
        AttributeList attributeList = new AttributeList();
        for (int i = 0; i < strArr.length; i++) {
            try {
                attributeList.add(new Attribute(strArr[i], getAttribute(strArr[i])));
            } catch (Exception e) {
                error("getAttributes(String[])", new StringBuffer().append("Failed to get \"").append(strArr[i]).append("\": ").append(e).toString());
                traceX("getAttributes(String[])", e);
            }
        }
        if (tracing()) {
            trace("getAttributes(String[])", "Exit");
        }
        return attributeList;
    }

    @Override // javax.management.DynamicMBean
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (tracing()) {
            trace("setAttribute()", "Entry");
        }
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attribute must not be null"), "Exception occured trying to set an attribute of a RequiredModelMBean");
        }
        String name = attribute.getName();
        Object value = attribute.getValue();
        ModelMBeanAttributeInfo attribute2 = this.modelMBeanInfo.getAttribute(name);
        if (attribute2 == null) {
            throw new AttributeNotFoundException(new StringBuffer().append("setAttribute failed: ").append(name).append(" is not found ").toString());
        }
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        Descriptor descriptor = attribute2.getDescriptor();
        if (descriptor != null) {
            if (!attribute2.isWritable()) {
                throw new AttributeNotFoundException(new StringBuffer().append("setAttribute failed: ").append(name).append(" is not writable ").toString());
            }
            String str = (String) descriptor.getFieldValue("setMethod");
            String type = attribute2.getType();
            Object obj = "Unknown";
            try {
                obj = getAttribute(name);
            } catch (Throwable th) {
            }
            Attribute attribute3 = new Attribute(name, obj);
            if (str == null) {
                throw new MBeanException(new ServiceNotFoundException("No setter method defined"), "Exception thrown trying to set an attribute");
            }
            invoke(str, new Object[]{value}, new String[]{type});
            Object fieldValue = descriptor.getFieldValue("currencyTimeLimit");
            String obj2 = fieldValue != null ? fieldValue.toString() : null;
            if (obj2 == null && mBeanDescriptor != null) {
                Object fieldValue2 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
                obj2 = fieldValue2 != null ? fieldValue2.toString() : null;
            }
            if (obj2 != null && !obj2.equals(WorkException.INTERNAL)) {
                if (tracing()) {
                    trace("setAttribute()", new StringBuffer().append("setting cached value of ").append(name).append(" to ").append(value).toString());
                }
                descriptor.setField("value", value);
                descriptor.setField("lastUpdatedTimeStamp", new Long(new Date().getTime()).toString());
                attribute2.setDescriptor(descriptor);
                this.modelMBeanInfo.setDescriptor(descriptor, "attribute");
                if (tracing()) {
                    trace("setAttribute()", new StringBuffer().append("new descriptor is ").append(descriptor.toString()).toString());
                    trace("setAttribute()", new StringBuffer().append("AttributeInfo descriptor is ").append(attribute2.getDescriptor().toString()).toString());
                    trace("setAttribute()", new StringBuffer().append("AttributeInfo descriptor is ").append(this.modelMBeanInfo.getDescriptor(name, "attribute").toString()).toString());
                }
            }
            if (tracing()) {
                trace("setAttribute()", "sending sendAttributeNotification");
            }
            sendAttributeChangeNotification(attribute3, attribute);
            if (tracing()) {
                trace("setAttribute(Attribute)", "Exit");
                return;
            }
            return;
        }
        if (tracing()) {
            trace("setAttribute(String)", new StringBuffer().append("setMethod failed ").append(name).append(" not in attributeDescriptor\n").toString());
        }
        throw new InvalidAttributeValueException("Unable to resolve attribute value, no defined in descriptor for attribute");
    }

    @Override // javax.management.DynamicMBean
    public AttributeList setAttributes(AttributeList attributeList) {
        if (tracing()) {
            trace("setAttributes(AttributeList)", "Entry");
        }
        if (attributeList == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributes must not be null"), "Exception occured trying to set attributes of a RequiredModelMBean");
        }
        AttributeList attributeList2 = new AttributeList();
        Iterator it = attributeList.iterator();
        while (it.hasNext()) {
            Attribute attribute = (Attribute) it.next();
            try {
                setAttribute(attribute);
                attributeList2.add(attribute);
            } catch (Exception e) {
                attributeList2.remove(attribute);
            }
        }
        return attributeList2;
    }

    private ModelMBeanInfo createDefaultModelMBeanInfo() {
        return new ModelMBeanInfoSupport(getClass().getName(), "Default ModelMBean", null, null, null, null);
    }

    private synchronized void writeToLog(String str, String str2) throws Exception {
        if (tracing()) {
            trace("writeToLog()", new StringBuffer().append("Notification Logging to ").append(str).append(": ").append(str2).toString());
        }
        if (str == null || str2 == null) {
            if (tracing()) {
                trace("writeToLog()", "Bad input parameters");
                return;
            }
            return;
        }
        try {
            PrintStream printStream = new PrintStream(new FileOutputStream(str, true));
            printStream.println(str2);
            printStream.close();
            if (tracing()) {
                trace("writeToLog()", new StringBuffer().append("Successfully opened log ").append(str).toString());
            }
        } catch (Exception e) {
            if (tracing()) {
                trace("writeToLog", new StringBuffer().append("Exception ").append(e.toString()).append(" trying to write to the Notification log file ").append(str).toString());
            }
            throw e;
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IllegalArgumentException {
        if (tracing()) {
            trace("addNotificationListener(NotificationListener, NotificationFilter, Object)", "Entry");
        }
        if (notificationListener == null) {
            throw new IllegalArgumentException("notification listener must not be null");
        }
        if (this.generalBroadcaster == null) {
            this.generalBroadcaster = new NotificationBroadcasterSupport();
        }
        this.generalBroadcaster.addNotificationListener(notificationListener, notificationFilter, obj);
        if (tracing()) {
            trace("addNotificationListener(NotificationListener, NotificationFilter, Object)", "NotificationListener added");
            trace("addNotificationListener(NotificationListener, NotificationFilter, Object)", "Exit");
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        if (notificationListener == null) {
            throw new ListenerNotFoundException("Notification listener is null");
        }
        if (tracing()) {
            trace("removeNotificationListener(NotificationListener)", "Entry");
        }
        if (this.generalBroadcaster == null) {
            throw new ListenerNotFoundException("No notification listeners registered");
        }
        this.generalBroadcaster.removeNotificationListener(notificationListener);
        if (tracing()) {
            trace("removeNotificationListener(NotificationListener)", "Exit");
        }
    }

    @Override // javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        if (notificationListener == null) {
            throw new ListenerNotFoundException("Notification listener is null");
        }
        if (tracing()) {
            trace("removeNotificationListener(NotificationListener, NotificationFilter, Object)", "Entry");
        }
        if (this.generalBroadcaster == null) {
            throw new ListenerNotFoundException("No notification listeners registered");
        }
        this.generalBroadcaster.removeNotificationListener(notificationListener, notificationFilter, obj);
        if (tracing()) {
            trace("removeNotificationListener(NotificationListener, NotificationFilter, Object)", "Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendNotification(Notification notification) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("sendNotification(Notification)", "Entry");
        }
        if (notification == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("notification object must not be null"), "Exception occured trying to send a notification from a RequiredModelMBean");
        }
        Descriptor descriptor = this.modelMBeanInfo.getDescriptor(notification.getType(), Context.NOTIFICATION_SERVICE);
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        if (descriptor != null) {
            String str = (String) descriptor.getFieldValue("log");
            if (str == null && mBeanDescriptor != null) {
                str = (String) mBeanDescriptor.getFieldValue("log");
            }
            if (str != null && (str.equalsIgnoreCase("t") || str.equalsIgnoreCase("true"))) {
                String str2 = (String) descriptor.getFieldValue("logfile");
                if (str2 == null && mBeanDescriptor != null) {
                    str2 = (String) mBeanDescriptor.getFieldValue("logfile");
                }
                if (str2 != null) {
                    try {
                        writeToLog(str2, new StringBuffer().append("LogMsg: ").append(new Date(notification.getTimeStamp()).toString()).append(Instruction.argsep).append(notification.getType()).append(Instruction.argsep).append(notification.getMessage()).append(" Severity = ").append((String) descriptor.getFieldValue("severity")).toString());
                    } catch (Exception e) {
                        error("sendNotification(Notification)", new StringBuffer().append("Failed to log ").append(notification.getType()).append(" notification: ").append(e).toString());
                        traceX("sendNotification(Notification)", e);
                    }
                }
            }
        }
        if (this.generalBroadcaster != null) {
            this.generalBroadcaster.sendNotification(notification);
        }
        if (tracing()) {
            trace("sendNotification(Notification)", "sendNotification sent provided notification object");
        }
        if (tracing()) {
            trace("sendNotification(Notification)", "Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendNotification(String str) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("sendNotification(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("notification message must not be null"), "Exception occured trying to send a text notification from a ModelMBean");
        }
        sendNotification(new Notification("jmx.modelmbean.generic", this, 1L, str));
        if (tracing()) {
            trace("sendNotification(string)", "Notification sent");
        }
        if (tracing()) {
            trace("sendNotification(String)", "Exit");
        }
    }

    private static final boolean hasNotification(ModelMBeanInfo modelMBeanInfo, String str) {
        if (modelMBeanInfo == null) {
            return false;
        }
        try {
            return modelMBeanInfo.getNotification(str) != null;
        } catch (MBeanException e) {
            return false;
        } catch (RuntimeOperationsException e2) {
            return false;
        }
    }

    private static final ModelMBeanNotificationInfo makeGenericInfo() {
        return new ModelMBeanNotificationInfo(new String[]{"jmx.modelmbean.generic"}, "GENERIC", "A text notification has been issued by the managed resource", new DescriptorSupport(new String[]{"name=GENERIC", "descriptorType=notification", "log=T", "severity=5", "displayName=jmx.modelmbean.generic"}));
    }

    private static final ModelMBeanNotificationInfo makeAttributeChangeInfo() {
        return new ModelMBeanNotificationInfo(new String[]{"jmx.attribute.change"}, "ATTRIBUTE_CHANGE", "Signifies that an observed MBean attribute value has changed", new DescriptorSupport(new String[]{"name=ATTRIBUTE_CHANGE", "descriptorType=notification", "log=T", "severity=5", "displayName=jmx.attribute.change"}));
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        if (tracing()) {
            trace("getNotificationInfo()", "Entry");
        }
        boolean hasNotification = hasNotification(this.modelMBeanInfo, "GENERIC");
        boolean hasNotification2 = hasNotification(this.modelMBeanInfo, "ATTRIBUTE_CHANGE");
        ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr = (ModelMBeanNotificationInfo[]) this.modelMBeanInfo.getNotifications();
        ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr2 = new ModelMBeanNotificationInfo[(modelMBeanNotificationInfoArr == null ? 0 : modelMBeanNotificationInfoArr.length) + (hasNotification ? 0 : 1) + (hasNotification2 ? 0 : 1)];
        int i = 0;
        if (!hasNotification) {
            i = 0 + 1;
            modelMBeanNotificationInfoArr2[0] = makeGenericInfo();
        }
        if (!hasNotification2) {
            int i2 = i;
            i++;
            modelMBeanNotificationInfoArr2[i2] = makeAttributeChangeInfo();
        }
        int length = modelMBeanNotificationInfoArr.length;
        int i3 = i;
        for (int i4 = 0; i4 < length; i4++) {
            modelMBeanNotificationInfoArr2[i3 + i4] = modelMBeanNotificationInfoArr[i4];
        }
        if (tracing()) {
            trace("getNotificationInfo()", "Exit");
        }
        return modelMBeanNotificationInfoArr2;
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void addAttributeChangeNotificationListener(NotificationListener notificationListener, String str, Object obj) throws MBeanException, RuntimeOperationsException, IllegalArgumentException {
        if (tracing()) {
            trace("addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Entry");
        }
        if (notificationListener == null) {
            throw new IllegalArgumentException("Listener to be registered must not be null");
        }
        if (this.attributeBroadcaster == null) {
            this.attributeBroadcaster = new NotificationBroadcasterSupport();
        }
        AttributeChangeNotificationFilter attributeChangeNotificationFilter = new AttributeChangeNotificationFilter();
        MBeanAttributeInfo[] attributes = this.modelMBeanInfo.getAttributes();
        boolean z = false;
        if (str == null) {
            if (attributes != null && attributes.length > 0) {
                for (MBeanAttributeInfo mBeanAttributeInfo : attributes) {
                    attributeChangeNotificationFilter.enableAttribute(mBeanAttributeInfo.getName());
                }
            }
        } else if (attributes != null && attributes.length > 0) {
            int i = 0;
            while (true) {
                if (i < attributes.length) {
                    if (!str.equals(attributes[i].getName())) {
                        i++;
                    } else {
                        z = true;
                        attributeChangeNotificationFilter.enableAttribute(str);
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z) {
                throw new RuntimeOperationsException(new IllegalArgumentException("The attribute name does not exist"), "Exception occured trying to add an AttributeChangeNotification listener");
            }
        }
        if (tracing()) {
            trace("addAttributeChangeNotificationListener(NotificationListener, String, Object)", new StringBuffer().append("Set attribute change filter to ").append(attributeChangeNotificationFilter.getEnabledAttributes().firstElement().toString()).toString());
        }
        this.attributeBroadcaster.addNotificationListener(notificationListener, attributeChangeNotificationFilter, obj);
        if (tracing()) {
            trace("addAttributeChangeNotificationListener", new StringBuffer().append("added for ").append(str).toString());
        }
        if (tracing()) {
            trace("addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void removeAttributeChangeNotificationListener(NotificationListener notificationListener, String str) throws MBeanException, RuntimeOperationsException, ListenerNotFoundException {
        if (notificationListener == null) {
            throw new ListenerNotFoundException("Notification listener is null");
        }
        if (tracing()) {
            trace("removeAttributeChangeNotificationListener(NotificationListener, String)", "Entry");
        }
        if (this.attributeBroadcaster == null) {
            throw new ListenerNotFoundException("No attribute change notification listeners registered");
        }
        MBeanAttributeInfo[] attributes = this.modelMBeanInfo.getAttributes();
        boolean z = false;
        if (attributes != null && attributes.length > 0) {
            int i = 0;
            while (true) {
                if (i >= attributes.length) {
                    break;
                } else if (!attributes[i].getName().equals(str)) {
                    i++;
                } else {
                    z = true;
                    break;
                }
            }
        }
        if (!z && str != null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid attribute name"), "Exception occured trying to remove attribute change notification listener");
        }
        this.attributeBroadcaster.removeNotificationListener(notificationListener);
        if (tracing()) {
            trace("removeAttributeChangeNotificationListener(NotificationListener, String)", "Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendAttributeChangeNotification(AttributeChangeNotification attributeChangeNotification) throws MBeanException, RuntimeOperationsException {
        String str;
        String str2;
        if (tracing()) {
            trace("sendAttributeChangeNotification(AttributeChangeNotification)", "Entry");
        }
        if (attributeChangeNotification == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attribute change notification object must not be null"), "Exception occured trying to send attribute change notification of a ModelMBean");
        }
        Object oldValue = attributeChangeNotification.getOldValue();
        Object newValue = attributeChangeNotification.getNewValue();
        if (oldValue == null) {
            oldValue = Jimple.NULL;
        }
        if (newValue == null) {
            newValue = Jimple.NULL;
        }
        if (tracing()) {
            trace("sendAttributeChangeNotification(AttributeChangeNotification)", new StringBuffer().append("Sending AttributeChangeNotification  with ").append(attributeChangeNotification.getAttributeName()).append(attributeChangeNotification.getAttributeType()).append(attributeChangeNotification.getNewValue()).append(attributeChangeNotification.getOldValue()).toString());
        }
        Descriptor descriptor = this.modelMBeanInfo.getDescriptor(attributeChangeNotification.getType(), Context.NOTIFICATION_SERVICE);
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        if (descriptor != null) {
            String str3 = (String) descriptor.getFieldValue("log");
            if (str3 == null && mBeanDescriptor != null) {
                str3 = (String) mBeanDescriptor.getFieldValue("log");
            }
            if (str3 != null && (str3.equalsIgnoreCase("t") || str3.equalsIgnoreCase("true"))) {
                String str4 = (String) descriptor.getFieldValue("logfile");
                if (str4 == null && mBeanDescriptor != null) {
                    str4 = (String) mBeanDescriptor.getFieldValue("logfile");
                }
                if (str4 != null) {
                    try {
                        writeToLog(str4, new StringBuffer().append("LogMsg: ").append(new Date(attributeChangeNotification.getTimeStamp()).toString()).append(Instruction.argsep).append(attributeChangeNotification.getType()).append(Instruction.argsep).append(attributeChangeNotification.getMessage()).append(" Name = ").append(attributeChangeNotification.getAttributeName()).append(" Old value = ").append(oldValue).append(" New value = ").append(newValue).toString());
                    } catch (Exception e) {
                        error("sendAttributeChangeNotification(AttributeChangeNotification)", new StringBuffer().append("Failed to log ").append(attributeChangeNotification.getType()).append(" notification: ").append(e).toString());
                        traceX("sendAttributeChangeNotification(AttributeChangeNotification)", e);
                    }
                }
            }
        } else if (mBeanDescriptor != null && (str = (String) mBeanDescriptor.getFieldValue("log")) != null && ((str.equalsIgnoreCase("t") || str.equalsIgnoreCase("true")) && (str2 = (String) mBeanDescriptor.getFieldValue("logfile")) != null)) {
            try {
                writeToLog(str2, new StringBuffer().append("LogMsg: ").append(new Date(attributeChangeNotification.getTimeStamp()).toString()).append(Instruction.argsep).append(attributeChangeNotification.getType()).append(Instruction.argsep).append(attributeChangeNotification.getMessage()).append(" Name = ").append(attributeChangeNotification.getAttributeName()).append(" Old value = ").append(oldValue).append(" New value = ").append(newValue).toString());
            } catch (Exception e2) {
                error("sendAttributeChangeNotification(AttributeChangeNotification)", new StringBuffer().append("Failed to log ").append(attributeChangeNotification.getType()).append(" notification: ").append(e2).toString());
                traceX("sendAttributeChangeNotification(AttributeChangeNotification)", e2);
            }
        }
        if (this.attributeBroadcaster != null) {
            this.attributeBroadcaster.sendNotification(attributeChangeNotification);
        }
        if (this.generalBroadcaster != null) {
            this.generalBroadcaster.sendNotification(attributeChangeNotification);
        }
        if (tracing()) {
            trace("sendAttributeChangeNotification(AttributeChangeNotification)", "sent notification");
        }
        if (tracing()) {
            trace("sendAttributeChangeNotification(AttributeChangeNotification)", "Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendAttributeChangeNotification(Attribute attribute, Attribute attribute2) throws MBeanException, RuntimeOperationsException {
        if (tracing()) {
            trace("sendAttributeChangeNotification(Attribute, Attribute)", "Entry");
        }
        if (attribute == null || attribute2 == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute object must not be null"), "Exception occured trying to send attribute change notification of a ModelMBean");
        }
        if (!attribute.getName().equals(attribute2.getName())) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute names are not the same"), "Exception occured trying to send attribute change notification of a ModelMBean");
        }
        Object value = attribute2.getValue();
        Object value2 = attribute.getValue();
        String str = "unknown";
        if (value != null) {
            str = value.getClass().getName();
        }
        if (value2 != null) {
            str = value2.getClass().getName();
        }
        sendAttributeChangeNotification(new AttributeChangeNotification(this, 1L, new Date().getTime(), "AttributeChangeDetected", attribute.getName(), str, attribute.getValue(), attribute2.getValue()));
        if (tracing()) {
            trace("sendAttributeChangeNotification(Attribute, Attribute)", "Exit");
        }
    }

    protected ClassLoaderRepository getClassLoaderRepository() {
        return MBeanServerFactory.getClassLoaderRepository(this.server);
    }

    private Class loadClass(String str) throws ClassNotFoundException {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            ClassLoaderRepository classLoaderRepository = getClassLoaderRepository();
            if (classLoaderRepository == null) {
                throw new ClassNotFoundException(str);
            }
            return classLoaderRepository.loadClass(str);
        }
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        if (objectName == null) {
            throw new NullPointerException("name of RequiredModelMBean to registered is null");
        }
        this.server = mBeanServer;
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
        this.registered = bool.booleanValue();
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
        this.registered = false;
        this.server = null;
    }

    private static final boolean tracing() {
        return Trace.isSelected(1, 128);
    }

    private void trace(String str, String str2, String str3) {
        Trace.send(1, 128, str, str2, new StringBuffer().append(Integer.toHexString(hashCode())).append(Instruction.argsep).append(str3).toString());
    }

    private void traceX(String str, Throwable th) {
        Trace.send(1, 128, this.currClass, str, th);
    }

    private void trace(String str, String str2) {
        trace(this.currClass, str, str2);
    }

    private void error(String str, String str2) {
        Trace.send(0, 128, this.currClass, str, new StringBuffer().append(Integer.toHexString(hashCode())).append(Instruction.argsep).append(str2).toString());
    }

    private boolean debugging() {
        return Trace.isSelected(2, 128);
    }

    private void debug(String str, String str2, String str3) {
        Trace.send(2, 128, str, str2, new StringBuffer().append(Integer.toHexString(hashCode())).append(Instruction.argsep).append(str3).toString());
    }

    private void debug(String str, String str2) {
        debug(this.currClass, str, str2);
    }
}
