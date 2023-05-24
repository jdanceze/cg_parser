package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.PopInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BPopInst.class */
public class BPopInst extends AbstractInst implements PopInst {
    protected Type mType;

    public BPopInst(Type aType) {
        this.mType = aType;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BPopInst(this.mType);
    }

    @Override // soot.baf.PopInst
    public int getWordCount() {
        return getInMachineCount();
    }

    @Override // soot.baf.PopInst
    public void setWordCount(int count) {
        throw new RuntimeException("not implemented");
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "pop";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInst
    public final String getParameters() {
        return "";
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return AbstractJasminClass.sizeOfType(this.mType);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).casePopInst(this);
    }

    public Type getType() {
        return this.mType;
    }
}
