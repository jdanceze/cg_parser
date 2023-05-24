package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.RefType;
import soot.Scene;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.RefSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JCaughtExceptionRef.class */
public class JCaughtExceptionRef implements CaughtExceptionRef {
    @Override // soot.EquivTo
    public boolean equivTo(Object c) {
        return c instanceof CaughtExceptionRef;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return 1729;
    }

    @Override // soot.Value
    public Object clone() {
        return new JCaughtExceptionRef();
    }

    public String toString() {
        return "@caughtexception";
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.identityRef(this);
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.jimple.CaughtExceptionRef, soot.Value
    public Type getType() {
        return RefType.v(Scene.v().getBaseExceptionType().getClassName());
    }

    @Override // soot.jimple.CaughtExceptionRef, soot.util.Switchable
    public void apply(Switch sw) {
        ((RefSwitch) sw).caseCaughtExceptionRef(this);
    }
}
