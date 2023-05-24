package soot.jimple.infoflow.util;

import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/ExtendedAtomicInteger.class */
public class ExtendedAtomicInteger extends AtomicInteger {
    private static final long serialVersionUID = -365647246646024478L;

    public ExtendedAtomicInteger() {
    }

    public ExtendedAtomicInteger(int initialValue) {
        super(initialValue);
    }

    public void subtract(int diff) {
        int curValue;
        do {
            curValue = get();
        } while (!compareAndSet(curValue, curValue - diff));
    }
}
