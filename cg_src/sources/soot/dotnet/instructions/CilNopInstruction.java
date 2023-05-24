package soot.dotnet.instructions;

import soot.Body;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilNopInstruction.class */
public class CilNopInstruction extends AbstractCilnstruction {
    public CilNopInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        NopStmt nopStmt = Jimple.v().newNopStmt();
        jb.getUnits().add((UnitPatchingChain) nopStmt);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
