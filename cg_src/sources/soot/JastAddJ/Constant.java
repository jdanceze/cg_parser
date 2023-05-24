package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant.class */
public class Constant {
    public boolean error = false;

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant$ConstantInt.class */
    static class ConstantInt extends Constant {
        private int value;

        public ConstantInt(int i) {
            this.value = i;
        }

        @Override // soot.JastAddJ.Constant
        int intValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        long longValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        float floatValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        double doubleValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        String stringValue() {
            return new Integer(this.value).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant$ConstantLong.class */
    static class ConstantLong extends Constant {
        private long value;

        public ConstantLong(long l) {
            this.value = l;
        }

        @Override // soot.JastAddJ.Constant
        int intValue() {
            return (int) this.value;
        }

        @Override // soot.JastAddJ.Constant
        long longValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        float floatValue() {
            return (float) this.value;
        }

        @Override // soot.JastAddJ.Constant
        double doubleValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        String stringValue() {
            return new Long(this.value).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant$ConstantFloat.class */
    static class ConstantFloat extends Constant {
        private float value;

        public ConstantFloat(float f) {
            this.value = f;
        }

        @Override // soot.JastAddJ.Constant
        int intValue() {
            return (int) this.value;
        }

        @Override // soot.JastAddJ.Constant
        long longValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        float floatValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        double doubleValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        String stringValue() {
            return new Float(this.value).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant$ConstantDouble.class */
    static class ConstantDouble extends Constant {
        private double value;

        public ConstantDouble(double d) {
            this.value = d;
        }

        @Override // soot.JastAddJ.Constant
        int intValue() {
            return (int) this.value;
        }

        @Override // soot.JastAddJ.Constant
        long longValue() {
            return (long) this.value;
        }

        @Override // soot.JastAddJ.Constant
        float floatValue() {
            return (float) this.value;
        }

        @Override // soot.JastAddJ.Constant
        double doubleValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        String stringValue() {
            return new Double(this.value).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant$ConstantChar.class */
    static class ConstantChar extends Constant {
        private char value;

        public ConstantChar(char c) {
            this.value = c;
        }

        @Override // soot.JastAddJ.Constant
        int intValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        long longValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        float floatValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        double doubleValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        String stringValue() {
            return new Character(this.value).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant$ConstantBoolean.class */
    static class ConstantBoolean extends Constant {
        private boolean value;

        public ConstantBoolean(boolean b) {
            this.value = b;
        }

        @Override // soot.JastAddJ.Constant
        boolean booleanValue() {
            return this.value;
        }

        @Override // soot.JastAddJ.Constant
        String stringValue() {
            return new Boolean(this.value).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Constant$ConstantString.class */
    static class ConstantString extends Constant {
        private String value;

        public ConstantString(String s) {
            this.value = s;
        }

        @Override // soot.JastAddJ.Constant
        String stringValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int intValue() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long longValue() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float floatValue() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double doubleValue() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean booleanValue() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String stringValue() {
        throw new UnsupportedOperationException();
    }

    protected Constant() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constant create(int i) {
        return new ConstantInt(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constant create(long l) {
        return new ConstantLong(l);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constant create(float f) {
        return new ConstantFloat(f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constant create(double d) {
        return new ConstantDouble(d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constant create(boolean b) {
        return new ConstantBoolean(b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constant create(char c) {
        return new ConstantChar(c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Constant create(String s) {
        return new ConstantString(s);
    }
}
