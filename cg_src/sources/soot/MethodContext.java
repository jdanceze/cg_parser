package soot;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/MethodContext.class */
public final class MethodContext implements MethodOrMethodContext {
    private SootMethod method;
    private Context context;

    @Override // soot.MethodOrMethodContext
    public SootMethod method() {
        return this.method;
    }

    @Override // soot.MethodOrMethodContext
    public Context context() {
        return this.context;
    }

    private MethodContext(SootMethod method, Context context) {
        this.method = method;
        this.context = context;
    }

    public int hashCode() {
        return this.method.hashCode() + this.context.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof MethodContext) {
            MethodContext other = (MethodContext) o;
            return this.method.equals(other.method) && this.context.equals(other.context);
        }
        return false;
    }

    public static MethodOrMethodContext v(SootMethod method, Context context) {
        if (context == null) {
            return method;
        }
        MethodContext probe = new MethodContext(method, context);
        Map<MethodContext, MethodContext> map = G.v().MethodContext_map;
        MethodContext ret = map.get(probe);
        if (ret == null) {
            map.put(probe, probe);
            return probe;
        }
        return ret;
    }

    public String toString() {
        return "Method " + this.method + " in context " + this.context;
    }
}
