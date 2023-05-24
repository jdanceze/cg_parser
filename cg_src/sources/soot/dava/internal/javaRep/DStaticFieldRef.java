package soot.dava.internal.javaRep;

import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.jimple.StaticFieldRef;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DStaticFieldRef.class */
public class DStaticFieldRef extends StaticFieldRef {
    private boolean supressDeclaringClass;

    @Override // soot.jimple.StaticFieldRef, soot.Value
    public void toString(UnitPrinter up) {
        if (!this.supressDeclaringClass) {
            up.type(this.fieldRef.declaringClass().getType());
            up.literal(".");
        }
        up.fieldRef(this.fieldRef);
    }

    public DStaticFieldRef(SootFieldRef fieldRef, String myClassName) {
        super(fieldRef);
        this.supressDeclaringClass = myClassName.equals(fieldRef.declaringClass().getName());
    }

    public DStaticFieldRef(SootFieldRef fieldRef, boolean supressDeclaringClass) {
        super(fieldRef);
        this.supressDeclaringClass = supressDeclaringClass;
    }

    @Override // soot.jimple.StaticFieldRef, soot.Value
    public Object clone() {
        return new DStaticFieldRef(this.fieldRef, this.supressDeclaringClass);
    }
}
