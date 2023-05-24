package soot.tagkit;

import java.util.LinkedList;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/FirstTagAggregator.class */
public abstract class FirstTagAggregator extends TagAggregator {
    @Override // soot.tagkit.TagAggregator
    public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
        if (units.size() <= 0 || units.getLast() != u) {
            units.add(u);
            tags.add(t);
        }
    }
}
