package soot.dexpler.instructions;

import java.util.List;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.analysis.AnalyzedInstruction;
import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.analysis.InlineMethodResolver;
import org.jf.dexlib2.analysis.MethodAnalyzer;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction35mi;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import soot.dexpler.DexBody;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/ExecuteInlineInstruction.class */
public class ExecuteInlineInstruction extends MethodInvocationInstruction implements OdexInstruction {
    private Method targetMethod;

    public ExecuteInlineInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
        this.targetMethod = null;
    }

    @Override // soot.dexpler.instructions.OdexInstruction
    public void deOdex(DexFile parentFile, Method method, ClassPath cp) {
        if (!(parentFile instanceof DexBackedOdexFile)) {
            throw new RuntimeException("ODEX instruction in non-ODEX file");
        }
        DexBackedOdexFile odexFile = (DexBackedOdexFile) parentFile;
        InlineMethodResolver inlineMethodResolver = InlineMethodResolver.createInlineMethodResolver(odexFile.getOdexVersion());
        MethodAnalyzer analyzer = new MethodAnalyzer(cp, method, inlineMethodResolver, false);
        this.targetMethod = inlineMethodResolver.resolveExecuteInline(new AnalyzedInstruction(analyzer, this.instruction, -1, -1));
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        int acccessFlags = this.targetMethod.getAccessFlags();
        if (AccessFlags.STATIC.isSet(acccessFlags)) {
            jimplifyStatic(body);
        } else if (AccessFlags.PRIVATE.isSet(acccessFlags)) {
            jimplifySpecial(body);
        } else {
            jimplifyVirtual(body);
        }
    }

    @Override // soot.dexpler.instructions.MethodInvocationInstruction
    protected List<Integer> getUsedRegistersNums() {
        if (this.instruction instanceof Instruction35mi) {
            return getUsedRegistersNums((Instruction35mi) this.instruction);
        }
        if (this.instruction instanceof Instruction3rmi) {
            return getUsedRegistersNums((Instruction3rmi) this.instruction);
        }
        throw new RuntimeException("Instruction is not an ExecuteInline");
    }
}
