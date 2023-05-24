package javax.servlet.jsp.tagext;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagLibraryValidator.class */
public abstract class TagLibraryValidator {
    private Map initParameters;

    public void setInitParameters(Map map) {
        this.initParameters = map;
    }

    public Map getInitParameters() {
        return this.initParameters;
    }

    public ValidationMessage[] validate(String prefix, String uri, PageData page) {
        return null;
    }

    public void release() {
        this.initParameters = null;
    }
}
