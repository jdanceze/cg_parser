package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/SwitchLabelElement.class */
public class SwitchLabelElement {
    public final int key;
    @Nonnull
    public final Label target;

    public SwitchLabelElement(int key, @Nonnull Label target) {
        this.key = key;
        this.target = target;
    }
}
