package org.jf.dexlib2.analysis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/UnknownClassProto.class */
public class UnknownClassProto implements TypeProto {
    @Nonnull
    protected final ClassPath classPath;

    public UnknownClassProto(@Nonnull ClassPath classPath) {
        this.classPath = classPath;
    }

    public String toString() {
        return "Ujava/lang/Object;";
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nonnull
    public ClassPath getClassPath() {
        return this.classPath;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nullable
    public String getSuperclass() {
        return null;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    public boolean isInterface() {
        return false;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    public boolean implementsInterface(@Nonnull String iface) {
        return false;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nonnull
    public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
        if (other.getType().equals(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR)) {
            return other;
        }
        if (other instanceof ArrayProto) {
            return this.classPath.getClass(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR);
        }
        return this;
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nonnull
    public String getType() {
        return "Ujava/lang/Object;";
    }

    @Override // org.jf.dexlib2.analysis.TypeProto
    @Nullable
    public FieldReference getFieldByOffset(int fieldOffset) {
        return this.classPath.getClass(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR).getFieldByOffset(fieldOffset);
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
