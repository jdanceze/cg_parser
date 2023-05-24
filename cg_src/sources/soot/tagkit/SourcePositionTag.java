package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SourcePositionTag.class */
public class SourcePositionTag extends PositionTag {
    public static final String NAME = "SourcePositionTag";

    public SourcePositionTag(int i, int j) {
        super(i, j);
    }

    @Override // soot.tagkit.PositionTag, soot.tagkit.Tag
    public String getName() {
        return NAME;
    }
}
