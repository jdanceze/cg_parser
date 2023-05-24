package soot.jimple;

import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.StringTools;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/ClassConstant.class */
public class ClassConstant extends Constant {
    public final String value;

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassConstant(String s) {
        this.value = s;
    }

    public static ClassConstant v(String value) {
        if (value.indexOf(46) > -1) {
            throw new RuntimeException("ClassConstants must use class names separated by '/', not '.'!");
        }
        return new ClassConstant(value);
    }

    public static ClassConstant fromType(Type tp) {
        return v(sootTypeToString(tp));
    }

    private static String sootTypeToString(Type tp) {
        if (tp instanceof RefType) {
            return "L" + ((RefType) tp).getClassName().replace('.', '/') + ";";
        }
        if (tp instanceof ArrayType) {
            return "[" + sootTypeToString(((ArrayType) tp).getElementType());
        }
        if (tp instanceof PrimType) {
            if (tp instanceof IntType) {
                return "I";
            }
            if (tp instanceof ByteType) {
                return "B";
            }
            if (tp instanceof CharType) {
                return "C";
            }
            if (tp instanceof DoubleType) {
                return "D";
            }
            if (tp instanceof FloatType) {
                return "F";
            }
            if (tp instanceof LongType) {
                return "J";
            }
            if (tp instanceof ShortType) {
                return "S";
            }
            if (tp instanceof BooleanType) {
                return "Z";
            }
            throw new RuntimeException("Unsupported primitive type");
        }
        throw new RuntimeException("Unsupported type" + tp);
    }

    public boolean isRefType() {
        String tmp = this.value;
        return !tmp.isEmpty() && tmp.charAt(0) == 'L' && tmp.charAt(tmp.length() - 1) == ';';
    }

    public Type toSootType() {
        String tmp;
        Type baseType;
        int numDimensions = 0;
        String str = this.value;
        while (true) {
            tmp = str;
            if (tmp.isEmpty() || tmp.charAt(0) != '[') {
                break;
            }
            numDimensions++;
            str = tmp.substring(1);
        }
        if (!tmp.isEmpty() && tmp.charAt(0) == 'L') {
            String tmp2 = tmp.substring(1);
            int lastIdx = tmp2.length() - 1;
            if (!tmp2.isEmpty() && tmp2.charAt(lastIdx) == ';') {
                tmp2 = tmp2.substring(0, lastIdx);
            }
            baseType = RefType.v(tmp2.replace('/', '.'));
        } else {
            switch (tmp.hashCode()) {
                case 66:
                    if (tmp.equals("B")) {
                        baseType = ByteType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                case 67:
                    if (tmp.equals("C")) {
                        baseType = CharType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                case 68:
                    if (tmp.equals("D")) {
                        baseType = DoubleType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                case 70:
                    if (tmp.equals("F")) {
                        baseType = FloatType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                case 73:
                    if (tmp.equals("I")) {
                        baseType = IntType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                case 74:
                    if (tmp.equals("J")) {
                        baseType = LongType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                case 83:
                    if (tmp.equals("S")) {
                        baseType = ShortType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                case 90:
                    if (tmp.equals("Z")) {
                        baseType = BooleanType.v();
                        break;
                    }
                    throw new RuntimeException("Unsupported class constant: " + this.value);
                default:
                    throw new RuntimeException("Unsupported class constant: " + this.value);
            }
        }
        return numDimensions > 0 ? ArrayType.v(baseType, numDimensions) : baseType;
    }

    public String toInternalString() {
        String internal;
        String str = this.value;
        while (true) {
            internal = str;
            if (internal.isEmpty() || internal.charAt(0) != '[') {
                break;
            }
            str = internal.substring(1);
        }
        int lastIdx = internal.length() - 1;
        if (!internal.isEmpty() && internal.charAt(lastIdx) == ';') {
            internal = internal.substring(0, lastIdx);
            if (!internal.isEmpty() && internal.charAt(0) == 'L') {
                internal = internal.substring(1);
            }
        }
        return internal;
    }

    public boolean equals(Object c) {
        return (c instanceof ClassConstant) && ((ClassConstant) c).value.equals(this.value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return "class " + StringTools.getQuotedStringOf(this.value);
    }

    public String getValue() {
        return this.value;
    }

    @Override // soot.Value
    public Type getType() {
        if (Options.v().src_prec() == 7) {
            return RefType.v(DotnetBasicTypes.SYSTEM_RUNTIMETYPEHANDLE);
        }
        return RefType.v("java.lang.Class");
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseClassConstant(this);
    }
}
