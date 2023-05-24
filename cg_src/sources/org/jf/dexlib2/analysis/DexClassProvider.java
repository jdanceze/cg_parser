package org.jf.dexlib2.analysis;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/DexClassProvider.class */
public class DexClassProvider implements ClassProvider {
    private final DexFile dexFile;
    private Map<String, ClassDef> classMap = Maps.newHashMap();

    public DexClassProvider(DexFile dexFile) {
        this.dexFile = dexFile;
        for (ClassDef classDef : dexFile.getClasses()) {
            this.classMap.put(classDef.getType(), classDef);
        }
    }

    @Override // org.jf.dexlib2.analysis.ClassProvider
    @Nullable
    public ClassDef getClassDef(String type) {
        return this.classMap.get(type);
    }
}
