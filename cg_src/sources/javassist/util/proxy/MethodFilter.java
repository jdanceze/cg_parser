package javassist.util.proxy;

import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/util/proxy/MethodFilter.class */
public interface MethodFilter {
    boolean isHandled(Method method);
}
