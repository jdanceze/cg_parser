package soot.dexpler;

import soot.tagkit.InnerClassTag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexInnerClassParser.class */
public class DexInnerClassParser {
    public static String getOuterClassNameFromTag(InnerClassTag icTag) {
        String outerClass;
        if (icTag.getOuterClass() == null) {
            String inner = icTag.getInnerClass().replaceAll("/", ".");
            if (inner.contains("$-")) {
                outerClass = inner.substring(0, inner.indexOf("$-"));
            } else if (inner.contains("$")) {
                outerClass = inner.substring(0, inner.lastIndexOf(36));
            } else {
                outerClass = null;
            }
        } else {
            outerClass = icTag.getOuterClass().replaceAll("/", ".");
        }
        return outerClass;
    }
}
