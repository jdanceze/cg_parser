package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
import soot.jimple.internal.AbstractLengthExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DLengthExpr.class */
public class DLengthExpr extends AbstractLengthExpr implements Precedence {
    public DLengthExpr(Value op) {
        super(Grimp.v().newObjExprBox(op));
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 950;
    }

    @Override // soot.jimple.internal.AbstractUnopExpr, soot.Value
    public Object clone() {
        return new DLengthExpr(Grimp.cloneIfNecessary(getOp()));
    }

    @Override // soot.jimple.internal.AbstractLengthExpr, soot.Value
    public void toString(UnitPrinter up) {
        if (PrecedenceTest.needsBrackets(getOpBox(), this)) {
            up.literal("(");
        }
        getOpBox().toString(up);
        if (PrecedenceTest.needsBrackets(getOpBox(), this)) {
            up.literal(")");
        }
        up.literal(".");
        up.literal(XMLConstants.LENGTH_ATTRIBUTE);
    }

    @Override // soot.jimple.internal.AbstractLengthExpr
    public String toString() {
        StringBuffer b = new StringBuffer();
        if (PrecedenceTest.needsBrackets(getOpBox(), this)) {
            b.append("(");
        }
        b.append(getOpBox().getValue().toString());
        if (PrecedenceTest.needsBrackets(getOpBox(), this)) {
            b.append(")");
        }
        b.append(".length");
        return b.toString();
    }
}
