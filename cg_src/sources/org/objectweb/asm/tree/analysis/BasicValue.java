package org.objectweb.asm.tree.analysis;

import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.objectweb.asm.Type;
/* loaded from: gencallgraphv3.jar:asm-analysis-9.4.jar:org/objectweb/asm/tree/analysis/BasicValue.class */
public class BasicValue implements Value {
    public static final BasicValue UNINITIALIZED_VALUE = new BasicValue(null);
    public static final BasicValue INT_VALUE = new BasicValue(Type.INT_TYPE);
    public static final BasicValue FLOAT_VALUE = new BasicValue(Type.FLOAT_TYPE);
    public static final BasicValue LONG_VALUE = new BasicValue(Type.LONG_TYPE);
    public static final BasicValue DOUBLE_VALUE = new BasicValue(Type.DOUBLE_TYPE);
    public static final BasicValue REFERENCE_VALUE = new BasicValue(Type.getObjectType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME));
    public static final BasicValue RETURNADDRESS_VALUE = new BasicValue(Type.VOID_TYPE);
    private final Type type;

    public BasicValue(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    @Override // org.objectweb.asm.tree.analysis.Value
    public int getSize() {
        return (this.type == Type.LONG_TYPE || this.type == Type.DOUBLE_TYPE) ? 2 : 1;
    }

    public boolean isReference() {
        return this.type != null && (this.type.getSort() == 10 || this.type.getSort() == 9);
    }

    public boolean equals(Object value) {
        if (value == this) {
            return true;
        }
        if (value instanceof BasicValue) {
            if (this.type == null) {
                return ((BasicValue) value).type == null;
            }
            return this.type.equals(((BasicValue) value).type);
        }
        return false;
    }

    public int hashCode() {
        if (this.type == null) {
            return 0;
        }
        return this.type.hashCode();
    }

    public String toString() {
        if (this == UNINITIALIZED_VALUE) {
            return ".";
        }
        if (this == RETURNADDRESS_VALUE) {
            return "A";
        }
        if (this == REFERENCE_VALUE) {
            return "R";
        }
        return this.type.getDescriptor();
    }
}
