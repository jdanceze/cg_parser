package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/Tag.class */
public interface Tag {
    String getName();

    byte[] getValue() throws AttributeValueException;
}
