package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/Tag.class */
public interface Tag extends JspTag {
    public static final int SKIP_BODY = 0;
    public static final int EVAL_BODY_INCLUDE = 1;
    public static final int SKIP_PAGE = 5;
    public static final int EVAL_PAGE = 6;

    void setPageContext(PageContext pageContext);

    void setParent(Tag tag);

    Tag getParent();

    int doStartTag() throws JspException;

    int doEndTag() throws JspException;

    void release();
}
