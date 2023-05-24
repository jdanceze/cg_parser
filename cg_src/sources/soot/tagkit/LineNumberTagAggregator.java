package soot.tagkit;

import java.util.LinkedList;
import soot.G;
import soot.IdentityUnit;
import soot.Singletons;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/LineNumberTagAggregator.class */
public class LineNumberTagAggregator extends FirstTagAggregator {
    public LineNumberTagAggregator(Singletons.Global g) {
    }

    public static LineNumberTagAggregator v() {
        return G.v().soot_tagkit_LineNumberTagAggregator();
    }

    @Override // soot.tagkit.TagAggregator
    public boolean wantTag(Tag t) {
        return (t instanceof LineNumberTag) || (t instanceof SourceLnPosTag);
    }

    @Override // soot.tagkit.TagAggregator
    public String aggregatedName() {
        return "LineNumberTable";
    }

    @Override // soot.tagkit.FirstTagAggregator, soot.tagkit.TagAggregator
    public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
        if (!(u instanceof IdentityUnit)) {
            super.considerTag(t, u, tags, units);
        }
    }
}
