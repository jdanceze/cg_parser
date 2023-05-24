package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.RefType;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.instructions.CilBlockContainer;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilTryFaultInstruction.class */
public class CilTryFaultInstruction extends AbstractCilnstruction {
    public CilTryFaultInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilBlockContainer tryContainer = new CilBlockContainer(this.instruction.getTryBlock(), this.dotnetBody, CilBlockContainer.BlockContainerKind.TRY);
        Body tryContainerBlock = tryContainer.jimplify();
        CilBlockContainer faultBlockContainer = new CilBlockContainer(this.instruction.getFaultBlock(), this.dotnetBody, CilBlockContainer.BlockContainerKind.FAULT);
        Body faultBlockContainerBody = faultBlockContainer.jimplify();
        tryContainerBlock.getLocals().addAll(faultBlockContainerBody.getLocals());
        Map<Trap, Unit> tmpTrapEnds = new HashMap<>();
        Iterator<Trap> it = tryContainerBlock.getTraps().iterator();
        if (it.hasNext()) {
            Trap trap = it.next();
            tmpTrapEnds.put(trap, trap.getEndUnit());
            Unit handlerUnit = trap.getHandlerUnit();
            Iterator<Unit> iterator = tryContainerBlock.getUnits().iterator((UnitPatchingChain) handlerUnit);
            ArrayList<Unit> tmpUnits = new ArrayList<>();
            while (iterator.hasNext()) {
                Unit next = iterator.next();
                if (CilBlockContainer.isExitStmt(next)) {
                    tmpUnits.add(next);
                }
            }
            Iterator<Unit> it2 = tmpUnits.iterator();
            while (it2.hasNext()) {
                Unit unit = it2.next();
                faultBlockContainerBody.setMethod(new SootMethod("", new ArrayList(), RefType.v("")));
                Body cloneFaultBlock = (Body) faultBlockContainerBody.clone(true);
                tryContainerBlock.getUnits().insertBefore((Chain<UnitPatchingChain>) cloneFaultBlock.getUnits(), (UnitPatchingChain) unit);
                tryContainerBlock.getTraps().addAll(cloneFaultBlock.getTraps());
            }
        }
        for (Map.Entry<Trap, Unit> trapMap : tmpTrapEnds.entrySet()) {
            trapMap.getKey().setEndUnit(trapMap.getValue());
        }
        jb.getLocals().addAll(tryContainerBlock.getLocals());
        jb.getUnits().addAll(tryContainerBlock.getUnits());
        jb.getTraps().addAll(tryContainerBlock.getTraps());
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
