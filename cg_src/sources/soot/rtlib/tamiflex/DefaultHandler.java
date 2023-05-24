package soot.rtlib.tamiflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:soot/rtlib/tamiflex/DefaultHandler.class */
public class DefaultHandler implements IUnexpectedReflectiveCallHandler {
    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void methodInvoke(Object receiver, Method m) {
        System.err.println("Unexpected reflective call to method " + m);
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void constructorNewInstance(Constructor<?> c) {
        System.err.println("Unexpected reflective instantiation via constructor " + c);
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void classNewInstance(Class<?> c) {
        System.err.println("Unexpected reflective instantiation via Class.newInstance on class " + c);
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void classForName(String typeName) {
        System.err.println("Unexpected reflective loading of class " + typeName);
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void fieldSet(Object receiver, Field f) {
        System.err.println("Unexpected reflective field set: " + f);
    }

    @Override // soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler
    public void fieldGet(Object receiver, Field f) {
        System.err.println("Unexpected reflective field get: " + f);
    }
}
