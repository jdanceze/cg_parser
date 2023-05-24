package soot.dotnet.instructions;

import soot.Body;
import soot.Local;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilRethrowInstruction.class */
public class CilRethrowInstruction extends AbstractCilnstruction {
    public CilRethrowInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        Local variable = this.dotnetBody.variableManager.addOrGetVariable(this.instruction.getVariable(), jb);
        ThrowStmt throwStmt = Jimple.v().newThrowStmt(variable);
        jb.getUnits().add((UnitPatchingChain) throwStmt);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
