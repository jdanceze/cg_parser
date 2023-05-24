package soot.jimple.toolkits.typing.fast;

import soot.BooleanType;
import soot.ByteType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/TypeUtils.class */
public class TypeUtils {
    public static int getValueBitSize(Type type) {
        if (type instanceof BooleanType) {
            return 1;
        }
        if (type instanceof ByteType) {
            return 8;
        }
        if (type instanceof ShortType) {
            return 16;
        }
        if (type instanceof IntType) {
            return 32;
        }
        if (type instanceof LongType) {
            return 64;
        }
        if (type instanceof FloatType) {
            return 32;
        }
        if (type instanceof DoubleType) {
            return 64;
        }
        if (type instanceof Integer127Type) {
            return 8;
        }
        if (type instanceof Integer32767Type) {
            return 16;
        }
        if (type instanceof Integer1Type) {
            return 1;
        }
        if (type instanceof RefType) {
            return 64;
        }
        throw new IllegalArgumentException(type + " not supported.");
    }
}
