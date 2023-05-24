package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import javax.management.loading.ClassLoaderRepository;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServerFactory.class */
public class MBeanServerFactory {
    private static MBeanServerBuilder builder = null;
    private static final ObjectName delegateName;
    private static final ArrayList mBeanServerList;
    static Class class$javax$management$MBeanServerBuilder;
    static Class class$javax$management$MBeanServerFactory;

    private MBeanServerFactory() {
    }

    static {
        ObjectName objectName;
        try {
            objectName = new ObjectName("JMImplementation:type=MBeanServerDelegate");
        } catch (JMException e) {
            objectName = null;
            trace("<clinit>", new StringBuffer().append("internal error creating delegate ObjectName: ").append(e).toString());
        }
        delegateName = objectName;
        mBeanServerList = new ArrayList();
    }

    public static void releaseMBeanServer(MBeanServer mBeanServer) {
        checkPermission("releaseMBeanServer");
        removeMBeanServer(mBeanServer);
    }

    public static MBeanServer createMBeanServer() {
        return createMBeanServer(null);
    }

    public static MBeanServer createMBeanServer(String str) {
        checkPermission("createMBeanServer");
        MBeanServer newMBeanServer = newMBeanServer(str);
        addMBeanServer(newMBeanServer);
        return newMBeanServer;
    }

    public static MBeanServer newMBeanServer() {
        return newMBeanServer(null);
    }

    public static MBeanServer newMBeanServer(String str) {
        MBeanServer newMBeanServer;
        checkPermission("newMBeanServer");
        MBeanServerBuilder newMBeanServerBuilder = getNewMBeanServerBuilder();
        synchronized (newMBeanServerBuilder) {
            MBeanServerDelegate newMBeanServerDelegate = newMBeanServerBuilder.newMBeanServerDelegate();
            if (newMBeanServerDelegate == null) {
                throw new JMRuntimeException("MBeanServerBuilder.newMBeanServerDelegate() returned null");
            }
            newMBeanServer = newMBeanServerBuilder.newMBeanServer(str, null, newMBeanServerDelegate);
            if (newMBeanServer == null) {
                throw new JMRuntimeException("MBeanServerBuilder.newMBeanServer() returned null");
            }
        }
        return newMBeanServer;
    }

    public static synchronized ArrayList findMBeanServer(String str) {
        checkPermission("findMBeanServer");
        if (str == null) {
            return (ArrayList) mBeanServerList.clone();
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = mBeanServerList.iterator();
        while (it.hasNext()) {
            MBeanServer mBeanServer = (MBeanServer) it.next();
            if (str.equals(mBeanServerName(mBeanServer))) {
                arrayList.add(mBeanServer);
            }
        }
        return arrayList;
    }

    public static ClassLoaderRepository getClassLoaderRepository(MBeanServer mBeanServer) {
        return mBeanServer.getClassLoaderRepository();
    }

    private static String mBeanServerName(MBeanServer mBeanServer) {
        try {
            return (String) mBeanServer.getAttribute(delegateName, "MBeanServerId");
        } catch (JMException e) {
            return null;
        }
    }

    private static void checkPermission(String str) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanServerPermission(str));
        }
    }

    private static synchronized void addMBeanServer(MBeanServer mBeanServer) {
        mBeanServerList.add(mBeanServer);
    }

    private static synchronized void removeMBeanServer(MBeanServer mBeanServer) {
        if (!mBeanServerList.remove(mBeanServer)) {
            trace("removeMBeanServer", "MBeanServer was not in list!");
        }
    }

    private static Class loadBuilderClass(String str) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader.loadClass(str);
        }
        return Class.forName(str);
    }

    private static MBeanServerBuilder newBuilder(Class cls) {
        try {
            return (MBeanServerBuilder) cls.newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new JMRuntimeException(new StringBuffer().append("Failed to instantiate a MBeanServerBuilder from ").append(cls).append(": ").append(e2).toString(), e2);
        }
    }

    private static synchronized void checkMBeanServerBuilder() {
        Class<?> loadBuilderClass;
        Class<?> cls;
        try {
            String str = (String) AccessController.doPrivileged((PrivilegedAction<Object>) new GetPropertyAction("javax.management.builder.initial"));
            if (str != null) {
                try {
                    if (str.length() != 0) {
                        loadBuilderClass = loadBuilderClass(str);
                        if (builder == null && loadBuilderClass == builder.getClass()) {
                            return;
                        }
                        builder = newBuilder(loadBuilderClass);
                    }
                } catch (ClassNotFoundException e) {
                    throw new JMRuntimeException(new StringBuffer().append("Failed to load MBeanServerBuilder class ").append(str).append(": ").append(e).toString(), e);
                }
            }
            if (class$javax$management$MBeanServerBuilder == null) {
                cls = class$("javax.management.MBeanServerBuilder");
                class$javax$management$MBeanServerBuilder = cls;
            } else {
                cls = class$javax$management$MBeanServerBuilder;
            }
            loadBuilderClass = cls;
            if (builder == null) {
            }
            builder = newBuilder(loadBuilderClass);
        } catch (RuntimeException e2) {
            debug("checkMBeanServerBuilder", new StringBuffer().append("Failed to instantiate MBeanServerBuilder: ").append(e2).append("\n\t\tCheck the value of the ").append("javax.management.builder.initial").append(" property.").toString());
            throw e2;
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static synchronized MBeanServerBuilder getNewMBeanServerBuilder() {
        checkMBeanServerBuilder();
        return builder;
    }

    private static void trace(String str, String str2) {
        Class cls;
        if (Trace.isSelected(1, 1)) {
            if (class$javax$management$MBeanServerFactory == null) {
                cls = class$("javax.management.MBeanServerFactory");
                class$javax$management$MBeanServerFactory = cls;
            } else {
                cls = class$javax$management$MBeanServerFactory;
            }
            Trace.send(1, 1, cls.getName(), str, str2);
        }
    }

    private static void debug(String str, String str2) {
        Class cls;
        if (Trace.isSelected(2, 1)) {
            if (class$javax$management$MBeanServerFactory == null) {
                cls = class$("javax.management.MBeanServerFactory");
                class$javax$management$MBeanServerFactory = cls;
            } else {
                cls = class$javax$management$MBeanServerFactory;
            }
            Trace.send(2, 1, cls.getName(), str, str2);
        }
    }

    private static void error(String str, String str2) {
        Class cls;
        if (Trace.isSelected(0, 1)) {
            if (class$javax$management$MBeanServerFactory == null) {
                cls = class$("javax.management.MBeanServerFactory");
                class$javax$management$MBeanServerFactory = cls;
            } else {
                cls = class$javax$management$MBeanServerFactory;
            }
            Trace.send(0, 1, cls.getName(), str, str2);
        }
    }
}
