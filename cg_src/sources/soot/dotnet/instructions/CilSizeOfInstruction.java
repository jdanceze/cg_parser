package soot.dotnet.instructions;

import java.util.Collections;
import soot.Body;
import soot.IntType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.soot.DotnetClassConstant;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilSizeOfInstruction.class */
public class CilSizeOfInstruction extends AbstractCilnstruction {
    public CilSizeOfInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        String typeName = this.instruction.getType().getFullname();
        SootClass clazz = Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_RUNTIME_INTEROPSERVICES_MARSHAL);
        SootMethodRef methodRef = Scene.v().makeMethodRef(clazz, "SizeOf", Collections.singletonList(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OBJECT)), IntType.v(), true);
        return Jimple.v().newStaticInvokeExpr(methodRef, DotnetClassConstant.v(typeName));
    }
}
