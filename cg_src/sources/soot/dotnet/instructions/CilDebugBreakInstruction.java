package soot.dotnet.instructions;

import soot.Body;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.BreakpointStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilDebugBreakInstruction.class */
public class CilDebugBreakInstruction extends AbstractCilnstruction {
    public CilDebugBreakInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        BreakpointStmt breakpointStmt = Jimple.v().newBreakpointStmt();
        jb.getUnits().add((UnitPatchingChain) breakpointStmt);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
