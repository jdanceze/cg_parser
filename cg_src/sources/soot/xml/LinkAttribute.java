package soot.xml;
/* loaded from: gencallgraphv3.jar:soot/xml/LinkAttribute.class */
public class LinkAttribute {
    private final String info;
    private final int jimpleLink;
    private final int javaLink;
    private final String className;
    private final boolean isJimpleLink = true;
    private final boolean isJavaLink = true;
    private final String analysisType;

    public LinkAttribute(String info, int jimpleLink, int javaLink, String className, String type) {
        this.info = info;
        this.jimpleLink = jimpleLink;
        this.javaLink = javaLink;
        this.className = className;
        this.analysisType = type;
    }

    public String info() {
        return this.info;
    }

    public int jimpleLink() {
        return this.jimpleLink;
    }

    public int javaLink() {
        return this.javaLink;
    }

    public String className() {
        return this.className;
    }

    public boolean isJimpleLink() {
        return this.isJimpleLink;
    }

    public boolean isJavaLink() {
        return this.isJavaLink;
    }

    public String analysisType() {
        return this.analysisType;
    }
}
