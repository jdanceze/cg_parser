package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.Iterator;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.instructions.CilBlockContainer;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CatchFilterHandlerBody.class */
public class CatchFilterHandlerBody {
    private final ProtoIlInstructions.IlTryCatchHandlerMsg handlerMsg;
    private final Local exceptionVar;
    private final Unit nopStmtEnd;
    private final DotnetBody dotnetBody;

    public CatchFilterHandlerBody(DotnetBody dotnetBody, ProtoIlInstructions.IlTryCatchHandlerMsg handlerMsg, Local exceptionVar, Unit nopStmtEnd) {
        this.dotnetBody = dotnetBody;
        this.handlerMsg = handlerMsg;
        this.exceptionVar = exceptionVar;
        this.nopStmtEnd = nopStmtEnd;
    }

    public Body getFilterHandlerBody(Value generalExceptionVariable) {
        Body jb = new JimpleBody();
        AssignStmt assignStmt = Jimple.v().newAssignStmt(this.exceptionVar, generalExceptionVariable);
        jb.getUnits().add((UnitPatchingChain) assignStmt);
        NopStmt filterCondFalseNop = Jimple.v().newNopStmt();
        CilBlockContainer handlerFilterContainerBlock = new CilBlockContainer(this.handlerMsg.getFilter(), this.dotnetBody, CilBlockContainer.BlockContainerKind.CATCH_FILTER);
        Body handlerFilterContainerBlockBody = handlerFilterContainerBlock.jimplify();
        CilBlockContainer handlerBlock = new CilBlockContainer(this.handlerMsg.getBody(), this.dotnetBody, CilBlockContainer.BlockContainerKind.CATCH_HANDLER);
        Body handlerBody = handlerBlock.jimplify();
        ArrayList<Unit> tmpToInsert = new ArrayList<>();
        Iterator<Unit> it = handlerFilterContainerBlockBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit instanceof ReturnStmt) {
                tmpToInsert.add(unit);
            }
        }
        Iterator<Unit> it2 = tmpToInsert.iterator();
        while (it2.hasNext()) {
            Unit returnStmt = it2.next();
            Value returnValue = ((ReturnStmt) returnStmt).getOp();
            ConditionExpr cond = Jimple.v().newEqExpr(returnValue, IntConstant.v(0));
            IfStmt ifRetCondStmt = Jimple.v().newIfStmt(cond, filterCondFalseNop);
            GotoStmt gotoHandlerBodyCondTrueStmt = Jimple.v().newGotoStmt(handlerBody.getUnits().getFirst());
            handlerFilterContainerBlockBody.getUnits().insertAfter(gotoHandlerBodyCondTrueStmt, (GotoStmt) returnStmt);
            handlerFilterContainerBlockBody.getUnits().swapWith(returnStmt, (Unit) ifRetCondStmt);
            this.dotnetBody.blockEntryPointsManager.swapGotoEntryUnit(ifRetCondStmt, returnStmt);
        }
        jb.getUnits().addAll(handlerFilterContainerBlockBody.getUnits());
        if (lastStmtIsNotReturn(handlerBody)) {
            handlerBody.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(this.nopStmtEnd));
        }
        jb.getLocals().addAll(handlerBody.getLocals());
        jb.getUnits().addAll(handlerBody.getUnits());
        jb.getTraps().addAll(handlerBody.getTraps());
        jb.getUnits().add((UnitPatchingChain) filterCondFalseNop);
        return jb;
    }

    private static boolean lastStmtIsNotReturn(Body jb) {
        return !isExitStmt(jb.getUnits().getLast());
    }

    private static boolean isExitStmt(Unit unit) {
        return (unit instanceof ReturnStmt) || (unit instanceof ReturnVoidStmt) || (unit instanceof ThrowStmt);
    }
}
