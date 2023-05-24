package gnu.trove.impl;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/HashFunctions.class */
public final class HashFunctions {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HashFunctions.class.desiredAssertionStatus();
    }

    public static int hash(double value) {
        if ($assertionsDisabled || !Double.isNaN(value)) {
            long bits = Double.doubleToLongBits(value);
            return (int) (bits ^ (bits >>> 32));
        }
        throw new AssertionError("Values of NaN are not supported.");
    }

    public static int hash(float value) {
        if ($assertionsDisabled || !Float.isNaN(value)) {
            return Float.floatToIntBits(value * 6.6360896E8f);
        }
        throw new AssertionError("Values of NaN are not supported.");
    }

    public static int hash(int value) {
        return value;
    }

    public static int hash(long value) {
        return (int) (value ^ (value >>> 32));
    }

    public static int hash(Object object) {
        if (object == null) {
            return 0;
        }
        return object.hashCode();
    }

    public static int fastCeil(float v) {
        int possible_result = (int) v;
        if (v - possible_result > 0.0f) {
            possible_result++;
        }
        return possible_result;
    }
}
