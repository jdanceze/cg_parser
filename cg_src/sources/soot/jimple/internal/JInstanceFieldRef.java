package soot.jimple.internal;

import soot.SootFieldRef;
import soot.Value;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JInstanceFieldRef.class */
public class JInstanceFieldRef extends AbstractInstanceFieldRef {
    public JInstanceFieldRef(Value base, SootFieldRef fieldRef) {
        super(Jimple.v().newLocalBox(base), fieldRef);
    }

    @Override // soot.jimple.internal.AbstractInstanceFieldRef, soot.Value
    public Object clone() {
        return new JInstanceFieldRef(Jimple.cloneIfNecessary(getBase()), this.fieldRef);
    }
}
