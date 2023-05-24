package soot.asm;

import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:soot/asm/ScopeFinderTarget.class */
public class ScopeFinderTarget {
    public Object field;
    public static Object static_field = new Object();

    public ScopeFinderTarget() {
        this.field = new Object();
    }

    @Nullable
    public void method() {
        System.out.println("in method");
    }

    /* loaded from: gencallgraphv3.jar:soot/asm/ScopeFinderTarget$Inner.class */
    public static class Inner {
        public Object field = new Object();
        public static Object static_field = new Object();

        public void method() {
            System.out.println("in method");
        }

        /* loaded from: gencallgraphv3.jar:soot/asm/ScopeFinderTarget$Inner$InnerInner.class */
        public class InnerInner {
            public Object field = new Object();

            public InnerInner() {
            }

            public void method() {
                System.out.println("in method");
            }
        }
    }

    public ScopeFinderTarget(Object param) {
        this.field = param;
    }

    public void methodPara(String p1) {
        System.out.println(p1);
    }

    public void methodPara(String p1, String p2) {
        System.out.println(p1);
        System.out.println(p2);
    }
}
