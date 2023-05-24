package soot.dotnet.instructions;

import java.util.ArrayList;
import soot.Body;
import soot.RefType;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilBlock.class */
public class CilBlock implements CilInstruction {
    private final ProtoIlInstructions.IlBlock block;
    private final DotnetBody dotnetBody;
    private final CilBlockContainer blockContainer;
    private Unit entryUnit = null;

    public CilBlockContainer getDeclaredBlockContainer() {
        return this.blockContainer;
    }

    public CilBlock(ProtoIlInstructions.IlBlock block, DotnetBody dotnetBody, CilBlockContainer blockContainer) {
        this.block = block;
        this.dotnetBody = dotnetBody;
        this.blockContainer = blockContainer;
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        boolean setBlockGotoStmt = false;
        Body jbTmp = Jimple.v().newBody();
        jbTmp.setMethod(new SootMethod("", new ArrayList(), RefType.v("")));
        for (ProtoIlInstructions.IlInstructionMsg instruction : this.block.getListOfIlInstructionsList()) {
            CilInstruction cilInstruction = CilInstructionFactory.fromInstructionMsg(instruction, this.dotnetBody, this);
            cilInstruction.jimplify(jbTmp);
            if (!setBlockGotoStmt && jbTmp.getUnits().size() != 0) {
                setBlockGotoStmt = true;
                this.entryUnit = jbTmp.getUnits().getFirst();
                this.dotnetBody.blockEntryPointsManager.putBlockEntryPoint(getBlockName(), this.entryUnit);
                this.blockContainer.blockEntryPointsManager.putBlockEntryPoint(getBlockName(), this.entryUnit);
            }
        }
        if (jbTmp.getUnits().size() != 0) {
            this.dotnetBody.blockEntryPointsManager.putBlockEntryPoint("END_" + getBlockName(), jbTmp.getUnits().getLast());
            this.blockContainer.blockEntryPointsManager.putBlockEntryPoint("END_" + getBlockName(), this.entryUnit);
        }
        jb.getUnits().addAll(jbTmp.getUnits());
        jb.getLocals().addAll(jbTmp.getLocals());
        jb.getTraps().addAll(jbTmp.getTraps());
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException();
    }

    public String getBlockName() {
        return this.block.getBlockName();
    }

    public Unit getEntryUnit() {
        if (this.entryUnit == null) {
            throw new RuntimeException("getEntryUnit() was called before jimplifying!");
        }
        return this.entryUnit;
    }
}
