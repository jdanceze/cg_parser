package javax.servlet.jsp;

import java.io.Writer;
import java.util.Enumeration;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/JspContext.class */
public abstract class JspContext {
    public abstract void setAttribute(String str, Object obj);

    public abstract void setAttribute(String str, Object obj, int i);

    public abstract Object getAttribute(String str);

    public abstract Object getAttribute(String str, int i);

    public abstract Object findAttribute(String str);

    public abstract void removeAttribute(String str);

    public abstract void removeAttribute(String str, int i);

    public abstract int getAttributesScope(String str);

    public abstract Enumeration getAttributeNamesInScope(int i);

    public abstract JspWriter getOut();

    public abstract ExpressionEvaluator getExpressionEvaluator();

    public abstract VariableResolver getVariableResolver();

    public JspWriter pushBody(Writer writer) {
        return null;
    }

    public JspWriter popBody() {
        return null;
    }
}
