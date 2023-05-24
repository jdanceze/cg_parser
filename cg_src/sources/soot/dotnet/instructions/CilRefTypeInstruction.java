package soot.dotnet.instructions;

import soot.Body;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilRefTypeInstruction.class */
public class CilRefTypeInstruction extends AbstractCilnstruction {
    public CilRefTypeInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        return new CilRefAnyInstruction(this.instruction.getArgument(), this.dotnetBody, this.cilBlock).jimplifyExpr(jb);
    }
}
