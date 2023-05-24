package soot.dotnet.instructions;

import soot.Body;
import soot.ByteType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.ShortType;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilConvInstruction.class */
public class CilConvInstruction extends AbstractCilnstruction {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlPrimitiveType;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlPrimitiveType() {
        int[] iArr = $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlPrimitiveType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.valuesCustom().length];
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.I.ordinal()] = 13;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.I1.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.I2.ordinal()] = 4;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.I4.ordinal()] = 5;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.I8.ordinal()] = 6;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.NONE_PRIMITIVE_TYPE.ordinal()] = 1;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.None.ordinal()] = 2;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.R.ordinal()] = 16;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.R4.ordinal()] = 7;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.R8.ordinal()] = 8;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.Ref.ordinal()] = 15;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.U.ordinal()] = 14;
        } catch (NoSuchFieldError unused12) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.U1.ordinal()] = 9;
        } catch (NoSuchFieldError unused13) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.U2.ordinal()] = 10;
        } catch (NoSuchFieldError unused14) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.U4.ordinal()] = 11;
        } catch (NoSuchFieldError unused15) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.U8.ordinal()] = 12;
        } catch (NoSuchFieldError unused16) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.UNRECOGNIZED.ordinal()] = 18;
        } catch (NoSuchFieldError unused17) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.Unknown.ordinal()] = 17;
        } catch (NoSuchFieldError unused18) {
        }
        $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlPrimitiveType = iArr2;
        return iArr2;
    }

    public CilConvInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        Type convType;
        this.instruction.getSign();
        this.instruction.getInputType();
        ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType targetType = this.instruction.getTargetType();
        this.instruction.getResultType();
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getArgument(), this.dotnetBody, this.cilBlock);
        Value argument = cilExpr.jimplifyExpr(jb);
        switch ($SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlPrimitiveType()[targetType.ordinal()]) {
            case 3:
            case 9:
                convType = ByteType.v();
                break;
            case 4:
            case 10:
                convType = ShortType.v();
                break;
            case 5:
            case 11:
            case 13:
            case 14:
            case 15:
                convType = IntType.v();
                break;
            case 6:
            case 12:
                convType = LongType.v();
                break;
            case 7:
                convType = FloatType.v();
                break;
            case 8:
            case 16:
                convType = DoubleType.v();
                break;
            default:
                convType = UnknownType.v();
                break;
        }
        return Jimple.v().newCastExpr(argument, convType);
    }
}
