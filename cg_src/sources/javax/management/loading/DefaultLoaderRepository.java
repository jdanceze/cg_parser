package javax.management.loading;

import com.sun.jmx.trace.Trace;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/loading/DefaultLoaderRepository.class */
public class DefaultLoaderRepository {
    private static final String dbgTag = "DefaultLoaderRepository";

    public static Class loadClass(String str) throws ClassNotFoundException {
        debug("loadClass", str);
        return load(null, str);
    }

    public static Class loadClassWithout(ClassLoader classLoader, String str) throws ClassNotFoundException {
        debug("loadClassWithout", str);
        return load(classLoader, str);
    }

    private static Class load(ClassLoader classLoader, String str) throws ClassNotFoundException {
        for (MBeanServer mBeanServer : MBeanServerFactory.findMBeanServer(null)) {
            try {
                return mBeanServer.getClassLoaderRepository().loadClassWithout(classLoader, str);
            } catch (ClassNotFoundException e) {
            }
        }
        throw new ClassNotFoundException(str);
    }

    private static boolean isTraceOn() {
        return Trace.isSelected(1, 1);
    }

    private static void trace(String str, String str2, String str3) {
        Trace.send(1, 1, str, str2, str3);
    }

    private static void trace(String str, String str2) {
        trace(dbgTag, str, str2);
    }

    private static boolean isDebugOn() {
        return Trace.isSelected(2, 1);
    }

    private static void debug(String str, String str2, String str3) {
        Trace.send(2, 1, str, str2, str3);
    }

    private static void debug(String str, String str2) {
        debug(dbgTag, str, str2);
    }
}
