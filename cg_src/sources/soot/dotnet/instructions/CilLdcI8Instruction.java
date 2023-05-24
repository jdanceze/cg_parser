package soot.dotnet.instructions;

import soot.Body;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.LongConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilLdcI8Instruction.class */
public class CilLdcI8Instruction extends AbstractCilnstruction {
    public CilLdcI8Instruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        return LongConstant.v(this.instruction.getValueConstantInt64());
    }
}
