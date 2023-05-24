package com.sun.xml.bind.api;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/RawAccessor.class */
public abstract class RawAccessor<B, V> {
    public abstract V get(B b) throws AccessorException;

    public abstract void set(B b, V v) throws AccessorException;
}
