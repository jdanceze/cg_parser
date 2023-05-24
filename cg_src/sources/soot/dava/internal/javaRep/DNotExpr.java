package soot.dava.internal.javaRep;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.ShortType;
import soot.Type;
import soot.UnitPrinter;
import soot.UnknownType;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractUnopExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DNotExpr.class */
public class DNotExpr extends AbstractUnopExpr {
    public DNotExpr(Value op) {
        super(Grimp.v().newExprBox(op));
    }

    @Override // soot.jimple.internal.AbstractUnopExpr, soot.Value
    public Object clone() {
        return new DNotExpr(Grimp.cloneIfNecessary(getOpBox().getValue()));
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal(" ! (");
        getOpBox().toString(up);
        up.literal(")");
    }

    public String toString() {
        return " ! (" + getOpBox().getValue().toString() + ")";
    }

    @Override // soot.Value
    public Type getType() {
        Value op = getOpBox().getValue();
        if (op.getType().equals(IntType.v()) || op.getType().equals(ByteType.v()) || op.getType().equals(ShortType.v()) || op.getType().equals(BooleanType.v()) || op.getType().equals(CharType.v())) {
            return IntType.v();
        }
        if (op.getType().equals(LongType.v())) {
            return LongType.v();
        }
        if (op.getType().equals(DoubleType.v())) {
            return DoubleType.v();
        }
        if (op.getType().equals(FloatType.v())) {
            return FloatType.v();
        }
        return UnknownType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof DNotExpr) {
            return getOpBox().getValue().equivTo(((DNotExpr) o).getOpBox().getValue());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return getOpBox().getValue().equivHashCode();
    }
}
