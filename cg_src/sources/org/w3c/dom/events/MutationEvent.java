package org.w3c.dom.events;

import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/events/MutationEvent.class */
public interface MutationEvent extends Event {
    public static final short MODIFICATION = 1;
    public static final short ADDITION = 2;
    public static final short REMOVAL = 3;

    Node getRelatedNode();

    String getPrevValue();

    String getNewValue();

    String getAttrName();

    short getAttrChange();

    void initMutationEvent(String str, boolean z, boolean z2, Node node, String str2, String str3, String str4, short s);
}
