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
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilTryFinallyInstruction.class */
public class CilTryFinallyInstruction extends AbstractCilnstruction {
    public CilTryFinallyInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilBlockContainer tryContainer = new CilBlockContainer(this.instruction.getTryBlock(), this.dotnetBody, CilBlockContainer.BlockContainerKind.TRY);
        Body tryContainerBlock = tryContainer.jimplify();
        CilBlockContainer finallyBlockContainer = new CilBlockContainer(this.instruction.getFinallyBlock(), this.dotnetBody, CilBlockContainer.BlockContainerKind.FINALLY);
        Body finallyBlockContainerBody = finallyBlockContainer.jimplify();
        tryContainerBlock.getLocals().addAll(finallyBlockContainerBody.getLocals());
        Map<Trap, Unit> tmpTrapEnds = new HashMap<>();
        for (Trap trap : tryContainerBlock.getTraps()) {
            tmpTrapEnds.put(trap, trap.getEndUnit());
        }
        ArrayList<Unit> tmpUnits = new ArrayList<>();
        Iterator<Unit> it = tryContainerBlock.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (CilBlockContainer.isExitStmt(unit)) {
                tmpUnits.add(unit);
            }
        }
        Iterator<Unit> it2 = tmpUnits.iterator();
        while (it2.hasNext()) {
            Unit unit2 = it2.next();
            finallyBlockContainerBody.setMethod(new SootMethod("", new ArrayList(), RefType.v("")));
            Body cloneFinallyBlock = (Body) finallyBlockContainerBody.clone(true);
            tryContainerBlock.getUnits().insertBefore((Chain<UnitPatchingChain>) cloneFinallyBlock.getUnits(), (UnitPatchingChain) unit2);
            tryContainerBlock.getTraps().addAll(cloneFinallyBlock.getTraps());
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
