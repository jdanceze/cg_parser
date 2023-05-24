package javax.servlet.jsp.tagext;

import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/SimpleTag.class */
public interface SimpleTag extends JspTag {
    void doTag() throws JspException, IOException;

    void setParent(JspTag jspTag);

    JspTag getParent();

    void setJspContext(JspContext jspContext);

    void setJspBody(JspFragment jspFragment);
}
