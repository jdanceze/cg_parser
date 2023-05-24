package soot.dotnet.instructions;

import soot.Body;
import soot.Type;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilCastClassUnBoxInstruction.class */
public class CilCastClassUnBoxInstruction extends AbstractCilnstruction {
    public CilCastClassUnBoxInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        Type type = DotnetTypeFactory.toSootType(this.instruction.getType());
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getArgument(), this.dotnetBody, this.cilBlock);
        Value argument = cilExpr.jimplifyExpr(jb);
        return Jimple.v().newCastExpr(argument, type);
    }
}
