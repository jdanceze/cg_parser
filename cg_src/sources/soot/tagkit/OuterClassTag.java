package soot.tagkit;

import java.io.UnsupportedEncodingException;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/tagkit/OuterClassTag.class */
public class OuterClassTag implements Tag {
    public static final String NAME = "OuterClassTag";
    private final SootClass outerClass;
    private final String simpleName;
    private final boolean anon;

    public OuterClassTag(SootClass outer, String simpleName, boolean anon) {
        this.outerClass = outer;
        this.simpleName = simpleName;
        this.anon = anon;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        try {
            return this.outerClass.getName().getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    public SootClass getOuterClass() {
        return this.outerClass;
    }

    public String getSimpleName() {
        return this.simpleName;
    }

    public boolean isAnon() {
        return this.anon;
    }

    public String toString() {
        return "[outer class=" + this.outerClass.getName() + "]";
    }
}
