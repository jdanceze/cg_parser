package soot.jimple.toolkit.callgraph;

import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkit/callgraph/EntryPoint.class */
public class EntryPoint {
    public void ptaResolution() {
        Interface f = PhantomField.fld;
        try {
            Method m = f.getClass().getMethod(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString(), String.class);
            m.invoke(f, f.args());
            A x = PhantomField.fld2;
            Method m2 = x.getClass().getMethod(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString(), String.class);
            m2.invoke(x, f.args());
        } catch (Exception e) {
        }
    }

    public void typestateResolution() {
        Interface f = PhantomField.fld;
        try {
            Method m = f.getClass().getMethod(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString(), String.class);
            m.invoke(f, "foo");
            A x = PhantomField.fld2;
            Method m2 = x.getClass().getMethod(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString(), String.class);
            m2.invoke(x, "foo");
        } catch (Exception e) {
        }
    }
}
