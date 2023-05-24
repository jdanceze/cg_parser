package soot.dotnet.instructions;

import soot.Body;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilThrowInstruction.class */
public class CilThrowInstruction extends AbstractCilnstruction {
    public CilThrowInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getArgument(), this.dotnetBody, this.cilBlock);
        Value argument = cilExpr.jimplifyExpr(jb);
        ThrowStmt throwStmt = Jimple.v().newThrowStmt(argument);
        jb.getUnits().add((UnitPatchingChain) throwStmt);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
