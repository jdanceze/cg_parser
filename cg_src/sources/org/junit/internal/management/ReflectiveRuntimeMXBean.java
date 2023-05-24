package org.junit.internal.management;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.junit.internal.Classes;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ReflectiveRuntimeMXBean.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ReflectiveRuntimeMXBean.class */
final class ReflectiveRuntimeMXBean implements RuntimeMXBean {
    private final Object runtimeMxBean;

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/management/ReflectiveRuntimeMXBean$Holder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/management/ReflectiveRuntimeMXBean$Holder.class */
    private static final class Holder {
        private static final Method getInputArgumentsMethod;

        private Holder() {
        }

        static {
            Method inputArguments = null;
            try {
                Class<?> threadMXBeanClass = Classes.getClass("java.lang.management.RuntimeMXBean");
                inputArguments = threadMXBeanClass.getMethod("getInputArguments", new Class[0]);
            } catch (ClassNotFoundException e) {
            } catch (NoSuchMethodException e2) {
            } catch (SecurityException e3) {
            }
            getInputArgumentsMethod = inputArguments;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReflectiveRuntimeMXBean(Object runtimeMxBean) {
        this.runtimeMxBean = runtimeMxBean;
    }

    @Override // org.junit.internal.management.RuntimeMXBean
    public List<String> getInputArguments() {
        if (Holder.getInputArgumentsMethod != null) {
            try {
                return (List) Holder.getInputArgumentsMethod.invoke(this.runtimeMxBean, new Object[0]);
            } catch (ClassCastException e) {
            } catch (IllegalAccessException e2) {
            } catch (IllegalArgumentException e3) {
            } catch (InvocationTargetException e4) {
            }
        }
        return Collections.emptyList();
    }
}
