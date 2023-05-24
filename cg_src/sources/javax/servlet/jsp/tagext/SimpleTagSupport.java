package javax.servlet.jsp.tagext;

import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/SimpleTagSupport.class */
public class SimpleTagSupport implements SimpleTag {
    private JspTag parentTag;
    private JspContext jspContext;
    private JspFragment jspBody;
    static Class class$javax$servlet$jsp$tagext$JspTag;

    @Override // javax.servlet.jsp.tagext.SimpleTag
    public void doTag() throws JspException, IOException {
    }

    @Override // javax.servlet.jsp.tagext.SimpleTag
    public void setParent(JspTag parent) {
        this.parentTag = parent;
    }

    @Override // javax.servlet.jsp.tagext.SimpleTag
    public JspTag getParent() {
        return this.parentTag;
    }

    @Override // javax.servlet.jsp.tagext.SimpleTag
    public void setJspContext(JspContext pc) {
        this.jspContext = pc;
    }

    protected JspContext getJspContext() {
        return this.jspContext;
    }

    @Override // javax.servlet.jsp.tagext.SimpleTag
    public void setJspBody(JspFragment jspBody) {
        this.jspBody = jspBody;
    }

    protected JspFragment getJspBody() {
        return this.jspBody;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0085, code lost:
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final javax.servlet.jsp.tagext.JspTag findAncestorWithClass(javax.servlet.jsp.tagext.JspTag r3, java.lang.Class r4) {
        /*
            r0 = 0
            r5 = r0
            r0 = r3
            if (r0 == 0) goto L2f
            r0 = r4
            if (r0 == 0) goto L2f
            java.lang.Class r0 = javax.servlet.jsp.tagext.SimpleTagSupport.class$javax$servlet$jsp$tagext$JspTag
            if (r0 != 0) goto L1c
            java.lang.String r0 = "javax.servlet.jsp.tagext.JspTag"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            javax.servlet.jsp.tagext.SimpleTagSupport.class$javax$servlet$jsp$tagext$JspTag = r1
            goto L1f
        L1c:
            java.lang.Class r0 = javax.servlet.jsp.tagext.SimpleTagSupport.class$javax$servlet$jsp$tagext$JspTag
        L1f:
            r1 = r4
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto L31
            r0 = r4
            boolean r0 = r0.isInterface()
            r1 = r0
            r5 = r1
            if (r0 != 0) goto L31
        L2f:
            r0 = 0
            return r0
        L31:
            r0 = 0
            r6 = r0
            r0 = r3
            boolean r0 = r0 instanceof javax.servlet.jsp.tagext.SimpleTag
            if (r0 == 0) goto L47
            r0 = r3
            javax.servlet.jsp.tagext.SimpleTag r0 = (javax.servlet.jsp.tagext.SimpleTag) r0
            javax.servlet.jsp.tagext.JspTag r0 = r0.getParent()
            r6 = r0
            goto L58
        L47:
            r0 = r3
            boolean r0 = r0 instanceof javax.servlet.jsp.tagext.Tag
            if (r0 == 0) goto L58
            r0 = r3
            javax.servlet.jsp.tagext.Tag r0 = (javax.servlet.jsp.tagext.Tag) r0
            javax.servlet.jsp.tagext.Tag r0 = r0.getParent()
            r6 = r0
        L58:
            r0 = r6
            if (r0 != 0) goto L5e
            r0 = 0
            return r0
        L5e:
            r0 = r6
            boolean r0 = r0 instanceof javax.servlet.jsp.tagext.TagAdapter
            if (r0 == 0) goto L6d
            r0 = r6
            javax.servlet.jsp.tagext.TagAdapter r0 = (javax.servlet.jsp.tagext.TagAdapter) r0
            javax.servlet.jsp.tagext.JspTag r0 = r0.getAdaptee()
            r6 = r0
        L6d:
            r0 = r5
            if (r0 == 0) goto L79
            r0 = r4
            r1 = r6
            boolean r0 = r0.isInstance(r1)
            if (r0 != 0) goto L84
        L79:
            r0 = r4
            r1 = r6
            java.lang.Class r1 = r1.getClass()
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 == 0) goto L86
        L84:
            r0 = r6
            return r0
        L86:
            r0 = r6
            r3 = r0
            goto L31
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.servlet.jsp.tagext.SimpleTagSupport.findAncestorWithClass(javax.servlet.jsp.tagext.JspTag, java.lang.Class):javax.servlet.jsp.tagext.JspTag");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
