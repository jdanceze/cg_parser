package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/BodyTag.class */
public interface BodyTag extends IterationTag {
    public static final int EVAL_BODY_TAG = 2;
    public static final int EVAL_BODY_BUFFERED = 2;

    void setBodyContent(BodyContent bodyContent);

    void doInitBody() throws JspException;
}
