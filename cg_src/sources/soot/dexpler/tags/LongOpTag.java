package soot.dexpler.tags;

import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/tags/LongOpTag.class */
public class LongOpTag implements Tag {
    public static final String NAME = "LongOpTag";

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[1];
    }
}
