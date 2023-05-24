package soot.dotnet.instructions;

import java.util.ArrayList;
import soot.Body;
import soot.RefType;
import soot.SootMethod;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.members.method.BlockEntryPointsManager;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilBlockContainer.class */
public class CilBlockContainer implements CilInstruction {
    private final ProtoIlInstructions.IlBlockContainerMsg blockContainer;
    private final DotnetBody dotnetBody;
    public final BlockEntryPointsManager blockEntryPointsManager;
    private final Stmt skipBlockContainerStmt;
    private final BlockContainerKind blockContainerKind;

    /* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilBlockContainer$BlockContainerKind.class */
    public enum BlockContainerKind {
        NORMAL,
        TRY,
        CATCH_HANDLER,
        CATCH_FILTER,
        FAULT,
        FINALLY,
        CHILD;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static BlockContainerKind[] valuesCustom() {
            BlockContainerKind[] valuesCustom = values();
            int length = valuesCustom.length;
            BlockContainerKind[] blockContainerKindArr = new BlockContainerKind[length];
            System.arraycopy(valuesCustom, 0, blockContainerKindArr, 0, length);
            return blockContainerKindArr;
        }
    }

    public CilBlockContainer(ProtoIlInstructions.IlBlockContainerMsg blockContainer, DotnetBody dotnetBody) {
        this(blockContainer, dotnetBody, BlockContainerKind.NORMAL);
    }

    public CilBlockContainer(ProtoIlInstructions.IlBlockContainerMsg blockContainer, DotnetBody dotnetBody, BlockContainerKind blockContainerKind) {
        this.blockContainer = blockContainer;
        this.dotnetBody = dotnetBody;
        this.blockContainerKind = blockContainerKind;
        this.blockEntryPointsManager = new BlockEntryPointsManager();
        this.skipBlockContainerStmt = Jimple.v().newNopStmt();
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        if (this.blockContainer == null || this.blockContainer.getBlocksList().size() == 0 || this.blockContainer.getBlocksList().get(0).getListOfIlInstructionsCount() == 0) {
            return;
        }
        for (ProtoIlInstructions.IlBlock block : this.blockContainer.getBlocksList()) {
            CilBlock cilBlock = new CilBlock(block, this.dotnetBody, this);
            cilBlock.jimplify(jb);
        }
        if (isChildBlockContainer()) {
            jb.getUnits().add((UnitPatchingChain) this.skipBlockContainerStmt);
        }
        this.blockEntryPointsManager.swapGotoEntriesInJBody(jb);
    }

    public Body jimplify() {
        Body jbTmp = Jimple.v().newBody();
        jbTmp.setMethod(new SootMethod("", new ArrayList(), RefType.v("")));
        jimplify(jbTmp);
        return jbTmp;
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new RuntimeException(String.valueOf(getClass().getName()) + " does not have expressions, but statements!");
    }

    public static boolean LastStmtIsNotReturn(Body jb) {
        return jb.getUnits().size() == 0 || !isExitStmt(jb.getUnits().getLast());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isExitStmt(Unit unit) {
        return (unit instanceof ReturnStmt) || (unit instanceof ReturnVoidStmt) || (unit instanceof ThrowStmt);
    }

    public DotnetBody getDeclaringDotnetBody() {
        return this.dotnetBody;
    }

    public boolean isChildBlockContainer() {
        return !getBlockContainerKind().equals(BlockContainerKind.NORMAL);
    }

    public BlockContainerKind getBlockContainerKind() {
        return this.blockContainerKind;
    }

    public Stmt getSkipBlockContainerStmt() {
        return this.skipBlockContainerStmt;
    }
}
