package com.sun.xml.bind.v2.model.annotation;

import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.runtime.Location;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/MethodLocatable.class */
public class MethodLocatable<M> implements Locatable {
    private final Locatable upstream;
    private final M method;
    private final Navigator<?, ?, ?, M> nav;

    public MethodLocatable(Locatable upstream, M method, Navigator<?, ?, ?, M> nav) {
        this.upstream = upstream;
        this.method = method;
        this.nav = nav;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.upstream;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.nav.getMethodLocation(this.method);
    }
}
