package org.apache.http.util;

import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/util/ExceptionUtils.class */
public final class ExceptionUtils {
    private static final Method INIT_CAUSE_METHOD = getInitCauseMethod();
    static Class class$java$lang$Throwable;

    private static Method getInitCauseMethod() {
        Class cls;
        Class cls2;
        try {
            Class[] paramsClasses = new Class[1];
            if (class$java$lang$Throwable == null) {
                cls = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls;
            } else {
                cls = class$java$lang$Throwable;
            }
            paramsClasses[0] = cls;
            if (class$java$lang$Throwable == null) {
                cls2 = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls2;
            } else {
                cls2 = class$java$lang$Throwable;
            }
            return cls2.getMethod("initCause", paramsClasses);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static void initCause(Throwable throwable, Throwable cause) {
        if (INIT_CAUSE_METHOD != null) {
            try {
                INIT_CAUSE_METHOD.invoke(throwable, cause);
            } catch (Exception e) {
            }
        }
    }

    private ExceptionUtils() {
    }
}
