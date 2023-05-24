package soot.dexpler.tags;

import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/tags/IntOpTag.class */
public class IntOpTag implements Tag {
    public static final String NAME = "IntOpTag";

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[1];
    }
}
