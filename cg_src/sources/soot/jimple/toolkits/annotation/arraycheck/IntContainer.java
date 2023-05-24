package soot.jimple.toolkits.annotation.arraycheck;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/IntContainer.class */
class IntContainer {
    static IntContainer[] pool = new IntContainer[100];
    int value;

    static {
        for (int i = 0; i < 100; i++) {
            pool[i] = new IntContainer(i - 50);
        }
    }

    public IntContainer(int v) {
        this.value = v;
    }

    public static IntContainer v(int v) {
        if (v >= -50 && v <= 49) {
            return pool[v + 50];
        }
        return new IntContainer(v);
    }

    public IntContainer dup() {
        return new IntContainer(this.value);
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(Object other) {
        return (other instanceof IntContainer) && ((IntContainer) other).value == this.value;
    }

    public String toString() {
        return new StringBuilder().append(this.value).toString();
    }
}
