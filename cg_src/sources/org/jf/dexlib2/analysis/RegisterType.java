package org.jf.dexlib2.analysis;

import java.io.IOException;
import java.io.Writer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/RegisterType.class */
public class RegisterType {
    public final byte category;
    @Nullable
    public final TypeProto type;
    public static final byte UNKNOWN = 0;
    public static final byte UNINIT = 1;
    public static final byte NULL = 2;
    public static final byte ONE = 3;
    public static final byte BOOLEAN = 4;
    public static final byte BYTE = 5;
    public static final byte POS_BYTE = 6;
    public static final byte SHORT = 7;
    public static final byte POS_SHORT = 8;
    public static final byte CHAR = 9;
    public static final byte INTEGER = 10;
    public static final byte FLOAT = 11;
    public static final byte LONG_LO = 12;
    public static final byte LONG_HI = 13;
    public static final byte DOUBLE_LO = 14;
    public static final byte DOUBLE_HI = 15;
    public static final byte UNINIT_REF = 16;
    public static final byte UNINIT_THIS = 17;
    public static final byte REFERENCE = 18;
    public static final byte CONFLICTED = 19;
    public static final String[] CATEGORY_NAMES;
    protected static byte[][] mergeTable;
    public static final RegisterType UNKNOWN_TYPE;
    public static final RegisterType UNINIT_TYPE;
    public static final RegisterType NULL_TYPE;
    public static final RegisterType ONE_TYPE;
    public static final RegisterType BOOLEAN_TYPE;
    public static final RegisterType BYTE_TYPE;
    public static final RegisterType POS_BYTE_TYPE;
    public static final RegisterType SHORT_TYPE;
    public static final RegisterType POS_SHORT_TYPE;
    public static final RegisterType CHAR_TYPE;
    public static final RegisterType INTEGER_TYPE;
    public static final RegisterType FLOAT_TYPE;
    public static final RegisterType LONG_LO_TYPE;
    public static final RegisterType LONG_HI_TYPE;
    public static final RegisterType DOUBLE_LO_TYPE;
    public static final RegisterType DOUBLE_HI_TYPE;
    public static final RegisterType CONFLICTED_TYPE;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Type inference failed for: r0v7, types: [byte[], byte[][]] */
    static {
        $assertionsDisabled = !RegisterType.class.desiredAssertionStatus();
        CATEGORY_NAMES = new String[]{"Unknown", "Uninit", "Null", "One", "Boolean", "Byte", "PosByte", "Short", "PosShort", "Char", "Integer", "Float", "LongLo", "LongHi", "DoubleLo", "DoubleHi", "UninitRef", "UninitThis", "Reference", "Conflicted"};
        mergeTable = new byte[]{new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}, new byte[]{1, 1, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{2, 19, 2, 4, 4, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 18, 19}, new byte[]{3, 19, 4, 3, 4, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{4, 19, 4, 4, 4, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{5, 19, 5, 5, 5, 5, 5, 7, 7, 10, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{6, 19, 6, 6, 6, 5, 6, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{7, 19, 7, 7, 7, 7, 7, 7, 7, 10, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{8, 19, 8, 8, 8, 7, 8, 7, 8, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{9, 19, 9, 9, 9, 10, 9, 10, 9, 9, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{10, 19, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{11, 19, 11, 11, 11, 11, 11, 11, 11, 11, 10, 11, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{12, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 12, 19, 12, 19, 19, 19, 19, 19}, new byte[]{13, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 13, 19, 13, 19, 19, 19, 19}, new byte[]{14, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 12, 19, 14, 19, 19, 19, 19, 19}, new byte[]{15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 13, 19, 15, 19, 19, 19, 19}, new byte[]{16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, new byte[]{17, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 17, 19, 19}, new byte[]{18, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19}, new byte[]{19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}};
        UNKNOWN_TYPE = new RegisterType((byte) 0, null);
        UNINIT_TYPE = new RegisterType((byte) 1, null);
        NULL_TYPE = new RegisterType((byte) 2, null);
        ONE_TYPE = new RegisterType((byte) 3, null);
        BOOLEAN_TYPE = new RegisterType((byte) 4, null);
        BYTE_TYPE = new RegisterType((byte) 5, null);
        POS_BYTE_TYPE = new RegisterType((byte) 6, null);
        SHORT_TYPE = new RegisterType((byte) 7, null);
        POS_SHORT_TYPE = new RegisterType((byte) 8, null);
        CHAR_TYPE = new RegisterType((byte) 9, null);
        INTEGER_TYPE = new RegisterType((byte) 10, null);
        FLOAT_TYPE = new RegisterType((byte) 11, null);
        LONG_LO_TYPE = new RegisterType((byte) 12, null);
        LONG_HI_TYPE = new RegisterType((byte) 13, null);
        DOUBLE_LO_TYPE = new RegisterType((byte) 14, null);
        DOUBLE_HI_TYPE = new RegisterType((byte) 15, null);
        CONFLICTED_TYPE = new RegisterType((byte) 19, null);
    }

    private RegisterType(byte category, @Nullable TypeProto type) {
        if (!$assertionsDisabled && (((category != 18 && category != 16 && category != 17) || type == null) && (category == 18 || category == 16 || category == 17 || type != null))) {
            throw new AssertionError();
        }
        this.category = category;
        this.type = type;
    }

    public String toString() {
        return "(" + CATEGORY_NAMES[this.category] + (this.type == null ? "" : "," + this.type) + ")";
    }

    public void writeTo(Writer writer) throws IOException {
        writer.write(40);
        writer.write(CATEGORY_NAMES[this.category]);
        if (this.type != null) {
            writer.write(44);
            writer.write(this.type.getType());
        }
        writer.write(41);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterType that = (RegisterType) o;
        if (this.category != that.category || this.category == 16 || this.category == 17) {
            return false;
        }
        return this.type != null ? this.type.equals(that.type) : that.type == null;
    }

    public int hashCode() {
        int result = this.category;
        return (31 * result) + (this.type != null ? this.type.hashCode() : 0);
    }

    @Nonnull
    public static RegisterType getWideRegisterType(@Nonnull CharSequence type, boolean firstRegister) {
        switch (type.charAt(0)) {
            case 'D':
                if (firstRegister) {
                    return getRegisterType((byte) 14, (TypeProto) null);
                }
                return getRegisterType((byte) 15, (TypeProto) null);
            case 'J':
                if (firstRegister) {
                    return getRegisterType((byte) 12, (TypeProto) null);
                }
                return getRegisterType((byte) 13, (TypeProto) null);
            default:
                throw new ExceptionWithContext("Cannot use this method for narrow register type: %s", type);
        }
    }

    @Nonnull
    public static RegisterType getRegisterType(@Nonnull ClassPath classPath, @Nonnull CharSequence type) {
        switch (type.charAt(0)) {
            case 'B':
                return BYTE_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'D':
                return DOUBLE_LO_TYPE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new AnalysisException("Invalid type: " + ((Object) type), new Object[0]);
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INTEGER_TYPE;
            case 'J':
                return LONG_LO_TYPE;
            case 'L':
            case '[':
                return getRegisterType((byte) 18, classPath.getClass(type));
            case 'S':
                return SHORT_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
        }
    }

    @Nonnull
    public static RegisterType getRegisterTypeForLiteral(int literalValue) {
        if (literalValue < -32768) {
            return INTEGER_TYPE;
        }
        if (literalValue < -128) {
            return SHORT_TYPE;
        }
        if (literalValue < 0) {
            return BYTE_TYPE;
        }
        if (literalValue == 0) {
            return NULL_TYPE;
        }
        if (literalValue == 1) {
            return ONE_TYPE;
        }
        if (literalValue < 128) {
            return POS_BYTE_TYPE;
        }
        if (literalValue < 32768) {
            return POS_SHORT_TYPE;
        }
        if (literalValue < 65536) {
            return CHAR_TYPE;
        }
        return INTEGER_TYPE;
    }

    @Nonnull
    public RegisterType merge(@Nonnull RegisterType other) {
        if (other.equals(this)) {
            return this;
        }
        byte mergedCategory = mergeTable[this.category][other.category];
        TypeProto mergedType = null;
        if (mergedCategory == 18) {
            TypeProto type = this.type;
            if (type != null) {
                if (other.type != null) {
                    mergedType = type.getCommonSuperclass(other.type);
                } else {
                    mergedType = type;
                }
            } else {
                mergedType = other.type;
            }
        } else if (mergedCategory == 16 || mergedCategory == 17) {
            if (this.category == 0) {
                return other;
            }
            if ($assertionsDisabled || other.category == 0) {
                return this;
            }
            throw new AssertionError();
        }
        if (mergedType != null) {
            if (mergedType.equals(this.type)) {
                return this;
            }
            if (mergedType.equals(other.type)) {
                return other;
            }
        }
        return getRegisterType(mergedCategory, mergedType);
    }

    @Nonnull
    public static RegisterType getRegisterType(byte category, @Nullable TypeProto typeProto) {
        switch (category) {
            case 0:
                return UNKNOWN_TYPE;
            case 1:
                return UNINIT_TYPE;
            case 2:
                return NULL_TYPE;
            case 3:
                return ONE_TYPE;
            case 4:
                return BOOLEAN_TYPE;
            case 5:
                return BYTE_TYPE;
            case 6:
                return POS_BYTE_TYPE;
            case 7:
                return SHORT_TYPE;
            case 8:
                return POS_SHORT_TYPE;
            case 9:
                return CHAR_TYPE;
            case 10:
                return INTEGER_TYPE;
            case 11:
                return FLOAT_TYPE;
            case 12:
                return LONG_LO_TYPE;
            case 13:
                return LONG_HI_TYPE;
            case 14:
                return DOUBLE_LO_TYPE;
            case 15:
                return DOUBLE_HI_TYPE;
            case 16:
            case 17:
            case 18:
            default:
                return new RegisterType(category, typeProto);
            case 19:
                return CONFLICTED_TYPE;
        }
    }
}
