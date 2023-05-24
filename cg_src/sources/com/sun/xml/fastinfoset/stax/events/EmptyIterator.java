package com.sun.xml.fastinfoset.stax.events;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/EmptyIterator.class */
public class EmptyIterator implements Iterator {
    public static final EmptyIterator instance = new EmptyIterator();

    private EmptyIterator() {
    }

    public static EmptyIterator getInstance() {
        return instance;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return false;
    }

    @Override // java.util.Iterator
    public Object next() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.emptyIterator"));
    }
}
