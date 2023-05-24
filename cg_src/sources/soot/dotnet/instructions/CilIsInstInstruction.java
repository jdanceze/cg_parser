package soot.dotnet.instructions;

import soot.Body;
import soot.Local;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilIsInstInstruction.class */
public class CilIsInstInstruction extends AbstractCilnstruction {
    public CilIsInstInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        String type = this.instruction.getType().getFullname();
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getArgument(), this.dotnetBody, this.cilBlock);
        Value argument = cilExpr.jimplifyExpr(jb);
        return Jimple.v().newInstanceOfExpr(argument, DotnetTypeFactory.toSootType(type));
    }

    public void resolveRewritingIsInst(Body jb, Local variable, Value instanceOfExpr) {
        Local local = this.dotnetBody.variableManager.localGenerator.generateLocal(DotnetTypeFactory.toSootType(DotnetBasicTypes.SYSTEM_BOOLEAN));
        AssignStmt assignInstanceOfStmt = Jimple.v().newAssignStmt(local, instanceOfExpr);
        NopStmt nopStmt = Jimple.v().newNopStmt();
        AssignStmt assignIfTrueStmt = Jimple.v().newAssignStmt(variable, ((InstanceOfExpr) instanceOfExpr).getOp());
        AssignStmt assignIfFalseStmt = Jimple.v().newAssignStmt(variable, NullConstant.v());
        IfStmt ifStmt = Jimple.v().newIfStmt(Jimple.v().newEqExpr(local, IntConstant.v(1)), assignIfTrueStmt);
        GotoStmt gotoStmt = Jimple.v().newGotoStmt(nopStmt);
        jb.getUnits().add((UnitPatchingChain) assignInstanceOfStmt);
        jb.getUnits().add((UnitPatchingChain) ifStmt);
        jb.getUnits().add((UnitPatchingChain) assignIfFalseStmt);
        jb.getUnits().add((UnitPatchingChain) gotoStmt);
        jb.getUnits().add((UnitPatchingChain) assignIfTrueStmt);
        jb.getUnits().add((UnitPatchingChain) nopStmt);
        this.dotnetBody.variableManager.addLocalsToCast(variable.getName());
    }
}
