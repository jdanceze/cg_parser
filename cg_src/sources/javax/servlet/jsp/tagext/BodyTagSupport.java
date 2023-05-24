package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/BodyTagSupport.class */
public class BodyTagSupport extends TagSupport implements BodyTag {
    protected BodyContent bodyContent;

    @Override // javax.servlet.jsp.tagext.TagSupport, javax.servlet.jsp.tagext.Tag
    public int doStartTag() throws JspException {
        return 2;
    }

    @Override // javax.servlet.jsp.tagext.TagSupport, javax.servlet.jsp.tagext.Tag
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    @Override // javax.servlet.jsp.tagext.BodyTag
    public void setBodyContent(BodyContent b) {
        this.bodyContent = b;
    }

    @Override // javax.servlet.jsp.tagext.BodyTag
    public void doInitBody() throws JspException {
    }

    @Override // javax.servlet.jsp.tagext.TagSupport, javax.servlet.jsp.tagext.IterationTag
    public int doAfterBody() throws JspException {
        return 0;
    }

    @Override // javax.servlet.jsp.tagext.TagSupport, javax.servlet.jsp.tagext.Tag
    public void release() {
        this.bodyContent = null;
        super.release();
    }

    public BodyContent getBodyContent() {
        return this.bodyContent;
    }

    public JspWriter getPreviousOut() {
        return this.bodyContent.getEnclosingWriter();
    }
}
