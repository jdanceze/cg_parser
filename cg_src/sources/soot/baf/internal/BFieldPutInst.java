package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.baf.FieldPutInst;
import soot.baf.InstSwitch;
import soot.coffi.Instruction;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BFieldPutInst.class */
public class BFieldPutInst extends AbstractInst implements FieldPutInst {
    SootFieldRef fieldRef;

    public BFieldPutInst(SootFieldRef fieldRef) {
        if (fieldRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.fieldRef = fieldRef;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BFieldPutInst(this.fieldRef);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return AbstractJasminClass.sizeOfType(this.fieldRef.type()) + 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "fieldput";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInst
    public final String getParameters() {
        return Instruction.argsep + this.fieldRef.getSignature();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.baf.internal.AbstractInst
    public void getParameters(UnitPrinter up) {
        up.literal(Instruction.argsep);
        up.fieldRef(this.fieldRef);
    }

    @Override // soot.baf.FieldArgInst
    public SootFieldRef getFieldRef() {
        return this.fieldRef;
    }

    @Override // soot.baf.FieldArgInst
    public SootField getField() {
        return this.fieldRef.resolve();
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseFieldPutInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public boolean containsFieldRef() {
        return true;
    }
}
