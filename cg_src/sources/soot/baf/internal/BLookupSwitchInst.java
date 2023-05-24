package soot.baf.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.LookupSwitchInst;
import soot.coffi.Instruction;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BLookupSwitchInst.class */
public class BLookupSwitchInst extends AbstractSwitchInst implements LookupSwitchInst {
    List<IntConstant> lookupValues;

    public BLookupSwitchInst(Unit defaultTarget, List<IntConstant> lookupValues, List<? extends Unit> targets) {
        super(defaultTarget, targets);
        setLookupValues(lookupValues);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BLookupSwitchInst(getDefaultTarget(), this.lookupValues, getTargets());
    }

    @Override // soot.baf.LookupSwitchInst
    public void setLookupValues(List<IntConstant> lookupValues) {
        this.lookupValues = new ArrayList(lookupValues);
    }

    @Override // soot.baf.LookupSwitchInst
    public void setLookupValue(int index, int value) {
        this.lookupValues.set(index, IntConstant.v(value));
    }

    @Override // soot.baf.LookupSwitchInst
    public int getLookupValue(int index) {
        return this.lookupValues.get(index).value;
    }

    @Override // soot.baf.LookupSwitchInst
    public List<IntConstant> getLookupValues() {
        return Collections.unmodifiableList(this.lookupValues);
    }

    @Override // soot.baf.internal.AbstractInst
    public String getName() {
        return Jimple.LOOKUPSWITCH;
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(Jimple.LOOKUPSWITCH).append(Instruction.argsep);
        buffer.append("{").append(Instruction.argsep);
        for (int i = 0; i < this.lookupValues.size(); i++) {
            buffer.append("    case ").append(this.lookupValues.get(i)).append(": goto ").append(getTarget(i)).append(";").append(Instruction.argsep);
        }
        buffer.append("    default: goto ").append(getDefaultTarget()).append(";").append(Instruction.argsep);
        buffer.append("}");
        return buffer.toString();
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(Jimple.LOOKUPSWITCH);
        up.newline();
        up.literal("{");
        up.newline();
        for (int i = 0; i < this.lookupValues.size(); i++) {
            up.literal("    case ");
            up.constant(this.lookupValues.get(i));
            up.literal(": goto ");
            this.targetBoxes[i].toString(up);
            up.literal(";");
            up.newline();
        }
        up.literal("    default: goto ");
        this.defaultTargetBox.toString(up);
        up.literal(";");
        up.newline();
        up.literal("}");
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseLookupSwitchInst(this);
    }
}
