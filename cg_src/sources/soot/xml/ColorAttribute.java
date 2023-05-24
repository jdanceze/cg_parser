package soot.xml;
/* loaded from: gencallgraphv3.jar:soot/xml/ColorAttribute.class */
public class ColorAttribute {
    private final int red;
    private final int green;
    private final int blue;
    private final int fg;
    private final String analysisType;

    public ColorAttribute(int red, int green, int blue, boolean fg, String type) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.fg = fg ? 1 : 0;
        this.analysisType = type;
    }

    public int red() {
        return this.red;
    }

    public int green() {
        return this.green;
    }

    public int blue() {
        return this.blue;
    }

    public int fg() {
        return this.fg;
    }

    public String analysisType() {
        return this.analysisType;
    }
}
