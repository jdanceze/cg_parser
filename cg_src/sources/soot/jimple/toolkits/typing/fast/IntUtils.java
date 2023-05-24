package soot.jimple.toolkits.typing.fast;

import net.bytebuddy.asm.Advice;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.IntType;
import soot.IntegerType;
import soot.ShortType;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/IntUtils.class */
public class IntUtils {
    public static IntegerType getTypeByWidth(int maxValue) {
        switch (maxValue) {
            case 1:
                return Integer1Type.v();
            case 127:
                return Integer127Type.v();
            case Advice.MethodSizeHandler.UNDEFINED_SIZE /* 32767 */:
                return Integer32767Type.v();
            case Integer.MAX_VALUE:
                return IntType.v();
            default:
                throw new RuntimeException("Unsupported width: " + maxValue);
        }
    }

    public static int getMaxValue(IntegerType t) {
        if ((t instanceof Integer1Type) || (t instanceof BooleanType)) {
            return 1;
        }
        if ((t instanceof Integer127Type) || (t instanceof ByteType)) {
            return 127;
        }
        if ((t instanceof Integer32767Type) || (t instanceof ShortType) || (t instanceof CharType)) {
            return Advice.MethodSizeHandler.UNDEFINED_SIZE;
        }
        if (t instanceof IntType) {
            return Integer.MAX_VALUE;
        }
        throw new RuntimeException("Unsupported type: " + t);
    }
}
