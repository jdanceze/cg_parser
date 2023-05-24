package soot.dotnet.instructions;

import soot.Body;
import soot.Immediate;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilLdLenInstruction.class */
public class CilLdLenInstruction extends AbstractCilnstruction {
    public CilLdLenInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getArray(), this.dotnetBody, this.cilBlock);
        Value arr = cilExpr.jimplifyExpr(jb);
        if (!(arr instanceof Immediate)) {
            throw new RuntimeException("LdLen: Given value is no Immediate!");
        }
        return Jimple.v().newLengthExpr(arr);
    }
}
