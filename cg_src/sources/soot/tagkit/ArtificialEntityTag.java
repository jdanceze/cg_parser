package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/ArtificialEntityTag.class */
public class ArtificialEntityTag implements Tag {
    public static final String NAME = "ArtificialEntityTag";

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() throws AttributeValueException {
        throw new RuntimeException("ArtificialEntityTag has no value for bytecode");
    }
}
