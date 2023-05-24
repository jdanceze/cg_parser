package org.jf.dexlib2.util;

import org.jf.dexlib2.Opcode;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/InstructionUtil.class */
public final class InstructionUtil {
    public static boolean isInvokeStatic(Opcode opcode) {
        return opcode == Opcode.INVOKE_STATIC || opcode == Opcode.INVOKE_STATIC_RANGE;
    }

    public static boolean isInvokePolymorphic(Opcode opcode) {
        return opcode == Opcode.INVOKE_POLYMORPHIC || opcode == Opcode.INVOKE_POLYMORPHIC_RANGE;
    }

    private InstructionUtil() {
    }
}
