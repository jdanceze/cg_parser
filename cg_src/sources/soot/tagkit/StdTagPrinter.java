package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/StdTagPrinter.class */
public class StdTagPrinter implements TagPrinter {
    @Override // soot.tagkit.TagPrinter
    public String print(String aClassName, String aFieldOrMtdSignature, Tag aTag) {
        return aTag.toString();
    }
}
