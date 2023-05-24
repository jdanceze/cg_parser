package org.jf.dexlib2.writer.pool;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.writer.pool.TypeListPool;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/PoolClassDef.class */
public class PoolClassDef extends BaseTypeReference implements ClassDef {
    @Nonnull
    final ClassDef classDef;
    @Nonnull
    final TypeListPool.Key<List<String>> interfaces;
    @Nonnull
    final ImmutableSortedSet<Field> staticFields;
    @Nonnull
    final ImmutableSortedSet<Field> instanceFields;
    @Nonnull
    final ImmutableSortedSet<PoolMethod> directMethods;
    @Nonnull
    final ImmutableSortedSet<PoolMethod> virtualMethods;
    int classDefIndex = -1;
    int annotationDirectoryOffset = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PoolClassDef(@Nonnull ClassDef classDef) {
        this.classDef = classDef;
        this.interfaces = new TypeListPool.Key<>(ImmutableList.copyOf((Collection) classDef.getInterfaces()));
        this.staticFields = ImmutableSortedSet.copyOf((Iterable) classDef.getStaticFields());
        this.instanceFields = ImmutableSortedSet.copyOf((Iterable) classDef.getInstanceFields());
        this.directMethods = ImmutableSortedSet.copyOf(Iterables.transform(classDef.getDirectMethods(), PoolMethod.TRANSFORM));
        this.virtualMethods = ImmutableSortedSet.copyOf(Iterables.transform(classDef.getVirtualMethods(), PoolMethod.TRANSFORM));
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.classDef.getType();
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    public int getAccessFlags() {
        return this.classDef.getAccessFlags();
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nullable
    public String getSuperclass() {
        return this.classDef.getSuperclass();
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public List<String> getInterfaces() {
        return this.interfaces.types;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nullable
    public String getSourceFile() {
        return this.classDef.getSourceFile();
    }

    @Override // org.jf.dexlib2.iface.ClassDef, org.jf.dexlib2.iface.Annotatable
    @Nonnull
    public Set<? extends Annotation> getAnnotations() {
        return this.classDef.getAnnotations();
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<Field> getStaticFields() {
        return this.staticFields;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<Field> getInstanceFields() {
        return this.instanceFields;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<Field> getFields() {
        return new AbstractCollection<Field>() { // from class: org.jf.dexlib2.writer.pool.PoolClassDef.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<Field> iterator() {
                return Iterators.mergeSorted(ImmutableList.of(PoolClassDef.this.staticFields.iterator(), PoolClassDef.this.instanceFields.iterator()), Ordering.natural());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return PoolClassDef.this.staticFields.size() + PoolClassDef.this.instanceFields.size();
            }
        };
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<PoolMethod> getDirectMethods() {
        return this.directMethods;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public SortedSet<PoolMethod> getVirtualMethods() {
        return this.virtualMethods;
    }

    @Override // org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public Collection<PoolMethod> getMethods() {
        return new AbstractCollection<PoolMethod>() { // from class: org.jf.dexlib2.writer.pool.PoolClassDef.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            @Nonnull
            public Iterator<PoolMethod> iterator() {
                return Iterators.mergeSorted(ImmutableList.of(PoolClassDef.this.directMethods.iterator(), PoolClassDef.this.virtualMethods.iterator()), Ordering.natural());
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return PoolClassDef.this.directMethods.size() + PoolClassDef.this.virtualMethods.size();
            }
        };
    }
}
