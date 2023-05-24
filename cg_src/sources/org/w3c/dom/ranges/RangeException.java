package org.w3c.dom.ranges;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/ranges/RangeException.class */
public class RangeException extends RuntimeException {
    public short code;
    public static final short BAD_BOUNDARYPOINTS_ERR = 1;
    public static final short INVALID_NODE_TYPE_ERR = 2;

    public RangeException(short s, String str) {
        super(str);
        this.code = s;
    }
}
