package soot.dotnet.instructions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilInstructionFactory.class */
public class CilInstructionFactory {
    private static final Logger logger = LoggerFactory.getLogger(CilInstructionFactory.class);
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlOpCode;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlOpCode() {
        int[] iArr = $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlOpCode;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ProtoIlInstructions.IlInstructionMsg.IlOpCode.valuesCustom().length];
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.BINARY_NUMERIC_INSTRUCTION.ordinal()] = 14;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.BLOCK.ordinal()] = 36;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.BLOCK_CONTAINER.ordinal()] = 35;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.BOX.ordinal()] = 26;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.BRANCH.ordinal()] = 15;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CALL.ordinal()] = 3;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CALLVIRT.ordinal()] = 13;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CALL_INDIRECT.ordinal()] = 70;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CASTCLASS.ordinal()] = 24;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CK_FINITE.ordinal()] = 41;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.COMP.ordinal()] = 16;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CONV.ordinal()] = 21;
        } catch (NoSuchFieldError unused12) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CP_BLK.ordinal()] = 42;
        } catch (NoSuchFieldError unused13) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.CP_OBJ.ordinal()] = 43;
        } catch (NoSuchFieldError unused14) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.DEBUG_BREAK.ordinal()] = 40;
        } catch (NoSuchFieldError unused15) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.DEFAULT_VALUE.ordinal()] = 31;
        } catch (NoSuchFieldError unused16) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.DUP.ordinal()] = 44;
        } catch (NoSuchFieldError unused17) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.IF_INSTRUCTION.ordinal()] = 17;
        } catch (NoSuchFieldError unused18) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.INIT_BLK.ordinal()] = 45;
        } catch (NoSuchFieldError unused19) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.INIT_OBJ.ordinal()] = 46;
        } catch (NoSuchFieldError unused20) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.INVALID_BRANCH.ordinal()] = 69;
        } catch (NoSuchFieldError unused21) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.ISINST.ordinal()] = 25;
        } catch (NoSuchFieldError unused22) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDC_I4.ordinal()] = 8;
        } catch (NoSuchFieldError unused23) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDC_I8.ordinal()] = 47;
        } catch (NoSuchFieldError unused24) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDC_R4.ordinal()] = 48;
        } catch (NoSuchFieldError unused25) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDC_R8.ordinal()] = 49;
        } catch (NoSuchFieldError unused26) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDELEMA.ordinal()] = 23;
        } catch (NoSuchFieldError unused27) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDFLDA.ordinal()] = 7;
        } catch (NoSuchFieldError unused28) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDLEN.ordinal()] = 20;
        } catch (NoSuchFieldError unused29) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDLOC.ordinal()] = 9;
        } catch (NoSuchFieldError unused30) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDLOCA.ordinal()] = 30;
        } catch (NoSuchFieldError unused31) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDNULL.ordinal()] = 19;
        } catch (NoSuchFieldError unused32) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDOBJ.ordinal()] = 10;
        } catch (NoSuchFieldError unused33) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDSFLDA.ordinal()] = 18;
        } catch (NoSuchFieldError unused34) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LDSTR.ordinal()] = 5;
        } catch (NoSuchFieldError unused35) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LD_FLD.ordinal()] = 50;
        } catch (NoSuchFieldError unused36) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LD_FTN.ordinal()] = 51;
        } catch (NoSuchFieldError unused37) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LD_MEMBER_TOKEN.ordinal()] = 67;
        } catch (NoSuchFieldError unused38) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LD_SFLD.ordinal()] = 52;
        } catch (NoSuchFieldError unused39) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LD_TOKEN.ordinal()] = 53;
        } catch (NoSuchFieldError unused40) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LD_TYPE_TOKEN.ordinal()] = 68;
        } catch (NoSuchFieldError unused41) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LD_VIRT_FTN.ordinal()] = 54;
        } catch (NoSuchFieldError unused42) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LEAVE.ordinal()] = 4;
        } catch (NoSuchFieldError unused43) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.LOC_ALLOC.ordinal()] = 55;
        } catch (NoSuchFieldError unused44) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.MK_REF_ANY.ordinal()] = 56;
        } catch (NoSuchFieldError unused45) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.NEWARR.ordinal()] = 22;
        } catch (NoSuchFieldError unused46) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.NEWOBJ.ordinal()] = 12;
        } catch (NoSuchFieldError unused47) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.NO.ordinal()] = 57;
        } catch (NoSuchFieldError unused48) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.NONE_OP.ordinal()] = 1;
        } catch (NoSuchFieldError unused49) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.NOP.ordinal()] = 2;
        } catch (NoSuchFieldError unused50) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.NOT.ordinal()] = 32;
        } catch (NoSuchFieldError unused51) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.READONLY.ordinal()] = 58;
        } catch (NoSuchFieldError unused52) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.REF_ANY_TYPE.ordinal()] = 59;
        } catch (NoSuchFieldError unused53) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.REF_ANY_VAL.ordinal()] = 60;
        } catch (NoSuchFieldError unused54) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.RETHROW.ordinal()] = 38;
        } catch (NoSuchFieldError unused55) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.SIZE_OF.ordinal()] = 61;
        } catch (NoSuchFieldError unused56) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.STLOC.ordinal()] = 11;
        } catch (NoSuchFieldError unused57) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.STOBJ.ordinal()] = 6;
        } catch (NoSuchFieldError unused58) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.ST_SFLD.ordinal()] = 62;
        } catch (NoSuchFieldError unused59) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.SWITCH.ordinal()] = 63;
        } catch (NoSuchFieldError unused60) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.TAIL.ordinal()] = 64;
        } catch (NoSuchFieldError unused61) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.THROW.ordinal()] = 39;
        } catch (NoSuchFieldError unused62) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.TRY_CATCH.ordinal()] = 29;
        } catch (NoSuchFieldError unused63) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.TRY_CATCH_HANDLER.ordinal()] = 37;
        } catch (NoSuchFieldError unused64) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.TRY_FAULT.ordinal()] = 34;
        } catch (NoSuchFieldError unused65) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.TRY_FINALLY.ordinal()] = 33;
        } catch (NoSuchFieldError unused66) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.UNALIGNED.ordinal()] = 65;
        } catch (NoSuchFieldError unused67) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.UNBOX.ordinal()] = 28;
        } catch (NoSuchFieldError unused68) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.UNBOXANY.ordinal()] = 27;
        } catch (NoSuchFieldError unused69) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.UNRECOGNIZED.ordinal()] = 71;
        } catch (NoSuchFieldError unused70) {
        }
        try {
            iArr2[ProtoIlInstructions.IlInstructionMsg.IlOpCode.VOLATILE.ordinal()] = 66;
        } catch (NoSuchFieldError unused71) {
        }
        $SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlOpCode = iArr2;
        return iArr2;
    }

    public static CilInstruction fromInstructionMsg(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        if (instruction == null) {
            throw new RuntimeException("Cannot instantiate null instruction!");
        }
        switch ($SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlOpCode()[instruction.getOpCode().ordinal()]) {
            case 2:
                return new CilNopInstruction(instruction, dotnetBody, cilBlock);
            case 3:
            case 13:
                return new CilCallVirtInstruction(instruction, dotnetBody, cilBlock);
            case 4:
                return new CilLeaveInstruction(instruction, dotnetBody, cilBlock);
            case 5:
                return new CilLdStrInstruction(instruction, dotnetBody, cilBlock);
            case 6:
                return new CilStObjInstruction(instruction, dotnetBody, cilBlock);
            case 7:
                return new CilLdFldaInstruction(instruction, dotnetBody, cilBlock);
            case 8:
                return new CilLdcI4Instruction(instruction, dotnetBody, cilBlock);
            case 9:
            case 30:
                return new CilLdLocInstruction(instruction, dotnetBody, cilBlock);
            case 10:
                return fromInstructionMsg(instruction.getTarget(), dotnetBody, cilBlock);
            case 11:
                return new CilStLocInstruction(instruction, dotnetBody, cilBlock);
            case 12:
                return new CilNewObjInstruction(instruction, dotnetBody, cilBlock);
            case 14:
                return new CilBinaryNumericInstruction(instruction, dotnetBody, cilBlock);
            case 15:
                return new CilBranchInstruction(instruction, dotnetBody, cilBlock);
            case 16:
                return new CilCompInstruction(instruction, dotnetBody, cilBlock);
            case 17:
                return new CilIfInstruction(instruction, dotnetBody, cilBlock);
            case 18:
                return new CilLdsFldaInstruction(instruction, dotnetBody, cilBlock);
            case 19:
                return new CilLdNullInstruction(instruction, dotnetBody, cilBlock);
            case 20:
                return new CilLdLenInstruction(instruction, dotnetBody, cilBlock);
            case 21:
                return new CilConvInstruction(instruction, dotnetBody, cilBlock);
            case 22:
                return new CilNewArrInstruction(instruction, dotnetBody, cilBlock);
            case 23:
                return new CilLdElemaInstruction(instruction, dotnetBody, cilBlock);
            case 24:
            case 26:
            case 27:
            case 28:
                return new CilCastClassUnBoxInstruction(instruction, dotnetBody, cilBlock);
            case 25:
                return new CilIsInstInstruction(instruction, dotnetBody, cilBlock);
            case 29:
                return new CilTryCatchInstruction(instruction, dotnetBody, cilBlock);
            case 31:
                return new CilDefaultValueInstruction(instruction, dotnetBody, cilBlock);
            case 32:
                return new CilNotInstruction(instruction, dotnetBody, cilBlock);
            case 33:
                return new CilTryFinallyInstruction(instruction, dotnetBody, cilBlock);
            case 34:
                return new CilTryFaultInstruction(instruction, dotnetBody, cilBlock);
            case 35:
            case 36:
            case 37:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 50:
            case 52:
            case 53:
            case 57:
            case 58:
            case 62:
            case 64:
            case 65:
            case 66:
            default:
                throw new IllegalArgumentException("Opcode " + instruction.getOpCode().name() + " is not implemented!");
            case 38:
                return new CilRethrowInstruction(instruction, dotnetBody, cilBlock);
            case 39:
                return new CilThrowInstruction(instruction, dotnetBody, cilBlock);
            case 40:
                return new CilDebugBreakInstruction(instruction, dotnetBody, cilBlock);
            case 41:
                return new CilCkFiniteInstruction(instruction, dotnetBody, cilBlock);
            case 47:
                return new CilLdcI8Instruction(instruction, dotnetBody, cilBlock);
            case 48:
                return new CilLdcR4Instruction(instruction, dotnetBody, cilBlock);
            case 49:
                return new CilLdcR8Instruction(instruction, dotnetBody, cilBlock);
            case 51:
            case 54:
                return new CilLdFtnInstruction(instruction, dotnetBody, cilBlock);
            case 55:
                return new CilLocAllocInstruction(instruction, dotnetBody, cilBlock);
            case 56:
            case 60:
                return new CilRefAnyInstruction(instruction, dotnetBody, cilBlock);
            case 59:
                return new CilRefTypeInstruction(instruction, dotnetBody, cilBlock);
            case 61:
                return new CilSizeOfInstruction(instruction, dotnetBody, cilBlock);
            case 63:
                return new CilSwitchInstruction(instruction, dotnetBody, cilBlock);
            case 67:
                return new CilLdMemberTokenInstruction(instruction, dotnetBody, cilBlock);
            case 68:
                return new CilLdTypeTokenInstruction(instruction, dotnetBody, cilBlock);
        }
    }
}
