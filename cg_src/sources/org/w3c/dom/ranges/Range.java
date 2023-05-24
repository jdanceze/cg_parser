package org.w3c.dom.ranges;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/ranges/Range.class */
public interface Range {
    public static final short START_TO_START = 0;
    public static final short START_TO_END = 1;
    public static final short END_TO_END = 2;
    public static final short END_TO_START = 3;

    Node getStartContainer() throws DOMException;

    int getStartOffset() throws DOMException;

    Node getEndContainer() throws DOMException;

    int getEndOffset() throws DOMException;

    boolean getCollapsed() throws DOMException;

    Node getCommonAncestorContainer() throws DOMException;

    void setStart(Node node, int i) throws RangeException, DOMException;

    void setEnd(Node node, int i) throws RangeException, DOMException;

    void setStartBefore(Node node) throws RangeException, DOMException;

    void setStartAfter(Node node) throws RangeException, DOMException;

    void setEndBefore(Node node) throws RangeException, DOMException;

    void setEndAfter(Node node) throws RangeException, DOMException;

    void collapse(boolean z) throws DOMException;

    void selectNode(Node node) throws RangeException, DOMException;

    void selectNodeContents(Node node) throws RangeException, DOMException;

    short compareBoundaryPoints(short s, Range range) throws DOMException;

    void deleteContents() throws DOMException;

    DocumentFragment extractContents() throws DOMException;

    DocumentFragment cloneContents() throws DOMException;

    void insertNode(Node node) throws DOMException, RangeException;

    void surroundContents(Node node) throws DOMException, RangeException;

    Range cloneRange() throws DOMException;

    String toString() throws DOMException;

    void detach() throws DOMException;
}
