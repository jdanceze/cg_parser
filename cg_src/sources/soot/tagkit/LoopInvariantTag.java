package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/LoopInvariantTag.class */
public class LoopInvariantTag extends StringTag {
    public static final String NAME = "LoopInvariantTag";

    public LoopInvariantTag(String s) {
        super(s);
    }

    @Override // soot.tagkit.StringTag, soot.tagkit.Tag
    public String getName() {
        return NAME;
    }
}
