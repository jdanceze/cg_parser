package polyglot.util;
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/MaxLevels.class */
class MaxLevels {
    int maxLevel;
    int maxLevelInner;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaxLevels(int ml, int mli) {
        this.maxLevel = ml;
        this.maxLevelInner = mli;
    }

    public int hashCode() {
        return (this.maxLevel * 17) + this.maxLevelInner;
    }

    public boolean equals(Object o) {
        if (o instanceof MaxLevels) {
            MaxLevels m2 = (MaxLevels) o;
            return this.maxLevel == m2.maxLevel && this.maxLevelInner == m2.maxLevelInner;
        }
        return false;
    }

    public String toString() {
        return new StringBuffer().append("[").append(this.maxLevel).append("/").append(this.maxLevelInner).append("]").toString();
    }
}
