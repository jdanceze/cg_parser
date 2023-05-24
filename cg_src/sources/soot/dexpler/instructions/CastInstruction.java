package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.ShortType;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.dexpler.tags.IntOpTag;
import soot.dexpler.tags.LongOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/CastInstruction.class */
public class CastInstruction extends TaggedInstruction {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$org$jf$dexlib2$Opcode;

    static /* synthetic */ int[] $SWITCH_TABLE$org$jf$dexlib2$Opcode() {
        int[] iArr = $SWITCH_TABLE$org$jf$dexlib2$Opcode;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[Opcode.values().length];
        try {
            iArr2[Opcode.ADD_DOUBLE.ordinal()] = 163;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[Opcode.ADD_DOUBLE_2ADDR.ordinal()] = 195;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[Opcode.ADD_FLOAT.ordinal()] = 158;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[Opcode.ADD_FLOAT_2ADDR.ordinal()] = 190;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[Opcode.ADD_INT.ordinal()] = 136;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[Opcode.ADD_INT_2ADDR.ordinal()] = 168;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[Opcode.ADD_INT_LIT16.ordinal()] = 200;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[Opcode.ADD_INT_LIT8.ordinal()] = 208;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[Opcode.ADD_LONG.ordinal()] = 147;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[Opcode.ADD_LONG_2ADDR.ordinal()] = 179;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[Opcode.AGET.ordinal()] = 63;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            iArr2[Opcode.AGET_BOOLEAN.ordinal()] = 66;
        } catch (NoSuchFieldError unused12) {
        }
        try {
            iArr2[Opcode.AGET_BYTE.ordinal()] = 67;
        } catch (NoSuchFieldError unused13) {
        }
        try {
            iArr2[Opcode.AGET_CHAR.ordinal()] = 68;
        } catch (NoSuchFieldError unused14) {
        }
        try {
            iArr2[Opcode.AGET_OBJECT.ordinal()] = 65;
        } catch (NoSuchFieldError unused15) {
        }
        try {
            iArr2[Opcode.AGET_SHORT.ordinal()] = 69;
        } catch (NoSuchFieldError unused16) {
        }
        try {
            iArr2[Opcode.AGET_WIDE.ordinal()] = 64;
        } catch (NoSuchFieldError unused17) {
        }
        try {
            iArr2[Opcode.AND_INT.ordinal()] = 141;
        } catch (NoSuchFieldError unused18) {
        }
        try {
            iArr2[Opcode.AND_INT_2ADDR.ordinal()] = 173;
        } catch (NoSuchFieldError unused19) {
        }
        try {
            iArr2[Opcode.AND_INT_LIT16.ordinal()] = 205;
        } catch (NoSuchFieldError unused20) {
        }
        try {
            iArr2[Opcode.AND_INT_LIT8.ordinal()] = 213;
        } catch (NoSuchFieldError unused21) {
        }
        try {
            iArr2[Opcode.AND_LONG.ordinal()] = 152;
        } catch (NoSuchFieldError unused22) {
        }
        try {
            iArr2[Opcode.AND_LONG_2ADDR.ordinal()] = 184;
        } catch (NoSuchFieldError unused23) {
        }
        try {
            iArr2[Opcode.APUT.ordinal()] = 70;
        } catch (NoSuchFieldError unused24) {
        }
        try {
            iArr2[Opcode.APUT_BOOLEAN.ordinal()] = 73;
        } catch (NoSuchFieldError unused25) {
        }
        try {
            iArr2[Opcode.APUT_BYTE.ordinal()] = 74;
        } catch (NoSuchFieldError unused26) {
        }
        try {
            iArr2[Opcode.APUT_CHAR.ordinal()] = 75;
        } catch (NoSuchFieldError unused27) {
        }
        try {
            iArr2[Opcode.APUT_OBJECT.ordinal()] = 72;
        } catch (NoSuchFieldError unused28) {
        }
        try {
            iArr2[Opcode.APUT_SHORT.ordinal()] = 76;
        } catch (NoSuchFieldError unused29) {
        }
        try {
            iArr2[Opcode.APUT_WIDE.ordinal()] = 71;
        } catch (NoSuchFieldError unused30) {
        }
        try {
            iArr2[Opcode.ARRAY_LENGTH.ordinal()] = 34;
        } catch (NoSuchFieldError unused31) {
        }
        try {
            iArr2[Opcode.ARRAY_PAYLOAD.ordinal()] = 258;
        } catch (NoSuchFieldError unused32) {
        }
        try {
            iArr2[Opcode.CHECK_CAST.ordinal()] = 32;
        } catch (NoSuchFieldError unused33) {
        }
        try {
            iArr2[Opcode.CMPG_DOUBLE.ordinal()] = 49;
        } catch (NoSuchFieldError unused34) {
        }
        try {
            iArr2[Opcode.CMPG_FLOAT.ordinal()] = 47;
        } catch (NoSuchFieldError unused35) {
        }
        try {
            iArr2[Opcode.CMPL_DOUBLE.ordinal()] = 48;
        } catch (NoSuchFieldError unused36) {
        }
        try {
            iArr2[Opcode.CMPL_FLOAT.ordinal()] = 46;
        } catch (NoSuchFieldError unused37) {
        }
        try {
            iArr2[Opcode.CMP_LONG.ordinal()] = 50;
        } catch (NoSuchFieldError unused38) {
        }
        try {
            iArr2[Opcode.CONST.ordinal()] = 21;
        } catch (NoSuchFieldError unused39) {
        }
        try {
            iArr2[Opcode.CONST_16.ordinal()] = 20;
        } catch (NoSuchFieldError unused40) {
        }
        try {
            iArr2[Opcode.CONST_4.ordinal()] = 19;
        } catch (NoSuchFieldError unused41) {
        }
        try {
            iArr2[Opcode.CONST_CLASS.ordinal()] = 29;
        } catch (NoSuchFieldError unused42) {
        }
        try {
            iArr2[Opcode.CONST_HIGH16.ordinal()] = 22;
        } catch (NoSuchFieldError unused43) {
        }
        try {
            iArr2[Opcode.CONST_METHOD_HANDLE.ordinal()] = 263;
        } catch (NoSuchFieldError unused44) {
        }
        try {
            iArr2[Opcode.CONST_METHOD_TYPE.ordinal()] = 264;
        } catch (NoSuchFieldError unused45) {
        }
        try {
            iArr2[Opcode.CONST_STRING.ordinal()] = 27;
        } catch (NoSuchFieldError unused46) {
        }
        try {
            iArr2[Opcode.CONST_STRING_JUMBO.ordinal()] = 28;
        } catch (NoSuchFieldError unused47) {
        }
        try {
            iArr2[Opcode.CONST_WIDE.ordinal()] = 25;
        } catch (NoSuchFieldError unused48) {
        }
        try {
            iArr2[Opcode.CONST_WIDE_16.ordinal()] = 23;
        } catch (NoSuchFieldError unused49) {
        }
        try {
            iArr2[Opcode.CONST_WIDE_32.ordinal()] = 24;
        } catch (NoSuchFieldError unused50) {
        }
        try {
            iArr2[Opcode.CONST_WIDE_HIGH16.ordinal()] = 26;
        } catch (NoSuchFieldError unused51) {
        }
        try {
            iArr2[Opcode.DIV_DOUBLE.ordinal()] = 166;
        } catch (NoSuchFieldError unused52) {
        }
        try {
            iArr2[Opcode.DIV_DOUBLE_2ADDR.ordinal()] = 198;
        } catch (NoSuchFieldError unused53) {
        }
        try {
            iArr2[Opcode.DIV_FLOAT.ordinal()] = 161;
        } catch (NoSuchFieldError unused54) {
        }
        try {
            iArr2[Opcode.DIV_FLOAT_2ADDR.ordinal()] = 193;
        } catch (NoSuchFieldError unused55) {
        }
        try {
            iArr2[Opcode.DIV_INT.ordinal()] = 139;
        } catch (NoSuchFieldError unused56) {
        }
        try {
            iArr2[Opcode.DIV_INT_2ADDR.ordinal()] = 171;
        } catch (NoSuchFieldError unused57) {
        }
        try {
            iArr2[Opcode.DIV_INT_LIT16.ordinal()] = 203;
        } catch (NoSuchFieldError unused58) {
        }
        try {
            iArr2[Opcode.DIV_INT_LIT8.ordinal()] = 211;
        } catch (NoSuchFieldError unused59) {
        }
        try {
            iArr2[Opcode.DIV_LONG.ordinal()] = 150;
        } catch (NoSuchFieldError unused60) {
        }
        try {
            iArr2[Opcode.DIV_LONG_2ADDR.ordinal()] = 182;
        } catch (NoSuchFieldError unused61) {
        }
        try {
            iArr2[Opcode.DOUBLE_TO_FLOAT.ordinal()] = 132;
        } catch (NoSuchFieldError unused62) {
        }
        try {
            iArr2[Opcode.DOUBLE_TO_INT.ordinal()] = 130;
        } catch (NoSuchFieldError unused63) {
        }
        try {
            iArr2[Opcode.DOUBLE_TO_LONG.ordinal()] = 131;
        } catch (NoSuchFieldError unused64) {
        }
        try {
            iArr2[Opcode.EXECUTE_INLINE.ordinal()] = 229;
        } catch (NoSuchFieldError unused65) {
        }
        try {
            iArr2[Opcode.EXECUTE_INLINE_RANGE.ordinal()] = 230;
        } catch (NoSuchFieldError unused66) {
        }
        try {
            iArr2[Opcode.FILLED_NEW_ARRAY.ordinal()] = 37;
        } catch (NoSuchFieldError unused67) {
        }
        try {
            iArr2[Opcode.FILLED_NEW_ARRAY_RANGE.ordinal()] = 38;
        } catch (NoSuchFieldError unused68) {
        }
        try {
            iArr2[Opcode.FILL_ARRAY_DATA.ordinal()] = 39;
        } catch (NoSuchFieldError unused69) {
        }
        try {
            iArr2[Opcode.FLOAT_TO_DOUBLE.ordinal()] = 129;
        } catch (NoSuchFieldError unused70) {
        }
        try {
            iArr2[Opcode.FLOAT_TO_INT.ordinal()] = 127;
        } catch (NoSuchFieldError unused71) {
        }
        try {
            iArr2[Opcode.FLOAT_TO_LONG.ordinal()] = 128;
        } catch (NoSuchFieldError unused72) {
        }
        try {
            iArr2[Opcode.GOTO.ordinal()] = 41;
        } catch (NoSuchFieldError unused73) {
        }
        try {
            iArr2[Opcode.GOTO_16.ordinal()] = 42;
        } catch (NoSuchFieldError unused74) {
        }
        try {
            iArr2[Opcode.GOTO_32.ordinal()] = 43;
        } catch (NoSuchFieldError unused75) {
        }
        try {
            iArr2[Opcode.IF_EQ.ordinal()] = 51;
        } catch (NoSuchFieldError unused76) {
        }
        try {
            iArr2[Opcode.IF_EQZ.ordinal()] = 57;
        } catch (NoSuchFieldError unused77) {
        }
        try {
            iArr2[Opcode.IF_GE.ordinal()] = 54;
        } catch (NoSuchFieldError unused78) {
        }
        try {
            iArr2[Opcode.IF_GEZ.ordinal()] = 60;
        } catch (NoSuchFieldError unused79) {
        }
        try {
            iArr2[Opcode.IF_GT.ordinal()] = 55;
        } catch (NoSuchFieldError unused80) {
        }
        try {
            iArr2[Opcode.IF_GTZ.ordinal()] = 61;
        } catch (NoSuchFieldError unused81) {
        }
        try {
            iArr2[Opcode.IF_LE.ordinal()] = 56;
        } catch (NoSuchFieldError unused82) {
        }
        try {
            iArr2[Opcode.IF_LEZ.ordinal()] = 62;
        } catch (NoSuchFieldError unused83) {
        }
        try {
            iArr2[Opcode.IF_LT.ordinal()] = 53;
        } catch (NoSuchFieldError unused84) {
        }
        try {
            iArr2[Opcode.IF_LTZ.ordinal()] = 59;
        } catch (NoSuchFieldError unused85) {
        }
        try {
            iArr2[Opcode.IF_NE.ordinal()] = 52;
        } catch (NoSuchFieldError unused86) {
        }
        try {
            iArr2[Opcode.IF_NEZ.ordinal()] = 58;
        } catch (NoSuchFieldError unused87) {
        }
        try {
            iArr2[Opcode.IGET.ordinal()] = 77;
        } catch (NoSuchFieldError unused88) {
        }
        try {
            iArr2[Opcode.IGET_BOOLEAN.ordinal()] = 80;
        } catch (NoSuchFieldError unused89) {
        }
        try {
            iArr2[Opcode.IGET_BOOLEAN_QUICK.ordinal()] = 245;
        } catch (NoSuchFieldError unused90) {
        }
        try {
            iArr2[Opcode.IGET_BYTE.ordinal()] = 81;
        } catch (NoSuchFieldError unused91) {
        }
        try {
            iArr2[Opcode.IGET_BYTE_QUICK.ordinal()] = 246;
        } catch (NoSuchFieldError unused92) {
        }
        try {
            iArr2[Opcode.IGET_CHAR.ordinal()] = 82;
        } catch (NoSuchFieldError unused93) {
        }
        try {
            iArr2[Opcode.IGET_CHAR_QUICK.ordinal()] = 247;
        } catch (NoSuchFieldError unused94) {
        }
        try {
            iArr2[Opcode.IGET_OBJECT.ordinal()] = 79;
        } catch (NoSuchFieldError unused95) {
        }
        try {
            iArr2[Opcode.IGET_OBJECT_QUICK.ordinal()] = 237;
        } catch (NoSuchFieldError unused96) {
        }
        try {
            iArr2[Opcode.IGET_OBJECT_VOLATILE.ordinal()] = 223;
        } catch (NoSuchFieldError unused97) {
        }
        try {
            iArr2[Opcode.IGET_QUICK.ordinal()] = 235;
        } catch (NoSuchFieldError unused98) {
        }
        try {
            iArr2[Opcode.IGET_SHORT.ordinal()] = 83;
        } catch (NoSuchFieldError unused99) {
        }
        try {
            iArr2[Opcode.IGET_SHORT_QUICK.ordinal()] = 248;
        } catch (NoSuchFieldError unused100) {
        }
        try {
            iArr2[Opcode.IGET_VOLATILE.ordinal()] = 219;
        } catch (NoSuchFieldError unused101) {
        }
        try {
            iArr2[Opcode.IGET_WIDE.ordinal()] = 78;
        } catch (NoSuchFieldError unused102) {
        }
        try {
            iArr2[Opcode.IGET_WIDE_QUICK.ordinal()] = 236;
        } catch (NoSuchFieldError unused103) {
        }
        try {
            iArr2[Opcode.IGET_WIDE_VOLATILE.ordinal()] = 224;
        } catch (NoSuchFieldError unused104) {
        }
        try {
            iArr2[Opcode.INSTANCE_OF.ordinal()] = 33;
        } catch (NoSuchFieldError unused105) {
        }
        try {
            iArr2[Opcode.INT_TO_BYTE.ordinal()] = 133;
        } catch (NoSuchFieldError unused106) {
        }
        try {
            iArr2[Opcode.INT_TO_CHAR.ordinal()] = 134;
        } catch (NoSuchFieldError unused107) {
        }
        try {
            iArr2[Opcode.INT_TO_DOUBLE.ordinal()] = 123;
        } catch (NoSuchFieldError unused108) {
        }
        try {
            iArr2[Opcode.INT_TO_FLOAT.ordinal()] = 122;
        } catch (NoSuchFieldError unused109) {
        }
        try {
            iArr2[Opcode.INT_TO_LONG.ordinal()] = 121;
        } catch (NoSuchFieldError unused110) {
        }
        try {
            iArr2[Opcode.INT_TO_SHORT.ordinal()] = 135;
        } catch (NoSuchFieldError unused111) {
        }
        try {
            iArr2[Opcode.INVOKE_CUSTOM.ordinal()] = 261;
        } catch (NoSuchFieldError unused112) {
        }
        try {
            iArr2[Opcode.INVOKE_CUSTOM_RANGE.ordinal()] = 262;
        } catch (NoSuchFieldError unused113) {
        }
        try {
            iArr2[Opcode.INVOKE_DIRECT.ordinal()] = 107;
        } catch (NoSuchFieldError unused114) {
        }
        try {
            iArr2[Opcode.INVOKE_DIRECT_EMPTY.ordinal()] = 231;
        } catch (NoSuchFieldError unused115) {
        }
        try {
            iArr2[Opcode.INVOKE_DIRECT_RANGE.ordinal()] = 112;
        } catch (NoSuchFieldError unused116) {
        }
        try {
            iArr2[Opcode.INVOKE_INTERFACE.ordinal()] = 109;
        } catch (NoSuchFieldError unused117) {
        }
        try {
            iArr2[Opcode.INVOKE_INTERFACE_RANGE.ordinal()] = 114;
        } catch (NoSuchFieldError unused118) {
        }
        try {
            iArr2[Opcode.INVOKE_OBJECT_INIT_RANGE.ordinal()] = 232;
        } catch (NoSuchFieldError unused119) {
        }
        try {
            iArr2[Opcode.INVOKE_POLYMORPHIC.ordinal()] = 259;
        } catch (NoSuchFieldError unused120) {
        }
        try {
            iArr2[Opcode.INVOKE_POLYMORPHIC_RANGE.ordinal()] = 260;
        } catch (NoSuchFieldError unused121) {
        }
        try {
            iArr2[Opcode.INVOKE_STATIC.ordinal()] = 108;
        } catch (NoSuchFieldError unused122) {
        }
        try {
            iArr2[Opcode.INVOKE_STATIC_RANGE.ordinal()] = 113;
        } catch (NoSuchFieldError unused123) {
        }
        try {
            iArr2[Opcode.INVOKE_SUPER.ordinal()] = 106;
        } catch (NoSuchFieldError unused124) {
        }
        try {
            iArr2[Opcode.INVOKE_SUPER_QUICK.ordinal()] = 251;
        } catch (NoSuchFieldError unused125) {
        }
        try {
            iArr2[Opcode.INVOKE_SUPER_QUICK_RANGE.ordinal()] = 252;
        } catch (NoSuchFieldError unused126) {
        }
        try {
            iArr2[Opcode.INVOKE_SUPER_RANGE.ordinal()] = 111;
        } catch (NoSuchFieldError unused127) {
        }
        try {
            iArr2[Opcode.INVOKE_VIRTUAL.ordinal()] = 105;
        } catch (NoSuchFieldError unused128) {
        }
        try {
            iArr2[Opcode.INVOKE_VIRTUAL_QUICK.ordinal()] = 249;
        } catch (NoSuchFieldError unused129) {
        }
        try {
            iArr2[Opcode.INVOKE_VIRTUAL_QUICK_RANGE.ordinal()] = 250;
        } catch (NoSuchFieldError unused130) {
        }
        try {
            iArr2[Opcode.INVOKE_VIRTUAL_RANGE.ordinal()] = 110;
        } catch (NoSuchFieldError unused131) {
        }
        try {
            iArr2[Opcode.IPUT.ordinal()] = 84;
        } catch (NoSuchFieldError unused132) {
        }
        try {
            iArr2[Opcode.IPUT_BOOLEAN.ordinal()] = 87;
        } catch (NoSuchFieldError unused133) {
        }
        try {
            iArr2[Opcode.IPUT_BOOLEAN_QUICK.ordinal()] = 241;
        } catch (NoSuchFieldError unused134) {
        }
        try {
            iArr2[Opcode.IPUT_BYTE.ordinal()] = 88;
        } catch (NoSuchFieldError unused135) {
        }
        try {
            iArr2[Opcode.IPUT_BYTE_QUICK.ordinal()] = 242;
        } catch (NoSuchFieldError unused136) {
        }
        try {
            iArr2[Opcode.IPUT_CHAR.ordinal()] = 89;
        } catch (NoSuchFieldError unused137) {
        }
        try {
            iArr2[Opcode.IPUT_CHAR_QUICK.ordinal()] = 243;
        } catch (NoSuchFieldError unused138) {
        }
        try {
            iArr2[Opcode.IPUT_OBJECT.ordinal()] = 86;
        } catch (NoSuchFieldError unused139) {
        }
        try {
            iArr2[Opcode.IPUT_OBJECT_QUICK.ordinal()] = 240;
        } catch (NoSuchFieldError unused140) {
        }
        try {
            iArr2[Opcode.IPUT_OBJECT_VOLATILE.ordinal()] = 253;
        } catch (NoSuchFieldError unused141) {
        }
        try {
            iArr2[Opcode.IPUT_QUICK.ordinal()] = 238;
        } catch (NoSuchFieldError unused142) {
        }
        try {
            iArr2[Opcode.IPUT_SHORT.ordinal()] = 90;
        } catch (NoSuchFieldError unused143) {
        }
        try {
            iArr2[Opcode.IPUT_SHORT_QUICK.ordinal()] = 244;
        } catch (NoSuchFieldError unused144) {
        }
        try {
            iArr2[Opcode.IPUT_VOLATILE.ordinal()] = 220;
        } catch (NoSuchFieldError unused145) {
        }
        try {
            iArr2[Opcode.IPUT_WIDE.ordinal()] = 85;
        } catch (NoSuchFieldError unused146) {
        }
        try {
            iArr2[Opcode.IPUT_WIDE_QUICK.ordinal()] = 239;
        } catch (NoSuchFieldError unused147) {
        }
        try {
            iArr2[Opcode.IPUT_WIDE_VOLATILE.ordinal()] = 225;
        } catch (NoSuchFieldError unused148) {
        }
        try {
            iArr2[Opcode.LONG_TO_DOUBLE.ordinal()] = 126;
        } catch (NoSuchFieldError unused149) {
        }
        try {
            iArr2[Opcode.LONG_TO_FLOAT.ordinal()] = 125;
        } catch (NoSuchFieldError unused150) {
        }
        try {
            iArr2[Opcode.LONG_TO_INT.ordinal()] = 124;
        } catch (NoSuchFieldError unused151) {
        }
        try {
            iArr2[Opcode.MONITOR_ENTER.ordinal()] = 30;
        } catch (NoSuchFieldError unused152) {
        }
        try {
            iArr2[Opcode.MONITOR_EXIT.ordinal()] = 31;
        } catch (NoSuchFieldError unused153) {
        }
        try {
            iArr2[Opcode.MOVE.ordinal()] = 2;
        } catch (NoSuchFieldError unused154) {
        }
        try {
            iArr2[Opcode.MOVE_16.ordinal()] = 4;
        } catch (NoSuchFieldError unused155) {
        }
        try {
            iArr2[Opcode.MOVE_EXCEPTION.ordinal()] = 14;
        } catch (NoSuchFieldError unused156) {
        }
        try {
            iArr2[Opcode.MOVE_FROM16.ordinal()] = 3;
        } catch (NoSuchFieldError unused157) {
        }
        try {
            iArr2[Opcode.MOVE_OBJECT.ordinal()] = 8;
        } catch (NoSuchFieldError unused158) {
        }
        try {
            iArr2[Opcode.MOVE_OBJECT_16.ordinal()] = 10;
        } catch (NoSuchFieldError unused159) {
        }
        try {
            iArr2[Opcode.MOVE_OBJECT_FROM16.ordinal()] = 9;
        } catch (NoSuchFieldError unused160) {
        }
        try {
            iArr2[Opcode.MOVE_RESULT.ordinal()] = 11;
        } catch (NoSuchFieldError unused161) {
        }
        try {
            iArr2[Opcode.MOVE_RESULT_OBJECT.ordinal()] = 13;
        } catch (NoSuchFieldError unused162) {
        }
        try {
            iArr2[Opcode.MOVE_RESULT_WIDE.ordinal()] = 12;
        } catch (NoSuchFieldError unused163) {
        }
        try {
            iArr2[Opcode.MOVE_WIDE.ordinal()] = 5;
        } catch (NoSuchFieldError unused164) {
        }
        try {
            iArr2[Opcode.MOVE_WIDE_16.ordinal()] = 7;
        } catch (NoSuchFieldError unused165) {
        }
        try {
            iArr2[Opcode.MOVE_WIDE_FROM16.ordinal()] = 6;
        } catch (NoSuchFieldError unused166) {
        }
        try {
            iArr2[Opcode.MUL_DOUBLE.ordinal()] = 165;
        } catch (NoSuchFieldError unused167) {
        }
        try {
            iArr2[Opcode.MUL_DOUBLE_2ADDR.ordinal()] = 197;
        } catch (NoSuchFieldError unused168) {
        }
        try {
            iArr2[Opcode.MUL_FLOAT.ordinal()] = 160;
        } catch (NoSuchFieldError unused169) {
        }
        try {
            iArr2[Opcode.MUL_FLOAT_2ADDR.ordinal()] = 192;
        } catch (NoSuchFieldError unused170) {
        }
        try {
            iArr2[Opcode.MUL_INT.ordinal()] = 138;
        } catch (NoSuchFieldError unused171) {
        }
        try {
            iArr2[Opcode.MUL_INT_2ADDR.ordinal()] = 170;
        } catch (NoSuchFieldError unused172) {
        }
        try {
            iArr2[Opcode.MUL_INT_LIT16.ordinal()] = 202;
        } catch (NoSuchFieldError unused173) {
        }
        try {
            iArr2[Opcode.MUL_INT_LIT8.ordinal()] = 210;
        } catch (NoSuchFieldError unused174) {
        }
        try {
            iArr2[Opcode.MUL_LONG.ordinal()] = 149;
        } catch (NoSuchFieldError unused175) {
        }
        try {
            iArr2[Opcode.MUL_LONG_2ADDR.ordinal()] = 181;
        } catch (NoSuchFieldError unused176) {
        }
        try {
            iArr2[Opcode.NEG_DOUBLE.ordinal()] = 120;
        } catch (NoSuchFieldError unused177) {
        }
        try {
            iArr2[Opcode.NEG_FLOAT.ordinal()] = 119;
        } catch (NoSuchFieldError unused178) {
        }
        try {
            iArr2[Opcode.NEG_INT.ordinal()] = 115;
        } catch (NoSuchFieldError unused179) {
        }
        try {
            iArr2[Opcode.NEG_LONG.ordinal()] = 117;
        } catch (NoSuchFieldError unused180) {
        }
        try {
            iArr2[Opcode.NEW_ARRAY.ordinal()] = 36;
        } catch (NoSuchFieldError unused181) {
        }
        try {
            iArr2[Opcode.NEW_INSTANCE.ordinal()] = 35;
        } catch (NoSuchFieldError unused182) {
        }
        try {
            iArr2[Opcode.NOP.ordinal()] = 1;
        } catch (NoSuchFieldError unused183) {
        }
        try {
            iArr2[Opcode.NOT_INT.ordinal()] = 116;
        } catch (NoSuchFieldError unused184) {
        }
        try {
            iArr2[Opcode.NOT_LONG.ordinal()] = 118;
        } catch (NoSuchFieldError unused185) {
        }
        try {
            iArr2[Opcode.OR_INT.ordinal()] = 142;
        } catch (NoSuchFieldError unused186) {
        }
        try {
            iArr2[Opcode.OR_INT_2ADDR.ordinal()] = 174;
        } catch (NoSuchFieldError unused187) {
        }
        try {
            iArr2[Opcode.OR_INT_LIT16.ordinal()] = 206;
        } catch (NoSuchFieldError unused188) {
        }
        try {
            iArr2[Opcode.OR_INT_LIT8.ordinal()] = 214;
        } catch (NoSuchFieldError unused189) {
        }
        try {
            iArr2[Opcode.OR_LONG.ordinal()] = 153;
        } catch (NoSuchFieldError unused190) {
        }
        try {
            iArr2[Opcode.OR_LONG_2ADDR.ordinal()] = 185;
        } catch (NoSuchFieldError unused191) {
        }
        try {
            iArr2[Opcode.PACKED_SWITCH.ordinal()] = 44;
        } catch (NoSuchFieldError unused192) {
        }
        try {
            iArr2[Opcode.PACKED_SWITCH_PAYLOAD.ordinal()] = 256;
        } catch (NoSuchFieldError unused193) {
        }
        try {
            iArr2[Opcode.REM_DOUBLE.ordinal()] = 167;
        } catch (NoSuchFieldError unused194) {
        }
        try {
            iArr2[Opcode.REM_DOUBLE_2ADDR.ordinal()] = 199;
        } catch (NoSuchFieldError unused195) {
        }
        try {
            iArr2[Opcode.REM_FLOAT.ordinal()] = 162;
        } catch (NoSuchFieldError unused196) {
        }
        try {
            iArr2[Opcode.REM_FLOAT_2ADDR.ordinal()] = 194;
        } catch (NoSuchFieldError unused197) {
        }
        try {
            iArr2[Opcode.REM_INT.ordinal()] = 140;
        } catch (NoSuchFieldError unused198) {
        }
        try {
            iArr2[Opcode.REM_INT_2ADDR.ordinal()] = 172;
        } catch (NoSuchFieldError unused199) {
        }
        try {
            iArr2[Opcode.REM_INT_LIT16.ordinal()] = 204;
        } catch (NoSuchFieldError unused200) {
        }
        try {
            iArr2[Opcode.REM_INT_LIT8.ordinal()] = 212;
        } catch (NoSuchFieldError unused201) {
        }
        try {
            iArr2[Opcode.REM_LONG.ordinal()] = 151;
        } catch (NoSuchFieldError unused202) {
        }
        try {
            iArr2[Opcode.REM_LONG_2ADDR.ordinal()] = 183;
        } catch (NoSuchFieldError unused203) {
        }
        try {
            iArr2[Opcode.RETURN.ordinal()] = 16;
        } catch (NoSuchFieldError unused204) {
        }
        try {
            iArr2[Opcode.RETURN_OBJECT.ordinal()] = 18;
        } catch (NoSuchFieldError unused205) {
        }
        try {
            iArr2[Opcode.RETURN_VOID.ordinal()] = 15;
        } catch (NoSuchFieldError unused206) {
        }
        try {
            iArr2[Opcode.RETURN_VOID_BARRIER.ordinal()] = 233;
        } catch (NoSuchFieldError unused207) {
        }
        try {
            iArr2[Opcode.RETURN_VOID_NO_BARRIER.ordinal()] = 234;
        } catch (NoSuchFieldError unused208) {
        }
        try {
            iArr2[Opcode.RETURN_WIDE.ordinal()] = 17;
        } catch (NoSuchFieldError unused209) {
        }
        try {
            iArr2[Opcode.RSUB_INT.ordinal()] = 201;
        } catch (NoSuchFieldError unused210) {
        }
        try {
            iArr2[Opcode.RSUB_INT_LIT8.ordinal()] = 209;
        } catch (NoSuchFieldError unused211) {
        }
        try {
            iArr2[Opcode.SGET.ordinal()] = 91;
        } catch (NoSuchFieldError unused212) {
        }
        try {
            iArr2[Opcode.SGET_BOOLEAN.ordinal()] = 94;
        } catch (NoSuchFieldError unused213) {
        }
        try {
            iArr2[Opcode.SGET_BYTE.ordinal()] = 95;
        } catch (NoSuchFieldError unused214) {
        }
        try {
            iArr2[Opcode.SGET_CHAR.ordinal()] = 96;
        } catch (NoSuchFieldError unused215) {
        }
        try {
            iArr2[Opcode.SGET_OBJECT.ordinal()] = 93;
        } catch (NoSuchFieldError unused216) {
        }
        try {
            iArr2[Opcode.SGET_OBJECT_VOLATILE.ordinal()] = 254;
        } catch (NoSuchFieldError unused217) {
        }
        try {
            iArr2[Opcode.SGET_SHORT.ordinal()] = 97;
        } catch (NoSuchFieldError unused218) {
        }
        try {
            iArr2[Opcode.SGET_VOLATILE.ordinal()] = 221;
        } catch (NoSuchFieldError unused219) {
        }
        try {
            iArr2[Opcode.SGET_WIDE.ordinal()] = 92;
        } catch (NoSuchFieldError unused220) {
        }
        try {
            iArr2[Opcode.SGET_WIDE_VOLATILE.ordinal()] = 226;
        } catch (NoSuchFieldError unused221) {
        }
        try {
            iArr2[Opcode.SHL_INT.ordinal()] = 144;
        } catch (NoSuchFieldError unused222) {
        }
        try {
            iArr2[Opcode.SHL_INT_2ADDR.ordinal()] = 176;
        } catch (NoSuchFieldError unused223) {
        }
        try {
            iArr2[Opcode.SHL_INT_LIT8.ordinal()] = 216;
        } catch (NoSuchFieldError unused224) {
        }
        try {
            iArr2[Opcode.SHL_LONG.ordinal()] = 155;
        } catch (NoSuchFieldError unused225) {
        }
        try {
            iArr2[Opcode.SHL_LONG_2ADDR.ordinal()] = 187;
        } catch (NoSuchFieldError unused226) {
        }
        try {
            iArr2[Opcode.SHR_INT.ordinal()] = 145;
        } catch (NoSuchFieldError unused227) {
        }
        try {
            iArr2[Opcode.SHR_INT_2ADDR.ordinal()] = 177;
        } catch (NoSuchFieldError unused228) {
        }
        try {
            iArr2[Opcode.SHR_INT_LIT8.ordinal()] = 217;
        } catch (NoSuchFieldError unused229) {
        }
        try {
            iArr2[Opcode.SHR_LONG.ordinal()] = 156;
        } catch (NoSuchFieldError unused230) {
        }
        try {
            iArr2[Opcode.SHR_LONG_2ADDR.ordinal()] = 188;
        } catch (NoSuchFieldError unused231) {
        }
        try {
            iArr2[Opcode.SPARSE_SWITCH.ordinal()] = 45;
        } catch (NoSuchFieldError unused232) {
        }
        try {
            iArr2[Opcode.SPARSE_SWITCH_PAYLOAD.ordinal()] = 257;
        } catch (NoSuchFieldError unused233) {
        }
        try {
            iArr2[Opcode.SPUT.ordinal()] = 98;
        } catch (NoSuchFieldError unused234) {
        }
        try {
            iArr2[Opcode.SPUT_BOOLEAN.ordinal()] = 101;
        } catch (NoSuchFieldError unused235) {
        }
        try {
            iArr2[Opcode.SPUT_BYTE.ordinal()] = 102;
        } catch (NoSuchFieldError unused236) {
        }
        try {
            iArr2[Opcode.SPUT_CHAR.ordinal()] = 103;
        } catch (NoSuchFieldError unused237) {
        }
        try {
            iArr2[Opcode.SPUT_OBJECT.ordinal()] = 100;
        } catch (NoSuchFieldError unused238) {
        }
        try {
            iArr2[Opcode.SPUT_OBJECT_VOLATILE.ordinal()] = 255;
        } catch (NoSuchFieldError unused239) {
        }
        try {
            iArr2[Opcode.SPUT_SHORT.ordinal()] = 104;
        } catch (NoSuchFieldError unused240) {
        }
        try {
            iArr2[Opcode.SPUT_VOLATILE.ordinal()] = 222;
        } catch (NoSuchFieldError unused241) {
        }
        try {
            iArr2[Opcode.SPUT_WIDE.ordinal()] = 99;
        } catch (NoSuchFieldError unused242) {
        }
        try {
            iArr2[Opcode.SPUT_WIDE_VOLATILE.ordinal()] = 227;
        } catch (NoSuchFieldError unused243) {
        }
        try {
            iArr2[Opcode.SUB_DOUBLE.ordinal()] = 164;
        } catch (NoSuchFieldError unused244) {
        }
        try {
            iArr2[Opcode.SUB_DOUBLE_2ADDR.ordinal()] = 196;
        } catch (NoSuchFieldError unused245) {
        }
        try {
            iArr2[Opcode.SUB_FLOAT.ordinal()] = 159;
        } catch (NoSuchFieldError unused246) {
        }
        try {
            iArr2[Opcode.SUB_FLOAT_2ADDR.ordinal()] = 191;
        } catch (NoSuchFieldError unused247) {
        }
        try {
            iArr2[Opcode.SUB_INT.ordinal()] = 137;
        } catch (NoSuchFieldError unused248) {
        }
        try {
            iArr2[Opcode.SUB_INT_2ADDR.ordinal()] = 169;
        } catch (NoSuchFieldError unused249) {
        }
        try {
            iArr2[Opcode.SUB_LONG.ordinal()] = 148;
        } catch (NoSuchFieldError unused250) {
        }
        try {
            iArr2[Opcode.SUB_LONG_2ADDR.ordinal()] = 180;
        } catch (NoSuchFieldError unused251) {
        }
        try {
            iArr2[Opcode.THROW.ordinal()] = 40;
        } catch (NoSuchFieldError unused252) {
        }
        try {
            iArr2[Opcode.THROW_VERIFICATION_ERROR.ordinal()] = 228;
        } catch (NoSuchFieldError unused253) {
        }
        try {
            iArr2[Opcode.USHR_INT.ordinal()] = 146;
        } catch (NoSuchFieldError unused254) {
        }
        try {
            iArr2[Opcode.USHR_INT_2ADDR.ordinal()] = 178;
        } catch (NoSuchFieldError unused255) {
        }
        try {
            iArr2[Opcode.USHR_INT_LIT8.ordinal()] = 218;
        } catch (NoSuchFieldError unused256) {
        }
        try {
            iArr2[Opcode.USHR_LONG.ordinal()] = 157;
        } catch (NoSuchFieldError unused257) {
        }
        try {
            iArr2[Opcode.USHR_LONG_2ADDR.ordinal()] = 189;
        } catch (NoSuchFieldError unused258) {
        }
        try {
            iArr2[Opcode.XOR_INT.ordinal()] = 143;
        } catch (NoSuchFieldError unused259) {
        }
        try {
            iArr2[Opcode.XOR_INT_2ADDR.ordinal()] = 175;
        } catch (NoSuchFieldError unused260) {
        }
        try {
            iArr2[Opcode.XOR_INT_LIT16.ordinal()] = 207;
        } catch (NoSuchFieldError unused261) {
        }
        try {
            iArr2[Opcode.XOR_INT_LIT8.ordinal()] = 215;
        } catch (NoSuchFieldError unused262) {
        }
        try {
            iArr2[Opcode.XOR_LONG.ordinal()] = 154;
        } catch (NoSuchFieldError unused263) {
        }
        try {
            iArr2[Opcode.XOR_LONG_2ADDR.ordinal()] = 186;
        } catch (NoSuchFieldError unused264) {
        }
        $SWITCH_TABLE$org$jf$dexlib2$Opcode = iArr2;
        return iArr2;
    }

    public CastInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        int source = i.getRegisterB();
        Type targetType = getTargetType();
        CastExpr cast = Jimple.v().newCastExpr(body.getRegisterLocal(source), targetType);
        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), cast);
        assign.addTag(getTag());
        setUnit(assign);
        addTags(assign);
        body.add(assign);
    }

    private Type getTargetType() {
        Opcode opcode = this.instruction.getOpcode();
        switch ($SWITCH_TABLE$org$jf$dexlib2$Opcode()[opcode.ordinal()]) {
            case 121:
                setTag(new IntOpTag());
                return LongType.v();
            case 122:
                setTag(new IntOpTag());
                return FloatType.v();
            case 123:
                setTag(new IntOpTag());
                return DoubleType.v();
            case 124:
                setTag(new LongOpTag());
                return IntType.v();
            case 125:
                setTag(new LongOpTag());
                return FloatType.v();
            case 126:
                setTag(new LongOpTag());
                return DoubleType.v();
            case 127:
                setTag(new FloatOpTag());
                return IntType.v();
            case 128:
                setTag(new FloatOpTag());
                return LongType.v();
            case 129:
                setTag(new FloatOpTag());
                return DoubleType.v();
            case 130:
                setTag(new DoubleOpTag());
                return IntType.v();
            case 131:
                setTag(new DoubleOpTag());
                return LongType.v();
            case 132:
                setTag(new DoubleOpTag());
                return FloatType.v();
            case 133:
                setTag(new IntOpTag());
                return ByteType.v();
            case 134:
                setTag(new IntOpTag());
                return CharType.v();
            case 135:
                setTag(new IntOpTag());
                return ShortType.v();
            default:
                throw new RuntimeException("Invalid Opcode: " + opcode);
        }
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    boolean overridesRegister(int register) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }
}
