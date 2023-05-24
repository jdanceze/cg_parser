package soot.tagkit;

import java.util.LinkedList;
import soot.Unit;
import soot.baf.Inst;
/* loaded from: gencallgraphv3.jar:soot/tagkit/ImportantTagAggregator.class */
public abstract class ImportantTagAggregator extends TagAggregator {
    @Override // soot.tagkit.TagAggregator
    public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
        Inst i = (Inst) u;
        if (i.containsInvokeExpr() || i.containsFieldRef() || i.containsArrayRef() || i.containsNewExpr()) {
            units.add(u);
            tags.add(t);
        }
    }
}
