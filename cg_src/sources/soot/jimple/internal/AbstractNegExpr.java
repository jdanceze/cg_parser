package soot.jimple.internal;

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
import soot.ValueBox;
import soot.jimple.ExprSwitch;
import soot.jimple.NegExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractNegExpr.class */
public abstract class AbstractNegExpr extends AbstractUnopExpr implements NegExpr {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractNegExpr(ValueBox opBox) {
        super(opBox);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractNegExpr) {
            return this.opBox.getValue().equivTo(((AbstractNegExpr) o).opBox.getValue());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return this.opBox.getValue().equivHashCode();
    }

    public String toString() {
        return "neg " + this.opBox.getValue().toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("neg ");
        this.opBox.toString(up);
    }

    @Override // soot.Value
    public Type getType() {
        Type type = this.opBox.getValue().getType();
        IntType tyInt = IntType.v();
        ByteType tyByte = ByteType.v();
        ShortType tyShort = ShortType.v();
        CharType tyChar = CharType.v();
        BooleanType tyBool = BooleanType.v();
        if (tyInt.equals(type) || tyByte.equals(type) || tyShort.equals(type) || tyChar.equals(type) || tyBool.equals(type)) {
            return tyInt;
        }
        LongType tyLong = LongType.v();
        if (tyLong.equals(type)) {
            return tyLong;
        }
        DoubleType tyDouble = DoubleType.v();
        if (tyDouble.equals(type)) {
            return tyDouble;
        }
        FloatType tyFloat = FloatType.v();
        if (tyFloat.equals(type)) {
            return tyFloat;
        }
        return UnknownType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseNegExpr(this);
    }
}
