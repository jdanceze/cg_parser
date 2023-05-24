package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/LinkTag.class */
public class LinkTag extends StringTag {
    public static final String NAME = "LinkTag";
    private final Host link;
    private final String className;

    public LinkTag(String string, Host link, String className, String type) {
        super(string, type);
        this.link = link;
        this.className = className;
    }

    public LinkTag(String string, Host link, String className) {
        super(string);
        this.link = link;
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public Host getLink() {
        return this.link;
    }

    @Override // soot.tagkit.StringTag, soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.StringTag, soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("LinkTag has no value for bytecode");
    }
}
