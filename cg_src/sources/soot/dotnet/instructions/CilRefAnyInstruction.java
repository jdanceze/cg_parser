package soot.dotnet.instructions;

import soot.Body;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.soot.DotnetClassConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilRefAnyInstruction.class */
public class CilRefAnyInstruction extends AbstractCilnstruction {
    public CilRefAnyInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        return DotnetClassConstant.v(this.instruction.getType().getFullname());
    }
}
