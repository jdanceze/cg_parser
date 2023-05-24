package soot;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/AnySubType.class */
public class AnySubType extends RefLikeType {
    private RefType base;

    private AnySubType(RefType base) {
        this.base = base;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v8 */
    public static AnySubType v(RefType base) {
        if (base.getAnySubType() == null) {
            ?? r0 = base;
            synchronized (r0) {
                if (base.getAnySubType() == null) {
                    base.setAnySubType(new AnySubType(base));
                }
                r0 = r0;
            }
        }
        return base.getAnySubType();
    }

    @Override // soot.Type
    public String toString() {
        return "Any_subtype_of_" + this.base;
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseAnySubType(this);
    }

    @Override // soot.RefLikeType
    public Type getArrayElementType() {
        throw new RuntimeException("Attempt to get array base type of a non-array");
    }

    public RefType getBase() {
        return this.base;
    }

    public void setBase(RefType base) {
        this.base = base;
    }
}
