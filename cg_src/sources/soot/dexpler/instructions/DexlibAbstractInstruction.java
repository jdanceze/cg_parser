package soot.dexpler.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.FiveRegisterInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.RegisterRangeInstruction;
import soot.Type;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.options.Options;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Host;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLineNumberTag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/DexlibAbstractInstruction.class */
public abstract class DexlibAbstractInstruction {
    protected int lineNumber = -1;
    protected final Instruction instruction;
    protected final int codeAddress;
    protected Unit unit;

    public abstract void jimplify(DexBody dexBody);

    public Instruction getInstruction() {
        return this.instruction;
    }

    int movesRegister(int register) {
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int movesToRegister(int register) {
        return -1;
    }

    boolean overridesRegister(int register) {
        return false;
    }

    boolean isUsedAsFloatingPoint(DexBody body, int register) {
        return false;
    }

    public Set<Type> introducedTypes() {
        return Collections.emptySet();
    }

    public DexlibAbstractInstruction(Instruction instruction, int codeAddress) {
        this.instruction = instruction;
        this.codeAddress = codeAddress;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addTags(Host host) {
        Options options = Options.v();
        if (options.keep_line_number() && this.lineNumber != -1) {
            host.addTag(new LineNumberTag(this.lineNumber));
            host.addTag(new SourceLineNumberTag(this.lineNumber));
        }
        if (options.keep_offset()) {
            host.addTag(new BytecodeOffsetTag(this.codeAddress));
        }
    }

    public Unit getUnit() {
        return this.unit;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUnit(Unit u) {
        this.unit = u;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<Integer> getUsedRegistersNums(RegisterRangeInstruction instruction) {
        List<Integer> regs = new ArrayList<>();
        int start = instruction.getStartRegister();
        for (int i = start; i < start + instruction.getRegisterCount(); i++) {
            regs.add(Integer.valueOf(i));
        }
        return regs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<Integer> getUsedRegistersNums(FiveRegisterInstruction instruction) {
        int regCount = instruction.getRegisterCount();
        int[] regs = {instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG()};
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < Math.min(regCount, regs.length); i++) {
            l.add(Integer.valueOf(regs[i]));
        }
        return l;
    }
}
