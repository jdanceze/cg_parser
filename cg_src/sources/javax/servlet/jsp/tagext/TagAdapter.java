package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagAdapter.class */
public class TagAdapter implements Tag {
    private SimpleTag simpleTagAdaptee;
    private Tag parent;
    private boolean parentDetermined;

    public TagAdapter(SimpleTag adaptee) {
        if (adaptee == null) {
            throw new IllegalArgumentException();
        }
        this.simpleTagAdaptee = adaptee;
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public void setPageContext(PageContext pc) {
        throw new UnsupportedOperationException("Illegal to invoke setPageContext() on TagAdapter wrapper");
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public void setParent(Tag parentTag) {
        throw new UnsupportedOperationException("Illegal to invoke setParent() on TagAdapter wrapper");
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public Tag getParent() {
        if (!this.parentDetermined) {
            JspTag adapteeParent = this.simpleTagAdaptee.getParent();
            if (adapteeParent != null) {
                if (adapteeParent instanceof Tag) {
                    this.parent = (Tag) adapteeParent;
                } else {
                    this.parent = new TagAdapter((SimpleTag) adapteeParent);
                }
            }
            this.parentDetermined = true;
        }
        return this.parent;
    }

    public JspTag getAdaptee() {
        return this.simpleTagAdaptee;
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public int doStartTag() throws JspException {
        throw new UnsupportedOperationException("Illegal to invoke doStartTag() on TagAdapter wrapper");
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public int doEndTag() throws JspException {
        throw new UnsupportedOperationException("Illegal to invoke doEndTag() on TagAdapter wrapper");
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public void release() {
        throw new UnsupportedOperationException("Illegal to invoke release() on TagAdapter wrapper");
    }
}
