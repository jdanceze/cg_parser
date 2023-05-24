package com.sun.xml.bind.v2.model.annotation;

import com.sun.xml.bind.v2.runtime.Location;
import java.lang.annotation.Annotation;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/Quick.class */
public abstract class Quick implements Annotation, Locatable, Location {
    private final Locatable upstream;

    protected abstract Annotation getAnnotation();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Quick newInstance(Locatable locatable, Annotation annotation);

    /* JADX INFO: Access modifiers changed from: protected */
    public Quick(Locatable upstream) {
        this.upstream = upstream;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public final Location getLocation() {
        return this;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public final Locatable getUpstream() {
        return this.upstream;
    }

    @Override // java.lang.annotation.Annotation, com.sun.xml.bind.v2.runtime.Location
    public final String toString() {
        return getAnnotation().toString();
    }
}
