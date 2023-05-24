package javax.management;

import com.sun.jmx.mbeanserver.StandardMetaDataImpl;
import com.sun.jmx.trace.Trace;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/StandardMBean.class */
public class StandardMBean implements DynamicMBean {
    private static final String dbgTag = "StandardMBean";
    private Class mbeanInterface;
    private Object implementation;
    private final StandardMetaDataImpl meta;
    private MBeanInfo cachedMBeanInfo;

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/StandardMBean$StandardMBeanMeta.class */
    private final class StandardMBeanMeta extends StandardMetaDataImpl {
        private final StandardMBean this$0;

        public StandardMBeanMeta(StandardMBean standardMBean) {
            this.this$0 = standardMBean;
        }

        protected MBeanInfo getCachedMBeanInfo(Class cls) {
            if (cls == null) {
                return null;
            }
            synchronized (this.this$0) {
                Class implementationClass = this.this$0.getImplementationClass();
                if (implementationClass == null) {
                    return null;
                }
                if (cls.equals(implementationClass)) {
                    return this.this$0.getMBeanInfo();
                }
                return null;
            }
        }

        protected Class getCachedMBeanInterface(Class cls) {
            synchronized (this.this$0) {
                Class implementationClass = this.this$0.getImplementationClass();
                if (implementationClass == null) {
                    return null;
                }
                if (cls.equals(implementationClass)) {
                    return this.this$0.getMBeanInterface();
                }
                return null;
            }
        }

        protected void cacheMBeanInfo(Class cls, Class cls2, MBeanInfo mBeanInfo) {
        }
    }

    private StandardMBean(Object obj, Class cls, boolean z) throws NotCompliantMBeanException {
        if (obj == null) {
            if (!z) {
                throw new IllegalArgumentException("implementation is null");
            }
            obj = this;
        }
        this.meta = new StandardMBeanMeta(this);
        setImplementation(obj, cls);
    }

    public StandardMBean(Object obj, Class cls) throws NotCompliantMBeanException {
        this(obj, cls, false);
    }

    protected StandardMBean(Class cls) throws NotCompliantMBeanException {
        this(null, cls, true);
    }

    public synchronized void setImplementation(Object obj) throws NotCompliantMBeanException {
        setImplementation(obj, getMBeanInterface());
    }

    private synchronized void setImplementation(Object obj, Class cls) throws NotCompliantMBeanException {
        if (obj == null) {
            throw new IllegalArgumentException("implementation is null");
        }
        this.meta.testCompliance(obj.getClass(), cls);
        cacheMBeanInfo(null);
        this.implementation = obj;
        this.mbeanInterface = cls;
        if (this.mbeanInterface == null) {
            this.mbeanInterface = this.meta.getStandardMBeanInterface(obj.getClass());
        }
    }

    public synchronized Object getImplementation() {
        return this.implementation;
    }

    public final synchronized Class getMBeanInterface() {
        return this.mbeanInterface;
    }

    public synchronized Class getImplementationClass() {
        if (this.implementation == null) {
            return null;
        }
        return this.implementation.getClass();
    }

    @Override // javax.management.DynamicMBean
    public Object getAttribute(String str) throws AttributeNotFoundException, MBeanException, ReflectionException {
        return this.meta.getAttribute(getImplementation(), str);
    }

    @Override // javax.management.DynamicMBean
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        this.meta.setAttribute(getImplementation(), attribute);
    }

    @Override // javax.management.DynamicMBean
    public AttributeList getAttributes(String[] strArr) {
        try {
            return this.meta.getAttributes(getImplementation(), strArr);
        } catch (ReflectionException e) {
            throw new RuntimeOperationsException(new UndeclaredThrowableException(e, e.getMessage()), e.getMessage());
        }
    }

    @Override // javax.management.DynamicMBean
    public AttributeList setAttributes(AttributeList attributeList) {
        try {
            return this.meta.setAttributes(getImplementation(), attributeList);
        } catch (ReflectionException e) {
            throw new RuntimeOperationsException(new UndeclaredThrowableException(e, e.getMessage()), e.getMessage());
        }
    }

    @Override // javax.management.DynamicMBean
    public Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        return this.meta.invoke(getImplementation(), str, objArr, strArr);
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        Object implementation;
        MBeanInfo buildStandardMBeanInfo;
        try {
            MBeanInfo cachedMBeanInfo = getCachedMBeanInfo();
            if (cachedMBeanInfo != null) {
                return cachedMBeanInfo;
            }
        } catch (RuntimeException e) {
            debug("getMBeanInfo", new StringBuffer().append("failed to get cached MBeanInfo: ").append(e).toString());
            debugX("getMBeanInfo", e);
        }
        if (isTraceOn()) {
            trace("getMBeanInfo", new StringBuffer().append("Building MBeanInfo for ").append(getImplementationClass().getName()).toString());
        }
        try {
            synchronized (this) {
                implementation = getImplementation();
                buildStandardMBeanInfo = buildStandardMBeanInfo();
            }
            MBeanInfo mBeanInfo = new MBeanInfo(getClassName(buildStandardMBeanInfo), getDescription(buildStandardMBeanInfo), getAttributes(buildStandardMBeanInfo), getConstructors(buildStandardMBeanInfo, implementation), getOperations(buildStandardMBeanInfo), getNotifications(buildStandardMBeanInfo));
            try {
                cacheMBeanInfo(mBeanInfo);
            } catch (RuntimeException e2) {
                debug("cacheMBeanInfo", new StringBuffer().append("failed to cache MBeanInfo: ").append(e2).toString());
                debugX("cacheMBeanInfo", e2);
            }
            return mBeanInfo;
        } catch (NotCompliantMBeanException e3) {
            throw new RuntimeOperationsException(new UndeclaredThrowableException(e3, e3.getMessage()), e3.getMessage());
        }
    }

    protected String getClassName(MBeanInfo mBeanInfo) {
        return mBeanInfo == null ? getImplementationClass().getName() : mBeanInfo.getClassName();
    }

    protected String getDescription(MBeanInfo mBeanInfo) {
        if (mBeanInfo == null) {
            return null;
        }
        return mBeanInfo.getDescription();
    }

    protected String getDescription(MBeanFeatureInfo mBeanFeatureInfo) {
        if (mBeanFeatureInfo == null) {
            return null;
        }
        return mBeanFeatureInfo.getDescription();
    }

    protected String getDescription(MBeanAttributeInfo mBeanAttributeInfo) {
        return getDescription((MBeanFeatureInfo) mBeanAttributeInfo);
    }

    protected String getDescription(MBeanConstructorInfo mBeanConstructorInfo) {
        return getDescription((MBeanFeatureInfo) mBeanConstructorInfo);
    }

    protected String getDescription(MBeanConstructorInfo mBeanConstructorInfo, MBeanParameterInfo mBeanParameterInfo, int i) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getDescription();
    }

    protected String getParameterName(MBeanConstructorInfo mBeanConstructorInfo, MBeanParameterInfo mBeanParameterInfo, int i) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getName();
    }

    protected String getDescription(MBeanOperationInfo mBeanOperationInfo) {
        return getDescription((MBeanFeatureInfo) mBeanOperationInfo);
    }

    protected int getImpact(MBeanOperationInfo mBeanOperationInfo) {
        if (mBeanOperationInfo == null) {
            return 3;
        }
        return mBeanOperationInfo.getImpact();
    }

    protected String getParameterName(MBeanOperationInfo mBeanOperationInfo, MBeanParameterInfo mBeanParameterInfo, int i) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getName();
    }

    protected String getDescription(MBeanOperationInfo mBeanOperationInfo, MBeanParameterInfo mBeanParameterInfo, int i) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getDescription();
    }

    protected MBeanConstructorInfo[] getConstructors(MBeanConstructorInfo[] mBeanConstructorInfoArr, Object obj) {
        if (mBeanConstructorInfoArr == null) {
            return null;
        }
        if (obj == null || obj == this) {
            return mBeanConstructorInfoArr;
        }
        return null;
    }

    private MBeanNotificationInfo[] getNotifications(MBeanInfo mBeanInfo) {
        if (mBeanInfo == null) {
            return null;
        }
        return mBeanInfo.getNotifications();
    }

    protected synchronized MBeanInfo getCachedMBeanInfo() {
        return this.cachedMBeanInfo;
    }

    protected synchronized void cacheMBeanInfo(MBeanInfo mBeanInfo) {
        this.cachedMBeanInfo = mBeanInfo;
    }

    private synchronized MBeanInfo buildStandardMBeanInfo() throws NotCompliantMBeanException {
        return this.meta.buildMBeanInfo(getImplementationClass(), getMBeanInterface());
    }

    private MBeanConstructorInfo[] getConstructors(MBeanInfo mBeanInfo, Object obj) {
        MBeanConstructorInfo[] mBeanConstructorInfoArr;
        MBeanParameterInfo[] mBeanParameterInfoArr;
        MBeanConstructorInfo[] constructors = getConstructors(mBeanInfo.getConstructors(), obj);
        if (constructors != null) {
            int length = constructors.length;
            mBeanConstructorInfoArr = new MBeanConstructorInfo[length];
            for (int i = 0; i < length; i++) {
                MBeanConstructorInfo mBeanConstructorInfo = constructors[i];
                MBeanParameterInfo[] signature = mBeanConstructorInfo.getSignature();
                if (signature != null) {
                    int length2 = signature.length;
                    mBeanParameterInfoArr = new MBeanParameterInfo[length2];
                    for (int i2 = 0; i2 < length2; i2++) {
                        MBeanParameterInfo mBeanParameterInfo = signature[i2];
                        mBeanParameterInfoArr[i2] = new MBeanParameterInfo(getParameterName(mBeanConstructorInfo, mBeanParameterInfo, i2), mBeanParameterInfo.getType(), getDescription(mBeanConstructorInfo, mBeanParameterInfo, i2));
                    }
                } else {
                    mBeanParameterInfoArr = null;
                }
                mBeanConstructorInfoArr[i] = new MBeanConstructorInfo(mBeanConstructorInfo.getName(), getDescription(mBeanConstructorInfo), mBeanParameterInfoArr);
            }
        } else {
            mBeanConstructorInfoArr = null;
        }
        return mBeanConstructorInfoArr;
    }

    private MBeanOperationInfo[] getOperations(MBeanInfo mBeanInfo) {
        MBeanOperationInfo[] mBeanOperationInfoArr;
        MBeanParameterInfo[] mBeanParameterInfoArr;
        MBeanOperationInfo[] operations = mBeanInfo.getOperations();
        if (operations != null) {
            int length = operations.length;
            mBeanOperationInfoArr = new MBeanOperationInfo[length];
            for (int i = 0; i < length; i++) {
                MBeanOperationInfo mBeanOperationInfo = operations[i];
                MBeanParameterInfo[] signature = mBeanOperationInfo.getSignature();
                if (signature != null) {
                    int length2 = signature.length;
                    mBeanParameterInfoArr = new MBeanParameterInfo[length2];
                    for (int i2 = 0; i2 < length2; i2++) {
                        MBeanParameterInfo mBeanParameterInfo = signature[i2];
                        mBeanParameterInfoArr[i2] = new MBeanParameterInfo(getParameterName(mBeanOperationInfo, mBeanParameterInfo, i2), mBeanParameterInfo.getType(), getDescription(mBeanOperationInfo, mBeanParameterInfo, i2));
                    }
                } else {
                    mBeanParameterInfoArr = null;
                }
                mBeanOperationInfoArr[i] = new MBeanOperationInfo(mBeanOperationInfo.getName(), getDescription(mBeanOperationInfo), mBeanParameterInfoArr, mBeanOperationInfo.getReturnType(), getImpact(mBeanOperationInfo));
            }
        } else {
            mBeanOperationInfoArr = null;
        }
        return mBeanOperationInfoArr;
    }

    private MBeanAttributeInfo[] getAttributes(MBeanInfo mBeanInfo) {
        MBeanAttributeInfo[] mBeanAttributeInfoArr;
        MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
        if (attributes != null) {
            int length = attributes.length;
            mBeanAttributeInfoArr = new MBeanAttributeInfo[length];
            for (int i = 0; i < length; i++) {
                MBeanAttributeInfo mBeanAttributeInfo = attributes[i];
                mBeanAttributeInfoArr[i] = new MBeanAttributeInfo(mBeanAttributeInfo.getName(), mBeanAttributeInfo.getType(), getDescription(mBeanAttributeInfo), mBeanAttributeInfo.isReadable(), mBeanAttributeInfo.isWritable(), mBeanAttributeInfo.isIs());
            }
        } else {
            mBeanAttributeInfoArr = null;
        }
        return mBeanAttributeInfoArr;
    }

    private static boolean isTraceOn() {
        return Trace.isSelected(1, 16);
    }

    private static void trace(String str, String str2, String str3) {
        Trace.send(1, 16, str, str2, str3);
    }

    private static void trace(String str, String str2) {
        trace(dbgTag, str, str2);
    }

    private static boolean isDebugOn() {
        return Trace.isSelected(2, 16);
    }

    private static void debug(String str, String str2, String str3) {
        Trace.send(2, 16, str, str2, str3);
    }

    private static void debug(String str, String str2) {
        debug(dbgTag, str, str2);
    }

    private static void debugX(String str, Throwable th) {
        if (isDebugOn()) {
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            String stringWriter2 = stringWriter.toString();
            debug(dbgTag, str, new StringBuffer().append("Exception caught in ").append(str).append("(): ").append(th).toString());
            debug(dbgTag, str, stringWriter2);
        }
    }
}
