package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.DupInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDupInst.class */
public abstract class BDupInst extends AbstractInst implements DupInst {
    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return getUnderTypes().size() + getOpTypes().size();
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        int count = 0;
        for (Type t : getUnderTypes()) {
            count += AbstractJasminClass.sizeOfType(t);
        }
        for (Type t2 : getOpTypes()) {
            count += AbstractJasminClass.sizeOfType(t2);
        }
        return count;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return getUnderTypes().size() + (2 * getOpTypes().size());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        int count = 0;
        for (Type t : getUnderTypes()) {
            count += AbstractJasminClass.sizeOfType(t);
        }
        for (Type t2 : getOpTypes()) {
            count += 2 * AbstractJasminClass.sizeOfType(t2);
        }
        return count;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        throw new RuntimeException();
    }
}
