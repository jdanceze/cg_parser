package soot.dexpler.tags;

import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/tags/DoubleOpTag.class */
public class DoubleOpTag implements Tag {
    public static final String NAME = "DoubleOpTag";

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[1];
    }
}
