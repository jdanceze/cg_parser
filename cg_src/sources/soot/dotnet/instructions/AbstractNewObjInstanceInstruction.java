package soot.dotnet.instructions;

import java.util.List;
import soot.Body;
import soot.Local;
import soot.SootMethodRef;
import soot.UnitPatchingChain;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.SpecialInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/AbstractNewObjInstanceInstruction.class */
public abstract class AbstractNewObjInstanceInstruction extends AbstractCilnstruction {
    protected SootMethodRef methodRef;
    protected List<Local> listOfArgs;

    public AbstractNewObjInstanceInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    public SootMethodRef getMethodRef() {
        return this.methodRef;
    }

    public List<Local> getListOfArgs() {
        return this.listOfArgs;
    }

    public void resolveCallConstructorBody(Body jb, Local variableObject) {
        SpecialInvokeExpr specialInvokeExpr = Jimple.v().newSpecialInvokeExpr(variableObject, getMethodRef(), getListOfArgs());
        InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(specialInvokeExpr);
        jb.getUnits().add((UnitPatchingChain) invokeStmt);
    }
}
