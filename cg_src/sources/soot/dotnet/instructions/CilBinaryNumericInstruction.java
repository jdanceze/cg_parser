package soot.dotnet.instructions;

import soot.Body;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilBinaryNumericInstruction.class */
public class CilBinaryNumericInstruction extends AbstractCilnstruction {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlBinaryNumericOperator;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlBinaryNumericOperator() {
        int[] iArr = $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlBinaryNumericOperator;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.valuesCustom().length];
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.Add.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.BitAnd.ordinal()] = 7;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.BitOr.ordinal()] = 8;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.BitXor.ordinal()] = 9;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.Div.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.Mul.ordinal()] = 4;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.NONE_BINARY.ordinal()] = 1;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.Rem.ordinal()] = 6;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.ShiftLeft.ordinal()] = 10;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.ShiftRight.ordinal()] = 11;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.Sub.ordinal()] = 3;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.UNRECOGNIZED.ordinal()] = 12;
        } catch (NoSuchFieldError unused12) {
        }
        $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlBinaryNumericOperator = iArr2;
        return iArr2;
    }

    public CilBinaryNumericInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        Value left = DotnetBody.inlineCastExpr(CilInstructionFactory.fromInstructionMsg(this.instruction.getLeft(), this.dotnetBody, this.cilBlock).jimplifyExpr(jb));
        Value right = DotnetBody.inlineCastExpr(CilInstructionFactory.fromInstructionMsg(this.instruction.getRight(), this.dotnetBody, this.cilBlock).jimplifyExpr(jb));
        switch ($SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlBinaryNumericOperator()[this.instruction.getOperator().ordinal()]) {
            case 2:
                return Jimple.v().newAddExpr(left, right);
            case 3:
                return Jimple.v().newSubExpr(left, right);
            case 4:
                return Jimple.v().newMulExpr(left, right);
            case 5:
                return Jimple.v().newDivExpr(left, right);
            case 6:
                return Jimple.v().newRemExpr(left, right);
            case 7:
                return Jimple.v().newAndExpr(left, right);
            case 8:
                return Jimple.v().newOrExpr(left, right);
            case 9:
                return Jimple.v().newXorExpr(left, right);
            case 10:
                return Jimple.v().newShlExpr(left, right);
            case 11:
                if (this.instruction.getSign().equals(ProtoIlInstructions.IlInstructionMsg.IlSign.Signed)) {
                    return Jimple.v().newShrExpr(left, right);
                }
                return Jimple.v().newUshrExpr(left, right);
            default:
                return null;
        }
    }
}
