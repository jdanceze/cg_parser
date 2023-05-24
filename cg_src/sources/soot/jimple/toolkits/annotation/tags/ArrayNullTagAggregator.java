package soot.jimple.toolkits.annotation.tags;

import java.util.LinkedList;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.baf.Inst;
import soot.tagkit.Tag;
import soot.tagkit.TagAggregator;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/tags/ArrayNullTagAggregator.class */
public class ArrayNullTagAggregator extends TagAggregator {
    public ArrayNullTagAggregator(Singletons.Global g) {
    }

    public static ArrayNullTagAggregator v() {
        return G.v().soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator();
    }

    @Override // soot.tagkit.TagAggregator
    public boolean wantTag(Tag t) {
        return t instanceof OneByteCodeTag;
    }

    @Override // soot.tagkit.TagAggregator
    public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
        Inst i = (Inst) u;
        if (!i.containsInvokeExpr() && !i.containsFieldRef() && !i.containsArrayRef()) {
            return;
        }
        OneByteCodeTag obct = (OneByteCodeTag) t;
        if (units.size() == 0 || units.getLast() != u) {
            units.add(u);
            tags.add(new ArrayNullCheckTag());
        }
        ArrayNullCheckTag anct = (ArrayNullCheckTag) tags.getLast();
        anct.accumulate(obct.getValue()[0]);
    }

    @Override // soot.tagkit.TagAggregator
    public String aggregatedName() {
        return "ArrayNullCheckAttribute";
    }
}
