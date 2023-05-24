package soot.tagkit;

import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/tagkit/TagManager.class */
public class TagManager {
    private TagPrinter tagPrinter = new StdTagPrinter();

    public TagManager(Singletons.Global g) {
    }

    public static TagManager v() {
        return G.v().soot_tagkit_TagManager();
    }

    public Tag getTagFor(String tagName) {
        try {
            Class<?> cc = Class.forName("soot.tagkit." + tagName);
            return (Tag) cc.newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e2) {
            throw new RuntimeException();
        } catch (InstantiationException e3) {
            throw new RuntimeException(e3.toString());
        }
    }

    public void setTagPrinter(TagPrinter p) {
        this.tagPrinter = p;
    }

    public String print(String aClassName, String aFieldOrMtdSignature, Tag aTag) {
        return this.tagPrinter.print(aClassName, aFieldOrMtdSignature, aTag);
    }
}
