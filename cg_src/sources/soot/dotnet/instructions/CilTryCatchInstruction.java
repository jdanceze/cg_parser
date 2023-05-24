package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.instructions.CilBlockContainer;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilTryCatchInstruction.class */
public class CilTryCatchInstruction extends AbstractCilnstruction {
    public CilTryCatchInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        List<Unit> nopsToReplaceWithGoto = new ArrayList<>();
        SootClass exceptionClass = Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_EXCEPTION);
        NopStmt gotoEndTryCatchBlockNop = Jimple.v().newNopStmt();
        CilBlockContainer tryContainer = new CilBlockContainer(this.instruction.getTryBlock(), this.dotnetBody, CilBlockContainer.BlockContainerKind.TRY);
        Body tryContainerBlock = tryContainer.jimplify();
        if (CilBlockContainer.LastStmtIsNotReturn(tryContainerBlock)) {
            NopStmt nopStmt = Jimple.v().newNopStmt();
            tryContainerBlock.getUnits().add((UnitPatchingChain) nopStmt);
            nopsToReplaceWithGoto.add(nopStmt);
        }
        jb.getLocals().addAll(tryContainerBlock.getLocals());
        jb.getUnits().addAll(tryContainerBlock.getUnits());
        jb.getTraps().addAll(tryContainerBlock.getTraps());
        Local uncaughtExceptionVar = this.dotnetBody.variableManager.localGenerator.generateLocal(exceptionClass.getType());
        IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(uncaughtExceptionVar, Jimple.v().newCaughtExceptionRef());
        List<ProtoIlInstructions.IlTryCatchHandlerMsg> protoHandlersList = this.instruction.getHandlersList();
        List<CatchFilterHandlerBody> handlersWithFilterList = new ArrayList<>();
        List<CatchHandlerBody> handlersList = new ArrayList<>();
        CatchHandlerBody systemExceptionHandler = null;
        for (ProtoIlInstructions.IlTryCatchHandlerMsg handlerMsg : protoHandlersList) {
            Local exceptionVar = this.dotnetBody.variableManager.addOrGetVariable(handlerMsg.getVariable(), jb);
            if (handlerMsg.getHasFilter()) {
                CatchFilterHandlerBody filterHandler = new CatchFilterHandlerBody(this.dotnetBody, handlerMsg, exceptionVar, gotoEndTryCatchBlockNop);
                handlersWithFilterList.add(filterHandler);
            } else {
                CatchHandlerBody handler = new CatchHandlerBody(exceptionVar, handlerMsg, this.dotnetBody, tryContainerBlock, newIdentityStmt, nopsToReplaceWithGoto);
                handlersList.add(handler);
                if (handlerMsg.getVariable().getType().getFullname().equals(DotnetBasicTypes.SYSTEM_EXCEPTION)) {
                    systemExceptionHandler = handler;
                }
            }
        }
        for (CatchHandlerBody handlerBody : handlersList) {
            Body body = handlerBody.getBody();
            if (handlerBody == systemExceptionHandler) {
                Map<Trap, Unit> tmpTrapEnds = new HashMap<>();
                for (Trap trap : body.getTraps()) {
                    tmpTrapEnds.put(trap, trap.getEndUnit());
                }
                for (CatchFilterHandlerBody filterHandler2 : handlersWithFilterList) {
                    Local eVar = systemExceptionHandler.getExceptionVariable();
                    Body filterHandlerBody = filterHandler2.getFilterHandlerBody(eVar);
                    body.getUnits().insertAfter((Chain<UnitPatchingChain>) filterHandlerBody.getUnits(), (UnitPatchingChain) body.getUnits().getFirst());
                    body.getTraps().addAll(filterHandlerBody.getTraps());
                }
                for (Map.Entry<Trap, Unit> trapMap : tmpTrapEnds.entrySet()) {
                    trapMap.getKey().setEndUnit(trapMap.getValue());
                }
            }
            jb.getUnits().addAll(body.getUnits());
            jb.getTraps().addAll(body.getTraps());
        }
        if (systemExceptionHandler == null) {
            jb.getTraps().add(Jimple.v().newTrap(exceptionClass, tryContainerBlock.getUnits().getFirst(), tryContainerBlock.getUnits().getLast(), newIdentityStmt));
        }
        jb.getUnits().add((UnitPatchingChain) newIdentityStmt);
        if (systemExceptionHandler == null) {
            for (CatchFilterHandlerBody filterHandler3 : handlersWithFilterList) {
                Body filterHandlerBody2 = filterHandler3.getFilterHandlerBody(uncaughtExceptionVar);
                jb.getUnits().addAll(filterHandlerBody2.getUnits());
                jb.getTraps().addAll(filterHandlerBody2.getTraps());
            }
        }
        jb.getUnits().add((UnitPatchingChain) Jimple.v().newThrowStmt(uncaughtExceptionVar));
        jb.getUnits().add((UnitPatchingChain) gotoEndTryCatchBlockNop);
        for (Unit nop : nopsToReplaceWithGoto) {
            GotoStmt gotoStmt = Jimple.v().newGotoStmt(gotoEndTryCatchBlockNop);
            jb.getUnits().swapWith(nop, (Unit) gotoStmt);
        }
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
