package net.bytebuddy.dynamic.scaffold;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/TypeValidation.class */
public enum TypeValidation {
    ENABLED(true),
    DISABLED(false);
    
    private final boolean enabled;

    TypeValidation(boolean enabled) {
        this.enabled = enabled;
    }

    public static TypeValidation of(boolean enabled) {
        return enabled ? ENABLED : DISABLED;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
