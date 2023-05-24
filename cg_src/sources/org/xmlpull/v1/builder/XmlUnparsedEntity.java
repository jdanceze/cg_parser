package org.xmlpull.v1.builder;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlUnparsedEntity.class */
public interface XmlUnparsedEntity {
    String getName();

    String getSystemIdentifier();

    String getPublicIdentifier();

    String getDeclarationBaseUri();

    String getNotationName();

    XmlNotation getNotation();
}
