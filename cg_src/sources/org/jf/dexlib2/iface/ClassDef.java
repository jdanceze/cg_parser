package org.jf.dexlib2.iface;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/ClassDef.class */
public interface ClassDef extends TypeReference, Annotatable {
    @Override // 
    @Nonnull
    String getType();

    int getAccessFlags();

    @Nullable
    String getSuperclass();

    @Nonnull
    List<String> getInterfaces();

    @Nullable
    String getSourceFile();

    @Nonnull
    Set<? extends Annotation> getAnnotations();

    @Nonnull
    Iterable<? extends Field> getStaticFields();

    @Nonnull
    Iterable<? extends Field> getInstanceFields();

    @Nonnull
    Iterable<? extends Field> getFields();

    @Nonnull
    Iterable<? extends Method> getDirectMethods();

    @Nonnull
    Iterable<? extends Method> getVirtualMethods();

    @Nonnull
    Iterable<? extends Method> getMethods();
}
