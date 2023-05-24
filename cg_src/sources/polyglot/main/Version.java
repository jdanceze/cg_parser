package polyglot.main;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/main/Version.class */
public abstract class Version {
    public abstract String name();

    public abstract int major();

    public abstract int minor();

    public abstract int patch_level();

    public String toString() {
        return new StringBuffer().append("").append(major()).append(".").append(minor()).append(".").append(patch_level()).toString();
    }
}
