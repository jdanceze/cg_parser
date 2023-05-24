package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/KeyTag.class */
public class KeyTag implements Tag {
    public static final String NAME = "KeyTag";
    private final int red;
    private final int green;
    private final int blue;
    private final String key;
    private final String analysisType;

    public KeyTag(int r, int g, int b, String k, String type) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.key = k;
        this.analysisType = type;
    }

    public KeyTag(int color, String k, String type) {
        switch (color) {
            case 0:
                this.red = 255;
                this.green = 0;
                this.blue = 0;
                break;
            case 1:
                this.red = 45;
                this.green = 255;
                this.blue = 84;
                break;
            case 2:
                this.red = 255;
                this.green = 248;
                this.blue = 35;
                break;
            case 3:
                this.red = 174;
                this.green = 210;
                this.blue = 255;
                break;
            case 4:
                this.red = 255;
                this.green = 163;
                this.blue = 0;
                break;
            case 5:
                this.red = 159;
                this.green = 34;
                this.blue = 193;
                break;
            default:
                this.red = 220;
                this.green = 220;
                this.blue = 220;
                break;
        }
        this.key = k;
        this.analysisType = type;
    }

    public KeyTag(int color, String k) {
        this(color, k, null);
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

    public String key() {
        return this.key;
    }

    public String analysisType() {
        return this.analysisType;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[4];
    }
}
