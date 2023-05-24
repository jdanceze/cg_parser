package com.sun.xml.bind.v2.runtime.unmarshaller;

import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import org.xml.sax.Locator;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/LocatorExWrapper.class */
class LocatorExWrapper implements LocatorEx {
    private final Locator locator;

    public LocatorExWrapper(Locator locator) {
        this.locator = locator;
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx
    public ValidationEventLocator getLocation() {
        return new ValidationEventLocatorImpl(this.locator);
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return this.locator.getPublicId();
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return this.locator.getSystemId();
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return this.locator.getLineNumber();
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return this.locator.getColumnNumber();
    }
}
