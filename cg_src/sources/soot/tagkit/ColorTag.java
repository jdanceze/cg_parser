package soot.tagkit;

import java.awt.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/tagkit/ColorTag.class */
public class ColorTag implements Tag {
    private static final Logger logger = LoggerFactory.getLogger(ColorTag.class);
    public static final String NAME = "ColorTag";
    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int YELLOW = 2;
    public static final int BLUE = 3;
    public static final int ORANGE = 4;
    public static final int PURPLE = 5;
    private static final boolean DEFAULT_FOREGROUND = false;
    private static final String DEFAULT_ANALYSIS_TYPE = "Unknown";
    private final int red;
    private final int green;
    private final int blue;
    private final boolean foreground;
    private final String analysisType;

    public ColorTag(Color c) {
        this(c.getRed(), c.getGreen(), c.getBlue(), false, DEFAULT_ANALYSIS_TYPE);
    }

    public ColorTag(int r, int g, int b) {
        this(r, g, b, false, DEFAULT_ANALYSIS_TYPE);
    }

    public ColorTag(int r, int g, int b, boolean fg) {
        this(r, g, b, fg, DEFAULT_ANALYSIS_TYPE);
    }

    public ColorTag(int r, int g, int b, String type) {
        this(r, g, b, false, type);
    }

    public ColorTag(int r, int g, int b, boolean fg, String type) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.foreground = fg;
        this.analysisType = type;
    }

    public ColorTag(int color) {
        this(color, false, DEFAULT_ANALYSIS_TYPE);
    }

    public ColorTag(int color, String type) {
        this(color, false, type);
    }

    public ColorTag(int color, boolean fg) {
        this(color, fg, DEFAULT_ANALYSIS_TYPE);
    }

    public ColorTag(int color, boolean fg, String type) {
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
        this.foreground = fg;
        this.analysisType = type;
    }

    public String getAnalysisType() {
        return this.analysisType;
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    public boolean isForeground() {
        return this.foreground;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[2];
    }

    public String toString() {
        return this.red + Instruction.argsep + this.green + Instruction.argsep + this.blue;
    }
}
