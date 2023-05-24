package com.sun.xml.bind.v2.runtime;

import com.sun.istack.NotNull;
import javax.xml.namespace.NamespaceContext;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/NamespaceContext2.class */
public interface NamespaceContext2 extends NamespaceContext {
    String declareNamespace(String str, String str2, boolean z);

    int force(@NotNull String str, @NotNull String str2);
}
