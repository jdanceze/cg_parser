package soot.baf.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.TableSwitchInst;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BTableSwitchInst.class */
public class BTableSwitchInst extends AbstractSwitchInst implements TableSwitchInst {
    int lowIndex;
    int highIndex;

    public BTableSwitchInst(Unit defaultTarget, int lowIndex, int highIndex, List<? extends Unit> targets) {
        super(defaultTarget, targets);
        this.lowIndex = lowIndex;
        this.highIndex = highIndex;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BTableSwitchInst(getDefaultTarget(), this.lowIndex, this.highIndex, getTargets());
    }

    @Override // soot.baf.TableSwitchInst
    public void setLowIndex(int lowIndex) {
        this.lowIndex = lowIndex;
    }

    @Override // soot.baf.TableSwitchInst
    public void setHighIndex(int highIndex) {
        this.highIndex = highIndex;
    }

    @Override // soot.baf.TableSwitchInst
    public int getLowIndex() {
        return this.lowIndex;
    }

    @Override // soot.baf.TableSwitchInst
    public int getHighIndex() {
        return this.highIndex;
    }

    @Override // soot.baf.internal.AbstractInst
    public String getName() {
        return Jimple.TABLESWITCH;
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(Jimple.TABLESWITCH).append(Instruction.argsep);
        buffer.append('{').append(Instruction.argsep);
        int low = this.lowIndex;
        int high = this.highIndex;
        for (int i = low; i < high; i++) {
            buffer.append("    case ").append(i).append(": goto ").append(getTarget(i - low)).append(';').append(Instruction.argsep);
        }
        buffer.append("    case ").append(high).append(": goto ").append(getTarget(high - low)).append(';').append(Instruction.argsep);
        buffer.append("    default: goto ").append(getDefaultTarget()).append(';').append(Instruction.argsep);
        buffer.append('}');
        return buffer.toString();
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(Jimple.TABLESWITCH);
        up.newline();
        up.literal("{");
        up.newline();
        for (int i = this.lowIndex; i < this.highIndex; i++) {
            printCaseTarget(up, i);
        }
        printCaseTarget(up, this.highIndex);
        up.literal("    default: goto ");
        this.defaultTargetBox.toString(up);
        up.literal(";");
        up.newline();
        up.literal("}");
    }

    private void printCaseTarget(UnitPrinter up, int targetIndex) {
        up.literal("    case ");
        up.literal(Integer.toString(targetIndex));
        up.literal(": goto ");
        this.targetBoxes[targetIndex - this.lowIndex].toString(up);
        up.literal(";");
        up.newline();
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseTableSwitchInst(this);
    }
}
