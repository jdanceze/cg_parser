package com.sun.xml.bind.v2.runtime;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/NameList.class */
public final class NameList {
    public final String[] namespaceURIs;
    public final boolean[] nsUriCannotBeDefaulted;
    public final String[] localNames;
    public final int numberOfElementNames;
    public final int numberOfAttributeNames;

    public NameList(String[] namespaceURIs, boolean[] nsUriCannotBeDefaulted, String[] localNames, int numberElementNames, int numberAttributeNames) {
        this.namespaceURIs = namespaceURIs;
        this.nsUriCannotBeDefaulted = nsUriCannotBeDefaulted;
        this.localNames = localNames;
        this.numberOfElementNames = numberElementNames;
        this.numberOfAttributeNames = numberAttributeNames;
    }
}
