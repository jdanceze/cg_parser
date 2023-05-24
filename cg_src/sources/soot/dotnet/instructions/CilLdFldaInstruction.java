package soot.dotnet.instructions;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootResolver;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilLdFldaInstruction.class */
public class CilLdFldaInstruction extends AbstractCilnstruction {
    public CilLdFldaInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        SootClass declaringClass = SootResolver.v().makeClassRef(this.instruction.getField().getDeclaringType().getFullname());
        SootFieldRef fieldRef = Scene.v().makeFieldRef(declaringClass, this.instruction.getField().getName(), DotnetTypeFactory.toSootType(this.instruction.getField().getType()), false);
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getTarget(), this.dotnetBody, this.cilBlock);
        Value target = cilExpr.jimplifyExpr(jb);
        return Jimple.v().newInstanceFieldRef(target, fieldRef);
    }
}
