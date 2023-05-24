package soot.dotnet.instructions;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootResolver;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.AbstractDotnetMember;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilLdsFldaInstruction.class */
public class CilLdsFldaInstruction extends AbstractCilnstruction {
    public CilLdsFldaInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        SootClass declaringClass = SootResolver.v().makeClassRef(this.instruction.getField().getDeclaringType().getFullname());
        Value rewriteField = AbstractDotnetMember.checkRewriteCilSpecificMember(declaringClass, this.instruction.getField().getName());
        if (rewriteField != null) {
            return rewriteField;
        }
        SootFieldRef fieldRef = Scene.v().makeFieldRef(declaringClass, this.instruction.getField().getName(), DotnetTypeFactory.toSootType(this.instruction.getField().getType()), true);
        return Jimple.v().newStaticFieldRef(fieldRef);
    }
}
