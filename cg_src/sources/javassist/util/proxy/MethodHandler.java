package javassist.util.proxy;

import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/util/proxy/MethodHandler.class */
public interface MethodHandler {
    Object invoke(Object obj, Method method, Method method2, Object[] objArr) throws Throwable;
}
