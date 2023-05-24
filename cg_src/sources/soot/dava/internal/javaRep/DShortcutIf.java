package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.Expr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DShortcutIf.class */
public class DShortcutIf implements Expr {
    ValueBox testExprBox;
    ValueBox trueExprBox;
    ValueBox falseExprBox;
    Type exprType;

    public DShortcutIf(ValueBox test, ValueBox left, ValueBox right) {
        this.testExprBox = test;
        this.trueExprBox = left;
        this.falseExprBox = right;
    }

    @Override // soot.Value
    public Object clone() {
        return this;
    }

    @Override // soot.Value
    public List getUseBoxes() {
        List toReturn = new ArrayList();
        toReturn.addAll(this.testExprBox.getValue().getUseBoxes());
        toReturn.add(this.testExprBox);
        toReturn.addAll(this.trueExprBox.getValue().getUseBoxes());
        toReturn.add(this.trueExprBox);
        toReturn.addAll(this.falseExprBox.getValue().getUseBoxes());
        toReturn.add(this.falseExprBox);
        return toReturn;
    }

    @Override // soot.Value
    public Type getType() {
        return this.exprType;
    }

    public String toString() {
        String toReturn = String.valueOf("") + this.testExprBox.getValue().toString();
        return String.valueOf(String.valueOf(String.valueOf(String.valueOf(toReturn) + " ? ") + this.trueExprBox.getValue().toString()) + " : ") + this.falseExprBox.getValue().toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        this.testExprBox.getValue().toString(up);
        up.literal(" ? ");
        this.trueExprBox.getValue().toString(up);
        up.literal(" : ");
        this.falseExprBox.getValue().toString(up);
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        int toReturn = 0 + this.testExprBox.getValue().equivHashCode();
        return toReturn + this.trueExprBox.getValue().equivHashCode() + this.falseExprBox.getValue().equivHashCode();
    }
}
