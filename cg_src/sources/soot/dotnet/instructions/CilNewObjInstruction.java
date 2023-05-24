package soot.dotnet.instructions;

import java.util.ArrayList;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilNewObjInstruction.class */
public class CilNewObjInstruction extends AbstractNewObjInstanceInstruction {
    public CilNewObjInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        if (!this.instruction.hasMethod()) {
            throw new RuntimeException("NewObj: There is no method information in the method definiton!");
        }
        SootClass clazz = Scene.v().getSootClass(this.instruction.getMethod().getDeclaringType().getFullname());
        NewExpr newExpr = Jimple.v().newNewExpr(clazz.getType());
        ArrayList<Local> argsVariables = new ArrayList<>();
        ArrayList<Type> argsTypes = new ArrayList<>();
        for (ProtoIlInstructions.IlInstructionMsg a : this.instruction.getArgumentsList()) {
            argsVariables.add(this.dotnetBody.variableManager.addOrGetVariable(a.getVariable(), jb));
            argsTypes.add(DotnetTypeFactory.toSootType(a.getVariable().getType().getFullname()));
        }
        this.methodRef = Scene.v().makeConstructorRef(clazz, argsTypes);
        this.listOfArgs = argsVariables;
        return newExpr;
    }
}
