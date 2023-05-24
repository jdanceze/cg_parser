package soot.dava.internal.javaRep;

import org.apache.commons.cli.HelpFormatter;
import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.internal.GAssignStmt;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DDecrementStmt.class */
public class DDecrementStmt extends GAssignStmt {
    public DDecrementStmt(Value variable, Value rvalue) {
        super(variable, rvalue);
    }

    @Override // soot.grimp.internal.GAssignStmt, soot.jimple.internal.JAssignStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new DDecrementStmt(Grimp.cloneIfNecessary(getLeftOp()), Grimp.cloneIfNecessary(getRightOp()));
    }

    @Override // soot.jimple.internal.JAssignStmt
    public String toString() {
        return String.valueOf(getLeftOpBox().getValue().toString()) + HelpFormatter.DEFAULT_LONG_OPT_PREFIX;
    }

    @Override // soot.jimple.internal.JAssignStmt, soot.Unit
    public void toString(UnitPrinter up) {
        getLeftOpBox().toString(up);
        up.literal(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
    }
}
