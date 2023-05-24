package org.jf.dexlib2.analysis;

import javax.annotation.Nullable;
import org.jf.dexlib2.iface.ClassDef;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/ClassProvider.class */
public interface ClassProvider {
    @Nullable
    ClassDef getClassDef(String str);
}
