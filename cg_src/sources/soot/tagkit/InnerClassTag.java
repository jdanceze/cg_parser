package soot.tagkit;

import java.io.UnsupportedEncodingException;
/* loaded from: gencallgraphv3.jar:soot/tagkit/InnerClassTag.class */
public class InnerClassTag implements Tag {
    public static final String NAME = "InnerClassTag";
    private final String innerClass;
    private final String outerClass;
    private final String name;
    private final int accessFlags;

    public InnerClassTag(String innerClass, String outerClass, String name, int accessFlags) {
        this.innerClass = innerClass;
        this.outerClass = outerClass;
        this.name = name;
        this.accessFlags = accessFlags;
        if (innerClass != null && innerClass.startsWith("L") && innerClass.endsWith(";")) {
            throw new RuntimeException("InnerClass annotation type string must be of the form a/b/ClassName not '" + innerClass + "'");
        }
        if (outerClass != null && outerClass.startsWith("L") && outerClass.endsWith(";")) {
            throw new RuntimeException("OuterType annotation type string must be of the form a/b/ClassName not '" + innerClass + "'");
        }
        if (name != null && name.endsWith(";")) {
            throw new RuntimeException("InnerClass name cannot end with ';', got '" + name + "'");
        }
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        try {
            return this.innerClass.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    public String getInnerClass() {
        return this.innerClass;
    }

    public String getOuterClass() {
        return this.outerClass;
    }

    public String getShortName() {
        return this.name;
    }

    public int getAccessFlags() {
        return this.accessFlags;
    }

    public String toString() {
        return "[inner=" + this.innerClass + ", outer=" + this.outerClass + ", name=" + this.name + ",flags=" + this.accessFlags + "]";
    }
}
