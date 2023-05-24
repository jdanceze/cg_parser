package org.junit.internal.management;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.internal.Classes;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ReflectiveThreadMXBean.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ReflectiveThreadMXBean.class */
final class ReflectiveThreadMXBean implements ThreadMXBean {
    private final Object threadMxBean;

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ReflectiveThreadMXBean$Holder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ReflectiveThreadMXBean$Holder.class */
    private static final class Holder {
        static final Method getThreadCpuTimeMethod;
        static final Method isThreadCpuTimeSupportedMethod;
        private static final String FAILURE_MESSAGE = "Unable to access ThreadMXBean";

        private Holder() {
        }

        static {
            Method threadCpuTime = null;
            Method threadCpuTimeSupported = null;
            try {
                Class<?> threadMXBeanClass = Classes.getClass("java.lang.management.ThreadMXBean");
                threadCpuTime = threadMXBeanClass.getMethod("getThreadCpuTime", Long.TYPE);
                threadCpuTimeSupported = threadMXBeanClass.getMethod("isThreadCpuTimeSupported", new Class[0]);
            } catch (ClassNotFoundException e) {
            } catch (NoSuchMethodException e2) {
            } catch (SecurityException e3) {
            }
            getThreadCpuTimeMethod = threadCpuTime;
            isThreadCpuTimeSupportedMethod = threadCpuTimeSupported;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReflectiveThreadMXBean(Object threadMxBean) {
        this.threadMxBean = threadMxBean;
    }

    @Override // org.junit.internal.management.ThreadMXBean
    public long getThreadCpuTime(long id) {
        Exception error;
        if (Holder.getThreadCpuTimeMethod != null) {
            try {
                return ((Long) Holder.getThreadCpuTimeMethod.invoke(this.threadMxBean, Long.valueOf(id))).longValue();
            } catch (ClassCastException e) {
                error = e;
                throw new UnsupportedOperationException("Unable to access ThreadMXBean", error);
            } catch (IllegalAccessException e2) {
                error = e2;
                throw new UnsupportedOperationException("Unable to access ThreadMXBean", error);
            } catch (IllegalArgumentException e3) {
                error = e3;
                throw new UnsupportedOperationException("Unable to access ThreadMXBean", error);
            } catch (InvocationTargetException e4) {
                error = e4;
                throw new UnsupportedOperationException("Unable to access ThreadMXBean", error);
            }
        }
        throw new UnsupportedOperationException("Unable to access ThreadMXBean");
    }

    @Override // org.junit.internal.management.ThreadMXBean
    public boolean isThreadCpuTimeSupported() {
        if (Holder.isThreadCpuTimeSupportedMethod != null) {
            try {
                return ((Boolean) Holder.isThreadCpuTimeSupportedMethod.invoke(this.threadMxBean, new Object[0])).booleanValue();
            } catch (ClassCastException e) {
                return false;
            } catch (IllegalAccessException e2) {
                return false;
            } catch (IllegalArgumentException e3) {
                return false;
            } catch (InvocationTargetException e4) {
                return false;
            }
        }
        return false;
    }
}
