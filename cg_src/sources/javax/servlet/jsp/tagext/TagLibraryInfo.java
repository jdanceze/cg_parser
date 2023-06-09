package javax.servlet.jsp.tagext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagLibraryInfo.class */
public abstract class TagLibraryInfo {
    protected String prefix;
    protected String uri;
    protected TagInfo[] tags;
    protected TagFileInfo[] tagFiles;
    protected FunctionInfo[] functions;
    protected String tlibversion;
    protected String jspversion;
    protected String shortname;
    protected String urn;
    protected String info;

    protected TagLibraryInfo(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    public String getURI() {
        return this.uri;
    }

    public String getPrefixString() {
        return this.prefix;
    }

    public String getShortName() {
        return this.shortname;
    }

    public String getReliableURN() {
        return this.urn;
    }

    public String getInfoString() {
        return this.info;
    }

    public String getRequiredVersion() {
        return this.jspversion;
    }

    public TagInfo[] getTags() {
        return this.tags;
    }

    public TagFileInfo[] getTagFiles() {
        return this.tagFiles;
    }

    public TagInfo getTag(String shortname) {
        TagInfo[] tags = getTags();
        if (tags == null || tags.length == 0) {
            return null;
        }
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].getTagName().equals(shortname)) {
                return tags[i];
            }
        }
        return null;
    }

    public TagFileInfo getTagFile(String shortname) {
        TagFileInfo[] tagFiles = getTagFiles();
        if (tagFiles == null || tagFiles.length == 0) {
            return null;
        }
        for (int i = 0; i < tagFiles.length; i++) {
            if (tagFiles[i].getName().equals(shortname)) {
                return tagFiles[i];
            }
        }
        return null;
    }

    public FunctionInfo[] getFunctions() {
        return this.functions;
    }

    public FunctionInfo getFunction(String name) {
        if (this.functions == null || this.functions.length == 0) {
            System.err.println("No functions");
            return null;
        }
        for (int i = 0; i < this.functions.length; i++) {
            if (this.functions[i].getName().equals(name)) {
                return this.functions[i];
            }
        }
        return null;
    }
}
