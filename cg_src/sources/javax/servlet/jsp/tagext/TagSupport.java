package javax.servlet.jsp.tagext;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagSupport.class */
public class TagSupport implements IterationTag, Serializable {
    private Tag parent;
    private Hashtable values;
    protected String id;
    protected PageContext pageContext;
    static Class class$javax$servlet$jsp$tagext$Tag;

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0056, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final javax.servlet.jsp.tagext.Tag findAncestorWithClass(javax.servlet.jsp.tagext.Tag r3, java.lang.Class r4) {
        /*
            r0 = 0
            r5 = r0
            r0 = r3
            if (r0 == 0) goto L2f
            r0 = r4
            if (r0 == 0) goto L2f
            java.lang.Class r0 = javax.servlet.jsp.tagext.TagSupport.class$javax$servlet$jsp$tagext$Tag
            if (r0 != 0) goto L1c
            java.lang.String r0 = "javax.servlet.jsp.tagext.Tag"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            javax.servlet.jsp.tagext.TagSupport.class$javax$servlet$jsp$tagext$Tag = r1
            goto L1f
        L1c:
            java.lang.Class r0 = javax.servlet.jsp.tagext.TagSupport.class$javax$servlet$jsp$tagext$Tag
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
            r0 = r3
            javax.servlet.jsp.tagext.Tag r0 = r0.getParent()
            r6 = r0
            r0 = r6
            if (r0 != 0) goto L3e
            r0 = 0
            return r0
        L3e:
            r0 = r5
            if (r0 == 0) goto L4a
            r0 = r4
            r1 = r6
            boolean r0 = r0.isInstance(r1)
            if (r0 != 0) goto L55
        L4a:
            r0 = r4
            r1 = r6
            java.lang.Class r1 = r1.getClass()
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 == 0) goto L57
        L55:
            r0 = r6
            return r0
        L57:
            r0 = r6
            r3 = r0
            goto L31
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.servlet.jsp.tagext.TagSupport.findAncestorWithClass(javax.servlet.jsp.tagext.Tag, java.lang.Class):javax.servlet.jsp.tagext.Tag");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public int doStartTag() throws JspException {
        return 0;
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public int doEndTag() throws JspException {
        return 6;
    }

    @Override // javax.servlet.jsp.tagext.IterationTag
    public int doAfterBody() throws JspException {
        return 0;
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public void release() {
        this.parent = null;
        this.id = null;
        if (this.values != null) {
            this.values.clear();
        }
        this.values = null;
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public void setParent(Tag t) {
        this.parent = t;
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public Tag getParent() {
        return this.parent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override // javax.servlet.jsp.tagext.Tag
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setValue(String k, Object o) {
        if (this.values == null) {
            this.values = new Hashtable();
        }
        this.values.put(k, o);
    }

    public Object getValue(String k) {
        if (this.values == null) {
            return null;
        }
        return this.values.get(k);
    }

    public void removeValue(String k) {
        if (this.values != null) {
            this.values.remove(k);
        }
    }

    public Enumeration getValues() {
        if (this.values == null) {
            return null;
        }
        return this.values.keys();
    }
}
