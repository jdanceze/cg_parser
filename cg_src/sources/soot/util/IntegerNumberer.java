package soot.util;
/* loaded from: gencallgraphv3.jar:soot/util/IntegerNumberer.class */
public class IntegerNumberer implements Numberer<Long> {
    @Override // soot.util.Numberer
    public void add(Long o) {
    }

    @Override // soot.util.Numberer
    public long get(Long o) {
        if (o == null) {
            return 0L;
        }
        return o.longValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.util.Numberer
    public Long get(long number) {
        if (number == 0) {
            return null;
        }
        return new Long(number);
    }

    @Override // soot.util.Numberer
    public int size() {
        throw new RuntimeException("IntegerNumberer does not implement the size() method.");
    }

    @Override // soot.util.Numberer
    public boolean remove(Long o) {
        return false;
    }
}
