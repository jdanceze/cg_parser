package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/Annotatable.class */
public interface Annotatable {
    @Nonnull
    Set<? extends Annotation> getAnnotations();
}
