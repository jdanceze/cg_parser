package soot.dexpler.typing;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.FloatType;
import soot.IntType;
import soot.RefLikeType;
import soot.ShortType;
import soot.Type;
import soot.Value;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/dexpler/typing/UntypedIntOrFloatConstant.class */
public class UntypedIntOrFloatConstant extends UntypedConstant {
    private static final long serialVersionUID = 4413439694269487822L;
    public final int value;

    private UntypedIntOrFloatConstant(int value) {
        this.value = value;
    }

    public static UntypedIntOrFloatConstant v(int value) {
        return new UntypedIntOrFloatConstant(value);
    }

    public boolean equals(Object c) {
        return (c instanceof UntypedIntOrFloatConstant) && ((UntypedIntOrFloatConstant) c).value == this.value;
    }

    public int hashCode() {
        return this.value ^ (this.value >>> 32);
    }

    public FloatConstant toFloatConstant() {
        return FloatConstant.v(Float.intBitsToFloat(this.value));
    }

    public IntConstant toIntConstant() {
        return IntConstant.v(this.value);
    }

    @Override // soot.dexpler.typing.UntypedConstant
    public Value defineType(Type t) {
        if (t instanceof FloatType) {
            return toFloatConstant();
        }
        if ((t instanceof IntType) || (t instanceof CharType) || (t instanceof BooleanType) || (t instanceof ByteType) || (t instanceof ShortType)) {
            return toIntConstant();
        }
        if (this.value == 0 && (t instanceof RefLikeType)) {
            return NullConstant.v();
        }
        if (t == null) {
            return toIntConstant();
        }
        throw new RuntimeException("error: expected Float type or Int-like type. Got " + t);
    }
}
