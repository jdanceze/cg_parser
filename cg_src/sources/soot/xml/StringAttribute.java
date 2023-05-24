package soot.xml;
/* loaded from: gencallgraphv3.jar:soot/xml/StringAttribute.class */
public class StringAttribute {
    private final String info;
    private final String analysisType;

    public StringAttribute(String info, String type) {
        this.info = info;
        this.analysisType = type;
    }

    public String info() {
        return this.info;
    }

    public String analysisType() {
        return this.analysisType;
    }
}
