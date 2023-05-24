package org.jf.dexlib2.writer.builder;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.writer.builder.BuilderEncodedValues;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderClassDef.class */
public class BuilderClassDef extends BaseTypeReference implements ClassDef {
    @Nonnull
    final BuilderTypeReference type;
    final int accessFlags;
    @Nullable
    final BuilderTypeReference superclass;
    @Nonnull
    final BuilderTypeList interfaces;
    @Nullable
    final BuilderStringReference sourceFile;
    @Nonnull
    final BuilderAnnotationSet annotations;
    @Nonnull
    final SortedSet<BuilderField> staticFields;
    @Nonnull
    final SortedSet<BuilderField> instanceFields;
    @Nonnull
    final SortedSet<BuilderMethod> directMethods;
    @Nonnull
    final SortedSet<BuilderMethod> virtualMethods;
    @Nullable
    final BuilderEncodedValues.BuilderArrayEncodedValue staticInitializers;
    int classDefIndex = -1;
    int annotationDirectoryOffset = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BuilderClassDef(@Nonnull BuilderTypeReference type, int accessFlags, @Nullable BuilderTypeReference superclass, @Nonnull BuilderTypeList interfaces, @Nullable BuilderStringReference sourceFile, @Nonnull BuilderAnnotationSet annotations, @Nullable SortedSet<BuilderField> staticFields, @Nullable SortedSet<BuilderField> instanceFields, @Nullable Iterable<? extends BuilderMethod> methods, @Nullable BuilderEncodedValues.BuilderArrayEncodedValue staticInitializers) {
        methods = methods == null ? ImmutableList.of() : methods;
        staticFields = staticFields == null ? ImmutableSortedSet.of() : staticFields;
        instanceFields = instanceFields == null ? ImmutableSortedSet.of() : instanceFields;
        this.type = type;
        this.accessFlags = accessFlags;
        this.superclass = superclass;
        this.interfaces = interfaces;
        this.sourceFile = sourceFile;
        this.annotations = annotations;
        this.staticFields = staticFields;
        this.instanceFields = instanceFields;
        this.directMethods = ImmutableSortedSet.copyOf(Iterables.filter(methods, MethodUtil.METHOD_IS_DIRECT));
        this.virtualMethods = ImmutableSortedSet.copyOf(Iterables.filter(methods, MethodUtil.METHOD_IS_VIRTUAL));
        this.staticInitializers = staticInitializers;
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.type.getType();
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    public int getAccessFlags() {
        return this.accessFlags;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nullable
    public String getSuperclass() {
        if (this.superclass == null) {
            return null;
        }
        return this.superclass.getType();
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nullable
    public String getSourceFile() {
        if (this.sourceFile == null) {
            return null;
        }
        return this.sourceFile.getString();
    }

    @Override // org.jf.dexlib2.iface.ClassDef, org.jf.dexlib2.iface.Annotatable
    @Nonnull
    public BuilderAnnotationSet getAnnotations() {
        return this.annotations;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderField> getStaticFields() {
        return this.staticFields;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderField> getInstanceFields() {
        return this.instanceFields;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderMethod> getDirectMethods() {
        return this.directMethods;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<BuilderMethod> getVirtualMethods() {
        return this.virtualMethods;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public List<String> getInterfaces() {
        return Lists.transform(this.interfaces, Functions.toStringFunction());
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<BuilderField> getFields() {
        return new AbstractCollection<BuilderField>() { // from class: org.jf.dexlib2.writer.builder.BuilderClassDef.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<BuilderField> iterator() {
                return Iterators.mergeSorted(ImmutableList.of(BuilderClassDef.this.staticFields.iterator(), BuilderClassDef.this.instanceFields.iterator()), Ordering.natural());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return BuilderClassDef.this.staticFields.size() + BuilderClassDef.this.instanceFields.size();
            }
        };
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<BuilderMethod> getMethods() {
        return new AbstractCollection<BuilderMethod>() { // from class: org.jf.dexlib2.writer.builder.BuilderClassDef.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<BuilderMethod> iterator() {
                return Iterators.mergeSorted(ImmutableList.of(BuilderClassDef.this.directMethods.iterator(), BuilderClassDef.this.virtualMethods.iterator()), Ordering.natural());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return BuilderClassDef.this.directMethods.size() + BuilderClassDef.this.virtualMethods.size();
            }
        };
    }
}
