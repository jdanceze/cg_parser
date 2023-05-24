package com.sun.xml.txw2;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/ContentVisitor.class */
interface ContentVisitor {
    void onStartDocument();

    void onEndDocument();

    void onEndTag();

    void onPcdata(StringBuilder sb);

    void onCdata(StringBuilder sb);

    void onStartTag(String str, String str2, Attribute attribute, NamespaceDecl namespaceDecl);

    void onComment(StringBuilder sb);
}
