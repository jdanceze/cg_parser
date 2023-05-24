package soot.dotnet.instructions;

import java.util.List;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.dotnet.instructions.CilBlockContainer;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CatchHandlerBody.class */
public class CatchHandlerBody {
    private final Local exceptionVariable;
    private final ProtoIlInstructions.IlTryCatchHandlerMsg handlerMsg;
    private final DotnetBody dotnetBody;
    private final SootClass exceptionClass = Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_EXCEPTION);
    private final Body tryBody;
    private final Unit exceptionIdentityStmt;
    private final List<Unit> nopsToReplaceWithGoto;

    public CatchHandlerBody(Local exceptionVariable, ProtoIlInstructions.IlTryCatchHandlerMsg handlerMsg, DotnetBody dotnetBody, Body tryBody, Unit exceptionIdentityStmt, List<Unit> nopsToReplaceWithGoto) {
        this.exceptionVariable = exceptionVariable;
        this.handlerMsg = handlerMsg;
        this.dotnetBody = dotnetBody;
        this.tryBody = tryBody;
        this.exceptionIdentityStmt = exceptionIdentityStmt;
        this.nopsToReplaceWithGoto = nopsToReplaceWithGoto;
    }

    public Local getExceptionVariable() {
        return this.exceptionVariable;
    }

    public Body getBody() {
        Body jb = new JimpleBody();
        IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(this.exceptionVariable, Jimple.v().newCaughtExceptionRef());
        jb.getUnits().add((UnitPatchingChain) newIdentityStmt);
        CilBlockContainer handlerBlock = new CilBlockContainer(this.handlerMsg.getBody(), this.dotnetBody, CilBlockContainer.BlockContainerKind.CATCH_HANDLER);
        Body handlerBody = handlerBlock.jimplify();
        if (lastStmtIsNotReturn(handlerBody)) {
            NopStmt nopStmt = Jimple.v().newNopStmt();
            handlerBody.getUnits().add((UnitPatchingChain) nopStmt);
            this.nopsToReplaceWithGoto.add(nopStmt);
        }
        jb.getLocals().addAll(handlerBody.getLocals());
        jb.getUnits().addAll(handlerBody.getUnits());
        jb.getTraps().addAll(handlerBody.getTraps());
        Trap trap = Jimple.v().newTrap(Scene.v().getSootClass(this.exceptionVariable.getType().toString()), this.tryBody.getUnits().getFirst(), this.tryBody.getUnits().getLast(), newIdentityStmt);
        jb.getTraps().add(trap);
        Trap trapCatchThrow = Jimple.v().newTrap(this.exceptionClass, newIdentityStmt, handlerBody.getUnits().getLast(), this.exceptionIdentityStmt);
        jb.getTraps().add(trapCatchThrow);
        return jb;
    }

    private boolean lastStmtIsNotReturn(Body handlerBody) {
        return handlerBody.getUnits().size() == 0 || !isExitStmt(handlerBody.getUnits().getLast());
    }

    private static boolean isExitStmt(Unit unit) {
        return (unit instanceof ReturnStmt) || (unit instanceof ReturnVoidStmt) || (unit instanceof ThrowStmt);
    }
}
