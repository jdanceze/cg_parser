package soot.jimple.toolkits.annotation.nullcheck;

import soot.EquivalentValue;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/RefIntPair.class */
public class RefIntPair {
    private final EquivalentValue _ref;
    private final int _val;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RefIntPair(EquivalentValue r, int v, BranchedRefVarsAnalysis brva) {
        this._ref = r;
        this._val = v;
    }

    public EquivalentValue ref() {
        return this._ref;
    }

    public int val() {
        return this._val;
    }

    public String toString() {
        String prefix = "(" + this._ref + ", ";
        switch (this._val) {
            case 0:
                return String.valueOf(prefix) + "bottom)";
            case 1:
                return String.valueOf(prefix) + "null)";
            case 2:
                return String.valueOf(prefix) + "non-null)";
            case 99:
                return String.valueOf(prefix) + "top)";
            default:
                return String.valueOf(prefix) + this._val + ")";
        }
    }
}
