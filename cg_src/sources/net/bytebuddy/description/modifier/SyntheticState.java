package net.bytebuddy.description.modifier;

import net.bytebuddy.description.modifier.ModifierContributor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/SyntheticState.class */
public enum SyntheticState implements ModifierContributor.ForType, ModifierContributor.ForMethod, ModifierContributor.ForField, ModifierContributor.ForParameter {
    PLAIN(0),
    SYNTHETIC(4096);
    
    private final int mask;

    SyntheticState(int mask) {
        this.mask = mask;
    }

    @Override // net.bytebuddy.description.modifier.ModifierContributor
    public int getMask() {
        return this.mask;
    }

    @Override // net.bytebuddy.description.modifier.ModifierContributor
    public int getRange() {
        return 4096;
    }

    @Override // net.bytebuddy.description.modifier.ModifierContributor
    public boolean isDefault() {
        return this == PLAIN;
    }

    public boolean isSynthetic() {
        return this == SYNTHETIC;
    }
}
