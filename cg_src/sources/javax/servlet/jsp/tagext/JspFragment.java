package javax.servlet.jsp.tagext;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/JspFragment.class */
public interface JspFragment {
    void invoke(Writer writer) throws JspException, IOException;
}
