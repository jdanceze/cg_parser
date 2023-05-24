package soot.dexpler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedTypeReference;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.MultiDexContainer;
import soot.ArrayType;
import soot.CompilationDeathException;
import soot.PrimType;
import soot.Scene;
import soot.SootClass;
import soot.SootResolver;
import soot.Type;
import soot.VoidType;
import soot.dexpler.DexFileProvider;
import soot.javaToJimple.IInitialResolver;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexlibWrapper.class */
public class DexlibWrapper {
    private static final Set<String> systemAnnotationNames;
    private final DexClassLoader dexLoader = createDexClassLoader();
    private final Map<String, ClassInformation> classesToDefItems = new HashMap();
    private final Collection<MultiDexContainer.DexEntry<? extends DexFile>> dexFiles;

    static {
        Set<String> systemAnnotationNamesModifiable = new HashSet<>();
        systemAnnotationNamesModifiable.add(DexAnnotation.DALVIK_ANNOTATION_DEFAULT);
        systemAnnotationNamesModifiable.add(DexAnnotation.DALVIK_ANNOTATION_ENCLOSINGCLASS);
        systemAnnotationNamesModifiable.add(DexAnnotation.DALVIK_ANNOTATION_ENCLOSINGMETHOD);
        systemAnnotationNamesModifiable.add(DexAnnotation.DALVIK_ANNOTATION_INNERCLASS);
        systemAnnotationNamesModifiable.add(DexAnnotation.DALVIK_ANNOTATION_MEMBERCLASSES);
        systemAnnotationNamesModifiable.add(DexAnnotation.DALVIK_ANNOTATION_SIGNATURE);
        systemAnnotationNamesModifiable.add(DexAnnotation.DALVIK_ANNOTATION_THROWS);
        systemAnnotationNames = Collections.unmodifiableSet(systemAnnotationNamesModifiable);
    }

    /* loaded from: gencallgraphv3.jar:soot/dexpler/DexlibWrapper$ClassInformation.class */
    private static class ClassInformation {
        public MultiDexContainer.DexEntry<? extends DexFile> dexEntry;
        public ClassDef classDefinition;

        public ClassInformation(MultiDexContainer.DexEntry<? extends DexFile> entry, ClassDef classDef) {
            this.dexEntry = entry;
            this.classDefinition = classDef;
        }
    }

    public DexlibWrapper(File dexSource) {
        try {
            List<DexFileProvider.DexContainer<? extends DexFile>> containers = DexFileProvider.v().getDexFromSource(dexSource);
            this.dexFiles = new ArrayList(containers.size());
            for (DexFileProvider.DexContainer<? extends DexFile> container : containers) {
                this.dexFiles.add(container.getBase());
            }
        } catch (IOException e) {
            throw new CompilationDeathException("IOException during dex parsing", e);
        }
    }

    protected DexClassLoader createDexClassLoader() {
        return new DexClassLoader();
    }

    public void initialize() {
        for (MultiDexContainer.DexEntry<? extends DexFile> dexEntry : this.dexFiles) {
            for (ClassDef defItem : dexEntry.getDexFile().getClasses()) {
                String forClassName = Util.dottedClassName(defItem.getType());
                this.classesToDefItems.put(forClassName, new ClassInformation(dexEntry, defItem));
            }
        }
        for (MultiDexContainer.DexEntry<? extends DexFile> dexEntry2 : this.dexFiles) {
            DexFile dexFile = dexEntry2.getDexFile();
            if (dexFile instanceof DexBackedDexFile) {
                for (DexBackedTypeReference typeRef : ((DexBackedDexFile) dexFile).getTypeReferences()) {
                    String t = typeRef.getType();
                    Type st = DexType.toSoot(t);
                    if (st instanceof ArrayType) {
                        st = ((ArrayType) st).baseType;
                    }
                    String sootTypeName = st.toString();
                    if (!Scene.v().containsClass(sootTypeName)) {
                        if (!(st instanceof PrimType) && !(st instanceof VoidType) && !systemAnnotationNames.contains(sootTypeName)) {
                            SootResolver.v().makeClassRef(sootTypeName);
                        }
                    }
                    SootResolver.v().resolveClass(sootTypeName, 2);
                }
            }
        }
    }

    public IInitialResolver.Dependencies makeSootClass(SootClass sc, String className) {
        if (Util.isByteCodeClassName(className)) {
            className = Util.dottedClassName(className);
        }
        ClassInformation defItem = this.classesToDefItems.get(className);
        if (defItem != null) {
            return this.dexLoader.makeSootClass(sc, defItem.classDefinition, defItem.dexEntry);
        }
        throw new RuntimeException("Error: class not found in DEX files: " + className);
    }
}
