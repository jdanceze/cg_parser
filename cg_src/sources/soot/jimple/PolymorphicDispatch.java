package soot.jimple;

import java.lang.invoke.MethodHandles;
/* loaded from: gencallgraphv3.jar:soot/jimple/PolymorphicDispatch.class */
public class PolymorphicDispatch {
    public void unambiguousMethod() throws Throwable {
        java.lang.invoke.MethodHandle methodHandle = MethodHandles.lookup().findVirtual(PolymorphicDispatch.class, "someMethod", null);
        Object ob = (Object) methodHandle.invoke();
        System.out.println(ob);
    }

    public void ambiguousMethod() throws Throwable {
        java.lang.invoke.MethodHandle methodHandle = MethodHandles.lookup().findVirtual(PolymorphicDispatch.class, "someMethod", null);
        Object ob = (Object) methodHandle.invoke();
        System.out.println(ob);
        int res = (int) methodHandle.invoke(1);
        System.out.println(res);
    }
}
