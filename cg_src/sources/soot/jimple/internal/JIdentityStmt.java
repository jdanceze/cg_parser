package soot.jimple.internal;

import java.util.List;
import soot.Local;
import soot.RefType;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.ParameterRef;
import soot.jimple.StmtSwitch;
import soot.jimple.ThisRef;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JIdentityStmt.class */
public class JIdentityStmt extends AbstractDefinitionStmt implements IdentityStmt {
    public JIdentityStmt(Value local, Value identityValue) {
        this(Jimple.v().newLocalBox(local), Jimple.v().newIdentityRefBox(identityValue));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JIdentityStmt(ValueBox localBox, ValueBox identityValueBox) {
        super(localBox, identityValueBox);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JIdentityStmt(Jimple.cloneIfNecessary(getLeftOp()), Jimple.cloneIfNecessary(getRightOp()));
    }

    public String toString() {
        return String.valueOf(this.leftBox.getValue().toString()) + " := " + this.rightBox.getValue().toString();
    }

    public void toString(UnitPrinter up) {
        this.leftBox.toString(up);
        up.literal(" := ");
        this.rightBox.toString(up);
    }

    @Override // soot.jimple.IdentityStmt
    public void setLeftOp(Value local) {
        this.leftBox.setValue(local);
    }

    @Override // soot.jimple.IdentityStmt
    public void setRightOp(Value identityRef) {
        this.rightBox.setValue(identityRef);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseIdentityStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Value newRhs;
        Value currentRhs = getRightOp();
        if (currentRhs instanceof ThisRef) {
            newRhs = Baf.v().newThisRef((RefType) ((ThisRef) currentRhs).getType());
        } else if (!(currentRhs instanceof ParameterRef)) {
            if (currentRhs instanceof CaughtExceptionRef) {
                Unit u = Baf.v().newStoreInst(RefType.v(), context.getBafLocalOfJimpleLocal((Local) getLeftOp()));
                u.addAllTagsOf(this);
                out.add(u);
                return;
            }
            throw new RuntimeException("Don't know how to convert unknown rhs");
        } else {
            ParameterRef rhsPRef = (ParameterRef) currentRhs;
            newRhs = Baf.v().newParameterRef(rhsPRef.getType(), rhsPRef.getIndex());
        }
        Unit u2 = Baf.v().newIdentityInst(context.getBafLocalOfJimpleLocal((Local) getLeftOp()), newRhs);
        u2.addAllTagsOf(this);
        out.add(u2);
    }
}
