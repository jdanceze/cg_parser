package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/DeprecatedTag.class */
public class DeprecatedTag implements Tag {
    public static final String NAME = "DeprecatedTag";
    private final Boolean forRemoval;
    private final String since;

    public DeprecatedTag() {
        this.forRemoval = null;
        this.since = null;
    }

    public DeprecatedTag(Boolean forRemoval, String since) {
        this.forRemoval = forRemoval;
        this.since = since;
    }

    public String toString() {
        return "Deprecated";
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return "Deprecated";
    }

    public Boolean getForRemoval() {
        return this.forRemoval;
    }

    public String getSince() {
        return this.since;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("DeprecatedTag has no value for bytecode");
    }
}
