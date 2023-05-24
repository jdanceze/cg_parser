package soot.dotnet.instructions;

import soot.Body;
import soot.Local;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilIfInstruction.class */
public class CilIfInstruction extends AbstractCilnstruction {
    public CilIfInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        Value eqExpr;
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getCondition(), this.dotnetBody, this.cilBlock);
        Value condition = cilExpr.jimplifyExpr(jb);
        if (condition instanceof ConditionExpr) {
            eqExpr = condition;
        } else {
            Local tmpLocalCond = this.dotnetBody.variableManager.localGenerator.generateLocal(condition.getType());
            jb.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(tmpLocalCond, condition));
            eqExpr = Jimple.v().newEqExpr(tmpLocalCond, IntConstant.v(1));
        }
        CilInstruction trueInstruction = CilInstructionFactory.fromInstructionMsg(this.instruction.getTrueInst(), this.dotnetBody, this.cilBlock);
        Unit trueInstruct = Jimple.v().newNopStmt();
        IfStmt ifStmt = Jimple.v().newIfStmt(eqExpr, trueInstruct);
        jb.getUnits().add((UnitPatchingChain) ifStmt);
        String target = trueInstruction instanceof CilLeaveInstruction ? "RETURNLEAVE" : this.instruction.getTrueInst().getTargetLabel();
        this.cilBlock.getDeclaredBlockContainer().blockEntryPointsManager.gotoTargetsInBody.put(trueInstruct, target);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
