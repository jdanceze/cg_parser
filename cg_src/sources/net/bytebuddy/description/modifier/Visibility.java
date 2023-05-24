package net.bytebuddy.description.modifier;

import net.bytebuddy.description.modifier.ModifierContributor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/Visibility.class */
public enum Visibility implements ModifierContributor.ForType, ModifierContributor.ForMethod, ModifierContributor.ForField {
    PUBLIC(1),
    PACKAGE_PRIVATE(0),
    PROTECTED(4),
    PRIVATE(2);
    
    private final int mask;

    Visibility(int mask) {
        this.mask = mask;
    }

    @Override // net.bytebuddy.description.modifier.ModifierContributor
    public int getMask() {
        return this.mask;
    }

    @Override // net.bytebuddy.description.modifier.ModifierContributor
    public int getRange() {
        return 7;
    }

    @Override // net.bytebuddy.description.modifier.ModifierContributor
    public boolean isDefault() {
        return this == PACKAGE_PRIVATE;
    }

    public boolean isPublic() {
        return (this.mask & 1) != 0;
    }

    public boolean isProtected() {
        return (this.mask & 4) != 0;
    }

    public boolean isPackagePrivate() {
        return (isPublic() || isPrivate() || isProtected()) ? false : true;
    }

    public boolean isPrivate() {
        return (this.mask & 2) != 0;
    }

    public Visibility expandTo(Visibility visibility) {
        switch (visibility) {
            case PUBLIC:
                return PUBLIC;
            case PROTECTED:
                return this == PUBLIC ? PUBLIC : visibility;
            case PACKAGE_PRIVATE:
                return this == PRIVATE ? PACKAGE_PRIVATE : this;
            case PRIVATE:
                return this;
            default:
                throw new IllegalStateException("Unexpected visibility: " + visibility);
        }
    }
}
