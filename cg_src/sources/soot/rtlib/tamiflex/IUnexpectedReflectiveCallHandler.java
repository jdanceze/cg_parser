package soot.rtlib.tamiflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:soot/rtlib/tamiflex/IUnexpectedReflectiveCallHandler.class */
public interface IUnexpectedReflectiveCallHandler {
    void classNewInstance(Class<?> cls);

    void classForName(String str);

    void constructorNewInstance(Constructor<?> constructor);

    void methodInvoke(Object obj, Method method);

    void fieldSet(Object obj, Field field);

    void fieldGet(Object obj, Field field);
}
