package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.List;
import soot.Body;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.dotnet.exceptions.NoExpressionInstructionException;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.TableSwitchStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilSwitchInstruction.class */
public class CilSwitchInstruction extends AbstractCilnstruction {
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

    public CilSwitchInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction.getKeyInstr(), this.dotnetBody, this.cilBlock);
        Value key = cilExpr.jimplifyExpr(jb);
        int lowIndex = (int) this.instruction.getSwitchSections(0).getLabel();
        int highIndex = (int) this.instruction.getSwitchSections(this.instruction.getSwitchSectionsCount() - 1).getLabel();
        Unit defaultInstruct = Jimple.v().newNopStmt();
        switch ($SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlOpCode()[this.instruction.getDefaultInst().getOpCode().ordinal()]) {
            case 4:
                if (this.cilBlock.getDeclaredBlockContainer().isChildBlockContainer() && !this.instruction.getDefaultInst().getTargetLabel().equals("IL_0000")) {
                    defaultInstruct = this.cilBlock.getDeclaredBlockContainer().getSkipBlockContainerStmt();
                    break;
                } else {
                    this.dotnetBody.blockEntryPointsManager.gotoTargetsInBody.put(defaultInstruct, "RETURNLEAVE");
                    break;
                }
            case 15:
                this.cilBlock.getDeclaredBlockContainer().blockEntryPointsManager.gotoTargetsInBody.put(defaultInstruct, this.instruction.getDefaultInst().getTargetLabel());
                break;
            default:
                throw new RuntimeException("CilSwitchInstruction: Opcode " + this.instruction.getDefaultInst().getOpCode().name() + " not implemented!");
        }
        List<Unit> targets = new ArrayList<>();
        for (ProtoIlInstructions.IlSwitchSectionMsg section : this.instruction.getSwitchSectionsList()) {
            NopStmt nopStmt = Jimple.v().newNopStmt();
            switch ($SWITCH_TABLE$soot$dotnet$proto$ProtoIlInstructions$IlInstructionMsg$IlOpCode()[section.getTargetInstr().getOpCode().ordinal()]) {
                case 4:
                    if (this.cilBlock.getDeclaredBlockContainer().isChildBlockContainer() && !section.getTargetInstr().getTargetLabel().equals("IL_0000")) {
                        targets.add(this.cilBlock.getDeclaredBlockContainer().getSkipBlockContainerStmt());
                        break;
                    } else {
                        targets.add(nopStmt);
                        this.dotnetBody.blockEntryPointsManager.gotoTargetsInBody.put(nopStmt, "RETURNLEAVE");
                        break;
                    }
                case 15:
                    targets.add(nopStmt);
                    this.cilBlock.getDeclaredBlockContainer().blockEntryPointsManager.gotoTargetsInBody.put(nopStmt, section.getTargetInstr().getTargetLabel());
                    break;
                default:
                    throw new RuntimeException("CilSwitchInstruction: Opcode " + section.getTargetInstr().getOpCode().name() + " not implemented!");
            }
        }
        TableSwitchStmt tableSwitchStmt = Jimple.v().newTableSwitchStmt(key, lowIndex, highIndex, targets, defaultInstruct);
        jb.getUnits().add((UnitPatchingChain) tableSwitchStmt);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        throw new NoExpressionInstructionException(this.instruction);
    }
}
