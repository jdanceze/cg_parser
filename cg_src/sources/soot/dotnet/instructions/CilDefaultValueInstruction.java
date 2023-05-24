package soot.dotnet.instructions;

import java.util.ArrayList;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilDefaultValueInstruction.class */
public class CilDefaultValueInstruction extends AbstractNewObjInstanceInstruction {
    public CilDefaultValueInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        SootClass clazz = Scene.v().getSootClass(this.instruction.getType().getFullname());
        NewExpr newExpr = Jimple.v().newNewExpr(clazz.getType());
        this.methodRef = Scene.v().makeConstructorRef(clazz, new ArrayList());
        this.listOfArgs = new ArrayList();
        return newExpr;
    }
}
