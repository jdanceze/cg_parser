package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.HiddenApiRestriction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/Member.class */
public interface Member extends Annotatable {
    @Nonnull
    String getDefiningClass();

    @Nonnull
    String getName();

    int getAccessFlags();

    @Nonnull
    Set<HiddenApiRestriction> getHiddenApiRestrictions();
}
