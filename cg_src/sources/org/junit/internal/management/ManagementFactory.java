package org.junit.internal.management;

import java.lang.reflect.InvocationTargetException;
import org.junit.internal.Classes;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ManagementFactory.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ManagementFactory.class */
public class ManagementFactory {

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ManagementFactory$FactoryHolder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ManagementFactory$FactoryHolder.class */
    private static final class FactoryHolder {
        private static final Class<?> MANAGEMENT_FACTORY_CLASS;

        private FactoryHolder() {
        }

        static {
            Class<?> managementFactoryClass = null;
            try {
                managementFactoryClass = Classes.getClass("java.lang.management.ManagementFactory");
            } catch (ClassNotFoundException e) {
            }
            MANAGEMENT_FACTORY_CLASS = managementFactoryClass;
        }

        static Object getBeanObject(String methodName) {
            if (MANAGEMENT_FACTORY_CLASS != null) {
                try {
                    return MANAGEMENT_FACTORY_CLASS.getMethod(methodName, new Class[0]).invoke(null, new Object[0]);
                } catch (IllegalAccessException e) {
                    return null;
                } catch (IllegalArgumentException e2) {
                    return null;
                } catch (NoSuchMethodException e3) {
                    return null;
                } catch (SecurityException e4) {
                    return null;
                } catch (InvocationTargetException e5) {
                    return null;
                }
            }
            return null;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ManagementFactory$RuntimeHolder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ManagementFactory$RuntimeHolder.class */
    private static final class RuntimeHolder {
        private static final RuntimeMXBean RUNTIME_MX_BEAN = getBean(FactoryHolder.getBeanObject("getRuntimeMXBean"));

        private RuntimeHolder() {
        }

        private static final RuntimeMXBean getBean(Object runtimeMxBean) {
            return runtimeMxBean != null ? new ReflectiveRuntimeMXBean(runtimeMxBean) : new FakeRuntimeMXBean();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ManagementFactory$ThreadHolder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ManagementFactory$ThreadHolder.class */
    private static final class ThreadHolder {
        private static final ThreadMXBean THREAD_MX_BEAN = getBean(FactoryHolder.getBeanObject("getThreadMXBean"));

        private ThreadHolder() {
        }

        private static final ThreadMXBean getBean(Object threadMxBean) {
            return threadMxBean != null ? new ReflectiveThreadMXBean(threadMxBean) : new FakeThreadMXBean();
        }
    }

    public static RuntimeMXBean getRuntimeMXBean() {
        return RuntimeHolder.RUNTIME_MX_BEAN;
    }

    public static ThreadMXBean getThreadMXBean() {
        return ThreadHolder.THREAD_MX_BEAN;
    }
}
