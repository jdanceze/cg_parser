package org.jf.dexlib2.analysis;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.analysis.reflection.ReflectionClassDef;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.immutable.ImmutableDexFile;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/ClassPath.class */
public class ClassPath {
    @Nonnull
    private final TypeProto unknownClass;
    @Nonnull
    private List<ClassProvider> classProviders;
    private final boolean checkPackagePrivateAccess;
    public final int oatVersion;
    public static final int NOT_ART = -1;
    public static final int NOT_SPECIFIED = -2;
    private final CacheLoader<String, TypeProto> classLoader;
    @Nonnull
    private LoadingCache<String, TypeProto> loadedClasses;
    private final Supplier<OdexedFieldInstructionMapper> fieldInstructionMapperSupplier;

    public ClassPath(ClassProvider... classProviders) throws IOException {
        this(Arrays.asList(classProviders), false, -1);
    }

    public ClassPath(Iterable<ClassProvider> classProviders) throws IOException {
        this(classProviders, false, -1);
    }

    public ClassPath(@Nonnull Iterable<? extends ClassProvider> classProviders, boolean checkPackagePrivateAccess, int oatVersion) {
        this.classLoader = new CacheLoader<String, TypeProto>() { // from class: org.jf.dexlib2.analysis.ClassPath.1
            @Override // com.google.common.cache.CacheLoader
            public TypeProto load(String type) throws Exception {
                if (type.charAt(0) == '[') {
                    return new ArrayProto(ClassPath.this, type);
                }
                return new ClassProto(ClassPath.this, type);
            }
        };
        this.loadedClasses = CacheBuilder.newBuilder().build(this.classLoader);
        this.fieldInstructionMapperSupplier = Suppliers.memoize(new Supplier<OdexedFieldInstructionMapper>() { // from class: org.jf.dexlib2.analysis.ClassPath.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.Supplier
            public OdexedFieldInstructionMapper get() {
                return new OdexedFieldInstructionMapper(ClassPath.this.isArt());
            }
        });
        this.unknownClass = new UnknownClassProto(this);
        this.loadedClasses.put(this.unknownClass.getType(), this.unknownClass);
        this.checkPackagePrivateAccess = checkPackagePrivateAccess;
        this.oatVersion = oatVersion;
        loadPrimitiveType("Z");
        loadPrimitiveType("B");
        loadPrimitiveType("S");
        loadPrimitiveType("C");
        loadPrimitiveType("I");
        loadPrimitiveType("J");
        loadPrimitiveType("F");
        loadPrimitiveType("D");
        loadPrimitiveType("L");
        this.classProviders = Lists.newArrayList(classProviders);
        this.classProviders.add(getBasicClasses());
    }

    private void loadPrimitiveType(String type) {
        this.loadedClasses.put(type, new PrimitiveProto(this, type));
    }

    private static ClassProvider getBasicClasses() {
        return new DexClassProvider(new ImmutableDexFile(Opcodes.getDefault(), (Collection<? extends ClassDef>) ImmutableSet.of(new ReflectionClassDef(Class.class), new ReflectionClassDef(Cloneable.class), new ReflectionClassDef(Object.class), new ReflectionClassDef(Serializable.class), new ReflectionClassDef(String.class), new ReflectionClassDef(Throwable.class), new ReflectionClassDef[0])));
    }

    public boolean isArt() {
        return this.oatVersion != -1;
    }

    @Nonnull
    public TypeProto getClass(@Nonnull CharSequence type) {
        return this.loadedClasses.getUnchecked(type.toString());
    }

    @Nonnull
    public ClassDef getClassDef(String type) {
        for (ClassProvider provider : this.classProviders) {
            ClassDef classDef = provider.getClassDef(type);
            if (classDef != null) {
                return classDef;
            }
        }
        throw new UnresolvedClassException("Could not resolve class %s", type);
    }

    @Nonnull
    public TypeProto getUnknownClass() {
        return this.unknownClass;
    }

    public boolean shouldCheckPackagePrivateAccess() {
        return this.checkPackagePrivateAccess;
    }

    @Nonnull
    public OdexedFieldInstructionMapper getFieldInstructionMapper() {
        return this.fieldInstructionMapperSupplier.get();
    }
}
