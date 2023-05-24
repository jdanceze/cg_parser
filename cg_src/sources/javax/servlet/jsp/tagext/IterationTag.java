package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/IterationTag.class */
public interface IterationTag extends Tag {
    public static final int EVAL_BODY_AGAIN = 2;

    int doAfterBody() throws JspException;
}
