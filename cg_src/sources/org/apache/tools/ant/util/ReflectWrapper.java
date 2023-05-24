package org.apache.tools.ant.util;

import java.lang.reflect.Constructor;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ReflectWrapper.class */
public class ReflectWrapper {
    private Object obj;

    public ReflectWrapper(ClassLoader loader, String name) {
        try {
            Class<?> clazz = Class.forName(name, true, loader);
            Constructor<?> constructor = clazz.getConstructor(new Class[0]);
            this.obj = constructor.newInstance(new Object[0]);
        } catch (Exception t) {
            ReflectUtil.throwBuildException(t);
        }
    }

    public ReflectWrapper(Object obj) {
        this.obj = obj;
    }

    public <T> T getObject() {
        return (T) this.obj;
    }

    public <T> T invoke(String methodName) {
        return (T) ReflectUtil.invoke(this.obj, methodName);
    }

    public <T> T invoke(String methodName, Class<?> argType, Object arg) {
        return (T) ReflectUtil.invoke(this.obj, methodName, argType, arg);
    }

    public <T> T invoke(String methodName, Class<?> argType1, Object arg1, Class<?> argType2, Object arg2) {
        return (T) ReflectUtil.invoke(this.obj, methodName, argType1, arg1, argType2, arg2);
    }
}
