package soot.jimple.toolkits.pointer;

import soot.G;
import soot.Singletons;
import soot.tagkit.ImportantTagAggregator;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/DependenceTagAggregator.class */
public class DependenceTagAggregator extends ImportantTagAggregator {
    public DependenceTagAggregator(Singletons.Global g) {
    }

    public static DependenceTagAggregator v() {
        return G.v().soot_jimple_toolkits_pointer_DependenceTagAggregator();
    }

    @Override // soot.tagkit.TagAggregator
    public boolean wantTag(Tag t) {
        return t instanceof DependenceTag;
    }

    @Override // soot.tagkit.TagAggregator
    public String aggregatedName() {
        return "SideEffectAttribute";
    }
}
