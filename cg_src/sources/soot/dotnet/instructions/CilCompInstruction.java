package soot.dotnet.instructions;

import soot.Body;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.BinopExpr;
import soot.jimple.Constant;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilCompInstruction.class */
public class CilCompInstruction extends AbstractCilnstruction {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlComparisonKind;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlComparisonKind() {
        int[] iArr = $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlComparisonKind;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.valuesCustom().length];
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.Equality.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.GreaterThan.ordinal()] = 6;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.GreaterThanOrEqual.ordinal()] = 7;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.Inequality.ordinal()] = 3;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.LessThan.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.LessThanOrEqual.ordinal()] = 5;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.NONE_KIND.ordinal()] = 1;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.UNRECOGNIZED.ordinal()] = 8;
        } catch (NoSuchFieldError unused8) {
        }
        $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlComparisonKind = iArr2;
        return iArr2;
    }

    public CilCompInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        boolean expectedValueTrue;
        Value left = DotnetBody.inlineCastExpr(CilInstructionFactory.fromInstructionMsg(this.instruction.getLeft(), this.dotnetBody, this.cilBlock).jimplifyExpr(jb));
        Value right = DotnetBody.inlineCastExpr(CilInstructionFactory.fromInstructionMsg(this.instruction.getRight(), this.dotnetBody, this.cilBlock).jimplifyExpr(jb));
        ProtoIlInstructions.IlInstructionMsg.IlComparisonKind comparisonKind = this.instruction.getComparisonKind();
        if ((right instanceof BinopExpr) && (left instanceof Constant) && (comparisonKind == ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.Equality || comparisonKind == ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.Inequality)) {
            right = left;
            left = right;
        }
        if ((left instanceof BinopExpr) && (right instanceof IntConstant)) {
            IntConstant c = (IntConstant) right;
            if (c.value == 0) {
                expectedValueTrue = false;
            } else if (c.value == 1) {
                expectedValueTrue = true;
            } else {
                throw new RuntimeException("Missing case for c.value");
            }
            if (comparisonKind == ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.Inequality) {
                expectedValueTrue = !expectedValueTrue;
            }
            if (expectedValueTrue) {
                return left;
            }
            BinopExpr binop = (BinopExpr) left;
            if (left instanceof EqExpr) {
                return Jimple.v().newNeExpr(binop.getOp1(), binop.getOp2());
            }
            if (left instanceof NeExpr) {
                return Jimple.v().newEqExpr(binop.getOp1(), binop.getOp2());
            }
            if (left instanceof LtExpr) {
                return Jimple.v().newGeExpr(binop.getOp1(), binop.getOp2());
            }
            if (left instanceof LeExpr) {
                return Jimple.v().newGtExpr(binop.getOp1(), binop.getOp2());
            }
            if (left instanceof GeExpr) {
                return Jimple.v().newLtExpr(binop.getOp1(), binop.getOp2());
            }
            if (left instanceof GtExpr) {
                return Jimple.v().newLeExpr(binop.getOp1(), binop.getOp2());
            }
            return null;
        }
        switch ($SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlComparisonKind()[comparisonKind.ordinal()]) {
            case 2:
                return Jimple.v().newEqExpr(left, right);
            case 3:
                return Jimple.v().newNeExpr(left, right);
            case 4:
                return Jimple.v().newLtExpr(left, right);
            case 5:
                return Jimple.v().newLeExpr(left, right);
            case 6:
                return Jimple.v().newGtExpr(left, right);
            case 7:
                return Jimple.v().newGeExpr(left, right);
            default:
                return null;
        }
    }
}
