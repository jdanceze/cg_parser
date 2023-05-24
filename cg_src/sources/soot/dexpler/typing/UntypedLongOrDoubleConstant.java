package soot.dexpler.typing;

import soot.DoubleType;
import soot.LongType;
import soot.Type;
import soot.Value;
import soot.jimple.DoubleConstant;
import soot.jimple.LongConstant;
/* loaded from: gencallgraphv3.jar:soot/dexpler/typing/UntypedLongOrDoubleConstant.class */
public class UntypedLongOrDoubleConstant extends UntypedConstant {
    private static final long serialVersionUID = -3970057807907204253L;
    public final long value;

    private UntypedLongOrDoubleConstant(long value) {
        this.value = value;
    }

    public static UntypedLongOrDoubleConstant v(long value) {
        return new UntypedLongOrDoubleConstant(value);
    }

    public boolean equals(Object c) {
        return (c instanceof UntypedLongOrDoubleConstant) && ((UntypedLongOrDoubleConstant) c).value == this.value;
    }

    public int hashCode() {
        return (int) (this.value ^ (this.value >>> 32));
    }

    public DoubleConstant toDoubleConstant() {
        return DoubleConstant.v(Double.longBitsToDouble(this.value));
    }

    public LongConstant toLongConstant() {
        return LongConstant.v(this.value);
    }

    @Override // soot.dexpler.typing.UntypedConstant
    public Value defineType(Type t) {
        if (t instanceof DoubleType) {
            return toDoubleConstant();
        }
        if (t instanceof LongType) {
            return toLongConstant();
        }
        throw new RuntimeException("error: expected Double type or Long type. Got " + t);
    }
}
