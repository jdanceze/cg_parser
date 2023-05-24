package com.sun.xml.fastinfoset.util;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/ValueArray.class */
public abstract class ValueArray {
    public static final int DEFAULT_CAPACITY = 10;
    public static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE;
    protected int _size;
    protected int _readOnlyArraySize;
    protected int _maximumCapacity;

    public abstract void setReadOnlyArray(ValueArray valueArray, boolean z);

    public abstract void clear();

    public int getSize() {
        return this._size;
    }

    public int getMaximumCapacity() {
        return this._maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this._maximumCapacity = maximumCapacity;
    }
}
