package com.sun.xml.bind.marshaller;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/marshaller/NamespacePrefixMapper.class */
public abstract class NamespacePrefixMapper {
    private static final String[] EMPTY_STRING = new String[0];

    public abstract String getPreferredPrefix(String str, String str2, boolean z);

    public String[] getPreDeclaredNamespaceUris() {
        return EMPTY_STRING;
    }

    public String[] getPreDeclaredNamespaceUris2() {
        return EMPTY_STRING;
    }

    public String[] getContextualNamespaceDecls() {
        return EMPTY_STRING;
    }
}
