package soot.dotnet.instructions;

import soot.Body;
import soot.Immediate;
import soot.Local;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilLeaveInstruction.class */
public class CilLeaveInstruction extends AbstractCilnstruction {
    public CilLeaveInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        if (this.instruction.getValueInstruction().getOpCode().equals(ProtoIlInstructions.IlInstructionMsg.IlOpCode.NOP)) {
            jb.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
            return;
        }
        CilInstruction cilValueExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getValueInstruction(), this.dotnetBody, this.cilBlock);
        Value value = cilValueExpr.jimplifyExpr(jb);
        if (cilValueExpr instanceof AbstractNewObjInstanceInstruction) {
            Local tmpVariable = this.dotnetBody.variableManager.localGenerator.generateLocal(value.getType());
            AssignStmt assignStmt = Jimple.v().newAssignStmt(tmpVariable, value);
            jb.getUnits().add((UnitPatchingChain) assignStmt);
            ((AbstractNewObjInstanceInstruction) cilValueExpr).resolveCallConstructorBody(jb, tmpVariable);
            ReturnStmt ret = Jimple.v().newReturnStmt(tmpVariable);
            jb.getUnits().add((UnitPatchingChain) ret);
        } else if (!(value instanceof Immediate)) {
            Local tmpVariable2 = this.dotnetBody.variableManager.localGenerator.generateLocal(value.getType());
            AssignStmt assignStmt2 = Jimple.v().newAssignStmt(tmpVariable2, value);
            jb.getUnits().add((UnitPatchingChain) assignStmt2);
            ReturnStmt ret2 = Jimple.v().newReturnStmt(tmpVariable2);
            jb.getUnits().add((UnitPatchingChain) ret2);
        } else {
            ReturnStmt ret3 = Jimple.v().newReturnStmt(value);
            jb.getUnits().add((UnitPatchingChain) ret3);
        }
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
