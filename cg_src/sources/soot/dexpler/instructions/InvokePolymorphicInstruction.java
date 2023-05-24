package soot.dexpler.instructions;

import java.util.Arrays;
import java.util.List;
import org.jf.dexlib2.iface.instruction.DualReferenceInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JNewArrayExpr;
import soot.jimple.internal.JimpleLocal;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/InvokePolymorphicInstruction.class */
public class InvokePolymorphicInstruction extends MethodInvocationInstruction {
    public InvokePolymorphicInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        SootMethodRef ref = getVirtualSootMethodRef();
        if (ref.declaringClass().isInterface()) {
            ref = getInterfaceSootMethodRef();
        }
        List<Local> temp = buildParameters(body, ((MethodProtoReference) ((DualReferenceInstruction) this.instruction).getReference2()).getParameterTypes(), false);
        List<Local> parms = temp.subList(1, temp.size());
        Local invoker = temp.get(0);
        if (parms.size() > 0 && (parms.size() != 1 || !(parms.get(0) instanceof ArrayType))) {
            Body b = body.getBody();
            PatchingChain<Unit> units = b.getUnits();
            RefType rf = Scene.v().getObjectType();
            Local newArrL = new JimpleLocal("$u" + (b.getLocalCount() + 1), ArrayType.v(rf, 1));
            b.getLocals().add(newArrL);
            JAssignStmt newArr = new JAssignStmt(newArrL, new JNewArrayExpr(rf, IntConstant.v(parms.size())));
            units.add((PatchingChain<Unit>) newArr);
            int i = 0;
            for (Local l : parms) {
                units.add((PatchingChain<Unit>) new JAssignStmt(new JArrayRef(newArrL, IntConstant.v(i)), l));
                i++;
            }
            parms = Arrays.asList(newArrL);
        }
        if (ref.declaringClass().isInterface()) {
            this.invocation = Jimple.v().newInterfaceInvokeExpr(invoker, ref, parms);
        } else {
            this.invocation = Jimple.v().newVirtualInvokeExpr(invoker, ref, parms);
        }
        body.setDanglingInstruction(this);
    }
}
