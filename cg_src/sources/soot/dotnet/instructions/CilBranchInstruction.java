package soot.dotnet.instructions;

import soot.Body;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.GotoStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilBranchInstruction.class */
public class CilBranchInstruction extends AbstractCilnstruction {
    public CilBranchInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        Unit target = Jimple.v().newNopStmt();
        GotoStmt gotoStmt = Jimple.v().newGotoStmt(target);
        jb.getUnits().add((UnitPatchingChain) gotoStmt);
        this.dotnetBody.blockEntryPointsManager.gotoTargetsInBody.put(target, this.instruction.getTargetLabel());
        this.cilBlock.getDeclaredBlockContainer().blockEntryPointsManager.gotoTargetsInBody.put(target, this.instruction.getTargetLabel());
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
