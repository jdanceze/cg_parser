package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.RefType;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/NewInstanceInstruction.class */
public class NewInstanceInstruction extends DexlibAbstractInstruction {
    public NewInstanceInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        Instruction21c i = (Instruction21c) this.instruction;
        int dest = i.getRegisterA();
        String className = Util.dottedClassName(((TypeReference) i.getReference()).toString());
        RefType type = RefType.v(className);
        NewExpr n = Jimple.v().newNewExpr(type);
        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), n);
        setUnit(assign);
        addTags(assign);
        body.add(assign);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    boolean overridesRegister(int register) {
        OneRegisterInstruction i = (OneRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public Set<Type> introducedTypes() {
        ReferenceInstruction i = (ReferenceInstruction) this.instruction;
        Set<Type> types = new HashSet<>();
        types.add(DexType.toSoot((TypeReference) i.getReference()));
        return types;
    }
}
