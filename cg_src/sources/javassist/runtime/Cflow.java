package javassist.runtime;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/runtime/Cflow.class */
public class Cflow extends ThreadLocal<Depth> {

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/runtime/Cflow$Depth.class */
    public static class Depth {
        private int depth = 0;

        Depth() {
        }

        int value() {
            return this.depth;
        }

        void inc() {
            this.depth++;
        }

        void dec() {
            this.depth--;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.ThreadLocal
    public synchronized Depth initialValue() {
        return new Depth();
    }

    public void enter() {
        get().inc();
    }

    public void exit() {
        get().dec();
    }

    public int value() {
        return get().value();
    }
}
