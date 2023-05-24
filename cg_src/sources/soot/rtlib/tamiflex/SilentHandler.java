package soot.rtlib.tamiflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:soot/rtlib/tamiflex/SilentHandler.class */
public class SilentHandler implements IUnexpectedReflectiveCallHandler {
    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void methodInvoke(Object receiver, Method m) {
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void constructorNewInstance(Constructor<?> c) {
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void classNewInstance(Class<?> c) {
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void classForName(String typeName) {
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void fieldSet(Object receiver, Field f) {
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void fieldGet(Object receiver, Field f) {
    }
}
