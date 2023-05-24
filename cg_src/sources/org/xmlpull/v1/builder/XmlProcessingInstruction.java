package org.xmlpull.v1.builder;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlProcessingInstruction.class */
public interface XmlProcessingInstruction {
    String getTarget();

    String getContent();

    String getBaseUri();

    XmlNotation getNotation();

    XmlContainer getParent();
}
