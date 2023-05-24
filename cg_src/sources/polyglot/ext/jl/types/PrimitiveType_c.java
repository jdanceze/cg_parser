package polyglot.ext.jl.types;

import polyglot.main.Options;
import polyglot.types.PrimitiveType;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/PrimitiveType_c.class */
public class PrimitiveType_c extends Type_c implements PrimitiveType {
    protected PrimitiveType.Kind kind;

    protected PrimitiveType_c() {
    }

    public PrimitiveType_c(TypeSystem ts, PrimitiveType.Kind kind) {
        super(ts);
        this.kind = kind;
    }

    @Override // polyglot.types.PrimitiveType
    public PrimitiveType.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String toString() {
        return this.kind.toString();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String translate(Resolver c) {
        String s = this.kind.toString();
        if (Options.global.cppBackend() && s.equals("boolean")) {
            return "bool";
        }
        return s;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isPrimitive() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public PrimitiveType toPrimitive() {
        return this;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isVoid() {
        return this.kind == PrimitiveType.VOID;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isBoolean() {
        return this.kind == PrimitiveType.BOOLEAN;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isChar() {
        return this.kind == PrimitiveType.CHAR;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isByte() {
        return this.kind == PrimitiveType.BYTE;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isShort() {
        return this.kind == PrimitiveType.SHORT;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isInt() {
        return this.kind == PrimitiveType.INT;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isLong() {
        return this.kind == PrimitiveType.LONG;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isFloat() {
        return this.kind == PrimitiveType.FLOAT;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isDouble() {
        return this.kind == PrimitiveType.DOUBLE;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isIntOrLess() {
        return this.kind == PrimitiveType.CHAR || this.kind == PrimitiveType.BYTE || this.kind == PrimitiveType.SHORT || this.kind == PrimitiveType.INT;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isLongOrLess() {
        return isIntOrLess() || this.kind == PrimitiveType.LONG;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isNumeric() {
        return isLongOrLess() || this.kind == PrimitiveType.FLOAT || this.kind == PrimitiveType.DOUBLE;
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return this.kind.hashCode();
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject t) {
        if (t instanceof PrimitiveType) {
            PrimitiveType p = (PrimitiveType) t;
            return kind() == p.kind();
        }
        return false;
    }

    @Override // polyglot.types.PrimitiveType
    public String wrapperTypeString(TypeSystem ts) {
        return ts.wrapperTypeString(this);
    }

    @Override // polyglot.types.Named
    public String name() {
        return toString();
    }

    @Override // polyglot.types.Named
    public String fullName() {
        return name();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean descendsFromImpl(Type ancestor) {
        return false;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isImplicitCastValidImpl(Type toType) {
        if (toType.isPrimitive()) {
            PrimitiveType t = toType.toPrimitive();
            if (t.isVoid() || isVoid()) {
                return false;
            }
            if (this.ts.equals(t, this)) {
                return true;
            }
            if (t.isBoolean()) {
                return isBoolean();
            }
            if (!isBoolean() && isNumeric() && t.isNumeric()) {
                if (t.isDouble()) {
                    return true;
                }
                if (isDouble()) {
                    return false;
                }
                if (t.isFloat()) {
                    return true;
                }
                if (isFloat()) {
                    return false;
                }
                if (t.isLong()) {
                    return true;
                }
                if (isLong()) {
                    return false;
                }
                if (t.isInt()) {
                    return true;
                }
                if (isInt()) {
                    return false;
                }
                if (t.isShort()) {
                    return isShort() || isByte();
                } else if (isShort()) {
                    return false;
                } else {
                    if (t.isChar()) {
                        return isChar();
                    }
                    if (isChar()) {
                        return false;
                    }
                    if (t.isByte()) {
                        return isByte();
                    }
                    return isByte() ? false : false;
                }
            }
            return false;
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isCastValidImpl(Type toType) {
        if (isVoid() || toType.isVoid()) {
            return false;
        }
        if (this.ts.equals(this, toType)) {
            return true;
        }
        return isNumeric() && toType.isNumeric();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean numericConversionValidImpl(long value) {
        return numericConversionValidImpl(new Long(value));
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean numericConversionValidImpl(Object value) {
        long charValue;
        if (value == null || (value instanceof Float) || (value instanceof Double)) {
            return false;
        }
        if (value instanceof Number) {
            charValue = ((Number) value).longValue();
        } else if (value instanceof Character) {
            charValue = ((Character) value).charValue();
        } else {
            return false;
        }
        if (isLong()) {
            return true;
        }
        return isInt() ? -2147483648L <= charValue && charValue <= 2147483647L : isChar() ? 0 <= charValue && charValue <= 65535 : isShort() ? -32768 <= charValue && charValue <= 32767 : isByte() && -128 <= charValue && charValue <= 127;
    }
}
