package javax.servlet.jsp.tagext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagFileInfo.class */
public class TagFileInfo {
    private String name;
    private String path;
    private TagInfo tagInfo;

    public TagFileInfo(String name, String path, TagInfo tagInfo) {
        this.name = name;
        this.path = path;
        this.tagInfo = tagInfo;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public TagInfo getTagInfo() {
        return this.tagInfo;
    }
}
