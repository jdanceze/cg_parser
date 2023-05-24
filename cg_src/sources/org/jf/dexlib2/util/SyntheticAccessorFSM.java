package org.jf.dexlib2.util;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/SyntheticAccessorFSM.class */
public class SyntheticAccessorFSM {
    private static final byte[] _SyntheticAccessorFSM_actions = init__SyntheticAccessorFSM_actions_0();
    private static final short[] _SyntheticAccessorFSM_key_offsets = init__SyntheticAccessorFSM_key_offsets_0();
    private static final short[] _SyntheticAccessorFSM_trans_keys = init__SyntheticAccessorFSM_trans_keys_0();
    private static final byte[] _SyntheticAccessorFSM_single_lengths = init__SyntheticAccessorFSM_single_lengths_0();
    private static final byte[] _SyntheticAccessorFSM_range_lengths = init__SyntheticAccessorFSM_range_lengths_0();
    private static final short[] _SyntheticAccessorFSM_index_offsets = init__SyntheticAccessorFSM_index_offsets_0();
    private static final byte[] _SyntheticAccessorFSM_indicies = init__SyntheticAccessorFSM_indicies_0();
    private static final byte[] _SyntheticAccessorFSM_trans_targs = init__SyntheticAccessorFSM_trans_targs_0();
    private static final byte[] _SyntheticAccessorFSM_trans_actions = init__SyntheticAccessorFSM_trans_actions_0();
    static final int SyntheticAccessorFSM_start = 1;
    static final int SyntheticAccessorFSM_first_final = 17;
    static final int SyntheticAccessorFSM_error = 0;
    static final int SyntheticAccessorFSM_en_main = 1;
    public static final int ADD = 7;
    public static final int SUB = 8;
    public static final int MUL = 9;
    public static final int DIV = 10;
    public static final int REM = 11;
    public static final int AND = 12;
    public static final int OR = 13;
    public static final int XOR = 14;
    public static final int SHL = 15;
    public static final int SHR = 16;
    public static final int USHR = 17;
    public static final int INT = 0;
    public static final int LONG = 1;
    public static final int FLOAT = 2;
    public static final int DOUBLE = 3;
    public static final int POSITIVE_ONE = 1;
    public static final int NEGATIVE_ONE = -1;
    public static final int OTHER = 0;
    @Nonnull
    private final Opcodes opcodes;

    private static byte[] init__SyntheticAccessorFSM_actions_0() {
        return new byte[]{0, 1, 0, 1, 1, 1, 2, 1, 13, 1, 14, 1, 15, 1, 16, 1, 17, 1, 18, 1, 19, 1, 20, 1, 21, 1, 25, 2, 3, 7, 2, 4, 7, 2, 5, 7, 2, 6, 7, 2, 8, 12, 2, 9, 12, 2, 10, 12, 2, 11, 12, 2, 22, 23, 2, 22, 24, 2, 22, 25, 2, 22, 26, 2, 22, 27, 2, 22, 28};
    }

    private static short[] init__SyntheticAccessorFSM_key_offsets_0() {
        return new short[]{0, 0, 12, 82, 98, 102, 104, 166, 172, 174, 180, 184, 190, 192, 196, 198, 201, 203};
    }

    private static short[] init__SyntheticAccessorFSM_trans_keys_0() {
        return new short[]{82, 88, 89, 95, 96, 102, 103, 109, 110, 114, 116, 120, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 177, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 201, 202, 203, 204, 206, 207, 208, 216, 15, 17, 18, 25, 129, 143, 144, 176, 178, 205, 144, 145, 155, 156, 166, 167, 171, 172, 176, 177, 187, 188, 198, 199, 203, 204, 89, 95, 103, 109, 15, 17, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 177, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 201, 202, 203, 204, 206, 207, 144, 176, 178, 205, 89, 95, 103, 109, 129, 143, 15, 17, 89, 95, 103, 109, 129, 143, 89, 95, 103, 109, 89, 95, 103, 109, 129, 143, 15, 17, 89, 95, 103, 109, 15, 17, 14, 10, 12, 15, 17, 0};
    }

    private static byte[] init__SyntheticAccessorFSM_single_lengths_0() {
        return new byte[]{0, 0, 60, 16, 0, 0, 58, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
    }

    private static byte[] init__SyntheticAccessorFSM_range_lengths_0() {
        return new byte[]{0, 6, 5, 0, 2, 1, 2, 3, 1, 3, 2, 3, 1, 2, 1, 1, 1, 0};
    }

    private static short[] init__SyntheticAccessorFSM_index_offsets_0() {
        return new short[]{0, 0, 7, 73, 90, 93, 95, 156, 160, 162, 166, 169, 173, 175, 178, 180, 183, 185};
    }

    private static byte[] init__SyntheticAccessorFSM_indicies_0() {
        return new byte[]{0, 2, 0, 2, 3, 3, 1, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 21, 9, 10, 11, 22, 23, 9, 10, 11, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 10, 11, 12, 13, 14, 15, 16, 17, 20, 21, 10, 11, 22, 23, 10, 11, 24, 24, 4, 5, 6, 7, 9, 1, 25, 26, 27, 28, 29, 30, 31, 32, 25, 26, 27, 28, 29, 30, 31, 32, 1, 33, 33, 1, 34, 1, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 21, 9, 10, 11, 22, 23, 9, 10, 11, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 10, 11, 12, 13, 14, 15, 16, 17, 20, 21, 10, 11, 22, 23, 10, 11, 7, 9, 1, 35, 35, 36, 1, 37, 1, 35, 35, 38, 1, 35, 35, 1, 39, 39, 40, 1, 41, 1, 39, 39, 1, 42, 1, 44, 43, 1, 45, 1, 1, 0};
    }

    private static byte[] init__SyntheticAccessorFSM_trans_targs_0() {
        return new byte[]{2, 0, 14, 15, 17, 3, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 11, 4, 4, 4, 4, 4, 4, 4, 4, 5, 17, 8, 9, 17, 10, 12, 13, 17, 17, 16, 17, 17};
    }

    private static byte[] init__SyntheticAccessorFSM_trans_actions_0() {
        return new byte[]{0, 0, 1, 0, 51, 3, 0, 27, 39, 7, 9, 11, 13, 15, 17, 19, 21, 23, 30, 42, 33, 45, 36, 48, 5, 27, 39, 30, 42, 33, 45, 36, 48, 1, 63, 1, 0, 66, 0, 1, 0, 60, 54, 0, 25, 57};
    }

    public SyntheticAccessorFSM(@Nonnull Opcodes opcodes) {
        this.opcodes = opcodes;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01cf  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x039e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x03a8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x03a2 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int test(java.util.List<? extends org.jf.dexlib2.iface.instruction.Instruction> r8) {
        /*
            Method dump skipped, instructions count: 957
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jf.dexlib2.util.SyntheticAccessorFSM.test(java.util.List):int");
    }

    private static int getIncrementType(int mathOp, int mathType, long constantValue, int putRegister, int returnRegister) {
        boolean isPrefix = putRegister == returnRegister;
        boolean negativeConstant = false;
        switch (mathType) {
            case 0:
            case 1:
                if (constantValue == 1) {
                    negativeConstant = false;
                    break;
                } else if (constantValue == -1) {
                    negativeConstant = true;
                    break;
                } else {
                    return -1;
                }
            case 2:
                float val = Float.intBitsToFloat((int) constantValue);
                if (val == 1.0f) {
                    negativeConstant = false;
                    break;
                } else if (val == -1.0f) {
                    negativeConstant = true;
                    break;
                } else {
                    return -1;
                }
            case 3:
                double val2 = Double.longBitsToDouble(constantValue);
                if (val2 == 1.0d) {
                    negativeConstant = false;
                    break;
                } else if (val2 == -1.0d) {
                    negativeConstant = true;
                    break;
                } else {
                    return -1;
                }
        }
        boolean isAdd = (mathOp == 7 && !negativeConstant) || (mathOp == 8 && negativeConstant);
        if (isPrefix) {
            if (isAdd) {
                return 4;
            }
            return 6;
        } else if (isAdd) {
            return 3;
        } else {
            return 5;
        }
    }
}
