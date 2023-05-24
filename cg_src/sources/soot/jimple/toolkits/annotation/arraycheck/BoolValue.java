package soot.jimple.toolkits.annotation.arraycheck;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/BoolValue.class */
class BoolValue {
    private boolean isRectangular;
    private static final BoolValue trueValue = new BoolValue(true);
    private static final BoolValue falseValue = new BoolValue(false);

    public BoolValue(boolean v) {
        this.isRectangular = v;
    }

    public static BoolValue v(boolean v) {
        if (v) {
            return trueValue;
        }
        return falseValue;
    }

    public boolean getValue() {
        return this.isRectangular;
    }

    public boolean or(BoolValue other) {
        if (other.getValue()) {
            this.isRectangular = true;
        }
        return this.isRectangular;
    }

    public boolean or(boolean other) {
        if (other) {
            this.isRectangular = true;
        }
        return this.isRectangular;
    }

    public boolean and(BoolValue other) {
        if (!other.getValue()) {
            this.isRectangular = false;
        }
        return this.isRectangular;
    }

    public boolean and(boolean other) {
        if (!other) {
            this.isRectangular = false;
        }
        return this.isRectangular;
    }

    public int hashCode() {
        if (this.isRectangular) {
            return 1;
        }
        return 0;
    }

    public boolean equals(Object other) {
        return (other instanceof BoolValue) && this.isRectangular == ((BoolValue) other).getValue();
    }

    public String toString() {
        return "[" + this.isRectangular + "]";
    }
}
