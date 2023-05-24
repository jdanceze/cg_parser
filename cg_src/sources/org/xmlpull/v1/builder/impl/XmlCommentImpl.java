package org.xmlpull.v1.builder.impl;

import org.xmlpull.v1.builder.XmlComment;
import org.xmlpull.v1.builder.XmlContainer;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlCommentImpl.class */
public class XmlCommentImpl implements XmlComment {
    private XmlContainer owner_;
    private String content_;

    /* JADX INFO: Access modifiers changed from: package-private */
    public XmlCommentImpl(XmlContainer owner, String content) {
        this.owner_ = owner;
        this.content_ = content;
        if (content == null) {
            throw new IllegalArgumentException("comment content can not be null");
        }
    }

    @Override // org.xmlpull.v1.builder.XmlComment
    public String getContent() {
        return this.content_;
    }

    @Override // org.xmlpull.v1.builder.XmlComment
    public XmlContainer getParent() {
        return this.owner_;
    }
}
