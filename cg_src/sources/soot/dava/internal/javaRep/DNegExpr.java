package soot.dava.internal.javaRep;

import org.apache.commons.cli.HelpFormatter;
import soot.UnitPrinter;
import soot.Value;
import soot.coffi.Instruction;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNegExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DNegExpr.class */
public class DNegExpr extends AbstractNegExpr {
    public DNegExpr(Value op) {
        super(Grimp.v().newExprBox(op));
    }

    @Override // soot.jimple.internal.AbstractUnopExpr, soot.Value
    public Object clone() {
        return new DNegExpr(Grimp.cloneIfNecessary(getOp()));
    }

    @Override // soot.jimple.internal.AbstractNegExpr, soot.Value
    public void toString(UnitPrinter up) {
        up.literal("(");
        up.literal(HelpFormatter.DEFAULT_OPT_PREFIX);
        up.literal(Instruction.argsep);
        up.literal("(");
        getOpBox().toString(up);
        up.literal(")");
        up.literal(")");
    }

    @Override // soot.jimple.internal.AbstractNegExpr
    public String toString() {
        return "(- (" + getOpBox().getValue().toString() + "))";
    }
}
