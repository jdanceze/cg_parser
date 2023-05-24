package soot.dotnet.instructions;

import soot.Body;
import soot.Local;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilStObjInstruction.class */
public class CilStObjInstruction extends AbstractCilnstruction {
    public CilStObjInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilInstruction cilTargetExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getTarget(), this.dotnetBody, this.cilBlock);
        Value target = cilTargetExpr.jimplifyExpr(jb);
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getValueInstruction(), this.dotnetBody, this.cilBlock);
        Value value = cilExpr.jimplifyExpr(jb);
        if ((value instanceof Local) && !target.getType().toString().equals(value.getType().toString()) && value.getType().toString().equals(DotnetBasicTypes.SYSTEM_OBJECT) && !target.getType().toString().equals(DotnetBasicTypes.SYSTEM_OBJECT)) {
            value = Jimple.v().newCastExpr(value, target.getType());
        }
        if ((value instanceof CastExpr) && !(target instanceof Local)) {
            Value generatedLocal = this.dotnetBody.variableManager.localGenerator.generateLocal(target.getType());
            AssignStmt assignStmt = Jimple.v().newAssignStmt(generatedLocal, value);
            jb.getUnits().add((UnitPatchingChain) assignStmt);
            value = generatedLocal;
        }
        AssignStmt astm = Jimple.v().newAssignStmt(target, value);
        jb.getUnits().add((UnitPatchingChain) astm);
        if (cilExpr instanceof AbstractNewObjInstanceInstruction) {
            if (!(target instanceof Local)) {
                throw new RuntimeException("STOBJ: The given target is not a local! The value is: " + target.toString() + " of type " + target.getType() + "! The resolving method body is: " + this.dotnetBody.getDotnetMethodSig().getSootMethodSignature().getSignature());
            }
            ((AbstractNewObjInstanceInstruction) cilExpr).resolveCallConstructorBody(jb, (Local) target);
        }
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
