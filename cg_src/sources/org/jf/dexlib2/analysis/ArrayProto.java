package org.jf.dexlib2.analysis;

import com.google.common.base.Strings;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.util.TypeUtils;
import org.jf.util.ExceptionWithContext;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/ArrayProto.class */
public class ArrayProto implements TypeProto {
    protected final ClassPath classPath;
    protected final int dimensions;
    protected final String elementType;
    private static final String BRACKETS = Strings.repeat("[", 256);

    public ArrayProto(@Nonnull ClassPath classPath, @Nonnull String type) {
        this.classPath = classPath;
        int i = 0;
        while (type.charAt(i) == '[') {
            i++;
            if (i == type.length()) {
                throw new ExceptionWithContext("Invalid array type: %s", type);
            }
        }
        if (i == 0) {
            throw new ExceptionWithContext("Invalid array type: %s", type);
        }
        this.dimensions = i;
        this.elementType = type.substring(i);
    }

    public String toString() {
        return getType();
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nonnull
    public ClassPath getClassPath() {
        return this.classPath;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nonnull
    public String getType() {
        return makeArrayType(this.elementType, this.dimensions);
    }

    public int getDimensions() {
        return this.dimensions;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    public boolean isInterface() {
        return false;
    }

    @Nonnull
    public String getElementType() {
        return this.elementType;
    }

    @Nonnull
    public String getImmediateElementType() {
        if (this.dimensions > 1) {
            return makeArrayType(this.elementType, this.dimensions - 1);
        }
        return this.elementType;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    public boolean implementsInterface(@Nonnull String iface) {
        return iface.equals("Ljava/lang/Cloneable;") || iface.equals("Ljava/io/Serializable;");
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nullable
    public String getSuperclass() {
        return TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nonnull
    public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
        if (other instanceof ArrayProto) {
            if (TypeUtils.isPrimitiveType(getElementType()) || TypeUtils.isPrimitiveType(((ArrayProto) other).getElementType())) {
                if (this.dimensions == ((ArrayProto) other).dimensions && getElementType().equals(((ArrayProto) other).getElementType())) {
                    return this;
                }
                return this.classPath.getClass(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR);
            } else if (this.dimensions == ((ArrayProto) other).dimensions) {
                TypeProto thisClass = this.classPath.getClass(this.elementType);
                TypeProto otherClass = this.classPath.getClass(((ArrayProto) other).elementType);
                TypeProto mergedClass = thisClass.getCommonSuperclass(otherClass);
                if (thisClass == mergedClass) {
                    return this;
                }
                if (otherClass == mergedClass) {
                    return other;
                }
                return this.classPath.getClass(makeArrayType(mergedClass.getType(), this.dimensions));
            } else {
                int dimensions = Math.min(this.dimensions, ((ArrayProto) other).dimensions);
                return this.classPath.getClass(makeArrayType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR, dimensions));
            }
        } else if (other instanceof ClassProto) {
            try {
                if (other.isInterface()) {
                    if (implementsInterface(other.getType())) {
                        return other;
                    }
                }
            } catch (UnresolvedClassException e) {
            }
            return this.classPath.getClass(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR);
        } else {
            return other.getCommonSuperclass(this);
        }
    }

    @Nonnull
    private static String makeArrayType(@Nonnull String elementType, int dimensions) {
        return BRACKETS.substring(0, dimensions) + elementType;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nullable
    public FieldReference getFieldByOffset(int fieldOffset) {
        if (fieldOffset == 8) {
            return new ImmutableFieldReference(getType(), XMLConstants.LENGTH_ATTRIBUTE, "int");
        }
        return null;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nullable
    public Method getMethodByVtableIndex(int vtableIndex) {
        return this.classPath.getClass(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR).getMethodByVtableIndex(vtableIndex);
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    public int findMethodIndexInVtable(@Nonnull MethodReference method) {
        return this.classPath.getClass(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR).findMethodIndexInVtable(method);
    }
}
