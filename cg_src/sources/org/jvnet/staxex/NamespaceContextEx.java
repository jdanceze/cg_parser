package org.jvnet.staxex;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/NamespaceContextEx.class */
public interface NamespaceContextEx extends NamespaceContext, Iterable<Binding> {

    /* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/NamespaceContextEx$Binding.class */
    public interface Binding {
        String getPrefix();

        String getNamespaceURI();
    }

    @Override // java.lang.Iterable
    Iterator<Binding> iterator();
}
