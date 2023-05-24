package org.xmlpull.v1.builder;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlDoctype.class */
public interface XmlDoctype extends XmlContainer {
    String getSystemIdentifier();

    String getPublicIdentifier();

    Iterator children();

    XmlDocument getParent();

    XmlProcessingInstruction addProcessingInstruction(String str, String str2);

    void removeAllProcessingInstructions();
}
